package org.warn.fm.ui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Set;

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
import org.warn.fm.backup.BackupFile;
import org.warn.fm.backup.BackupHelper;
import org.warn.fm.ui.FileTreeHelper;
import org.warn.fm.ui.ListManagerHelper;
import org.warn.fm.ui.UIContainer;

public class MainActionListener implements ActionListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MainActionListener.class);
	
	private BackupHelper backupHelper;
	private JFrame mainFrane;
	private JTree fileTree;
	private JTextField backupLocationTxt;
	private JButton backupBtn;
	private JLabel statusLbl;
	private FileTreeHelper fileTreeHelper;
	
	public MainActionListener( BackupHelper backupHelper, JFrame mainFrame, JTree fileTree, JTextField backupLocationTxt, JButton backupBtn,  JLabel statusLbl ) {
		this.backupHelper = backupHelper;
		this.mainFrane = mainFrame;
		this.fileTree = fileTree;
		this.backupLocationTxt = backupLocationTxt;
		this.backupBtn = backupBtn;
		this.statusLbl = statusLbl;
		this.fileTreeHelper = new FileTreeHelper();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		String command = e.getActionCommand();
		switch(command) {
			
			case UIContainer.SCAN_BTN_ACTION:
				LOGGER.debug("Starting scan..");
				this.statusLbl.setText("Scanning directories for new or modified files ...");
				SwingUtilities.invokeLater( new Runnable() {
					public void run() {
						long startTime = System.currentTimeMillis();
						Set<BackupFile> newOrModifiedFiles = backupHelper.scanForFileChanges();
						long endTime = System.currentTimeMillis();
						long duration = (endTime - startTime) / 1000;
						LOGGER.debug("Scan completed in " + duration + " second(s)");
						statusLbl.setText("Scan completed in " + duration + " second(s)");
						if( newOrModifiedFiles != null && newOrModifiedFiles.size() > 0 ) {
							backupBtn.setEnabled(true);
							fileTreeHelper.clearTree(fileTree);
							fileTree.setRootVisible(true);
							DefaultTreeModel treeModel = new DefaultTreeModel( fileTreeHelper.addNodes( newOrModifiedFiles ) );
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
				JPanel includeDirsListPanel = ListManagerHelper.createListPanel( backupHelper.getIncludeDirs() );
				JOptionPane.showMessageDialog( mainFrane, includeDirsListPanel, "Manage Include Directories", JOptionPane.NO_OPTION );
				break;
			
			case UIContainer.MANAGE_INCLUDE_PATTERNS_ACTION:
				JPanel includePatternsListPanel = ListManagerHelper.createListPanel( backupHelper.getIncludePatterns() );
				JOptionPane.showMessageDialog( mainFrane, includePatternsListPanel, "Manage Include Patterns", JOptionPane.NO_OPTION );
				break;
			
			case UIContainer.MANAGE_EXCLUDE_DIRS_ACTION:
				JPanel excludeDirsListPanel = ListManagerHelper.createListPanel( backupHelper.getExcludeDirs() );
				JOptionPane.showMessageDialog( mainFrane, excludeDirsListPanel, "Manage Exclude Directories", JOptionPane.NO_OPTION );
				break;
			
			case UIContainer.MANAGE_EXCLUDE_PATTERNS_ACTION:
				JPanel excludePatternsListPanel = ListManagerHelper.createListPanel( backupHelper.getExcludePatterns() );
				JOptionPane.showMessageDialog( mainFrane, excludePatternsListPanel, "Manage Exclude Patterns", JOptionPane.NO_OPTION );
				break;
			
		}
	}
	
}
