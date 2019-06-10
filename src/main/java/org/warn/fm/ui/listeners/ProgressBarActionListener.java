package org.warn.fm.ui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.warn.fm.ui.ProgressBarTask;

public class ProgressBarActionListener implements ActionListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger( ProgressBarActionListener.class );
	
	private JFrame frame;
	private ProgressBarTask task;
	private JButton startButton;
	private ProgressBarPropertyChangeListener pbPropertyChangeListener;
	
	public ProgressBarActionListener( JButton startButton, ProgressBarPropertyChangeListener pbPropertyChangeListener, 
			JFrame frame, ProgressBarTask task ) {
		this.startButton = startButton;
		this.pbPropertyChangeListener = pbPropertyChangeListener;
		this.frame = frame;
		this.task = task;
	}
	
	/**
	 * Invoked when the user presses the start button.
	 */
	public void actionPerformed(ActionEvent evt) {
//		startButton.setEnabled(false);
		//setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		// Instances of javax.swing.SwingWorker are not reusuable, so
		// we create new instances as needed.
//		task = new ProgressBarTask( this.startButton );
//		task.addPropertyChangeListener( pbPropertyChangeListener );
//		task.execute();
		
		LOGGER.debug("cancel pressed..");
		task.cancel(true);
		//frame.setCursor(null);
		frame.dispose();
	}
	
}
