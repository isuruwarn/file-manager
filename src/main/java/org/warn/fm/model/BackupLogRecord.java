package org.warn.fm.model;

import java.util.Calendar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BackupLogRecord {
	
	@NonNull
	private Calendar lastBackupTime;
	
	@NonNull
	public BackupStatus backupStatus;
	
	private int totalFileCount;
	
	private int savedFileCount;
	
	private long savedFileSize;
	
	private int failedFileCount;
	
	private double duration;
	
	@NonNull
	private String backupLocation;
	
}
