cd "%PROGRAMFILES%\TightVNC"
set vnc= start winvnc.exe -connect

rem --
rem -- Below is the list of all computers that
rem -- vnc will broadcast to.  You can edit this
rem -- list to reflect the names of your computer lab.
rem --

%vnc% pc1
%vnc% pc2
%vnc% pc3
%vnc% pc4
%vnc% pc5
%vnc% pc6
%vnc% pc7
%vnc% pc8
%vnc% pc9
%vnc% pc10
