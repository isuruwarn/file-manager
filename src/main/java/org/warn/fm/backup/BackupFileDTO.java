package org.warn.fm.backup;

import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;

import org.warn.fm.util.GlobalConstants;

public class BackupFileDTO implements Comparable<BackupFileDTO> {
	
	private Path path;
	private DeltaType deltaType;
	private FileTime createdTime;
	private FileTime modifiedTime;

	public BackupFileDTO( Path path, DeltaType deltaType, FileTime createdTime, FileTime modifiedTime ) {
		this.path = path;
		this.deltaType = deltaType;
		this.createdTime = createdTime;
		this.modifiedTime = modifiedTime;
	}

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}

	public DeltaType getDeltaType() {
		return deltaType;
	}

	public void setDeltaType(DeltaType deltaType) {
		this.deltaType = deltaType;
	}

	public FileTime getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(FileTime createdTime) {
		this.createdTime = createdTime;
	}

	public FileTime getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(FileTime modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat( GlobalConstants.TIMESTAMP_FORMAT );
		return "Created: " + sdf.format( this.createdTime.toMillis() ) + " | Modified: " + sdf.format( this.modifiedTime.toMillis() ) 
			+ " | " + this.deltaType +  " | " + this.path;
	}

	public int compareTo(BackupFileDTO o) {
		return this.path.compareTo( o.getPath() );
	}
	
}
