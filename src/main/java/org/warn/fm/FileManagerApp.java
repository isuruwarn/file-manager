package org.warn.fm;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.warn.fm.backup.BackupHelper;
import org.warn.fm.config.ConfigConstants;
import org.warn.utils.config.UserConfig;

public class FileManagerApp {
	
	private static final Logger LOGGER = LoggerFactory.getLogger( FileManagerApp.class );
	
	public static void main( String[] args ) {
		
		UserConfig uc = new UserConfig( null, ConfigConstants.FILEMAN_HOME_DIR_NAME, ConfigConstants.FILEMAN_CONFIG_FILE );
		
		final BackupHelper bh = new BackupHelper(uc);
		//bh.scanForFileChanges();
		
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			
		} catch (InstantiationException e) {
			e.printStackTrace();
			
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			
		} catch (UnsupportedLookAndFeelException e) {
			System.err.println("Error - " + e.getMessage());
			
		}
		
    	javax.swing.SwingUtilities.invokeLater( new Runnable() {
    		
    		public void run() {
    			new UIContainer(bh);
            }
    		
        });
		
//		String path = "C:\\Users\\i830520\\Documents\\Misc\\Tech";
//		String pattern = ".*";
//		FileHelper fh = new FileHelper();
//		fh.deleteFiles( path, pattern );
		
	}
}
