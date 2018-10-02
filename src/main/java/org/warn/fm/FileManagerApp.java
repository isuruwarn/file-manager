package org.warn.fm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.warn.fm.backup.BackupHelper;
import org.warn.fm.config.ConfigConstants;
import org.warn.utils.config.UserConfig;
import org.warn.utils.core.Env;

public class FileManagerApp {
	
	private static final Logger LOGGER = LoggerFactory.getLogger( FileManagerApp.class );
	
	public static void main( String[] args ) {
		
		UserConfig uc = new UserConfig( null, ConfigConstants.FILEMAN_HOME_DIR_NAME, ConfigConstants.FILEMAN_CONFIG_FILE );
		
		BackupHelper bh = new BackupHelper(uc);
		bh.scanForFileChanges();
		
//		String path = "C:\\Users\\i830520\\Documents\\Misc\\Tech";
//		String pattern = ".*";
//		FileHelper fh = new FileHelper();
//		fh.deleteFiles( path, pattern );
		
	}
}
