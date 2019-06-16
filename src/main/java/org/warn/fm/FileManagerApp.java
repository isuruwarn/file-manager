package org.warn.fm;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.warn.fm.backup.BackupHelper;
import org.warn.fm.config.ConfigConstants;
import org.warn.fm.ui.UIContainer;
import org.warn.utils.config.UserConfig;

public class FileManagerApp {
	
//	private final Logger LOGGER = LoggerFactory.getLogger( FileManagerApp.class );
	
	public static void main( String[] args ) {
		
		System.setProperty( ConfigConstants.FILEMAN_LOG_PROPERTY_NAME, ConfigConstants.FILEMAN_LOG_FILE );
		final UserConfig uc = new UserConfig( null, ConfigConstants.FILEMAN_HOME_DIR_NAME, ConfigConstants.FILEMAN_CONFIG_FILE );
		final BackupHelper bh = new BackupHelper(uc);
		
		String osName = System.getProperty("os.name");
		if( osName.toLowerCase().contains("windows") ) {
			try {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
				
			} catch (ClassNotFoundException e) {
//				LOGGER.error("Error while loading UI Manager", e);
			} catch (InstantiationException e) {
//				LOGGER.error("Error while loading UI Manager", e);
			} catch (IllegalAccessException e) {
//				LOGGER.error("Error while loading UI Manager", e);
			} catch (UnsupportedLookAndFeelException e) {
//				LOGGER.error("Error while loading UI Manager", e);
			}
		}
		
		SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				new UIContainer( bh, uc );
			}
		});
		
	}
}
