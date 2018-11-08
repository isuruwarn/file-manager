package org.warn.fm;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.warn.fm.backup.BackupFile;
import org.warn.fm.backup.BackupHelper;



public class UIContainer {
		
	 JFrame mainFrame;
	private JLabel scanFromDateLabel;
	private JTextField scanFromDateTxt;
	private JButton scanBtn;
	private JButton backupBtn;
	
	private BackupHelper backupHelper;
	
	public UIContainer( BackupHelper backupHelper ) {
		
		this.backupHelper = backupHelper;
		MainOCRActionListener mainActionListener = new MainOCRActionListener();
		
		this.scanFromDateLabel = new JLabel("Scan from");
		
		this.scanFromDateTxt = new JTextField( this.backupHelper.getlastBackupTime() );
		
		this.scanBtn = new JButton( "Scan" );
		this.scanBtn.addActionListener(mainActionListener);
		this.scanBtn.setEnabled(true);
		
		this.backupBtn = new JButton( "Backup" );
		this.backupBtn.addActionListener(mainActionListener);
		this.backupBtn.setEnabled(false);
		
		
		
		// layout manager configurations
		GridBagConstraints scanBtnGridCons = new GridBagConstraints();
		scanBtnGridCons.gridx = 0;
		scanBtnGridCons.gridy = 0;
		scanBtnGridCons.insets = new Insets(5,10,5,10);
		scanBtnGridCons.anchor = GridBagConstraints.LINE_START;
		
		GridBagConstraints scanFromDateLabelGridCons = new GridBagConstraints();
		scanFromDateLabelGridCons.gridx = 1;
		scanFromDateLabelGridCons.gridy = 0;
		scanFromDateLabelGridCons.insets = new Insets(5,10,5,10);
		scanFromDateLabelGridCons.anchor = GridBagConstraints.LINE_START;
		
		GridBagConstraints scanFromDateTxtGridCons = new GridBagConstraints();
		scanFromDateTxtGridCons.gridx = 2;
		scanFromDateTxtGridCons.gridy = 0;
		scanFromDateTxtGridCons.insets = new Insets(5,10,5,10);
		scanFromDateTxtGridCons.anchor = GridBagConstraints.LINE_START;
		
		GridBagConstraints backupBtnGridCons = new GridBagConstraints();
		backupBtnGridCons.gridx = 0;
		backupBtnGridCons.gridy = 1;
		backupBtnGridCons.insets = new Insets(5,10,5,10);
		backupBtnGridCons.anchor = GridBagConstraints.LINE_START;
		
		JPanel panel = new JPanel();
		panel.setLayout( new GridBagLayout() );
		panel.add( scanFromDateLabel, scanFromDateLabelGridCons );
		panel.add( scanFromDateTxt, scanFromDateTxtGridCons );
		panel.add( scanBtn, scanBtnGridCons );
		panel.add( backupBtn, backupBtnGridCons );
		
		mainFrame = new JFrame( "" );
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setPreferredSize( new Dimension( 700, 800 ) );
		mainFrame.setMinimumSize( new Dimension( 700, 800 ) );
		mainFrame.getContentPane().add(panel);
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null); // position to center of screen 
		mainFrame.setVisible(true);
		
	}
	
	
	
	class MainOCRActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			String command = e.getActionCommand();
			
			switch(command) {
				
				case "Scan":
					Set<BackupFile> newOrModifiedFiles = backupHelper.scanForFileChanges();
					if( newOrModifiedFiles != null && newOrModifiedFiles.size() > 0 ) {
						backupBtn.setEnabled(true);
					}
					break;
				
				case "Backup":
					System.out.println("backup..");
					break;
				
			
			}
		}

	}
	
	
}
