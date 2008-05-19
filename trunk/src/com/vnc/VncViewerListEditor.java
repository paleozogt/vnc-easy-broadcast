package com.vnc;

import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.collections.iterators.ArrayIterator;

import com.vnc.easybroadcast.VncBroadcaster;

public class VncViewerListEditor extends JPanel {
	private static String WIN_TITLE= "Easy Vnc Broadcast";
	private static String BTN_LOAD= "Load List";
	private static String BTN_SAVE= "Save List";

	private static String BTN_ADD= "Add Client";
	private static String BTN_DEL= "Delete Client";

	private static String PROMPT_ADDCLIENT= "Enter the client hostname";
	
	VncViewersList clients= new VncViewersList();
	JList clientList= new JList(clients);	
	
	JFrame parent;
	
	public VncViewerListEditor(JFrame parent) {	
		this.parent= parent;
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
	}

	private JPanel makeButtonPanel() {
	    JPanel btnPanel= new JPanel();
	    
	    addButton(btnPanel, BTN_ADD, new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				addClient();
			}
	    });
	    addButton(btnPanel, BTN_DEL, new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				delClient();
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
	    
	    return btnPanel;
	}
	
	private static JButton addButton(JPanel panel, String label, ActionListener action) {
		JButton btn= new JButton(label);
		btn.addActionListener(action);
		panel.add(btn);
		return btn;
	}
	
	void addClient() {		
		 String inputValue = JOptionPane.showInputDialog(PROMPT_ADDCLIENT);
		 if (inputValue == null) return;
		 clients.add(new VncViewerInfo(inputValue));
		 clientList.updateUI();
	}
	
	void delClient() {
		// get the selected indices, but
		// make sure to reverse-sort it
		// so that we can delete them all
		// from the list at once without worrying
		// about adjusting each index
		List<Integer> selected= 
			IteratorUtils.toList(new ArrayIterator(clientList.getSelectedIndices()));
		Collections.reverse(selected);
		Iterator<Integer> it= selected.iterator();
		
		while (it.hasNext()) {
			int idx= it.next();
			clients.remove(idx);			
		}
		clientList.updateUI();
		clientList.clearSelection();
	}
	
	void loadVncClients() {
		FileDialog dlg= new FileDialog(parent, BTN_LOAD, FileDialog.LOAD);
		dlg.setVisible(true);
		if (dlg.getFile() == null) return;

		File file= new File(dlg.getDirectory(), dlg.getFile());
		System.out.println("loading " + file);
		clients.loadHosts(file, null);
		clientList.updateUI();
	}
	
	void saveVncClients() {
		FileDialog dlg= new FileDialog(parent, BTN_SAVE, FileDialog.SAVE);
		dlg.setVisible(true);
		if (dlg.getFile() == null) return;
		
		File file= new File(dlg.getDirectory(), dlg.getFile());
		System.out.println("loading " + file);
		clients.saveToFile(file);
	}
}
