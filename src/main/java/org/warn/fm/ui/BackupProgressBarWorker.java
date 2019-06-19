package org.warn.fm.ui;

import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.warn.fm.backup.BackupHelper;
import org.warn.fm.model.BackupResult;
import org.warn.fm.model.BackupScanResult;
import org.warn.utils.datetime.DateTimeUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BackupProgressBarWorker extends SwingWorker<Void, Integer> {
	
	private JFrame frame;
	private BackupHelper backupHelper;
	private BackupScanResult lastScanResult;
	private String backupLocationTxt;
	private BackupResult lastBackupResult;
	private JLabel statusLbl;
	private JTextField scanFromDateTxt;
	private JButton backupBtn;
	
	public BackupProgressBarWorker( JFrame frame, BackupHelper backupHelper, BackupScanResult lastScanResult, String backupLocationTxt, 
			JLabel statusLbl, JTextField scanFromDateTxt, JButton backupBtn ) {
		this.frame = frame;
		this.backupHelper = backupHelper;
		this.lastScanResult = lastScanResult;
		this.backupLocationTxt = backupLocationTxt;
		this.statusLbl = statusLbl;
		this.scanFromDateTxt = scanFromDateTxt;
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
		SwingUtilities.invokeLater( () -> {
			backupBtn.setEnabled(false);
			if( lastBackupResult != null ) {
				String statusMessage = "Backup process completed in " + lastBackupResult.getDuration() + " second(s)";
				log.debug(statusMessage);
				statusLbl.setText(statusMessage);
				scanFromDateTxt.setText( DateTimeUtil.fullTimestampSDF.format( lastBackupResult.getBackupTime().getTimeInMillis() ) );
				new BackupResultsFrame( lastBackupResult );
			}
		});
	}
	
}
