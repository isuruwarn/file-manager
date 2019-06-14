package org.warn.fm.ui.listeners;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JProgressBar;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProgressBarPropertyChangeListener implements PropertyChangeListener {

	private JProgressBar progressBar;
	
	/**
	 * Invoked when task's progress property changes.
	 */
	public void propertyChange( PropertyChangeEvent evt ) {
		if( "progress" == evt.getPropertyName() ) {
			int progress = (Integer) evt.getNewValue();
			progressBar.setValue(progress);
		}
	}
}
