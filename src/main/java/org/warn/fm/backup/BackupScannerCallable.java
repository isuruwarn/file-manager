package org.warn.fm.backup;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.EnumSet;
import java.util.concurrent.Callable;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BackupScannerCallable implements Callable<BackupScanner> {
	
	private String topLevelDir;
	private BackupScanner scanner;
	
	public BackupScannerCallable( String topLevelDir, BackupScanner scanner ) {
		this.topLevelDir = topLevelDir;
		this.scanner = scanner;
	}
	
	@Override
	public BackupScanner call() throws Exception {
		EnumSet<FileVisitOption> opts = EnumSet.of( FileVisitOption.FOLLOW_LINKS );
		try {
			Files.walkFileTree( Paths.get( topLevelDir ), opts, Integer.MAX_VALUE, scanner );
		} catch( IOException e ) {
			log.error("Error while scanning for file changes", e);
		}
		return this.scanner;
	}

}
