package com.util.gui;

import javax.swing.BoundedRangeModel;

public interface ProgressModel extends BoundedRangeModel {
	public String getMessage();
	public void setMessage(String message);
}
