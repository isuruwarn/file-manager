package org.warn.fm.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.warn.fm.backup.BackupHelper;
import org.warn.fm.ui.listeners.MainActionListener;

public class UIContainer {
	
	public static final String TITLE = "File Manager";
	public static final String SCAN_BTN_ACTION = "Scan";
	public static final String BACKUP_BTN_ACTION = "Backup";
	public static final String SCAN_AND_BACKUP_BTN_ACTION = "Scan and Backup";
	public static final String BROWSE_BTN_ACTION = "Browse";
	public static final String SCAN_FROM_LBL = "Scan from";
	public static final String SETTINGS_BTN_ACTION = "\u2630";
	
	private static final int FRAME_WIDTH = 1000;
	private static final int FRAME_HEIGHT = 1000;
	private static final int SCAN_BTN_WIDTH = 100;
	private static final int SCAN_BTN_HEIGHT = 25;
	private static final int SCAN_FROM_LBL_WIDTH = 100;
	private static final int SCAN_FROM_LBL_HEIGHT = 25;
	private static final int SCAN_FROM_TXT_WIDTH = 100;
	private static final int SCAN_FROM_TXT_HEIGHT = 25;
	private static final int BACKUP_BTN_WIDTH = 100;
	private static final int BACKUP_BTN_HEIGHT = 25;
	private static final int BROWSE_BTN_WIDTH = 100;
	private static final int BROWSE_BTN_HEIGHT = 25;
	private static final int SETTINGS_BTN_WIDTH = 100;
	private static final int SETTINGS_BTN_HEIGHT = 25;
	private static final int TARGET_LOCATION_TXT_WIDTH = 275;
	private static final int TARGET_LOCATION_TXT_HEIGHT = 25;
	
	public UIContainer( BackupHelper backupHelper ) {
		
		JLabel scanFromDateLabel = new JLabel(SCAN_FROM_LBL);
		scanFromDateLabel.setPreferredSize( new Dimension( SCAN_FROM_LBL_WIDTH, SCAN_FROM_LBL_HEIGHT ) );
		scanFromDateLabel.setMinimumSize( new Dimension( SCAN_FROM_LBL_WIDTH, SCAN_FROM_LBL_HEIGHT ) );
		
		JTextField scanFromDateTxt = new JTextField( backupHelper.getlastBackupTime() );
		scanFromDateTxt.setPreferredSize( new Dimension( SCAN_FROM_TXT_WIDTH, SCAN_FROM_TXT_HEIGHT ) );
		scanFromDateTxt.setMinimumSize( new Dimension( SCAN_FROM_TXT_WIDTH, SCAN_FROM_TXT_HEIGHT ) );
		
		JButton scanBtn = new JButton(SCAN_BTN_ACTION);
		scanBtn.setPreferredSize( new Dimension( SCAN_BTN_WIDTH, SCAN_BTN_HEIGHT ) );
		scanBtn.setMinimumSize( new Dimension( SCAN_BTN_WIDTH, SCAN_BTN_HEIGHT ) );
		
		JButton backupBtn = new JButton(BACKUP_BTN_ACTION);
		backupBtn.setPreferredSize( new Dimension( BACKUP_BTN_WIDTH, BACKUP_BTN_HEIGHT ) );
		backupBtn.setMinimumSize( new Dimension( BACKUP_BTN_WIDTH, BACKUP_BTN_HEIGHT ) );
		backupBtn.setEnabled(false);
		
		JTextField targetLocationTxt = new JTextField();
		backupBtn.setPreferredSize( new Dimension( TARGET_LOCATION_TXT_WIDTH, TARGET_LOCATION_TXT_HEIGHT ) );
		backupBtn.setMinimumSize( new Dimension( TARGET_LOCATION_TXT_WIDTH, TARGET_LOCATION_TXT_HEIGHT ) );
		
		JButton targetLocBrowseBtn = new JButton(BROWSE_BTN_ACTION);
		targetLocBrowseBtn.setPreferredSize( new Dimension( BROWSE_BTN_WIDTH, BROWSE_BTN_WIDTH ) );
		targetLocBrowseBtn.setMinimumSize( new Dimension( BROWSE_BTN_WIDTH, BROWSE_BTN_WIDTH ) );
		
		// layout manager configurations
		
		GridBagConstraints scanFromDateLabelGridCons = new GridBagConstraints();
		scanFromDateLabelGridCons.gridx = 0;
		scanFromDateLabelGridCons.gridy = 0;
		scanFromDateLabelGridCons.insets = new Insets(5,10,5,10);
		scanFromDateLabelGridCons.anchor = GridBagConstraints.LINE_START;
		
		GridBagConstraints scanFromDateTxtGridCons = new GridBagConstraints();
		scanFromDateTxtGridCons.gridx = 1;
		scanFromDateTxtGridCons.gridy = 0;
		scanFromDateTxtGridCons.insets = new Insets(5,10,5,10);
		scanFromDateTxtGridCons.anchor = GridBagConstraints.LINE_START;
		
		GridBagConstraints scanBtnGridCons = new GridBagConstraints();
		scanBtnGridCons.gridx = 2;
		scanBtnGridCons.gridy = 0;
		scanBtnGridCons.insets = new Insets(5,10,5,10);
		scanBtnGridCons.anchor = GridBagConstraints.LINE_START;
		
		GridBagConstraints backupBtnGridCons = new GridBagConstraints();
		backupBtnGridCons.gridx = 0;
		backupBtnGridCons.gridy = 1;
		backupBtnGridCons.insets = new Insets(5,10,5,10);
		backupBtnGridCons.anchor = GridBagConstraints.LINE_START;
		
		JPanel panel = new JPanel();
		//panel.setPreferredSize( new Dimension( FRAME_WIDTH, FRAME_HEIGHT ) );
		//panel.setMinimumSize( new Dimension( FRAME_WIDTH, FRAME_HEIGHT ) );
		panel.setLayout( new GridBagLayout() );
		panel.add( scanFromDateLabel, scanFromDateLabelGridCons );
		panel.add( scanFromDateTxt, scanFromDateTxtGridCons );
		panel.add( scanBtn, scanBtnGridCons );
		panel.add( backupBtn, backupBtnGridCons );
		
		JFrame mainFrame = new JFrame(TITLE);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setPreferredSize( new Dimension( FRAME_WIDTH, FRAME_HEIGHT ) );
		mainFrame.setMinimumSize( new Dimension( FRAME_WIDTH, FRAME_HEIGHT ) );
		mainFrame.getContentPane().add(panel);
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null); // position to center of screen 
		mainFrame.setVisible(true);
		
		MainActionListener mainActionListener = new MainActionListener( backupHelper, backupBtn );
		scanBtn.addActionListener(mainActionListener);
		backupBtn.addActionListener(mainActionListener);
		
	}
	
}
