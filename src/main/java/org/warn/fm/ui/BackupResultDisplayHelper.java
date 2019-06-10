package org.warn.fm.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.warn.fm.backup.BackupResult;

public class BackupResultDisplayHelper {

//	private static final String COPY_ITEM_ACTION = "Copy";
	private static final String FAILED_ITEMS_LBL = "Failed items:";
	private static final String BACKUP_STATUS_LBL = 
			"<html>" +
				"Total Files: %d<br>" + 
				"Duration: %f second(s)<br>" + 
				FAILED_ITEMS_LBL + " %d<br>" +
			"</html>";
	
	private static final int MAIN_PANEL_WIDTH = 450;
	private static final int MAIN_PANEL_HEIGHT = 100;
//	private static final int COPY_BTN_WIDTH = 75;
//	private static final int COPY_BTN_HEIGHT = 25;
	private static final int BACKUP_STATUS_LBL_WIDTH = 400;
	private static final int BACKUP_STATUS_LBL_HEIGHT = 75;
//	private static final int FAILED_ITEMS_LBL_WIDTH = 400;
//	private static final int FAILED_ITEMS_LBL_HEIGHT = 25;
	
	public static JPanel createBackupResultsPanel( BackupResult backupResult ) {
		
		JLabel backupStatusLabel = new JLabel( String.format( BACKUP_STATUS_LBL, 
				backupResult.getTotalFileCount(), backupResult.getDuration(), backupResult.getFailedFiles().size() ) );
		backupStatusLabel.setPreferredSize( new Dimension( BACKUP_STATUS_LBL_WIDTH, BACKUP_STATUS_LBL_HEIGHT ) );
		backupStatusLabel.setMinimumSize( new Dimension( BACKUP_STATUS_LBL_WIDTH, BACKUP_STATUS_LBL_HEIGHT ) );
		
//		JLabel failedItemsLabel = new JLabel(FAILED_ITEMS_LBL);
//		failedItemsLabel.setPreferredSize( new Dimension( FAILED_ITEMS_LBL_WIDTH, FAILED_ITEMS_LBL_HEIGHT ) );
//		failedItemsLabel.setMinimumSize( new Dimension( FAILED_ITEMS_LBL_WIDTH, FAILED_ITEMS_LBL_HEIGHT ) );
		
//		JButton copyBtn = new JButton(COPY_ITEM_ACTION);
//		copyBtn.setPreferredSize( new Dimension( COPY_BTN_WIDTH, COPY_BTN_HEIGHT ) );
//		copyBtn.setMinimumSize( new Dimension( COPY_BTN_WIDTH, COPY_BTN_HEIGHT ) );
//		if( backupResult.getFailedFiles().size() <= 0 ) {
//			copyBtn.setEnabled(false);
//		}
		
		JPanel listPanel = new JPanel();
		listPanel.setPreferredSize( new Dimension( MAIN_PANEL_WIDTH, MAIN_PANEL_HEIGHT ) );
		listPanel.setMinimumSize( new Dimension( MAIN_PANEL_WIDTH, MAIN_PANEL_HEIGHT ) );
		listPanel.add(backupStatusLabel, BorderLayout.LINE_START);
		//listPanel.add(failedItemsLabel, BorderLayout.LINE_START);
		//listPanel.add(copyBtn, BorderLayout.CENTER);
		
		return listPanel;
	}
	
}