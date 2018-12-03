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
	private Calendar scanFromDate;
	private Set<String> includeFilePatterns;
	private Set<String> excludeDirs;
	private Set<String> excludeDirPatterns;
	private Set<String> excludeFilePatterns;
	private Set<BackupFile> newOrModifiedFiles;
	
	public BackupScanner( Calendar scanFromDate, Set<String> includeFilePatterns, 
			Set<String> excludeDirs, Set<String> excludeDirPatterns, Set<String> excludeFilePatterns ) {
		this.scanFromDate = scanFromDate;
		this.includeFilePatterns = includeFilePatterns;
		this.excludeDirs = excludeDirs;
		this.excludeDirPatterns = excludeDirPatterns;
		this.excludeFilePatterns = excludeFilePatterns;
		this.totalFileCount = new AtomicInteger(0);
		this.newOrModifiedFiles = new TreeSet<BackupFile>();
	}

	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		String dirName = dir.getName( dir.getNameCount()-1 ).toString();
		if( this.excludeDirs.contains( dir.toString() ) || this.excludeDirPatterns.contains( dirName ) ) {
			return SKIP_SUBTREE;
		}
		return CONTINUE;
	}

	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		
		String fileName = file.getFileName().toString();
		totalFileCount.incrementAndGet();
		
//		if( fileName.contains(".js") ) {
//			LOGGER.debug(fileName);
//		}
 		
		// TODO FIX - run contains on each string in collection
		if( this.includeFilePatterns.size() > 0 && !this.includeFilePatterns.contains( fileName ) ) {
			return CONTINUE;
		}
		
		// TODO FIX - run contains on each string in collection
		if( this.excludeFilePatterns.contains( fileName ) ) {
			return CONTINUE;
		}
		
		FileTime createdTime = attrs.creationTime();
		FileTime modifiedTime = attrs.lastModifiedTime();
		
		if( createdTime!=null && createdTime.toMillis() >= this.scanFromDate.getTimeInMillis() ) {
			newOrModifiedFiles.add( new BackupFile( file, DeltaType.NEW, createdTime, modifiedTime ) );
			
		} else if( modifiedTime!=null && modifiedTime.toMillis() >= this.scanFromDate.getTimeInMillis() ) {
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
