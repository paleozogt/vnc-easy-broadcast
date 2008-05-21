!ifndef ADDREMOVEPROGS
	!include AddRemProgs.nsh
!endif

!define VNC_SETTINGS "SOFTWARE\ORL\WinVNC3"
!define VNC_VIEWER_SETTINGS "Software\ORL\VNCviewer\settings"

!define TIGHTVNC_NAME "TightVNC"
!define ULTRAVNC_NAME "UltraVNC"
!define REALVNC_NAME  "RealVnc"

!define TIGHTVNC_UNINSTALL "${ADDREMOVEPROGS}\${TIGHTVNC_NAME}_is1"
!define ULTRAVNC_UNINSTALL "${ADDREMOVEPROGS}\{A8AD990E-355A-4413-8647-A9B168978423}_is1"
!define REALVNC_UNINSTALL  "${ADDREMOVEPROGS}\${REALVNC_NAME}_is1"

!macro getProgramLocation vnc_curr vnc_key vnc_type vnc_path
	ReadRegStr ${vnc_path} HKLM ${vnc_key} ${ADDREMPROGS_LOCATION}
	StrCpy ${vnc_type} ${vnc_curr}
	StrCmp ${location} "" 0 Found   
!macroend

!macro detectVnc vnctype location
	!insertmacro getProgramLocation ${TIGHTVNC_NAME} "${TIGHTVNC_UNINSTALL}" ${vnctype} ${location}	
	!insertmacro getProgramLocation ${ULTRAVNC_NAME} "${ULTRAVNC_UNINSTALL}" ${vnctype} ${location}	

	# TODO: figure out how to support RealVNC (it doesn't seem to support -viewonly from command-line!)
	#	!insertmacro getProgramLocation ${REALVNC_NAME}  "${REALVNC_UNINSTALL}" ${vnctype} ${location}		

Found:
	; chop trailing slashes, which can screw up IfFileExists
	StrCpy $0 "${location}" 1 -1
	StrCmp "$0" "\" 0 DetectDone
	StrCpy ${location} "${location}" -1
DetectDone:
!macroend