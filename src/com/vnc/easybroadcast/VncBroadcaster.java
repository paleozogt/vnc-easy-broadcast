package com.vnc.easybroadcast;

import java.io.IOException;
import java.util.Formatter;

public class VncBroadcaster {
	// TODO: make this platform-independent
	private static String vncBroadcastCmd= "winvnc -connect ";
	private static int waittime= 3000;
	private static Runtime runtime= Runtime.getRuntime();

	synchronized public static void broadcast(String host) {
		try {
			runtime.exec(vncBroadcastCmd + host);
			sleep(waittime);
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	
	private static void sleep(int ms) {
		try {
			Thread.sleep(waittime);
		} catch (Exception e) {		
		}
	}
}
