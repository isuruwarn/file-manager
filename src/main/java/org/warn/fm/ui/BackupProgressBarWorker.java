package org.warn.fm.ui;

import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingWorker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.warn.fm.backup.BackupHelper;
import org.warn.fm.model.BackupResult;
import org.warn.fm.model.BackupScanResult;

public class BackupProgressBarWorker extends SwingWorker<Void, Integer> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BackupProgressBarWorker.class);
	
	private JFrame frame;
	private BackupHelper backupHelper;
	private BackupScanResult lastScanResult;
	private String backupLocationTxt;
	private BackupResult lastBackupResult;
	private JLabel statusLbl;
	private JButton backupBtn;
	
	public BackupProgressBarWorker( JFrame frame, BackupHelper backupHelper, BackupScanResult lastScanResult, String backupLocationTxt, 
			JLabel statusLbl, JButton backupBtn ) {
		this.frame = frame;
		this.backupHelper = backupHelper;
		this.lastScanResult = lastScanResult;
		this.backupLocationTxt = backupLocationTxt;
		this.statusLbl = statusLbl;
		this.backupBtn = backupBtn;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		lastBackupResult = backupHelper.backup( lastScanResult.getNewOrModifiedFiles(), backupLocationTxt, this );
		return null;
	}
	
	public void setTaskBarProgress( int progress ) {
		setProgress(progress);
	}
	
	/*
	 * Executed in event dispatching thread
	 */
	@Override
	public void done() {
		Toolkit.getDefaultToolkit().beep();
		frame.dispose();
		backupBtn.setEnabled(false);
		if( lastBackupResult != null ) {
			statusLbl.setText("Backup process completed in " + lastBackupResult.getDuration() + " second(s)");
			LOGGER.debug("Backup process completed in " + lastBackupResult.getDuration() + " second(s)");
			new BackupResultsFrame( lastBackupResult );
		}
	}
	
}
