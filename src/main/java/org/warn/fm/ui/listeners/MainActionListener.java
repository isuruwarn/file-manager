package org.warn.fm.ui.listeners;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultTreeModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.warn.fm.backup.BackupHelper;
import org.warn.fm.backup.BackupScanResult;
import org.warn.fm.ui.FileTreeHelper;
import org.warn.fm.ui.ListManagerHelper;
import org.warn.fm.ui.ProgressBar;
import org.warn.fm.ui.UIContainer;
import org.warn.fm.util.FileManagerUtil;
import org.warn.fm.util.GlobalConstants;
import org.warn.utils.swing.UICommons;

public class MainActionListener implements ActionListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MainActionListener.class);
	
	private BackupHelper backupHelper;
	private JFrame mainFrame;
	private JTextField scanFromDateTxt;
	private JTextField backupLocationTxt;
	private JButton backupBtn;
	private JTree fileTree;
	private JLabel resultsLbl;
	private JLabel statusLbl;
	private FileTreeHelper fileTreeHelper;
	private BackupScanResult lastScanResult;
	
	public MainActionListener( BackupHelper backupHelper, JFrame mainFrame, JTextField scanFromDateTxt, JTextField backupLocationTxt, 
			JButton backupBtn, JTree fileTree, JLabel resultsLbl,  JLabel statusLbl ) {
		this.backupHelper = backupHelper;
		this.mainFrame = mainFrame;
		this.scanFromDateTxt = scanFromDateTxt;
		this.backupLocationTxt = backupLocationTxt;
		this.backupBtn = backupBtn;
		this.fileTree = fileTree;
		this.resultsLbl = resultsLbl;
		this.statusLbl = statusLbl;
		this.fileTreeHelper = new FileTreeHelper();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		String command = e.getActionCommand();
		switch(command) {
			
			case UIContainer.SCAN_BTN_ACTION:
				LOGGER.debug("Starting scan..");
				fileTreeHelper.clearTree(fileTree);
				fileTree.setRootVisible(false);
				SimpleDateFormat sdf = new SimpleDateFormat( GlobalConstants.FULL_TS_FORMAT );
				Calendar scanFromDate = Calendar.getInstance();
				try {
					scanFromDate.setTime( sdf.parse( scanFromDateTxt.getText() ) );
				} catch (ParseException ex) {
					LOGGER.error("Error while parsing Scan From Date", e);
					JOptionPane.showMessageDialog( mainFrame, GlobalConstants.ERR_MSG_INVALID_SCAN_FROM_DATE, GlobalConstants.ERR_MSG_TITLE, JOptionPane.ERROR_MESSAGE );
					break;
				}
				this.statusLbl.setText("Scanning directories for new or modified files ...");
				
				SwingUtilities.invokeLater( new Runnable() {
					public void run() {
						mainFrame.setCursor( Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR ) );
						lastScanResult = backupHelper.scanForFileChanges( scanFromDate );
						resultsLbl.setText( "<html>" +
								"Total Files scanned: " + lastScanResult.getTotalFileCount() + "<br>" + 
								"New or Modified Files: " + lastScanResult.getNewOrModifiedFileCount() + "<br>" + 
								"New or Modified File Size: " + FileManagerUtil.printFileSizeUserFriendly( lastScanResult.getNewOrModifiedFileSize() ) +
								"</html>" );
						statusLbl.setText("Scan completed in " + lastScanResult.getDuration() + " second(s)");
						if( lastScanResult.getNewOrModifiedFileCount() > 0 ) {
							backupBtn.setEnabled(true);
							fileTree.setRootVisible(true);
							DefaultTreeModel treeModel = new DefaultTreeModel( fileTreeHelper.addNodes( lastScanResult.getNewOrModifiedFiles() ) );
							fileTree.setModel(treeModel);
							for( int i = 0; i < fileTree.getRowCount(); i++ ) {
								fileTree.expandRow(i);
							}
						}
						mainFrame.setCursor(null);
					}
				});
				break;
			
			case UIContainer.BACKUP_BTN_ACTION:
//				if( lastScanResult != null ) {
//					if( lastScanResult.getNewOrModifiedFileCount() > 0 ) {
//						if( backupLocationTxt.getText() != null && !backupLocationTxt.getText().isEmpty() ) {
//							LOGGER.debug("Starting backup process for {} file(s)..", lastScanResult.getNewOrModifiedFileCount());
//							this.statusLbl.setText("Starting backup process for " + lastScanResult.getNewOrModifiedFileCount() + " file(s) ...");
							
//							ProgressBarDemo2 demo = new ProgressBarDemo2();
//							demo.createAndShowGUI();
				
							ProgressBar pb = new ProgressBar( backupHelper, lastScanResult, backupLocationTxt.getText(), statusLbl );
							//pb.createAndShowGUI();
							
//							SwingUtilities.invokeLater( new Runnable() {
//								public void run() {
//									BackupResult result = backupHelper.backup( 
//											lastScanResult.getNewOrModifiedFiles(), backupLocationTxt.getText(), progressBar );
//									JPanel backupResultPanel = ListManagerHelper.createBackupResultListPanel( result );
//									JOptionPane.showOptionDialog( mainFrame, backupResultPanel, GlobalConstants.BACKUP_RESULTS, JOptionPane.NO_OPTION, -1, null, new Object[]{}, null );
//									LOGGER.debug("Backup process completed in " + result.getDuration() + " second(s)");
//									statusLbl.setText("Backup process completed in " + result.getDuration() + " second(s)");
//								}
//							});
							
//						} else {
//							// show message - please select backup location
//							JOptionPane.showMessageDialog( mainFrame, GlobalConstants.ERR_MSG_SELECT_BACKUP_LOCATION, GlobalConstants.ERR_MSG_TITLE, JOptionPane.ERROR_MESSAGE );
//						}
//					} else {
//						// show message - no new or modified files found
//						JOptionPane.showMessageDialog( mainFrame, GlobalConstants.ERR_MSG_NO_BACKUP_NEEDED, GlobalConstants.ERR_MSG_TITLE, JOptionPane.ERROR_MESSAGE );
//					}
//				}
				break;
			
			case UIContainer.BROWSE_BTN_ACTION:
				UICommons.chooseDirectory( backupLocationTxt );
				break;
			
			case UIContainer.MANAGE_INCLUDE_DIRS_ACTION:
				JPanel includeDirsListPanel = ListManagerHelper.createListPanel( GlobalConstants.MANAGE_INCLUDE_DIRS, this.backupHelper.getIncludeDirs(), this.backupHelper );
				JOptionPane.showMessageDialog( mainFrame, includeDirsListPanel, GlobalConstants.MANAGE_INCLUDE_DIRS, JOptionPane.NO_OPTION, new ImageIcon("") );
				break;
			
			case UIContainer.MANAGE_INCLUDE_FILE_PATTERNS_ACTION:
				JPanel includePatternsListPanel = ListManagerHelper.createListPanel( GlobalConstants.MANAGE_INCLUDE_FILE_PATTERNS, this.backupHelper.getIncludeFilePatterns(), this.backupHelper );
				JOptionPane.showMessageDialog( mainFrame, includePatternsListPanel, GlobalConstants.MANAGE_INCLUDE_FILE_PATTERNS, JOptionPane.NO_OPTION, new ImageIcon("") );
				break;
			
			case UIContainer.MANAGE_EXCLUDE_DIRS_ACTION:
				JPanel excludeDirsListPanel = ListManagerHelper.createListPanel( GlobalConstants.MANAGE_EXCLUDE_DIRS, this.backupHelper.getExcludeDirs(), this.backupHelper );
				JOptionPane.showMessageDialog( mainFrame, excludeDirsListPanel, GlobalConstants.MANAGE_EXCLUDE_DIRS, JOptionPane.NO_OPTION, new ImageIcon("") );
				break;
			
			case UIContainer.MANAGE_EXCLUDE_DIR_PATTERNS_ACTION:
				JPanel excludeDirPatternsListPanel = ListManagerHelper.createListPanel( GlobalConstants.MANAGE_EXCLUDE_DIR_PATTERNS, this.backupHelper.getExcludeDirPatterns(), this.backupHelper );
				JOptionPane.showMessageDialog( mainFrame, excludeDirPatternsListPanel, GlobalConstants.MANAGE_EXCLUDE_DIR_PATTERNS, JOptionPane.NO_OPTION, new ImageIcon("") );
				break;
			
			case UIContainer.MANAGE_EXCLUDE_FILE_PATTERNS_ACTION:
				JPanel excludeFilePatternsListPanel = ListManagerHelper.createListPanel( GlobalConstants.MANAGE_EXCLUDE_FILE_PATTERNS, this.backupHelper.getExcludeFilePatterns(), this.backupHelper );
				JOptionPane.showMessageDialog( mainFrame, excludeFilePatternsListPanel, GlobalConstants.MANAGE_EXCLUDE_FILE_PATTERNS, JOptionPane.NO_OPTION, new ImageIcon("") );
				break;
			
			case UIContainer.ADD_TO_INCLUDE_FILE_PATTERNS_ACTION:
				String strNewListItem = FileTreeHelper.getSelectedPath(this.fileTree);
				if( strNewListItem != null ) {
					this.backupHelper.addToIncludeExcludeList( GlobalConstants.MANAGE_INCLUDE_FILE_PATTERNS, strNewListItem );
					statusLbl.setText("Include directories have been updated..");
				}
				break;
			
			case UIContainer.ADD_TO_EXCLUDE_DIRS_ACTION:
				strNewListItem = FileTreeHelper.getSelectedPath(this.fileTree);
				if( strNewListItem != null ) {
					this.backupHelper.addToIncludeExcludeList( GlobalConstants.MANAGE_EXCLUDE_DIRS, strNewListItem );
					statusLbl.setText("Exclude directories have been updated..");
				}
				break;
			
			case UIContainer.ADD_TO_EXCLUDE_DIR_PATTERNS_ACTION:
				strNewListItem = FileTreeHelper.getSelectedPath(this.fileTree);
				if( strNewListItem != null ) {
					this.backupHelper.addToIncludeExcludeList( GlobalConstants.MANAGE_EXCLUDE_DIR_PATTERNS, strNewListItem );
					statusLbl.setText("Exclude directory patterns have been updated..");
				}
				break;
			
			case UIContainer.ADD_TO_EXCLUDE_FILE_PATTERNS_ACTION:
				strNewListItem = FileTreeHelper.getSelectedPath(this.fileTree);
				if( strNewListItem != null ) {
					this.backupHelper.addToIncludeExcludeList( GlobalConstants.MANAGE_EXCLUDE_FILE_PATTERNS, strNewListItem );
					statusLbl.setText("Exclude file patterns have been updated..");
				}
				break;
		}
	}
	
}
