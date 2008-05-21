!ifndef ADDREMOVEPROGS
	!include AddRemProgs.nsh
!endif
!include VncDetect.nsh

!define PRODNAME         "VncEasyBroadcast"
!define UNINSTALLER      "$INSTDIR\vncbunst.exe"
outFile "..\dist\${PRODNAME}Installer.exe"

!define SVC_WRAPPER_NAME "ServiceEx"
!define SVC_WRAPPER_FILE "$INSTDIR\${SVC_WRAPPER_NAME}.exe"
!define SVC_INI          "$INSTDIR\${SVC_NAME}.ini"
!define SVC_NAME         "VncListeningViewerService"

!define VNCVIEWER        "$INSTDIR\vncviewer.exe"
!define VNCSERVER        "$INSTDIR\WinVNC.exe"
!define VNCVIEWER_OPTS \
     "-listen -fullscreen -shared -viewonly -notoolbar -restricted"
!define VNCVIEWER_OPTS_TIGHTVNC "${VNCVIEWER_OPTS} -fitwindow"
!define VNCVIEWER_OPTS_ULTRAVNC "${VNCVIEWER_OPTS} -autoscaling"

!define BROADCAST_SHORTCUT "$SMPROGRAMS\$VncType\${PRODNAME}.lnk"
	 
!define UNINSTALL      "${ADDREMOVEPROGS}\${PRODNAME}"

!define MSG_INSTALL  "This will install ${PRODNAME}"
!define ERRMSG_NOVNC "Cannot find any installed VNC.  You must have VNC installed."
!define ERRMSG_NOTADMIN "You are a member of $0.  You must have administrator rights to install this program."

!define FIREWALL "netsh firewall add allowedprogram "

Var VncType
Var VncViewerOptions

!macro regService action
   nsExec::Exec '"${SVC_WRAPPER_FILE}" ${action} "${SVC_NAME}"'
!macroend

section "adminCheck"  
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

section "start"
   SetShellVarContext all
   messageBox MB_OK "${MSG_INSTALL}"
sectionEnd

section "vncDetect"
   # check whether vnc is even installed...
   #
   !insertmacro detectVnc $VncType $INSTDIR
messageBox MB_OK|MB_ICONEXCLAMATION "vnctype= $VncType location=$INSTDIR"
   IfFileExists $INSTDIR HasFile 0
      messageBox MB_OK|MB_ICONEXCLAMATION "${ERRMSG_NOVNC}"
      Quit
   HasFile:

   # set up vnc viewer options for whatever flavor we're using   
   StrCmp $VncType "${TIGHTVNC_NAME}" 0 IsOtherVnc
   StrCpy $VncViewerOptions "${VNCVIEWER_OPTS_TIGHTVNC}"
   Goto Done
IsOtherVnc:
   StrCpy $VncViewerOptions "${VNCVIEWER_OPTS_ULTRAVNC}"
   Goto Done
   
Done:   
   WriteRegStr HKLM "SOFTWARE\ORL\WinVNC3" "VncFlavor" "$VncType"   
sectionEnd

section "setupUninstall"
   # create the uninstaller
   WriteUninstaller ${UNINSTALLER}
   
   # set up add/remove programs
   WriteRegStr HKLM ${UNINSTALL} "DisplayName" "${PRODNAME}"
   WriteRegStr HKLM ${UNINSTALL} "UninstallString" "${UNINSTALLER}"
   WriteRegStr HKLM ${UNINSTALL} ${ADDREMPROGS_LOCATION} "$INSTDIR"
sectionEnd

section "setupVncServer"
   # disable shutdown for vnc server
   WriteRegDWORD HKLM "${VNC_SETTINGS}\Default" "AllowShutdown" 0x0

   # disable property editing for server
   #WriteRegDWORD HKLM "${VNC_SETTINGS}\Default" "AllowProperties" 0x0
sectionEnd

section "setupVncViewerService"
   # make sure we have the full-screen
   # prompt turned off for the viewer
   #
   WriteRegDWORD HKU \
      ".DEFAULT\${VNC_VIEWER_SETTINGS}" \
      "SkipFullScreenPrompt" 0x1

   # copy the service wrapper
   #
   IfFileExists ${SVC_WRAPPER_FILE} 0 NoFile
      # if it exists, remove the service
      # so that we can overwrite it
      !insertmacro regService remove
   NoFile:
      SetOutPath $INSTDIR
      File "..\lib\ServiceEx\${SVC_WRAPPER_NAME}.exe"
   
   # set up the service wrapper ini
   #
   WriteINIStr "${SVC_INI}" "${SVC_WRAPPER_NAME}" "ServiceExeFullPath" "${VNCVIEWER}"
   WriteINIStr "${SVC_INI}" "${SVC_WRAPPER_NAME}" "options" "$VncViewerOptions"
   WriteINIStr "${SVC_INI}" "${SVC_WRAPPER_NAME}" "desktop" "true"
   WriteINIStr "${SVC_INI}" "${SVC_WRAPPER_NAME}" "StartNow" "true"
sectionEnd

# register/start the vncviewer service
#
section "vncviewerservice"
   !insertmacro regService install
sectionEnd

section "firewall"
   # poke a hole in xp's firewall
   nsExec::Exec '${FIREWALL} "${VNCVIEWER}" VncViewer ENABLE'
   nsExec::Exec '${FIREWALL} "${VNCSERVER}" VncServer ENABLE'
sectionEnd

section "broadcaster"
   File ..\dist\${PRODNAME}.exe
   CreateShortcut ${BROADCAST_SHORTCUT} $INSTDIR\${PRODNAME}
sectionEnd




# Uninstallation
#
section "uninstall"   
   SetShellVarContext all

   # get the vnc type that we're using and remove the key
   ReadRegStr $VncType HKLM ${VNC_SETTINGS} "VncFlavor" 
   DeleteRegValue HKLM ${VNC_SETTINGS} "VncFlavor" 
   
   !insertmacro regService remove
   Delete ${BROADCAST_SHORTCUT}
   Delete $INSTDIR\${PRODNAME}.exe
   Delete ${SVC_WRAPPER_FILE}
   Delete ${SVC_INI}
   Delete ${UNINSTALLER}
   DeleteRegKey HKLM ${UNINSTALL}
sectionEnd

