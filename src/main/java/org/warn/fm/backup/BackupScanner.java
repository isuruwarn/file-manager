package org.warn.fm.backup;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.SKIP_SUBTREE;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
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
import java.util.concurrent.atomic.AtomicLong;

import org.warn.fm.model.BackupFile;
import org.warn.fm.model.DeltaType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BackupScanner implements FileVisitor<Path> {
	
	private AtomicInteger totalFileCount;
	private AtomicLong newOrModifiedFileSize;
	private Calendar scanFromDate;
	private Set<String> includeFilePatterns;
	private Set<String> excludeDirs;
	private Set<String> excludeDirPatterns;
	private Set<String> excludeFilePatterns;
	private Set<BackupFile> newOrModifiedFiles;
	
	public BackupScanner( Calendar scanFromDate, Set<String> includeFilePatterns, 
			Set<String> excludeDirs, Set<String> excludeDirPatterns, Set<String> excludeFilePatterns ) {
		// TODO input validation
		this.scanFromDate = scanFromDate;
		this.includeFilePatterns = includeFilePatterns;
		this.excludeDirs = excludeDirs;
		this.excludeDirPatterns = excludeDirPatterns;
		this.excludeFilePatterns = excludeFilePatterns;
		this.totalFileCount = new AtomicInteger(0);
		this.newOrModifiedFileSize = new AtomicLong(0);
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
		
		// IF even one include file pattern has been defined, then exclude all other file patterns.
		// ELSE consider all file patterns that do not match exclude file patterns
		if( this.includeFilePatterns.size() > 0 ) {
			boolean matchFound = false;
			for( String filePattern: this.includeFilePatterns ) {
				if( fileName.contains( filePattern ) ) {
					matchFound = true;
				}
				if( !matchFound ) {
					return CONTINUE;
				}
			}
		} else {
			for( String filePattern: this.excludeFilePatterns ) {
				if( fileName.contains( filePattern ) ) {
					return CONTINUE;
				}
			}
		}
		
		FileTime createdTime = attrs.creationTime();
		FileTime modifiedTime = attrs.lastModifiedTime();
		if( createdTime!=null && createdTime.toMillis() >= this.scanFromDate.getTimeInMillis() ) {
			newOrModifiedFiles.add( new BackupFile( file, DeltaType.NEW, createdTime, modifiedTime, attrs.size() ) );
			newOrModifiedFileSize.addAndGet( attrs.size() );
			
		} else if( modifiedTime!=null && modifiedTime.toMillis() >= this.scanFromDate.getTimeInMillis() ) {
			newOrModifiedFiles.add( new BackupFile( file, DeltaType.MODIFIED, createdTime, modifiedTime, attrs.size() ) );
			newOrModifiedFileSize.addAndGet( attrs.size() );
		}
		
		return CONTINUE;
	}

	public FileVisitResult visitFileFailed( Path file, IOException exc ) throws IOException {
		if( exc instanceof FileSystemLoopException ) {
			log.error("Cycle detected: " + file);
		} else if( exc instanceof AccessDeniedException ) {
			log.error("Access denied to file - " + exc.getLocalizedMessage() );
		} else {
			log.error("Error while accessing file \"{}\"", file, exc);
		}
		return CONTINUE;
	}

	public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
		return CONTINUE;
	}

	public AtomicInteger getTotalFileCount() {
		return totalFileCount;
	}
	
	public AtomicLong getNewOrModifiedFileSize() {
		return newOrModifiedFileSize;
	}

	public Set<BackupFile> getNewOrModifiedFiles() {
		return newOrModifiedFiles;
	}
	
}
