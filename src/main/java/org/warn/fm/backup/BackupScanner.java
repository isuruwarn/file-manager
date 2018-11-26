package org.warn.fm.backup;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.SKIP_SUBTREE;

import java.io.IOException;
import java.nio.file.FileSystemLoopException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Calendar;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BackupScanner implements FileVisitor<Path> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger( BackupScanner.class );
	
	private AtomicInteger totalFileCount;
	private Calendar lastBackupTime;
	private Set<String> excludeDirs;
	private Set<BackupFile> newOrModifiedFiles;
	private Set<String> excludePatterns;
	
	public BackupScanner( Calendar lastBackupTime, Set<String> excludeDirs, Set<String> excludePatterns ) {
		this.lastBackupTime = lastBackupTime;
		this.excludeDirs = excludeDirs;
		this.excludePatterns = excludePatterns;
		this.totalFileCount = new AtomicInteger(0);
		this.newOrModifiedFiles = new TreeSet<BackupFile>();
	}

	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		String dirName = dir.getName( dir.getNameCount()-1 ).toString();
		if( this.excludeDirs.contains( dir.toString() ) || this.excludePatterns.contains( dirName ) ) {
			return SKIP_SUBTREE;
		}
		return CONTINUE;
	}

	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		totalFileCount.incrementAndGet();
		FileTime createdTime = attrs.creationTime();
		FileTime modifiedTime = attrs.lastModifiedTime();
		if( createdTime!=null && createdTime.toMillis() >= this.lastBackupTime.getTimeInMillis() ) {
			newOrModifiedFiles.add( new BackupFile( file, DeltaType.NEW, createdTime, modifiedTime ) );
		} else if( modifiedTime!=null && modifiedTime.toMillis() >= this.lastBackupTime.getTimeInMillis() ) {
			newOrModifiedFiles.add( new BackupFile( file, DeltaType.MODIFIED, createdTime, modifiedTime ) );
		}
		return CONTINUE;
	}

	public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
		if( exc instanceof FileSystemLoopException ) {
			LOGGER.error("Cycle detected: " + file);
		} else {
			LOGGER.error("Error while accessing file \"{}\"", file, exc);
		}
		return CONTINUE;
	}

	public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
		return CONTINUE;
	}

	public AtomicInteger getTotalFileCount() {
		return totalFileCount;
	}

	public Set<BackupFile> getNewOrModifiedFiles() {
		return newOrModifiedFiles;
	}

}
