package org.warn.fm.ui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import org.warn.fm.ui.BackupProgressBar;
import org.warn.fm.ui.BackupProgressBarWorker;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class ProgressBarActionListener implements ActionListener {
	
	private JFrame frame;
	private BackupProgressBarWorker task;
	
	/**
	 * Invoked when the user presses the start button.
	 */
	public void actionPerformed( ActionEvent evt ) {
		if( evt.getActionCommand().equals( BackupProgressBar.CANCEL_BTN ) ) {
			log.info("Cancel requested. Terminating backup process..");
			task.cancel(true);
			//frame.setCursor(null);
			frame.dispose();
		}
	}
	
}
