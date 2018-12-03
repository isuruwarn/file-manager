package org.warn.fm.ui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
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
import org.warn.fm.ui.UIContainer;
import org.warn.fm.util.GlobalConstants;

public class MainActionListener implements ActionListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MainActionListener.class);
	
	private BackupHelper backupHelper;
	private JFrame mainFrane;
	private JTextField scanFromDateTxt;
	private JTextField backupLocationTxt;
	private JButton backupBtn;
	private JTree fileTree;
	private JLabel resultsLbl;
	private JLabel statusLbl;
	private FileTreeHelper fileTreeHelper;
	
	public MainActionListener( BackupHelper backupHelper, JFrame mainFrame, JTextField scanFromDateTxt, JTextField backupLocationTxt, 
			JButton backupBtn, JTree fileTree, JLabel resultsLbl,  JLabel statusLbl ) {
		this.backupHelper = backupHelper;
		this.mainFrane = mainFrame;
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
				SimpleDateFormat sdf = new SimpleDateFormat( GlobalConstants.FULL_TS_FORMAT );
				Calendar scanFromDate = Calendar.getInstance();
				try {
					scanFromDate.setTime( sdf.parse( scanFromDateTxt.getText() ) );
				} catch (ParseException ex) {
					LOGGER.error("Error while parsing Scan From Date", e);
					JOptionPane.showMessageDialog( mainFrane, GlobalConstants.ERR_MSG_INVALID_SCAN_FROM_DATE, GlobalConstants.ERR_MSG_TITLE, JOptionPane.ERROR_MESSAGE );
					break;
				}
				this.statusLbl.setText("Scanning directories for new or modified files ...");
				SwingUtilities.invokeLater( new Runnable() {
					public void run() {
						fileTreeHelper.clearTree(fileTree);
						fileTree.setRootVisible(false);
						BackupScanResult scanResult = backupHelper.scanForFileChanges( scanFromDate );
						resultsLbl.setText( "<html>" +
								"Total Files scanned: " + scanResult.getTotalFileCount() + "<br>" + 
								"New or Modified Files: " + scanResult.getNewOrModifiedFileCount() +
								"</html>" );
						statusLbl.setText("Scan completed in " + scanResult.getDuration() + " second(s)");
						if( scanResult.getNewOrModifiedFileCount() > 0 ) {
							backupBtn.setEnabled(true);
							fileTree.setRootVisible(true);
							DefaultTreeModel treeModel = new DefaultTreeModel( fileTreeHelper.addNodes( scanResult.getNewOrModifiedFiles() ) );
							fileTree.setModel(treeModel);
							for( int i = 0; i < fileTree.getRowCount(); i++ ) {
								fileTree.expandRow(i);
							}
						}
					}
				});
				break;
			
			case UIContainer.BACKUP_BTN_ACTION:
				LOGGER.debug("Starting backup process..");
				this.statusLbl.setText("Starting backup process ...");
				SwingUtilities.invokeLater( new Runnable() {
					public void run() {
						long startTime = System.currentTimeMillis();
						// TODO
						long endTime = System.currentTimeMillis();
						long duration = (endTime - startTime) / 1000;
						LOGGER.debug("Backup process completed in " + duration + " second(s)");
						statusLbl.setText("Backup process completed in " + duration + " second(s)");
					}
				});
				break;
			
			case UIContainer.BROWSE_BTN_ACTION:
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
				int returnVal = fc.showOpenDialog(null);
				if( returnVal == JFileChooser.APPROVE_OPTION ) {
					File file = fc.getSelectedFile();
					backupLocationTxt.setText( file.getPath() );
				}
				break;
			
			case UIContainer.MANAGE_INCLUDE_DIRS_ACTION:
				JPanel includeDirsListPanel = ListManagerHelper.createListPanel( GlobalConstants.MANAGE_INCLUDE_DIRS, this.backupHelper.getIncludeDirs(), this.backupHelper );
				JOptionPane.showMessageDialog( mainFrane, includeDirsListPanel, GlobalConstants.MANAGE_INCLUDE_DIRS, JOptionPane.NO_OPTION, new ImageIcon("") );
				break;
			
			case UIContainer.MANAGE_INCLUDE_FILE_PATTERNS_ACTION:
				JPanel includePatternsListPanel = ListManagerHelper.createListPanel( GlobalConstants.MANAGE_INCLUDE_FILE_PATTERNS, this.backupHelper.getIncludeFilePatterns(), this.backupHelper );
				JOptionPane.showMessageDialog( mainFrane, includePatternsListPanel, GlobalConstants.MANAGE_INCLUDE_FILE_PATTERNS, JOptionPane.NO_OPTION, new ImageIcon("") );
				break;
			
			case UIContainer.MANAGE_EXCLUDE_DIRS_ACTION:
				JPanel excludeDirsListPanel = ListManagerHelper.createListPanel( GlobalConstants.MANAGE_EXCLUDE_DIRS, this.backupHelper.getExcludeDirs(), this.backupHelper );
				JOptionPane.showMessageDialog( mainFrane, excludeDirsListPanel, GlobalConstants.MANAGE_EXCLUDE_DIRS, JOptionPane.NO_OPTION, new ImageIcon("") );
				break;
			
			case UIContainer.MANAGE_EXCLUDE_DIR_PATTERNS_ACTION:
				JPanel excludeDirPatternsListPanel = ListManagerHelper.createListPanel( GlobalConstants.MANAGE_EXCLUDE_DIR_PATTERNS, this.backupHelper.getExcludeDirPatterns(), this.backupHelper );
				JOptionPane.showMessageDialog( mainFrane, excludeDirPatternsListPanel, GlobalConstants.MANAGE_EXCLUDE_DIR_PATTERNS, JOptionPane.NO_OPTION, new ImageIcon("") );
				break;
			
			case UIContainer.MANAGE_EXCLUDE_FILE_PATTERNS_ACTION:
				JPanel excludeFilePatternsListPanel = ListManagerHelper.createListPanel( GlobalConstants.MANAGE_EXCLUDE_FILE_PATTERNS, this.backupHelper.getExcludeFilePatterns(), this.backupHelper );
				JOptionPane.showMessageDialog( mainFrane, excludeFilePatternsListPanel, GlobalConstants.MANAGE_EXCLUDE_FILE_PATTERNS, JOptionPane.NO_OPTION, new ImageIcon("") );
				break;
			
		}
	}
	
}
