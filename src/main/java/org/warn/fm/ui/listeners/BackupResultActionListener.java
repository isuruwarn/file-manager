package org.warn.fm.ui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.warn.fm.model.BackupResult;
import org.warn.fm.ui.BackupResultsFrame;
import org.warn.fm.ui.BackupResultsInfoFrame;
import org.warn.fm.util.GlobalConstants;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BackupResultActionListener implements ActionListener {

	private BackupResult backupResult;
	
	@Override
	public void actionPerformed( ActionEvent e ) {
		
		String command = e.getActionCommand();
		switch(command) {
			
			case BackupResultsFrame.VIEW_FAILED_FILES_ACTION:
				new BackupResultsInfoFrame( GlobalConstants.VIEW_FAILED_FILES, backupResult.getFailedFiles() );
				break;
				
			case BackupResultsFrame.VIEW_SAVED_FILES_ACTION:
				new BackupResultsInfoFrame( GlobalConstants.VIEW_SAVED_FILES, backupResult.getSavedFiles() );
				break;
			
		}
	}

}
