package org.warn.fm.model;

import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Objects;

import org.warn.fm.util.GlobalConstants;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BackupFile implements Comparable<BackupFile> {
	
	public static final String SAVED = "Saved successfully";
	public static final String FAILED = "Failed";
	
	private Path path;
	private DeltaType deltaType;
	private FileTime createdTime;
	private FileTime modifiedTime;
	private long fileSize;
	private String statusMessage;
	
	public BackupFile( Path path ) {
		this.path = path;
	}
	
	public BackupFile( Path path, DeltaType deltaType, FileTime createdTime, FileTime modifiedTime, long fileSize ) {
		this.path = path;
		this.deltaType = deltaType;
		this.createdTime = createdTime;
		this.modifiedTime = modifiedTime;
		this.fileSize = fileSize;
	}
	
	public String getInfo() {
		SimpleDateFormat sdf = new SimpleDateFormat( GlobalConstants.FULL_TS_FORMAT );
		return ( this.createdTime==null ? "" : "Created: " + sdf.format( this.createdTime.toMillis() ) +  " | " ) 
			+ ( this.modifiedTime==null ? "" : "Modified: " + sdf.format( this.modifiedTime.toMillis() ) +  " | " ) 
			+ ( this.deltaType==null ? "" : this.deltaType + " | " )
			+ this.fileSize + " | "
			+ this.path;
	}
	
	public String toString() {
		if( this.path==null || this.path.getFileName()==null ) {
			return null;
		}
		return this.path.getFileName().toString();
	}

	public boolean equals( Object other ) {
		// if the other object is null, should return false
		if( other == null ) {
			return false;
		}
		// if other object is of a different type, return false
		// the getClass() method is used because the instanceof operator can return true for subclass instances as well
		if( this.getClass() != other.getClass() ) {
			return false;
		}
		BackupFile obj = (BackupFile) other;
		// if both objects are non-null, but path is null in both, return true
		if( this.path == null && obj.path == null ) {
			return true;
		}
		// if path is null in this object, but non-null in the other, return false
		if( this.path == null && obj.path != null ) {
			return false;
		}
		// if path is non-null in this object, call equals on the Path object 
		return this.path.equals(obj.path);
	}
	
	public int hashCode() {
		return Objects.hash( this.path );
	}
	
	public int compareTo(BackupFile obj) {
		if( this.path == null && obj.path == null ) {
			return 0;
		}
		if( this.path != null && obj.path == null ) {
			return 1;
		}
		if( this.path != null ) {
			return this.path.compareTo( obj.path );
		}
		return -1;
	}
	
}
