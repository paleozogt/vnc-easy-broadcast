!define PRODNAME         "VncEasyBroadcast"
!define UNINSTALLER      "$INSTDIR\vncbunst.exe"

!define SVC_WRAPPER_NAME "ServiceEx"
!define SVC_WRAPPER_FILE "$INSTDIR\${SVC_WRAPPER_NAME}.exe"
!define SVC_INI          "$INSTDIR\${SVC_NAME}.ini"
!define SVC_NAME         "VncListeningViewerService"

!define VNCBRAND         "TightVNC"
!define VNCVIEWER        "$INSTDIR\vncviewer.exe"
!define VNCSERVER        "$INSTDIR\WinVNC.exe"
!define VNCVIEWER_OPTS \
     "-listen -viewonly -fullscreen -notoolbar -shared -restricted -fitwindow"

!define ADDREMOVEPROGS "Software\Microsoft\Windows\CurrentVersion\Uninstall\${PRODNAME}"

!define MSG_INSTALL  "This will install ${PRODNAME}"
!define ERRMSG_NOVNC "Cannot find ${VNCBRAND}.  You must have ${VNCBRAND} installed."
!define ERRMSG_NOTADMIN "You are a member of $0.  You must have administrator rights to install this program."

!define FIREWALL "netsh firewall add allowedprogram "

!macro regService action
   nsExec::Exec '"${SVC_WRAPPER_FILE}" ${action} "${SVC_NAME}"'
!macroend


outFile "${PRODNAME}.exe"
installDir $PROGRAMFILES\${VNCBRAND}

section "admincheck"  
    # call userInfo plugin to get user info.
    # The plugin puts the result in the stack
    userInfo::getAccountType
   
    # pop the result from the stack into $0
    pop $0
   
    # compare the result with the string "Admin" to see if the user is admin.
    # If match, jump 3 lines down.
    strCmp $0 "Admin" IsAdmin
       messageBox MB_OK|MB_ICONEXCLAMATION "${ERRMSG_NOTADMIN}"
       Quit
    IsAdmin:  
sectionEnd

section
   messageBox MB_OK "${MSG_INSTALL}"

   # check whether vnc is even installed...
   #
   IfFileExists ${VNCVIEWER} 0 NoFile
      Goto IfEnd
   NoFile:
      messageBox MB_OK|MB_ICONEXCLAMATION "${ERRMSG_NOVNC}"
      Quit
   IfEnd:

   # create the uninstaller
   WriteUninstaller ${UNINSTALLER}

   # set up add/remove programs
   WriteRegStr HKLM ${ADDREMOVEPROGS} "DisplayName" "${PRODNAME}"
   WriteRegStr HKLM ${ADDREMOVEPROGS} "UninstallString" "${UNINSTALLER}"
   WriteRegStr HKLM ${ADDREMOVEPROGS} "InstallLocation" "$INSTDIR"
sectionEnd

section "setupVncServer"

   # disable shutdown for vnc server

   WriteRegDWORD HKLM \
      "SOFTWARE\ORL\WinVNC3\Default" \
      "AllowShutdown" 0x0

   # disable property editing for server

   #WriteRegDWORD HKLM \
      "SOFTWARE\ORL\WinVNC3\Default" \
      "AllowProperties" 0x0
sectionEnd

section "firewall"
   # poke a hole in xp's firewall

   nsExec::Exec '${FIREWALL} "${VNCVIEWER}" VncViewer ENABLE'
   nsExec::Exec '${FIREWALL} "${VNCSERVER}" VncServer ENABLE'
sectionEnd

section "setupVncViewerService"
   # make sure we have the full-screen
   # prompt turned off for the viewer
   #
   WriteRegDWORD HKU \
      ".DEFAULT\Software\ORL\VNCviewer\settings" \
      "SkipFullScreenPrompt" 0x1

   # copy the service wrapper
   #
   IfFileExists ${SVC_WRAPPER_FILE} 0 NoFile
      # if it exists, remove the service
      # so that we can overwrite it
      !insertmacro regService remove
   NoFile:
      SetOutPath $INSTDIR
      File "${SVC_WRAPPER_NAME}.exe"

   # set up the service wrapper ini
   #
   WriteINIStr "${SVC_INI}" "${SVC_WRAPPER_NAME}" "ServiceExeFullPath" "${VNCVIEWER}"
   WriteINIStr "${SVC_INI}" "${SVC_WRAPPER_NAME}" "options" "${VNCVIEWER_OPTS}"
   WriteINIStr "${SVC_INI}" "${SVC_WRAPPER_NAME}" "desktop" "true"
   WriteINIStr "${SVC_INI}" "${SVC_WRAPPER_NAME}" "StartNow" "true"
sectionEnd

# register/start the vncviewer service
#
section
   !insertmacro regService install
sectionEnd


# Uninstallation
#
section "uninstall"
   !insertmacro regService remove
   Delete ${SVC_WRAPPER_FILE}
   Delete ${SVC_INI}
   Delete ${UNINSTALLER}
   DeleteRegKey HKLM ${ADDREMOVEPROGS}
sectionEnd
