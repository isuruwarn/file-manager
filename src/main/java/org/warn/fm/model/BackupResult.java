package org.warn.fm.model;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class BackupResult {
	
	@NonNull
	private Set<BackupFile> savedFiles;
	
	@NonNull
	private Set<BackupFile> failedFiles;
	
	private final int totalFileCount;
	
	private final double duration; // in seconds
	
	private final String backupLocation;
	
	private final long savedFileSize;
	
	public BackupStatus getBackupStatus() {
		
		if( savedFiles.size() > 0 && failedFiles.size() <= 0 )
			return BackupStatus.SUCCESS;
		
		if( savedFiles.size() > 0 && failedFiles.size() > 0 )
			return BackupStatus.SOME_FAILED;
		
		if( savedFiles.size() <= 0 && failedFiles.size() > 0 )
			return BackupStatus.FAILED;
		
		return BackupStatus.NONE;
	}
	
//	public String getBackupResultsSummary( String summaryLabel, String statusMessage ) {
//		return String.format( summaryLabel, 
//				statusMessage, 
//				backupLocation,
//				totalFileCount, 
//				duration, 
//				savedFiles.size(), FileManagerUtil.printFileSizeUserFriendly( savedFileSize ),
//				failedFiles.size() );
//	}
	
}
