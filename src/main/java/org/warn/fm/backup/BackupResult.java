package org.warn.fm.backup;

import java.util.HashSet;
import java.util.Set;

public class BackupResult {
	
	private Set<BackupFile> failedFiles = new HashSet<BackupFile>();
	private int totalFileCount;
	private double duration;

	public Set<BackupFile> getFailedFiles() {
		return failedFiles;
	}
	
	public void addFailedFile( BackupFile f ) {
		this.failedFiles.add(f);
	}

	public int getTotalFileCount() {
		return totalFileCount;
	}

	public void setTotalFileCount(int totalFileCount) {
		this.totalFileCount = totalFileCount;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}
	
	
}
