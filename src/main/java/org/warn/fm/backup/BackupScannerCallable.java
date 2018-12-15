package org.warn.fm.backup;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.EnumSet;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BackupScannerCallable implements Callable<BackupScanner> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BackupScannerCallable.class);
	
	private String rootDir;
	private BackupScanner scanner;
	
	public BackupScannerCallable( String rootDir, BackupScanner scanner ) {
		this.rootDir = rootDir;
		this.scanner = scanner;
	}
	
	@Override
	public BackupScanner call() throws Exception {
		EnumSet<FileVisitOption> opts = EnumSet.of( FileVisitOption.FOLLOW_LINKS );
		try {
			Files.walkFileTree( Paths.get( rootDir ), opts, Integer.MAX_VALUE, scanner );
		} catch( IOException e ) {
			LOGGER.error("Error while scanning for file changes", e);
		}
		return this.scanner;
	}

}
