package com.vnc.easybroadcast;

import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Iterator;
import java.awt.event.WindowAdapter;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.collections.iterators.ArrayIterator;

import com.vnc.*;

public class VncEasyBroadcast
{
	private static String CLI_BCASTFILE= "broadcastfile";	
	private static Option cliBcasfile = 
		OptionBuilder.withArgName( CLI_BCASTFILE )
				    .hasArg()
				    .withDescription(  "broadcast file" )
				    .create( CLI_BCASTFILE );
	
	public static void main(String[] args) {
		try {
			System.out.println("class path= " + 
					System.getProperties().getProperty("java.class.path"));
			
			//System.setProperty("user.dir", "C:\\Program Files\\tightvnc");
			
			UIManager.setLookAndFeel(
		            UIManager.getSystemLookAndFeelClassName());
				
			// parse the command line
			Options options = new Options();
			options.addOption(cliBcasfile);
		    CommandLineParser parser = new GnuParser();
	        CommandLine cli = parser.parse( options, args );

	        // get the broadcast file if it was explicitly
	        // specified, or if it was the only argument
	        String bcastfile= null;
	        if (cli.hasOption(CLI_BCASTFILE)) {
	        	bcastfile= cli.getOptionValue(CLI_BCASTFILE);
	        } else if (args.length == 1) {
	        	bcastfile= args[0];
	        }

	        if (bcastfile != null) {
	        	File file= new File(bcastfile);
	        	VncViewersList clients= 
	        		new VncViewersList(file, null);
	        	VncBroadcaster.broadcast(clients, null);
			} else {
	        	VncEasyBroadcastWindow window= new VncEasyBroadcastWindow();
	        }
		} catch (Exception e) {
			System.out.println(e);			
		}

	}
}
