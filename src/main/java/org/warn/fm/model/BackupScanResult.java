package org.warn.fm.model;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BackupScanResult {
	
	private Set<BackupFile> newOrModifiedFiles;
	private int totalFileCount;
	private long newOrModifiedFileSize;
	private double duration;
	
	public int getNewOrModifiedFileCount() {
		if( this.newOrModifiedFiles != null ) {
			return this.newOrModifiedFiles.size();
		}
		return 0;
	}

}
