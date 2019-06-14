package org.warn.fm.ui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.warn.fm.ui.BackupProgressBar;
import org.warn.fm.ui.BackupProgressBarWorker;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProgressBarActionListener implements ActionListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger( ProgressBarActionListener.class );
	
	private JFrame frame;
	private BackupProgressBarWorker task;
	
	/**
	 * Invoked when the user presses the start button.
	 */
	public void actionPerformed( ActionEvent evt ) {
		if( evt.getActionCommand().equals( BackupProgressBar.CANCEL_BTN ) ) {
			LOGGER.info("Cancel requested. Terminating backup process..");
			task.cancel(true);
			//frame.setCursor(null);
			frame.dispose();
		}
	}
	
}
