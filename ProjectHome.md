VNC is a great tool for remote-controlling other computers.  A little-known feature is that you can also use VNC to _broadcast_ your screen to other computers.  Broadcasting your screen can be great for teaching computer concepts to a lab or doing demos.  You don't even need an LCD projector!

Unfortunately, the major flavors of VNC ([TightVNC](http://www.tightvnc.com/), [UltraVNC](http://www.uvnc.com/), [RealVNC](http://www.realvnc.com/)) don't make broadcasting very easy.  It requires you to explicitly configure each computer to accept broadcasts, which can be rather time-consuming.  Also, while TightVNC and UltraVNC have an "Add New Client" menu, they don't have a "Add Many Clients".  To do it you have to set up DOS batch files, which is not very easy for someone who just wants to start broadcasting.

VNC Easy Broadcast aims to make this process easier.  The installer automatically configures VNC to accept broadcasts.  And you can save lists of computers to a file and broadcast to them with the click of a button.

After installing VNC Easy Broadcast, look for "VncEasyBroadcast" in your Start menu in the folder of your VNC software.  (For example, if you are using TightVNC, VncEasyBroadcast will be under "TightVNC".)



---


Features:
  * Sets up listening vncviewer as a service
  * Creates firewall exceptions for vnc server and listening viewer
  * Locks out users from changing/quitting the vnc server
  * Locks out users from quitting the listening vnc viewer
  * Save/load lists of computers to a file
  * Files compatible with [VNC Thumbnail Viewer](http://thetechnologyteacher.wordpress.com/vncthumbnailviewer/), allowing you to broadcast to your lab for teaching one moment, and observe them the next, all using the same file.

Requirements:
  * MS-Windows sp2 (MacOS X and Ubuntu support coming soon!)
  * [Java](http://www.java.com/getjava/) 1.5 or greater
  * Either [TightVNC](http://www.tightvnc.com/) or [UltraVNC](http://www.uvnc.com/) already installed


---


23-May-08: 1.2 Released
  * Added gui for easy broadcasting
  * Better installer

24-Apr-08: 1.1 Released
  * Installer automates broadcast setup