package org.warn.fm.ui;

import java.awt.Cursor;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.warn.fm.backup.BackupHelper;
import org.warn.fm.backup.BackupScanResult;
import org.warn.fm.ui.listeners.ProgressBarActionListener;
import org.warn.fm.ui.listeners.ProgressBarPropertyChangeListener;

public class ProgressBar {
	
//	private static final int FRAME_WIDTH = 225;
//	private static final int FRAME_HEIGHT = 150;
//	
//	private static final String TITLE = "Backup progress";
//	private static final String CANCEL_BTN = "Cancel";
//	
//	private AtomicBoolean cancelPressed;
//	private JFrame frame;
//	private JProgressBar progressBar;
//	private JButton cancelButton;
	
	private JProgressBar progressBar;
	private JButton startButton;
	private JPanel panel;
	private JFrame frame;
	private ProgressBarPropertyChangeListener pbPropertyChangeListener;
	private ProgressBarTask task;
	
	public ProgressBar( BackupHelper backupHelper, BackupScanResult lastScanResult, String backupLocationTxt, JLabel statusLbl ) {
//		
//		this.cancelPressed = new AtomicBoolean(false);
//		cancelButton = new JButton(CANCEL_BTN);
//		cancelButton.addActionListener( new ProgressBarActionListener() );
		
		frame = new JFrame("Backing up files");
		
		// Create the demo's UI.
		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		
		startButton = new JButton("Cancel");
		startButton.setActionCommand("cancel");
		
		pbPropertyChangeListener = new ProgressBarPropertyChangeListener( progressBar );
		
		task = new ProgressBarTask( this.startButton, frame, backupHelper, lastScanResult, backupLocationTxt, statusLbl );
		task.addPropertyChangeListener( pbPropertyChangeListener );
		
		startButton.addActionListener( new ProgressBarActionListener( startButton, pbPropertyChangeListener, frame, task ) );
		
		panel = new JPanel();
		panel.add(progressBar);
		panel.add(startButton);
		panel.setBorder( BorderFactory.createEmptyBorder(20, 20, 20, 20) );
		
		createAndShowGUI();
	}
	
	/**
	 * Create the GUI and show it. As with all GUI code, this must run on the
	 * event-dispatching thread.
	 */
	public void createAndShowGUI() {
		// Create and set up the window.
//		frame = new JFrame("Backing up files");
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create and set up the content pane.
		//JComponent newContentPane = new ProgressBar( progressBarActionListener );
		this.panel.setOpaque(true); // content panes must be opaque
		frame.setContentPane( this.panel );

		// Display the window.
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null); // position to center of screen
		//frame.setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) );
		
//		startButton.doClick();
		
//		task = new ProgressBarTask( this.startButton, frame );
//		task.addPropertyChangeListener( pbPropertyChangeListener );
		task.execute();
	}

//	public JProgressBar getProgressBar() {
//		return progressBar;
//	}
	
}
