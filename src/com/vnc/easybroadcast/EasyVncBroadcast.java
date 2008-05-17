package com.vnc.easybroadcast;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.List;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ListIterator;
import java.awt.event.WindowAdapter;

public class EasyVncBroadcast  extends Frame
{

	private static String WIN_TITLE= "Easy Vnc Broadcast";
	private static String BTN_LOAD= "Load List";
	private static String BTN_SAVE= "Save List";
	private static String BTN_BROADCAST= "Broadcast";
	private static String BTN_DISCONNECT= "Disconnect";

	public static void main(String[] args) {
		EasyVncBroadcast window= new EasyVncBroadcast();
	
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
		Panel panel= new Panel(layout);
		panel.setBackground(Color.LIGHT_GRAY);
		add(panel);
	    panel.setPreferredSize(new Dimension(250, 250));

	    List vnclist= new List(15);
	    vnclist.setSize(
	    		new Dimension(vnclist.getWidth(), vnclist.getHeight()*2));
	    


	    Panel listPanel= new Panel();
	    listPanel.add(vnclist);
	    
	    panel.add(listPanel);
	    
	    Panel btnPanel= new Panel();
	    

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
	    
	    panel.add(btnPanel);
	    
		setTitle(WIN_TITLE);
		setVisible(true);
		pack();
	}

	private static Button addButton(Panel panel, String label, ActionListener action) {
		Button btn= new Button(label);
		btn.addActionListener(action);
		panel.add(btn);
		return btn;
	}
	
	private void quit() {
		this.dispose();
		System.exit(0);
	}
	
	void broadcast() {
		
	}
	
	void disconnect() {
		
	}
	
	void loadVncClients() {
		
	}
	
	void saveVncClients() {
		
	}
}
