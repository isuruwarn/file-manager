package org.warn.fm.ui;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.warn.fm.backup.BackupHelper;
import org.warn.fm.model.BackupScanResult;
import org.warn.fm.ui.listeners.ProgressBarActionListener;
import org.warn.fm.ui.listeners.ProgressBarPropertyChangeListener;

public class BackupProgressBar {
	
	public static final String CANCEL_BTN = "Cancel";
	
	private static final int FRAME_WIDTH = 275;
	private static final int FRAME_HEIGHT = 150;
	private static final String TITLE = "Backing up files";

	private JProgressBar progressBar;
	private JButton cancelButton;
	private JPanel panel;
	private JFrame frame;
	private ProgressBarPropertyChangeListener pbPropertyChangeListener;
	private BackupProgressBarWorker task;
	
	public BackupProgressBar( 
			BackupHelper backupHelper, BackupScanResult lastScanResult, String backupLocationTxt, JLabel statusLbl, JButton backupBtn ) {
		
		frame = new JFrame(TITLE);
		
		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		
		pbPropertyChangeListener = new ProgressBarPropertyChangeListener( progressBar );
		task = new BackupProgressBarWorker( frame, backupHelper, lastScanResult, backupLocationTxt, statusLbl, backupBtn );
		task.addPropertyChangeListener( pbPropertyChangeListener );
		
		cancelButton = new JButton(CANCEL_BTN);
		cancelButton.setActionCommand(CANCEL_BTN);
		cancelButton.addActionListener( new ProgressBarActionListener( frame, task ) );
		
		panel = new JPanel();
		panel.add(progressBar);
		panel.add(cancelButton);
		panel.setBorder( BorderFactory.createEmptyBorder(20, 20, 20, 20) );
		panel.setOpaque(true); // content panes must be opaque
		
	}
	
	public void displayAndExecute() {
		
		// display the window
		frame.setPreferredSize( new Dimension( FRAME_WIDTH, FRAME_HEIGHT ) );
		frame.setMinimumSize( new Dimension( FRAME_WIDTH, FRAME_HEIGHT ) );
		frame.setContentPane( this.panel );
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null); // position to center of screen
		//frame.setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) );
		
		task.execute();
	}
	
}
