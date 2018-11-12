package org.warn.fm;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.warn.fm.backup.BackupHelper;
import org.warn.fm.config.ConfigConstants;
import org.warn.fm.ui.UIContainer;
import org.warn.utils.config.UserConfig;

public class FileManagerApp {
	
	private static final Logger LOGGER = LoggerFactory.getLogger( FileManagerApp.class );
	
	public static void main( String[] args ) {
		
		UserConfig uc = new UserConfig( null, ConfigConstants.FILEMAN_HOME_DIR_NAME, ConfigConstants.FILEMAN_CONFIG_FILE );
		BackupHelper bh = new BackupHelper(uc);
		//bh.scanForFileChanges();
		
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			
		} catch (ClassNotFoundException e) {
			LOGGER.error("Error while loading UI Manager", e);
		} catch (InstantiationException e) {
			LOGGER.error("Error while loading UI Manager", e);
		} catch (IllegalAccessException e) {
			LOGGER.error("Error while loading UI Manager", e);
		} catch (UnsupportedLookAndFeelException e) {
			LOGGER.error("Error while loading UI Manager", e);
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
