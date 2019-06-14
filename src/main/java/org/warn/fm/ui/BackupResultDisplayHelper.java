package org.warn.fm.ui;

//import java.awt.BorderLayout;
//import java.awt.Dimension;
//import java.awt.event.ComponentAdapter;
//import java.awt.event.ComponentEvent;
//import java.text.SimpleDateFormat;
//import java.util.Set;
//import java.util.concurrent.atomic.AtomicInteger;
//
//import javax.swing.JButton;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import javax.swing.JScrollPane;
//import javax.swing.JTable;
//
//import org.warn.fm.model.BackupFile;
//import org.warn.fm.model.BackupResult;
//import org.warn.fm.ui.listeners.BackupResultActionListener;
//import org.warn.fm.util.GlobalConstants;

public class BackupResultDisplayHelper {
//
//	public static final String VIEW_SAVED_FILES_ACTION = "View Saved";
//	public static final String VIEW_FAILED_FILES_ACTION = "View Failed";
//	public static final String SAVE_BACKUP_INFO_ACTION = "Save Backup Info";
//	private static final String FAILED_ITEMS_LBL = "Failed Files:";
//	private static final String BACKUP_STATUS_LBL = // TODO add status, backup location, etc 
//			"<html>" +
//				"Total Files: %d<br>" + 
//				"Duration: %f second(s)<br>" + 
//				FAILED_ITEMS_LBL + " %d<br>" +
//			"</html>";
//	
//	private static final String[] FILE_INFO_TABLE_COLUMNS = { "File Name", "Location", "Status", "Created Date", "Modified Date" };
//	private static final SimpleDateFormat fullTimestampSDF = new SimpleDateFormat( GlobalConstants.FULL_TS_FORMAT );
//	
//	private static final int MAIN_PANEL_WIDTH = 450;
//	private static final int MAIN_PANEL_HEIGHT = 150;
//	private static final int FILE_INFO_PANEL_WIDTH = 900;
//	private static final int FILE_INFO_PANEL_HEIGHT = 600;
//	private static final int FILE_INFO_SCROLL_PANE_WIDTH = 880;
//	private static final int FILE_INFO_SCROLL_PANE_HEIGHT = 580;
//	private static final int VIEW_SAVED_FILES_BTN_WIDTH = 125;
//	private static final int VIEW_SAVED_FILES_BTN_HEIGHT = 25;
//	private static final int VIEW_FAILED_FILES_BTN_WIDTH = 125;
//	private static final int VIEW_FAILED_FILES_BTN_HEIGHT = 25;
//	private static final int SAVE_BACKUP_INFO_BTN_WIDTH = 125;
//	private static final int SAVE_BACKUP_INFO_BTN_HEIGHT = 25;
//	private static final int BACKUP_STATUS_LBL_WIDTH = 400;
//	private static final int BACKUP_STATUS_LBL_HEIGHT = 75;
//	
//	public static JPanel createBackupResultsPanel( BackupResult backupResult, JFrame frame ) {
//		
//		JLabel backupStatusLabel = new JLabel( String.format( BACKUP_STATUS_LBL, 
//				backupResult.getTotalFileCount(), backupResult.getDuration(), backupResult.getFailedFiles().size() ) );
//		backupStatusLabel.setPreferredSize( new Dimension( BACKUP_STATUS_LBL_WIDTH, BACKUP_STATUS_LBL_HEIGHT ) );
//		backupStatusLabel.setMinimumSize( new Dimension( BACKUP_STATUS_LBL_WIDTH, BACKUP_STATUS_LBL_HEIGHT ) );
//		
//		BackupResultActionListener backupResultActionListener = new BackupResultActionListener( backupResult );
//		
//		JButton viewSavedFilesBtn = new JButton(VIEW_SAVED_FILES_ACTION);
//		viewSavedFilesBtn.setPreferredSize( new Dimension( VIEW_SAVED_FILES_BTN_WIDTH, VIEW_SAVED_FILES_BTN_HEIGHT ) );
//		viewSavedFilesBtn.setMinimumSize( new Dimension( VIEW_SAVED_FILES_BTN_WIDTH, VIEW_SAVED_FILES_BTN_HEIGHT ) );
//		if( backupResult.getSavedFiles().size() <= 0 ) {
//			viewSavedFilesBtn.setEnabled(false);
//		}
//		viewSavedFilesBtn.addActionListener(backupResultActionListener);
//		
//		JButton viewFailedFilesBtn = new JButton(VIEW_FAILED_FILES_ACTION);
//		viewFailedFilesBtn.setPreferredSize( new Dimension( VIEW_FAILED_FILES_BTN_WIDTH, VIEW_FAILED_FILES_BTN_HEIGHT ) );
//		viewFailedFilesBtn.setMinimumSize( new Dimension( VIEW_FAILED_FILES_BTN_WIDTH, VIEW_FAILED_FILES_BTN_HEIGHT ) );
//		if( backupResult.getFailedFiles().size() <= 0 ) {
//			viewFailedFilesBtn.setEnabled(false);
//		}
//		viewFailedFilesBtn.addActionListener(backupResultActionListener);
//		
//		JButton saveBackupInfoBtn = new JButton(SAVE_BACKUP_INFO_ACTION);
//		saveBackupInfoBtn.setPreferredSize( new Dimension( SAVE_BACKUP_INFO_BTN_WIDTH, SAVE_BACKUP_INFO_BTN_HEIGHT ) );
//		saveBackupInfoBtn.setMinimumSize( new Dimension( SAVE_BACKUP_INFO_BTN_WIDTH, SAVE_BACKUP_INFO_BTN_HEIGHT ) );
//		saveBackupInfoBtn.addActionListener(backupResultActionListener);
//		
//		JPanel backupResultsPanel = new JPanel();
//		backupResultsPanel.setPreferredSize( new Dimension( MAIN_PANEL_WIDTH, MAIN_PANEL_HEIGHT ) );
//		backupResultsPanel.setMinimumSize( new Dimension( MAIN_PANEL_WIDTH, MAIN_PANEL_HEIGHT ) );
//		backupResultsPanel.add( backupStatusLabel, BorderLayout.LINE_START );
//		backupResultsPanel.add( viewSavedFilesBtn, BorderLayout.LINE_START );
//		backupResultsPanel.add( viewFailedFilesBtn, BorderLayout.CENTER );
//		backupResultsPanel.add( saveBackupInfoBtn, BorderLayout.LINE_END );
//		
//		return backupResultsPanel;
//	}
//	
//	public static void showBackupFileInfo( String title, Set<BackupFile> backupFiles ) {
//		
//		/*
//		DefaultListModel<BackupFile> listModel = new DefaultListModel<BackupFile>();
//		for( BackupFile f: backupFiles ) {
//			listModel.addElement(f);
//		}
//		JList<BackupFile> jList = new JList<BackupFile>(listModel);
//		jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//		//jList.setSelectedIndex(0);
//		jList.setVisibleRowCount(15);
//		*/
//		final AtomicInteger i = new AtomicInteger(0);
//		String[][] data = new String[backupFiles.size()][5];
//		backupFiles.stream().forEachOrdered( f -> {
//			data[i.get()][0] = f.toString();
//			data[i.get()][1] = f.getPath().toString();
//			data[i.get()][2] = f.getStatusMessage();
//			data[i.get()][3] = fullTimestampSDF.format( f.getCreatedTime().toMillis() );
//			data[i.get()][4] = fullTimestampSDF.format( f.getModifiedTime().toMillis() );
//			i.incrementAndGet();
//		});
//		
//		final JTable table = new JTable( data, FILE_INFO_TABLE_COLUMNS );
//        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
//        table.setFillsViewportHeight(true);
//		
//		JScrollPane fileInfoScrollPane = new JScrollPane(table);
//		fileInfoScrollPane.setPreferredSize( new Dimension( FILE_INFO_SCROLL_PANE_WIDTH, FILE_INFO_SCROLL_PANE_HEIGHT ) );
//		fileInfoScrollPane.setMinimumSize( new Dimension( FILE_INFO_SCROLL_PANE_WIDTH, FILE_INFO_SCROLL_PANE_HEIGHT ) );
//		fileInfoScrollPane.createVerticalScrollBar();
//		fileInfoScrollPane.createHorizontalScrollBar();
//		
//		JPanel fileInfoPanel = new JPanel();
//		fileInfoPanel.setPreferredSize( new Dimension( FILE_INFO_PANEL_WIDTH, FILE_INFO_PANEL_HEIGHT ) );
//		fileInfoPanel.setMinimumSize( new Dimension( FILE_INFO_PANEL_WIDTH, FILE_INFO_PANEL_HEIGHT ) );
//		fileInfoPanel.add( fileInfoScrollPane, BorderLayout.LINE_START );
//		fileInfoPanel.addComponentListener( new ComponentAdapter() {
//			@Override
//			public void componentResized(ComponentEvent e) {
//				int width = fileInfoPanel.getWidth();
//				int height = fileInfoPanel.getHeight();
//				fileInfoScrollPane.setPreferredSize( new Dimension( width, height ) );
//				fileInfoScrollPane.setSize( new Dimension( width, height ) );
//			}
//		});
//		
//		JFrame webpageFrame = new JFrame(title);
//		webpageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//		webpageFrame.setPreferredSize( new Dimension( FILE_INFO_PANEL_WIDTH, FILE_INFO_PANEL_HEIGHT ) );
//		webpageFrame.setMinimumSize( new Dimension( FILE_INFO_PANEL_WIDTH, FILE_INFO_PANEL_HEIGHT ) );
//		webpageFrame.add( fileInfoPanel );
//		webpageFrame.setLocationRelativeTo(null); // position to center of screen
//		webpageFrame.pack();
//		webpageFrame.setVisible(true);
//		webpageFrame.setFocusCycleRoot(true);
//		webpageFrame.setFocusable(true);
//		webpageFrame.toFront();
//	}
	
}