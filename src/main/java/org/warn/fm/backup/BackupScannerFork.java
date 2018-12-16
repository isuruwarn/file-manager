package org.warn.fm.backup;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumSet;
import java.util.Set;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BackupScannerFork extends RecursiveAction {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(BackupScannerFork.class);
	
	private static final int THRESHOLD = 1000;
	private Path topLevelDirPath;
	private BackupScanner scanner;
	private AtomicInteger totalFileCount;
	private Set<BackupFile> newOrModifiedFiles;
	
	public BackupScannerFork( String topLevelDir, BackupScanner scanner, Set<BackupFile> newOrModifiedFiles, AtomicInteger totalFileCount ) {
		this.topLevelDirPath = Paths.get(topLevelDir);
		this.scanner = scanner;
		this.newOrModifiedFiles = newOrModifiedFiles;
		this.totalFileCount = totalFileCount;
	}
	
	@Override
	protected void compute() {
//		if( topLevelDirPath.toFile().listFiles().length < THRESHOLD ) {
//			computeDirectly();
//			return;
//		}
//
//		int split = topLevelDirPath.toFile().listFiles().length / 2;
//		
//		invokeAll( new BackupScannerFork( topLevelDirPath.toString(), scanner,  newOrModifiedFiles, totalFileCount ),
//				new BackupScannerFork( topLevelDirPath.toString(), scanner,  newOrModifiedFiles, totalFileCount )
//				);
	}
	
//	private void computeDirectly() {
//		try {
//			EnumSet<FileVisitOption> opts = EnumSet.of( FileVisitOption.FOLLOW_LINKS );
//			Files.walkFileTree( topLevelDirPath, opts, Integer.MAX_VALUE, scanner );
//			this.newOrModifiedFiles.addAll( scanner.getNewOrModifiedFiles() );
//			this.totalFileCount.addAndGet( scanner.getTotalFileCount().get() );
//		} catch( IOException e ) {
//			LOGGER.error("Error while scanning for file changes", e);
//		}
//	}

}
