package com.vnc.easybroadcast;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;
import java.awt.event.WindowAdapter;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;

import org.apache.commons.collections.iterators.ArrayIterator;

import com.vnc.*;

public class EasyVncBroadcast  extends JFrame
{
	private static String WIN_TITLE= "Easy Vnc Broadcast";
	private static String BTN_LOAD= "Load List";
	private static String BTN_SAVE= "Save List";

	private static String BTN_ADD= "Add Client";
	private static String BTN_DEL= "Delete Client";

	private static String BTN_BROADCAST= "Broadcast";
	private static String BTN_DISCONNECT= "Disconnect";

	VncViewersList clients= new VncViewersList();
	JList clientList= new JList(clients);
	
	public static void main(String[] args) {
		try {
			System.out.println("class path= " + 
					System.getProperties().getProperty("java.class.path"));
			
			
			
			UIManager.setLookAndFeel(
		            UIManager.getSystemLookAndFeelClassName());
			EasyVncBroadcast window= new EasyVncBroadcast();
		} catch (Exception e) {		
		}

	}

	public EasyVncBroadcast() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);		
				if(e.getComponent() == EasyVncBroadcast.this) {
					quit();
				}
			}
		});
		
		GridLayout layout= new GridLayout(1, 2, 1, 1);
		JPanel panel= new JPanel(layout);
		add(panel);
	    panel.setPreferredSize(new Dimension(380, 180));

	    clientList.setFixedCellWidth(150);
	    clientList.setVisibleRowCount(10);

	    JPanel listPanel= new JPanel();
	    listPanel.add(new JScrollPane(clientList, 
	    		ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
	    		ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER));	    
	    panel.add(listPanel);

	    panel.add(makeButtonPanel());
	    
		setTitle(WIN_TITLE);
		setLocationRelativeTo(null);
		setVisible(true);
		pack();
	}

	private JPanel makeButtonPanel() {
	    JPanel btnPanel= new JPanel();
	    
	    addButton(btnPanel, BTN_ADD, new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
			}
	    });
	    addButton(btnPanel, BTN_DEL, new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
			}
	    });	    
	    	    
	    addButton(btnPanel, BTN_LOAD, new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				loadVncClients();
			}
	    });
	    addButton(btnPanel, BTN_SAVE, new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				saveVncClients();
			}
	    });
	    
	    addButton(btnPanel, BTN_BROADCAST, new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				broadcast();
			}
	    });
	    addButton(btnPanel, BTN_DISCONNECT, new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				disconnect();
			}
	    });	    
	    
	    return btnPanel;
	}
	
	private static JButton addButton(JPanel panel, String label, ActionListener action) {
		JButton btn= new JButton(label);
		btn.addActionListener(action);
		panel.add(btn);
		return btn;
	}
	
	private void quit() {
		this.dispose();
		System.exit(0);
	}
	
	void broadcast() {
		Iterator<Integer> it= new ArrayIterator(clientList.getSelectedIndices());
		while (it.hasNext()) {
			VncViewerInfo clientinfo= clients.get(it.next());
			VncBroadcaster.broadcast(clientinfo.getHost());
		}
	}
	
	void disconnect() {
		
	}
	
	void loadVncClients() {
		FileDialog dlg= new FileDialog(this, BTN_LOAD, FileDialog.LOAD);
		dlg.setVisible(true);
		if (dlg.getFile() == null) return;

		// TODO: prompt for encryption password
		File file= new File(dlg.getDirectory(), dlg.getFile());
		System.out.println("loading " + file);
		clients.loadHosts(file, "");
		clientList.updateUI();
	}
	
	void saveVncClients() {
		FileDialog dlg= new FileDialog(this, BTN_SAVE, FileDialog.SAVE);
		dlg.setVisible(true);
		if (dlg.getFile() == null) return;
		
		// TODO: prompt for encryption password
		File file= new File(dlg.getDirectory(), dlg.getFile());
		System.out.println("loading " + file);
		clients.saveToFile(file);
	}
}
