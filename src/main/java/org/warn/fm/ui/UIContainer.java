package org.warn.fm.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.warn.fm.backup.BackupHelper;
import org.warn.fm.config.ConfigConstants;
import org.warn.fm.ui.listeners.MainActionListener;
import org.warn.utils.config.UserConfig;

public class UIContainer {
	
	public static final String TITLE = "File Manager";
	public static final String SCAN_BTN_ACTION = "Scan";
	public static final String BACKUP_BTN_ACTION = "Backup";
	public static final String SCAN_AND_BACKUP_BTN_ACTION = "Scan and Backup";
	public static final String BROWSE_BTN_ACTION = "Browse";
	public static final String SCAN_FROM_LBL = "Scan from";
	public static final String BACKUP_LOCATION_LBL = "Backup Location";
	public static final String SETTINGS_BTN_ACTION = "\u2630";
	
	private static final int FRAME_WIDTH = 800;
	private static final int FRAME_HEIGHT = 800;
	private static final int SCAN_FROM_LBL_WIDTH = 100;
	private static final int SCAN_FROM_LBL_HEIGHT = 25;
	private static final int SCAN_FROM_TXT_WIDTH = 100;
	private static final int SCAN_FROM_TXT_HEIGHT = 25;
	private static final int FILL_SPACE1_WIDTH = 15;
	private static final int FILL_SPACE1_HEIGHT = 25;
	private static final int SCAN_BTN_WIDTH = 100;
	private static final int SCAN_BTN_HEIGHT = 25;
	private static final int BACKUP_BTN_WIDTH = 100;
	private static final int BACKUP_BTN_HEIGHT = 25;
	private static final int BROWSE_BTN_WIDTH = 15;
	private static final int BROWSE_BTN_HEIGHT = 25;
	private static final int SETTINGS_BTN_WIDTH = 100;
	private static final int SETTINGS_BTN_HEIGHT = 25;
	private static final int TARGET_LOCATION_LBL_WIDTH = 100;
	private static final int TARGET_LOCATION_LBL_HEIGHT = 25;
	private static final int TARGET_LOCATION_TXT_WIDTH = 275;
	private static final int TARGET_LOCATION_TXT_HEIGHT = 25;
	
