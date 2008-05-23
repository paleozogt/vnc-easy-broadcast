package com.util.gui;

import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JDialog;
import javax.swing.JProgressBar;
import javax.swing.BoundedRangeModel; 
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ProgressBarDialog extends JDialog {
	JProgressBar bar;
	DefaultProgressModel barModel;
	String origTitle;

	public ProgressBarDialog(Frame owner, String title, int min, int max ) {
		super(owner, title);
		origTitle= title;
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

		barModel= new DefaultProgressModel(min, max);
		bar= new JProgressBar(barModel);
		barModel.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				updateTitle();
			}
		});
		
		setPreferredSize(new Dimension(300, 75));
		add(bar);
		pack();
		setLocationRelativeTo(this);			
	}

	public ProgressModel getModel() {
		return barModel;
	}
	
	void updateTitle() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setTitle(origTitle + " " + barModel.getMessage());
				bar.updateUI();
			}
		});
	}
}

class DefaultProgressModel 
	extends DefaultBoundedRangeModel 
	implements ProgressModel {

	public DefaultProgressModel(int min, int max) {
		super(0, 0, min, max);
	}

	private String message;
	public void setMessage(String message) { 
		this.message= message;
		this.fireStateChanged();
	}
	public String getMessage() { return message; }
}

