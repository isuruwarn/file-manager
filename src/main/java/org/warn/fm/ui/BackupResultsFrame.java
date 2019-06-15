package org.warn.fm.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.warn.fm.model.BackupResult;
import org.warn.fm.model.BackupStatus;
import org.warn.fm.ui.listeners.BackupResultActionListener;
import org.warn.fm.util.GlobalConstants;

public class BackupResultsFrame {

	public static final String VIEW_SAVED_FILES_ACTION = "View Saved";
	public static final String VIEW_FAILED_FILES_ACTION = "View Failed";
	
	private static final int MAIN_PANEL_WIDTH = 500;
	private static final int MAIN_PANEL_HEIGHT = 225;
	private static final int VIEW_SAVED_FILES_BTN_WIDTH = 125;
	private static final int VIEW_SAVED_FILES_BTN_HEIGHT = 25;
	private static final int VIEW_FAILED_FILES_BTN_WIDTH = 125;
	private static final int VIEW_FAILED_FILES_BTN_HEIGHT = 25;
	private static final int BACKUP_STATUS_LBL_WIDTH = 400;
	private static final int BACKUP_STATUS_LBL_HEIGHT = 125;
	
	public BackupResultsFrame( BackupResult backupResult ) {
		
		String statusIcon = "/img/success48.png";
		if( backupResult.getBackupStatus().equals( BackupStatus.SOME_FAILED) ) {
			statusIcon = "/img/some_failed48.png";
		} else if( backupResult.getBackupStatus().equals( BackupStatus.FAILED) ) {
			statusIcon = "/img/failed48.png";
		}
		
		JLabel backupStatusIcon = new JLabel( new ImageIcon( getClass().getResource(statusIcon) ) );
		JLabel backupStatusLabel = new JLabel( backupResult.getBackupResultsSummaryHTML() );
		backupStatusLabel.setPreferredSize( new Dimension( BACKUP_STATUS_LBL_WIDTH, BACKUP_STATUS_LBL_HEIGHT ) );
		backupStatusLabel.setMinimumSize( new Dimension( BACKUP_STATUS_LBL_WIDTH, BACKUP_STATUS_LBL_HEIGHT ) );
		
		BackupResultActionListener backupResultActionListener = new BackupResultActionListener( backupResult );
		
		JButton viewSavedFilesBtn = new JButton(VIEW_SAVED_FILES_ACTION);
		viewSavedFilesBtn.setPreferredSize( new Dimension( VIEW_SAVED_FILES_BTN_WIDTH, VIEW_SAVED_FILES_BTN_HEIGHT ) );
		viewSavedFilesBtn.setMinimumSize( new Dimension( VIEW_SAVED_FILES_BTN_WIDTH, VIEW_SAVED_FILES_BTN_HEIGHT ) );
		if( backupResult.getSavedFiles().size() <= 0 ) {
			viewSavedFilesBtn.setEnabled(false);
		}
		viewSavedFilesBtn.addActionListener(backupResultActionListener);
		
		JButton viewFailedFilesBtn = new JButton(VIEW_FAILED_FILES_ACTION);
		viewFailedFilesBtn.setPreferredSize( new Dimension( VIEW_FAILED_FILES_BTN_WIDTH, VIEW_FAILED_FILES_BTN_HEIGHT ) );
		viewFailedFilesBtn.setMinimumSize( new Dimension( VIEW_FAILED_FILES_BTN_WIDTH, VIEW_FAILED_FILES_BTN_HEIGHT ) );
		if( backupResult.getFailedFiles().size() <= 0 ) {
			viewFailedFilesBtn.setEnabled(false);
		}
		viewFailedFilesBtn.addActionListener(backupResultActionListener);
		
		JPanel backupResultsPanel = new JPanel();
		backupResultsPanel.setPreferredSize( new Dimension( MAIN_PANEL_WIDTH, MAIN_PANEL_HEIGHT ) );
		backupResultsPanel.setMinimumSize( new Dimension( MAIN_PANEL_WIDTH, MAIN_PANEL_HEIGHT ) );
		backupResultsPanel.add( backupStatusIcon, BorderLayout.NORTH );
		backupResultsPanel.add( backupStatusLabel, BorderLayout.LINE_START );
		backupResultsPanel.add( viewSavedFilesBtn, BorderLayout.LINE_START );
		backupResultsPanel.add( viewFailedFilesBtn, BorderLayout.CENTER );
		
		JFrame backupResultsFrame = new JFrame( GlobalConstants.BACKUP_RESULTS );
		backupResultsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		backupResultsFrame.setPreferredSize( new Dimension( MAIN_PANEL_WIDTH, MAIN_PANEL_HEIGHT ) );
		backupResultsFrame.setMinimumSize( new Dimension( MAIN_PANEL_WIDTH, MAIN_PANEL_HEIGHT ) );
		backupResultsFrame.setResizable(false);
		backupResultsFrame.add( backupResultsPanel );
		backupResultsFrame.setLocationRelativeTo(null); // position to center of screen
		backupResultsFrame.pack();
		backupResultsFrame.setVisible(true);
	}
	
}