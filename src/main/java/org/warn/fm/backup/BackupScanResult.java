package org.warn.fm.backup;

import java.util.Set;

public class BackupScanResult {
	
	private Set<BackupFile> newOrModifiedFiles;
	private int totalFileCount;
	private double duration;
	
	public BackupScanResult( Set<BackupFile> newOrModifiedFiles, int totalFileCount, double duration ) {
		this.newOrModifiedFiles = newOrModifiedFiles;
		this.totalFileCount = totalFileCount;
		this.duration = duration;
	}

	public Set<BackupFile> getNewOrModifiedFiles() {
		return newOrModifiedFiles;
	}

	public void setNewOrModifiedFiles(Set<BackupFile> newOrModifiedFiles) {
		this.newOrModifiedFiles = newOrModifiedFiles;
	}
	
	public int getNewOrModifiedFileCount() {
		if( this.newOrModifiedFiles != null ) {
			return this.newOrModifiedFiles.size();
		}
		return 0;
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