	public UIContainer( BackupHelper backupHelper, UserConfig userConfig ) {
		
		JLabel scanFromDateLabel = new JLabel(SCAN_FROM_LBL);
		scanFromDateLabel.setPreferredSize( new Dimension( SCAN_FROM_LBL_WIDTH, SCAN_FROM_LBL_HEIGHT ) );
		scanFromDateLabel.setMinimumSize( new Dimension( SCAN_FROM_LBL_WIDTH, SCAN_FROM_LBL_HEIGHT ) );
		
		JTextField scanFromDateTxt = new JTextField( backupHelper.getlastBackupTime() );
		scanFromDateTxt.setPreferredSize( new Dimension( SCAN_FROM_TXT_WIDTH, SCAN_FROM_TXT_HEIGHT ) );
		scanFromDateTxt.setMinimumSize( new Dimension( SCAN_FROM_TXT_WIDTH, SCAN_FROM_TXT_HEIGHT ) );
		
		JLabel fillSpace1Lbl = new JLabel();
		fillSpace1Lbl.setPreferredSize( new Dimension( FILL_SPACE1_WIDTH, FILL_SPACE1_HEIGHT ) );
		fillSpace1Lbl.setMinimumSize( new Dimension( FILL_SPACE1_WIDTH, FILL_SPACE1_HEIGHT ) );
		
		JButton scanBtn = new JButton(SCAN_BTN_ACTION);
		scanBtn.setPreferredSize( new Dimension( SCAN_BTN_WIDTH, SCAN_BTN_HEIGHT ) );
		scanBtn.setMinimumSize( new Dimension( SCAN_BTN_WIDTH, SCAN_BTN_HEIGHT ) );
		
		JLabel backupLocationLbl = new JLabel(BACKUP_LOCATION_LBL);
		backupLocationLbl.setPreferredSize( new Dimension( TARGET_LOCATION_LBL_WIDTH, TARGET_LOCATION_LBL_HEIGHT ) );
		backupLocationLbl.setMinimumSize( new Dimension( TARGET_LOCATION_LBL_WIDTH, TARGET_LOCATION_LBL_HEIGHT ) );
		
		String lastBackupLocation = userConfig.getProperty( ConfigConstants.EL_LAST_BACKUP_LOCATION );
		JTextField backupLocationTxt = new JTextField( lastBackupLocation==null?"":lastBackupLocation );
		backupLocationTxt.setPreferredSize( new Dimension( TARGET_LOCATION_TXT_WIDTH, TARGET_LOCATION_TXT_HEIGHT ) );
		backupLocationTxt.setMinimumSize( new Dimension( TARGET_LOCATION_TXT_WIDTH, TARGET_LOCATION_TXT_HEIGHT ) );
		
		JButton backupLocBrowseBtn = new JButton(BROWSE_BTN_ACTION);
		backupLocBrowseBtn.setPreferredSize( new Dimension( BROWSE_BTN_WIDTH, BROWSE_BTN_WIDTH ) );
		backupLocBrowseBtn.setMinimumSize( new Dimension( BROWSE_BTN_WIDTH, BROWSE_BTN_HEIGHT ) );
		
		JButton backupBtn = new JButton(BACKUP_BTN_ACTION);
		backupBtn.setPreferredSize( new Dimension( BACKUP_BTN_WIDTH, BACKUP_BTN_HEIGHT ) );
		backupBtn.setMinimumSize( new Dimension( BACKUP_BTN_WIDTH, BACKUP_BTN_HEIGHT ) );
		backupBtn.setEnabled(false);
		
		JButton settingsBtn = new JButton(SCAN_BTN_ACTION);
		settingsBtn.setPreferredSize( new Dimension( SETTINGS_BTN_WIDTH, SETTINGS_BTN_HEIGHT ) );
		settingsBtn.setMinimumSize( new Dimension( SETTINGS_BTN_WIDTH, SETTINGS_BTN_HEIGHT ) );
		
		GridBagConstraints x0y0GridCons = new GridBagConstraints();
		x0y0GridCons.gridx = 0;
		x0y0GridCons.gridy = 0;
		x0y0GridCons.insets = new Insets(5,10,5,10);
		x0y0GridCons.anchor = GridBagConstraints.LINE_START;
		
		GridBagConstraints x1y0GridCons = new GridBagConstraints();
		x1y0GridCons.gridx = 1;
		x1y0GridCons.gridy = 0;
		x1y0GridCons.insets = new Insets(5,10,5,0);
		x1y0GridCons.anchor = GridBagConstraints.BASELINE_LEADING;
		
		GridBagConstraints x2y0GridCons = new GridBagConstraints();
		x2y0GridCons.gridx = 2;
		x2y0GridCons.gridy = 0;
		x2y0GridCons.insets = new Insets(5,0,5,10);
		
		GridBagConstraints x3y0GridCons = new GridBagConstraints();
		x3y0GridCons.gridx = 3;
		x3y0GridCons.gridy = 0;
		x3y0GridCons.insets = new Insets(5,10,5,10);
		
		GridBagConstraints x0y1GridCons = new GridBagConstraints();
		x0y1GridCons.gridx = 0;
		x0y1GridCons.gridy = 1;
		x0y1GridCons.insets = new Insets(5,10,5,10);
		x0y1GridCons.anchor = GridBagConstraints.LINE_START;
		
		GridBagConstraints x1y1GridCons = new GridBagConstraints();
		x1y1GridCons.gridx = 1;
		x1y1GridCons.gridy = 1;
		x1y1GridCons.insets = new Insets(5,10,5,0);
		
		GridBagConstraints x2y1GridCons = new GridBagConstraints();
		x2y1GridCons.gridx = 2;
		x2y1GridCons.gridy = 1;
		x2y1GridCons.insets = new Insets(5,0,5,10);
		
		GridBagConstraints x3y1GridCons = new GridBagConstraints();
		x3y1GridCons.gridx = 3;
		x3y1GridCons.gridy = 1;
		x3y1GridCons.insets = new Insets(5,10,5,10);
		
		JPanel panel = new JPanel();
		//panel.setPreferredSize( new Dimension( FRAME_WIDTH, FRAME_HEIGHT ) );
		//panel.setMinimumSize( new Dimension( FRAME_WIDTH, FRAME_HEIGHT ) );
		panel.setLayout( new GridBagLayout() );
		panel.add( scanFromDateLabel, x0y0GridCons );
		panel.add( scanFromDateTxt, x1y0GridCons );
		panel.add( fillSpace1Lbl, x2y0GridCons );
		panel.add( scanBtn, x3y0GridCons );
		panel.add( backupLocationLbl, x0y1GridCons );
		panel.add( backupLocationTxt, x1y1GridCons );
		panel.add( backupLocBrowseBtn, x2y1GridCons );
		panel.add( backupBtn, x3y1GridCons );

		JFrame mainFrame = new JFrame(TITLE);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setPreferredSize( new Dimension( FRAME_WIDTH, FRAME_HEIGHT ) );
		mainFrame.setMinimumSize( new Dimension( FRAME_WIDTH, FRAME_HEIGHT ) );
		mainFrame.getContentPane().add(panel);
		mainFrame.isResizable();
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null); // position to center of screen 
		mainFrame.setVisible(true);
		
		MainActionListener mainActionListener = new MainActionListener( backupHelper, backupBtn );
		scanBtn.addActionListener(mainActionListener);
		backupBtn.addActionListener(mainActionListener);
		
	}
	
}
