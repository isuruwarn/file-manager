package org.warn.fm;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.warn.fm.backup.BackupHelper;
import org.warn.fm.ui.UIContainer;
import org.warn.fm.util.ConfigConstants;
import org.warn.utils.config.UserConfig;

public class FileManagerApp {
	
	private final Logger log = LoggerFactory.getLogger( FileManagerApp.class );
	
	public static void main( String[] args ) {
		System.setProperty( ConfigConstants.FILEMAN_LOG_PROPERTY_NAME, ConfigConstants.FILEMAN_LOG_FILE );
		new FileManagerApp().loadApp();
	}
	
	public void loadApp() {
		
		log.info("Starting FileManager application..");
		final UserConfig uc = new UserConfig( null, ConfigConstants.FILEMAN_HOME_DIR_NAME, ConfigConstants.FILEMAN_CONFIG_FILE );
		final BackupHelper bh = new BackupHelper(uc);
		
		String osName = System.getProperty("os.name");
		if( osName.toLowerCase().contains("windows") ) {
			try {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
				
			} catch (ClassNotFoundException e) {
				log.error("Error while loading UI Manager", e);
			} catch (InstantiationException e) {
				log.error("Error while loading UI Manager", e);
			} catch (IllegalAccessException e) {
				log.error("Error while loading UI Manager", e);
			} catch (UnsupportedLookAndFeelException e) {
				log.error("Error while loading UI Manager", e);
			}
		}
		
		SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				new UIContainer( bh, uc );
			}
		});
	}
}
