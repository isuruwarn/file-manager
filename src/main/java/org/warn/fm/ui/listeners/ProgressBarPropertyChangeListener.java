package org.warn.fm.ui.listeners;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JProgressBar;

public class ProgressBarPropertyChangeListener implements PropertyChangeListener {

	private JProgressBar progressBar;
	
	public ProgressBarPropertyChangeListener( JProgressBar progressBar ) {
		this.progressBar = progressBar;
	}
	
	/**
	 * Invoked when task's progress property changes.
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			int progress = (Integer) evt.getNewValue();
			progressBar.setValue(progress);
			//taskOutput.append(String.format("Completed %d%% of task.\n", task.getProgress()));
		}
	}
}
