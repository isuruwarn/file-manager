package org.warn.fm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.warn.fm.backup.BackupHelper;

public class FileManagerApp {
	
	private static final Logger LOGGER = LoggerFactory.getLogger( FileManagerApp.class );
	
	public static void main( String[] args ) {

		long startTime = System.currentTimeMillis();
		
		BackupHelper bh = new BackupHelper();
		bh.scanForFileChanges();
		
//		String path = "C:\\Users\\i830520\\Documents\\Misc\\Tech";
//		String pattern = ".*";
//		FileHelper fh = new FileHelper();
//		fh.deleteFiles( path, pattern );
		
		long endTime = System.currentTimeMillis();
		LOGGER.info("Completed in " + (endTime - startTime) / 1000 + " second(s)..");
	}
}
