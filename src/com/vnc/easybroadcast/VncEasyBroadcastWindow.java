package com.vnc.easybroadcast;

import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Iterator;

import javax.swing.BoundedRangeModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

import com.vnc.VncViewerInfo;
import com.vnc.VncViewerListEditor;
import com.vnc.VncViewersList;

public class VncEasyBroadcastWindow extends JFrame {
	private static String WIN_TITLE= "Vnc Easy Broadcast";
	private static String BTN_BROADCAST= "Broadcast";
	private static String BTN_EDIT= "Edit Broacast List";
	private static String DLG_BROADCAST= "Choose broadcast file...";
	private static String PROG_BROADCAST= "Broadcasting...";

	public VncEasyBroadcastWindow() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);		
				if(e.getComponent() == VncEasyBroadcastWindow.this) {
					quit();
				}
			}
		});

		JPanel panel= new JPanel();

		addButton(panel, BTN_BROADCAST, new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				broadcast();
			}
		});

		addButton(panel, BTN_EDIT, new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				editBroadcastList();
			}
		});	    

		add(panel);
		panel.setPreferredSize(new Dimension(250, 100));
		setTitle(WIN_TITLE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void quit() {
		this.dispose();
		System.exit(0);
	}

	private static JButton addButton(JPanel panel, String label, ActionListener action) {
		JButton btn= new JButton(label);
		btn.addActionListener(action);
		panel.add(btn);
		return btn;
	}

	private void broadcast() {

		// ask the user to load the file
		FileDialog dlg= new FileDialog(this, DLG_BROADCAST, FileDialog.LOAD);
		dlg.setVisible(true);
		if (dlg.getFile() == null) return;

		// load the file
		// TODO: prompt for encryption password
		File file= new File(dlg.getDirectory(), dlg.getFile());
		System.out.println("loading " + file);
		final VncViewersList clients= new VncViewersList();
		clients.loadHosts(file, "");
		System.out.println("loaded " + clients.size());

		// set up progress bar window
		final JDialog progwin= new JDialog();
		final JProgressBar bar= new JProgressBar(0, clients.size());
		progwin.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		progwin.setPreferredSize(new Dimension(300, 75));
		progwin.setTitle(PROG_BROADCAST);
		progwin.add(bar);
		progwin.pack();
		progwin.setLocationRelativeTo(this);
		progwin.setModal(true);

		// broadcast to everybody, but do it 
		// on the background thread so that we
		// can show a progress dialog
		new Thread() {
			public void run() { 
				try {
					VncBroadcaster.broadcast(clients, bar.getModel());					
				} catch (Exception e) {
					System.out.println(e);
				}

				// close the progress dialog on the swing thread
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						progwin.setVisible(false);
					}
				});					
			}
		}.start();

		progwin.setVisible(true);
		this.quit();
	}

	private void editBroadcastList() {
		final JFrame edframe= new JFrame();
		edframe.add(new VncViewerListEditor(this));
		edframe.setTitle(BTN_EDIT);
		edframe.pack();
		edframe.setLocationRelativeTo(this);
		edframe.setVisible(true);
		
		// hide this window until the 
		// other window closes
		this.setVisible(false);
		edframe.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);		
				if(e.getComponent() == edframe) {
					VncEasyBroadcastWindow.this.setVisible(true);
				}
			}
		});
	}
}
