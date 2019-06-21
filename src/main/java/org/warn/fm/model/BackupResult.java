package org.warn.fm.model;

import java.util.Calendar;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.warn.utils.datetime.DateTimeUtil;
import org.warn.utils.file.FileHelper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class BackupResult {
	
	private static final String STATUS_MSG_ALL_SUCCESSFUL = "Backup completed successfully!";
	private static final String STATUS_MSG_SOME_FAILED = "Backup failed for certain files";
	private static final String STATUS_MSG_ALL_FAILED = "Backup failed!";
	private static final String BACKUP_SUMMARY_LBL_HTML =
			"<html>" +
				"<br>" +
				"%s<br>" + // status message
				"<br>" +
				"Backup Time: %s<br>" + 
				"Backup Location: %s<br>" + 
				"Total Files: %d<br>" + 
				"Duration: %s<br>" + 
				"Saved Files: %d (%s)<br>" +
				"Failed Files: %d<br><br>" +
			"</html>";
	
	@NonNull
	private Calendar backupTime;
	
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
	
	public String getBackupResultsSummaryHTML() {
		return String.format( BACKUP_SUMMARY_LBL_HTML, 
				getBackupStatusMessage(), 
				DateTimeUtil.fullTimestampSDF.format( backupTime.getTimeInMillis() ),
				backupLocation,
				totalFileCount, 
				DateTimeUtil.formatDuration( TimeUnit.SECONDS, (int) duration ), 
				savedFiles.size(), FileHelper.printFileSizeUserFriendly( savedFileSize ),
				failedFiles.size() );
	}
	
	private String getBackupStatusMessage() {
		BackupStatus status = getBackupStatus();
		if( status.equals( BackupStatus.SUCCESS ) ) {
			return STATUS_MSG_ALL_SUCCESSFUL;
		}
		if( status.equals( BackupStatus.SOME_FAILED ) ) {
			return STATUS_MSG_SOME_FAILED;
		}
		if( status.equals( BackupStatus.FAILED ) ) {
			return STATUS_MSG_ALL_FAILED;
		}
		return "";
	}
	
}
