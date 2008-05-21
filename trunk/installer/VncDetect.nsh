!ifndef ADDREMOVEPROGS
	!include AddRemProgs.nsh
!endif

!define VNC_SETTINGS "SOFTWARE\ORL\WinVNC3"
!define VNC_VIEWER_SETTINGS "Software\ORL\VNCviewer\settings"

!define VNC_TIGHTVNC "TightVNC"
!define VNC_ULTRAVNC "UltraVNC"
!define VNC_REALVNC  "RealVnc"

!macro getProgramLocation vnc_curr vnc_key vnc_type vnc_path
	ReadRegStr ${vnc_path} HKLM ${vnc_key} ${ADDREMPROGS_LOCATION}
	StrCpy ${vnc_type} ${vnc_curr}
	StrCmp ${location} "" 0 Found   
!macroend

!macro detectVnc vnctype location
	!insertmacro getProgramLocation ${VNC_TIGHTVNC} "${ADDREMOVEPROGS}\${VNC_TIGHTVNC}_is1" ${vnctype} ${location}	
	!insertmacro getProgramLocation ${VNC_ULTRAVNC} "${ADDREMOVEPROGS}\${VNC_ULTRAVNC}" ${vnctype} ${location}	
	!insertmacro getProgramLocation ${VNC_REALVNC}  "${ADDREMOVEPROGS}\${VNC_REALVNC}" ${vnctype} ${location}		

Found:
	; chop trailing slashes, which can screw up IfFileExists
	StrCpy $0 "${location}" 1 -1
	StrCmp "$0" "\" 0 Done
	StrCpy ${location} "${location}" -1
Done:
!macroend