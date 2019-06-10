package org.warn.fm.ui;

import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.warn.fm.backup.BackupHelper;
import org.warn.fm.backup.BackupResult;
import org.warn.fm.backup.BackupScanResult;
import org.warn.fm.util.GlobalConstants;

public class BackupProgressBarWorker extends SwingWorker<Void, Integer> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BackupProgressBarWorker.class);
	
	private JFrame frame;
	private BackupHelper backupHelper;
	private BackupScanResult lastScanResult;
	private String backupLocationTxt;
	private BackupResult lastBackupResult;
	private JLabel statusLbl;
	
	public BackupProgressBarWorker( JFrame frame, BackupHelper backupHelper, BackupScanResult lastScanResult, String backupLocationTxt, JLabel statusLbl ) {
		this.frame = frame;
		this.backupHelper = backupHelper;
		this.lastScanResult = lastScanResult;
		this.backupLocationTxt = backupLocationTxt;
		this.statusLbl = statusLbl;
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
		if( lastBackupResult != null ) {
			statusLbl.setText("Backup process completed in " + lastBackupResult.getDuration() + " second(s)");
			LOGGER.debug("Backup process completed in " + lastBackupResult.getDuration() + " second(s)");
			JPanel backupResultPanel = ListManagerHelper.createBackupResultListPanel( lastBackupResult );
			JOptionPane.showOptionDialog( frame, backupResultPanel, GlobalConstants.BACKUP_RESULTS, JOptionPane.NO_OPTION, -1, null, new Object[]{}, null );
		}
	}
	
}
