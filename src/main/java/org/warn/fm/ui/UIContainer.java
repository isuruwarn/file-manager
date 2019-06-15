package org.warn.fm.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import org.warn.fm.backup.BackupHelper;
import org.warn.fm.config.ConfigConstants;
import org.warn.fm.ui.listeners.FileTreeMouseAdapter;
import org.warn.fm.ui.listeners.MainActionListener;
import org.warn.utils.config.UserConfig;

public class UIContainer {
	
	public static final String TITLE = "File Manager";
	public static final String SCAN_FROM_LBL = "Scan From Date";
	public static final String BACKUP_LOCATION_LBL = "Backup Location";
	public static final String FILE_TREE_ROOT_NODE = "..";
	
	public static final String SETTINGS_BTN_ACTION = "\u2630";
	public static final String MANAGE_INCLUDE_DIRS_ACTION = "Include directories..";
	public static final String MANAGE_INCLUDE_FILE_PATTERNS_ACTION = "Include File patterns..";
	public static final String MANAGE_EXCLUDE_DIRS_ACTION = "Exclude directories..";
	public static final String MANAGE_EXCLUDE_DIR_PATTERNS_ACTION = "Exclude directory patterns..";
	public static final String MANAGE_EXCLUDE_FILE_PATTERNS_ACTION = "Exclude file patterns..";
	public static final String VIEW_BACKUP_LOG_ACTION = "View Backup Log";
	public static final String ADD_TO_INCLUDE_FILE_PATTERNS_ACTION = "Add to Include File patterns";
	public static final String ADD_TO_EXCLUDE_DIRS_ACTION = "Add to Exclude directories";
	public static final String ADD_TO_EXCLUDE_DIR_PATTERNS_ACTION = "Add to Exclude directory patterns";
	public static final String ADD_TO_EXCLUDE_FILE_PATTERNS_ACTION = "Add to Exclude file patterns";
	public static final String SCAN_BTN_ACTION = "Scan";
	public static final String BACKUP_BTN_ACTION = "Backup";
	public static final String SCAN_AND_BACKUP_BTN_ACTION = "Scan and Backup";
	public static final String BROWSE_BTN_ACTION = "...";
	
	private static final int FRAME_WIDTH = 650;
	private static final int FRAME_HEIGHT = 775;
	private static final int SETTINGS_BTN_WIDTH = 30;
	private static final int SETTINGS_BTN_HEIGHT = 25;
	private static final int SCAN_FROM_LBL_WIDTH = 110;
	private static final int SCAN_FROM_LBL_HEIGHT = 25;
	private static final int SCAN_FROM_TXT_WIDTH = 175;
	private static final int SCAN_FROM_TXT_HEIGHT = 25;
	private static final int SCAN_BTN_WIDTH = 100;
	private static final int SCAN_BTN_HEIGHT = 25;
	private static final int FILE_TREE_WIDTH = 575;
	private static final int FILE_TREE_HEIGHT = 540;
	private static final int TARGET_LOCATION_LBL_WIDTH = 110;
	private static final int TARGET_LOCATION_LBL_HEIGHT = 25;
	private static final int TARGET_LOCATION_TXT_WIDTH = 275;
	private static final int TARGET_LOCATION_TXT_HEIGHT = 25;
	private static final int BROWSE_BTN_WIDTH = 50;
	private static final int BROWSE_BTN_HEIGHT = 25;
	private static final int BACKUP_BTN_WIDTH = 100;
	private static final int BACKUP_BTN_HEIGHT = 25;
	private static final int RESULTS_LBL_WIDTH = FRAME_WIDTH;
	private static final int RESULTS_LBL_HEIGHT = 70;
	private static final int STATUS_LBL_WIDTH = FRAME_WIDTH;
	private static final int STATUS_LBL_HEIGHT = 25;
	
