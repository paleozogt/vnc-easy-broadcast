package com.vnc.easybroadcast;

import java.io.File;
import java.io.IOException;
import java.util.Formatter;
import java.util.Iterator;

import com.util.gui.ProgressModel;
import com.vnc.VncViewerInfo;
import com.vnc.VncViewersList;

public class VncBroadcaster {
	// TODO: make the vnc command platform-independent
	private static String FMT_VNCCMD= "%s -connect %s";
	private static String VNCPROG= "winvnc";
	private static int waittime= 3500;
	private static Runtime runtime= Runtime.getRuntime();

	public static void broadcast(VncViewersList clients, ProgressModel prog) throws IOException {
		Iterator<VncViewerInfo> it= clients.iterator();
		while (it.hasNext()) {					
			VncViewerInfo client= it.next();
			String host= client.getHost();

			if (prog != null)
				prog.setMessage(host);
			
			broadcast(host);
			
			if (prog != null)
				prog.setValue(prog.getValue()+1);
		}
	}
	
	synchronized public static void broadcast(String host) throws IOException {
		String cmd= getBroadcastCmd(host);
		System.out.println(cmd);
		runtime.exec(cmd);
		sleep(waittime);
	}
	
	static String getBroadcastCmd(String host) {
		StringBuilder sb= new StringBuilder();
		Formatter fmt= new Formatter(sb);
		
		File vncprog= new File(System.getProperty("user.dir"), VNCPROG);
		
		fmt.format(FMT_VNCCMD, vncprog.toString(), host);
		return sb.toString();
	}
	
	private static void sleep(int ms) {
		try {
			Thread.sleep(waittime);
		} catch (Exception e) {		
		}
	}
}
