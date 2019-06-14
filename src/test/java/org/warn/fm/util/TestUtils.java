package org.warn.fm.util;

import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.IntStream;

import org.warn.fm.model.BackupFile;
import org.warn.fm.model.DeltaType;

public class TestUtils {
	
	public static Set<BackupFile> getBackupFileSet( int count, String status ) {
		
		Set<BackupFile> backupFiles = new TreeSet<BackupFile>();
		IntStream.range( 0, count ).forEach( i -> {
			
			BackupFile f1 =  new BackupFile( Paths.get("/c/Users/i830520/dev/git/isuru/file-manager/backupFile" + i), 
					DeltaType.NEW, FileTime.from( Instant.now() ), FileTime.from( Instant.now() ), 2048 );
			
			if( status == null ) {
				if( i % 3 == 0 ) {
					f1.setStatusMessage( BackupFile.FAILED );
				} else {
					f1.setStatusMessage( BackupFile.SAVED );
				}
			} else {
				f1.setStatusMessage( status );
			}
			
			backupFiles.add(f1);
		});
		
		return backupFiles;
	}

}