	public UIContainer( BackupHelper backupHelper, UserConfig userConfig ) {
		
		JLabel scanFromDateLabel = new JLabel(SCAN_FROM_LBL);
		scanFromDateLabel.setPreferredSize( new Dimension( SCAN_FROM_LBL_WIDTH, SCAN_FROM_LBL_HEIGHT ) );
		scanFromDateLabel.setMinimumSize( new Dimension( SCAN_FROM_LBL_WIDTH, SCAN_FROM_LBL_HEIGHT ) );
		//scanFromDateLabel.setBorder( BorderFactory.createLineBorder( Color.gray ) );
		
		JTextField scanFromDateTxt = new JTextField( backupHelper.getlastBackupTime() );
		scanFromDateTxt.setPreferredSize( new Dimension( SCAN_FROM_TXT_WIDTH, SCAN_FROM_TXT_HEIGHT ) );
		scanFromDateTxt.setMinimumSize( new Dimension( SCAN_FROM_TXT_WIDTH, SCAN_FROM_TXT_HEIGHT ) );
		
		JButton scanBtn = new JButton(SCAN_BTN_ACTION);
		scanBtn.setPreferredSize( new Dimension( SCAN_BTN_WIDTH, SCAN_BTN_HEIGHT ) );
		scanBtn.setMinimumSize( new Dimension( SCAN_BTN_WIDTH, SCAN_BTN_HEIGHT ) );
		
		JMenuItem addToExcludeDirsItem = new JMenuItem(ADD_TO_EXCLUDE_DIRS_ACTION);
		JMenuItem addToExcludeDirPatternsItem = new JMenuItem(ADD_TO_EXCLUDE_DIR_PATTERNS_ACTION);
		
		JPopupMenu dirPopup = new JPopupMenu();
		dirPopup.add(addToExcludeDirsItem);
		dirPopup.add(addToExcludeDirPatternsItem);
		
		JMenuItem addToIncludeFilePatternsItem = new JMenuItem(ADD_TO_INCLUDE_FILE_PATTERNS_ACTION);
		JMenuItem addToExcludeFilePatternsItem = new JMenuItem(ADD_TO_EXCLUDE_FILE_PATTERNS_ACTION);
		
		JPopupMenu filePopup = new JPopupMenu();
		filePopup.add(addToIncludeFilePatternsItem);
		filePopup.add(addToExcludeFilePatternsItem);
		
		FileTreeMouseAdapter fileTreeMouseAdapter = new FileTreeMouseAdapter( dirPopup, filePopup );
		JTree fileTree = new JTree( new DefaultMutableTreeNode( FILE_TREE_ROOT_NODE ) );
		fileTree.setRootVisible(false);
		fileTree.setEditable(true);
		fileTree.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
		fileTree.addMouseListener(fileTreeMouseAdapter);
		
	    ToolTipManager.sharedInstance().registerComponent(fileTree);
	    fileTree.setCellRenderer( new FileTreeCellRenderer() );
	    
		JScrollPane fileTreeScrollPane = new JScrollPane(fileTree);
		fileTreeScrollPane.setMinimumSize( new Dimension( FILE_TREE_WIDTH, FILE_TREE_HEIGHT ) );
		fileTreeScrollPane.setPreferredSize( new Dimension( FILE_TREE_WIDTH, FILE_TREE_HEIGHT ) );
		
		JLabel backupLocationLbl = new JLabel(BACKUP_LOCATION_LBL);
		backupLocationLbl.setPreferredSize( new Dimension( TARGET_LOCATION_LBL_WIDTH, TARGET_LOCATION_LBL_HEIGHT ) );
		backupLocationLbl.setMinimumSize( new Dimension( TARGET_LOCATION_LBL_WIDTH, TARGET_LOCATION_LBL_HEIGHT ) );
		//backupLocationLbl.setBorder( BorderFactory.createLineBorder( Color.gray ) );
		
		String lastBackupLocation = userConfig.getProperty( ConfigConstants.EL_LAST_BACKUP_LOCATION );
		JTextField backupLocationTxt = new JTextField( lastBackupLocation==null?"":lastBackupLocation );
		backupLocationTxt.setPreferredSize( new Dimension( TARGET_LOCATION_TXT_WIDTH, TARGET_LOCATION_TXT_HEIGHT ) );
		backupLocationTxt.setMinimumSize( new Dimension( TARGET_LOCATION_TXT_WIDTH, TARGET_LOCATION_TXT_HEIGHT ) );
		
		JButton backupLocBrowseBtn = new JButton(BROWSE_BTN_ACTION);
		backupLocBrowseBtn.setPreferredSize( new Dimension( BROWSE_BTN_WIDTH, BROWSE_BTN_HEIGHT ) );
		backupLocBrowseBtn.setMinimumSize( new Dimension( BROWSE_BTN_WIDTH, BROWSE_BTN_HEIGHT ) );
		
		JButton backupBtn = new JButton(BACKUP_BTN_ACTION);
		backupBtn.setPreferredSize( new Dimension( BACKUP_BTN_WIDTH, BACKUP_BTN_HEIGHT ) );
		backupBtn.setMinimumSize( new Dimension( BACKUP_BTN_WIDTH, BACKUP_BTN_HEIGHT ) );
		backupBtn.setEnabled(false);
		
		JMenuItem manageIncludeDirs = new JMenuItem( MANAGE_INCLUDE_DIRS_ACTION );
		JMenuItem manageIncludeFilePatterns = new JMenuItem( MANAGE_INCLUDE_FILE_PATTERNS_ACTION );
		JMenuItem manageExcludeDirs = new JMenuItem( MANAGE_EXCLUDE_DIRS_ACTION );
		JMenuItem manageExcludeDirPatterns = new JMenuItem( MANAGE_EXCLUDE_DIR_PATTERNS_ACTION );
		JMenuItem manageExcludeFilePatterns = new JMenuItem( MANAGE_EXCLUDE_FILE_PATTERNS_ACTION );
		JMenuItem viewBackupLog = new JMenuItem( VIEW_BACKUP_LOG_ACTION );
		
		JMenu settingsMenu = new JMenu(SETTINGS_BTN_ACTION);
		settingsMenu.setPreferredSize( new Dimension( SETTINGS_BTN_WIDTH, SETTINGS_BTN_HEIGHT ) );
		settingsMenu.setMinimumSize( new Dimension( SETTINGS_BTN_WIDTH, SETTINGS_BTN_HEIGHT ) );
		settingsMenu.add(manageIncludeDirs);
		settingsMenu.add(manageIncludeFilePatterns);
		settingsMenu.add(manageExcludeDirs);
		settingsMenu.add(manageExcludeDirPatterns);
		settingsMenu.add(manageExcludeFilePatterns);
		settingsMenu.add(viewBackupLog);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(settingsMenu);
		
		JLabel resultsLbl = new JLabel();
		resultsLbl.setPreferredSize( new Dimension( RESULTS_LBL_WIDTH, RESULTS_LBL_HEIGHT ) );
		resultsLbl.setMinimumSize( new Dimension( RESULTS_LBL_WIDTH, RESULTS_LBL_HEIGHT ) );
		//resultsLbl.setBorder( BorderFactory.createLineBorder( Color.gray ) );
		
		JLabel statusLbl = new JLabel();
		statusLbl.setPreferredSize( new Dimension( STATUS_LBL_WIDTH, STATUS_LBL_HEIGHT ) );
		statusLbl.setMinimumSize( new Dimension( STATUS_LBL_WIDTH, STATUS_LBL_HEIGHT ) );
		//statusLbl.setBorder( BorderFactory.createLineBorder( Color.gray ) );
		
		GridBagConstraints scanFromLblGridCons = new GridBagConstraints();
		scanFromLblGridCons.gridx = 0;
		scanFromLblGridCons.gridy = 0;
		scanFromLblGridCons.insets = new Insets(10,10,10,10);
		scanFromLblGridCons.anchor = GridBagConstraints.LINE_START;
		
		GridBagConstraints scanFromTxtGridCons = new GridBagConstraints();
		scanFromTxtGridCons.gridx = 1;
		scanFromTxtGridCons.gridy = 0;
		scanFromTxtGridCons.insets = new Insets(10,0,10,10);
		scanFromTxtGridCons.anchor = GridBagConstraints.BASELINE_LEADING;
		
		GridBagConstraints scanBtnGridCons = new GridBagConstraints();
		scanBtnGridCons.gridx = 2;
		scanBtnGridCons.gridy = 0;
		scanBtnGridCons.insets = new Insets(10,0,10,10);
		scanBtnGridCons.anchor = GridBagConstraints.BASELINE_LEADING;
		
		GridBagConstraints menuBarGridCons = new GridBagConstraints();
		menuBarGridCons.gridx = 4;
		menuBarGridCons.gridy = 0;
		menuBarGridCons.insets = new Insets(10,0,10,15);
		menuBarGridCons.anchor = GridBagConstraints.LINE_END;
		
		GridBagConstraints fileTreePaneGridCons = new GridBagConstraints();
		fileTreePaneGridCons.gridx = 0;
		fileTreePaneGridCons.gridy = 1;
		fileTreePaneGridCons.insets = new Insets(5,0,5,0);
		fileTreePaneGridCons.gridwidth = 5;
		fileTreePaneGridCons.anchor = GridBagConstraints.CENTER;
		
		GridBagConstraints backupLocLblGridCons = new GridBagConstraints();
		backupLocLblGridCons.gridx = 0;
		backupLocLblGridCons.gridy = 2;
		backupLocLblGridCons.insets = new Insets(10,10,10,10);
		backupLocLblGridCons.anchor = GridBagConstraints.LINE_START;
		
		GridBagConstraints backupLocTxtGridCons = new GridBagConstraints();
		backupLocTxtGridCons.gridx = 1;
		backupLocTxtGridCons.gridy = 2;
		backupLocTxtGridCons.gridwidth = 2;
		backupLocTxtGridCons.insets = new Insets(10,0,10,10);
		
		GridBagConstraints backupLocBtnGridCons = new GridBagConstraints();
		backupLocBtnGridCons.gridx = 3;
		backupLocBtnGridCons.gridy = 2;
		backupLocBtnGridCons.insets = new Insets(10,0,10,10);
		
		GridBagConstraints backupBtnGridCons = new GridBagConstraints();
		backupBtnGridCons.gridx = 4;
		backupBtnGridCons.gridy = 2;
		backupBtnGridCons.insets = new Insets(10,0,10,0);
		backupBtnGridCons.anchor = GridBagConstraints.BASELINE_LEADING;
		
		GridBagConstraints resultsLblGridCons = new GridBagConstraints();
		resultsLblGridCons.gridx = 0;
		resultsLblGridCons.gridy = 3;
		resultsLblGridCons.insets = new Insets(5,0,0,0);
		resultsLblGridCons.gridwidth = 5;
		resultsLblGridCons.anchor = GridBagConstraints.CENTER;
		
		GridBagConstraints statusLblGridCons = new GridBagConstraints();
		statusLblGridCons.gridx = 0;
		statusLblGridCons.gridy = 4;
		statusLblGridCons.insets = new Insets(10,0,0,0);
		statusLblGridCons.gridwidth = 5;
		statusLblGridCons.anchor = GridBagConstraints.CENTER;
		
		JPanel panel = new JPanel();
		panel.setLayout( new GridBagLayout() );
		panel.add( scanFromDateLabel, scanFromLblGridCons );
		panel.add( scanFromDateTxt, scanFromTxtGridCons );
		panel.add( menuBar, menuBarGridCons );
		panel.add( scanBtn, scanBtnGridCons );
		panel.add( fileTreeScrollPane, fileTreePaneGridCons );
		panel.add( backupLocationLbl, backupLocLblGridCons );
		panel.add( backupLocationTxt, backupLocTxtGridCons );
		panel.add( backupLocBrowseBtn, backupLocBtnGridCons );
		panel.add( backupBtn, backupBtnGridCons );
		panel.add( resultsLbl, resultsLblGridCons );
		panel.add( statusLbl, statusLblGridCons );
		
		JScrollPane mainScrollPane = new JScrollPane(panel);
		mainScrollPane.setPreferredSize( new Dimension( FRAME_WIDTH, FRAME_HEIGHT ) );
		mainScrollPane.setMinimumSize( new Dimension( FRAME_WIDTH, FRAME_HEIGHT ) );
		mainScrollPane.addComponentListener( new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				int w = mainScrollPane.getWidth();
				int h = mainScrollPane.getHeight();
				fileTreeScrollPane.setPreferredSize( new Dimension( w-40, h-225 ) );
				fileTreeScrollPane.setSize( new Dimension( w-40, h-225 ) );
				resultsLbl.setPreferredSize( new Dimension( w-20, RESULTS_LBL_HEIGHT ) );
				resultsLbl.setSize( new Dimension( w-20, RESULTS_LBL_HEIGHT ) );
				statusLbl.setPreferredSize( new Dimension( w-20, STATUS_LBL_HEIGHT ) );
				statusLbl.setSize( new Dimension( w-20, STATUS_LBL_HEIGHT ) );
			}
		});
		
		JFrame mainFrame = new JFrame(TITLE);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().add(mainScrollPane);
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null); // position to center of screen 
		mainFrame.setVisible(true);
		
		
		MainActionListener mainActionListener = new MainActionListener( backupHelper, mainFrame, scanFromDateTxt, backupLocationTxt, backupBtn, fileTree, resultsLbl, statusLbl );
		scanBtn.addActionListener(mainActionListener);
		manageIncludeDirs.addActionListener(mainActionListener);
		manageIncludeFilePatterns.addActionListener(mainActionListener);
		manageExcludeDirs.addActionListener(mainActionListener);
		manageExcludeDirPatterns.addActionListener(mainActionListener);
		manageExcludeFilePatterns.addActionListener(mainActionListener);
		viewBackupLog.addActionListener(mainActionListener);
		backupLocBrowseBtn.addActionListener(mainActionListener);
		backupBtn.addActionListener(mainActionListener);
		addToExcludeDirsItem.addActionListener(mainActionListener);
		addToExcludeDirPatternsItem.addActionListener(mainActionListener);
		addToIncludeFilePatternsItem.addActionListener(mainActionListener);
		addToExcludeFilePatternsItem.addActionListener(mainActionListener);
		
	}
	
}
