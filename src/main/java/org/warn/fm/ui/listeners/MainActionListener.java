package org.warn.fm.ui.listeners;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultTreeModel;

import org.slf4j.LoggerFactory;
import org.warn.fm.backup.BackupHelper;
import org.warn.fm.model.BackupScanResult;
import org.warn.fm.ui.BackupLogFrame;
import org.warn.fm.ui.BackupProgressBar;
import org.warn.fm.ui.FileTreeHelper;
import org.warn.fm.ui.ListPanelBuilder;
import org.warn.fm.ui.UIContainer;
import org.warn.fm.util.ConfigConstants;
import org.warn.fm.util.GlobalConstants;
import org.warn.utils.datetime.DateTimeUtil;
import org.warn.utils.file.FileHelper;
import org.warn.utils.file.FileOperations;
import org.warn.utils.swing.UICommons;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainActionListener implements ActionListener {
	
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
				
				log.info("Starting scan for created or modifiled files..");
				fileTreeHelper.clearTree(fileTree);
				fileTree.setRootVisible(false);
				Calendar scanFromDate = Calendar.getInstance();
				try {
					scanFromDate.setTime( DateTimeUtil.fullTimestampSDF.parse( scanFromDateTxt.getText() ) );
				} catch (ParseException ex) {
					log.error("Error while parsing Scan From Date", e);
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
								"New or Modified File Size: " + FileHelper.printFileSizeUserFriendly( lastScanResult.getNewOrModifiedFileSize() ) +
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
				if( lastScanResult != null ) {
					
					if( lastScanResult.getNewOrModifiedFileCount() > 0 ) {
						
						if( backupLocationTxt.getText() != null && !backupLocationTxt.getText().isEmpty() ) {
							
							if( !FileOperations.exists( backupLocationTxt.getText() ) ) {
								JOptionPane.showMessageDialog( mainFrame, GlobalConstants.ERR_MSG_BACKUP_DIR_DOES_NOT_EXIST, GlobalConstants.ERR_MSG_TITLE, JOptionPane.ERROR_MESSAGE );
								
							} else {
								log.debug("Starting backup process for {} file(s)..", lastScanResult.getNewOrModifiedFileCount());
								this.statusLbl.setText("Starting backup process for " + lastScanResult.getNewOrModifiedFileCount() + " file(s) ...");
								BackupProgressBar pb = new BackupProgressBar( backupHelper, lastScanResult, backupLocationTxt.getText(), statusLbl, scanFromDateTxt, backupBtn );
								pb.displayAndExecute();
							}
							
						} else {
							// show message - please select backup location
							JOptionPane.showMessageDialog( mainFrame, GlobalConstants.ERR_MSG_SELECT_BACKUP_LOCATION, GlobalConstants.ERR_MSG_TITLE, JOptionPane.ERROR_MESSAGE );
						}
					} else {
						// show message - no new or modified files found
						JOptionPane.showMessageDialog( mainFrame, GlobalConstants.ERR_MSG_NO_BACKUP_NEEDED, GlobalConstants.ERR_MSG_TITLE, JOptionPane.ERROR_MESSAGE );
					}
				}
				break;
			
			case UIContainer.BROWSE_BTN_ACTION:
				UICommons.chooseDirectory( backupLocationTxt );
				break;
			
			case UIContainer.MANAGE_INCLUDE_DIRS_ACTION:
				JPanel includeDirsListPanel = ListPanelBuilder.createListPanel( GlobalConstants.MANAGE_INCLUDE_DIRS, this.backupHelper.getIncludeDirs(), this.backupHelper );
				JOptionPane.showMessageDialog( mainFrame, includeDirsListPanel, GlobalConstants.MANAGE_INCLUDE_DIRS, JOptionPane.NO_OPTION, new ImageIcon("") );
				break;
			
			case UIContainer.MANAGE_INCLUDE_FILE_PATTERNS_ACTION:
				JPanel includePatternsListPanel = ListPanelBuilder.createListPanel( GlobalConstants.MANAGE_INCLUDE_FILE_PATTERNS, this.backupHelper.getIncludeFilePatterns(), this.backupHelper );
				JOptionPane.showMessageDialog( mainFrame, includePatternsListPanel, GlobalConstants.MANAGE_INCLUDE_FILE_PATTERNS, JOptionPane.NO_OPTION, new ImageIcon("") );
				break;
			
			case UIContainer.MANAGE_EXCLUDE_DIRS_ACTION:
				JPanel excludeDirsListPanel = ListPanelBuilder.createListPanel( GlobalConstants.MANAGE_EXCLUDE_DIRS, this.backupHelper.getExcludeDirs(), this.backupHelper );
				JOptionPane.showMessageDialog( mainFrame, excludeDirsListPanel, GlobalConstants.MANAGE_EXCLUDE_DIRS, JOptionPane.NO_OPTION, new ImageIcon("") );
				break;
			
			case UIContainer.MANAGE_EXCLUDE_DIR_PATTERNS_ACTION:
				JPanel excludeDirPatternsListPanel = ListPanelBuilder.createListPanel( GlobalConstants.MANAGE_EXCLUDE_DIR_PATTERNS, this.backupHelper.getExcludeDirPatterns(), this.backupHelper );
				JOptionPane.showMessageDialog( mainFrame, excludeDirPatternsListPanel, GlobalConstants.MANAGE_EXCLUDE_DIR_PATTERNS, JOptionPane.NO_OPTION, new ImageIcon("") );
				break;
			
			case UIContainer.MANAGE_EXCLUDE_FILE_PATTERNS_ACTION:
				JPanel excludeFilePatternsListPanel = ListPanelBuilder.createListPanel( GlobalConstants.MANAGE_EXCLUDE_FILE_PATTERNS, this.backupHelper.getExcludeFilePatterns(), this.backupHelper );
				JOptionPane.showMessageDialog( mainFrame, excludeFilePatternsListPanel, GlobalConstants.MANAGE_EXCLUDE_FILE_PATTERNS, JOptionPane.NO_OPTION, new ImageIcon("") );
				break;
			
			case UIContainer.VIEW_BACKUP_LOG_ACTION:
				new BackupLogFrame( this.backupHelper.getBackupLog() );
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
			
			case UIContainer.ENABLE_DEBUG_LOGS_ACTION:
				//TODO does not work. need to fix it.
				Logger fileOutputLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger( ConfigConstants.LOGBACK_FILE_OUTPUT_LOGGER );
				JCheckBoxMenuItem checkBox = (JCheckBoxMenuItem) e.getSource();
				if( checkBox.isSelected() ) 
					fileOutputLogger.setLevel( Level.DEBUG );
				else
					fileOutputLogger.setLevel( Level.INFO );
				break;
		}
	}
	
}
