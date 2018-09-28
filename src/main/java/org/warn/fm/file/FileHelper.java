package org.warn.fm.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileHelper {
	
	private static final Logger LOGGER = LoggerFactory.getLogger( FileHelper.class );
	
	public int deleteFiles( String path, String pattern ) {
		
		int deletedCount = 0;
		Path dir = Paths.get(path);
		Finder finder = new Finder(pattern);
		try {
			Files.walkFileTree( dir, finder );
		} catch (IOException e) {
			LOGGER.error("Error while scanning files for deletion", e);
		}
		
		for( Path p: finder.getMatchedFiles() ) {
			LOGGER.debug("DELETING FILE - " + p);
			try {
				Files.delete(p);
				deletedCount++;
			} catch( IOException e ) {
				LOGGER.error("Error while deleting files", e);
			}
		}
		
		LOGGER.debug("Deleted " + deletedCount + " file(s)..");
		return deletedCount;
	}

}
