package org.warn.fm.ui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JButton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.warn.fm.backup.BackupFile;
import org.warn.fm.backup.BackupHelper;
import org.warn.fm.ui.UIContainer;

public class MainActionListener implements ActionListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MainActionListener.class);
	
	private BackupHelper backupHelper;
	private JButton backupBtn;
	
	public MainActionListener( BackupHelper backupHelper, JButton backupBtn ) {
		this.backupHelper = backupHelper;
		this.backupBtn = backupBtn;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		String command = e.getActionCommand();
		switch(command) {
			
			case UIContainer.SCAN_BTN_ACTION:
				Set<BackupFile> newOrModifiedFiles = backupHelper.scanForFileChanges();
				if( newOrModifiedFiles != null && newOrModifiedFiles.size() > 0 ) {
					backupBtn.setEnabled(true);
				}
				break;
			
			case UIContainer.BACKUP_BTN_ACTION:
				LOGGER.debug("Starting backup process..");
				break;
			
		}
	}
}
