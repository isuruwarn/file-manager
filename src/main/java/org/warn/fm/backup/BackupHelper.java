package org.warn.fm.backup;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.warn.fm.util.GlobalConstants;

public class BackupHelper {
	
	public static final String DOCUMENTS = "Documents";
	public static final String PICTURES = "Pictures";
	public static final String MUSIC = "Music";
	public static final String VIDEOS = "Videos";
	public static final String USER_HOME_DIR = System.getProperty("user.home");
	
	private static final Logger LOGGER = LoggerFactory.getLogger( BackupHelper.class );
	
	private List<Path> searchDirs;
	private List<Path> excludeDirs;
	private List<String> excludePatterns;
	private Calendar lastBackupTime;
	
	public BackupHelper() {
		
		this.searchDirs = new ArrayList<Path>();
		
		// add default paths
		this.searchDirs.add( Paths.get( USER_HOME_DIR, DOCUMENTS ) );
		this.searchDirs.add( Paths.get( USER_HOME_DIR, PICTURES ) );
		this.searchDirs.add( Paths.get( USER_HOME_DIR, MUSIC ) );
		this.searchDirs.add( Paths.get( USER_HOME_DIR, VIDEOS ) );
		
		// add any user defined paths from config file
		this.searchDirs.add( Paths.get( USER_HOME_DIR, "dev" ) );
		
		// add any user defined exclude paths from config file
		this.excludeDirs = new ArrayList<Path>();
		this.excludeDirs.add( Paths.get( USER_HOME_DIR, "dev/android" ) );
		this.excludeDirs.add( Paths.get( USER_HOME_DIR, "dev/apache-httpd" ) );
		this.excludeDirs.add( Paths.get( USER_HOME_DIR, "dev/apache-jmeter-4.0" ) );
		this.excludeDirs.add( Paths.get( USER_HOME_DIR, "dev/apache-maven-3.3.9" ) );
		this.excludeDirs.add( Paths.get( USER_HOME_DIR, "dev/apache-tomee-plus-7.0.4" ) );
		this.excludeDirs.add( Paths.get( USER_HOME_DIR, "dev/eclipse" ) );
		this.excludeDirs.add( Paths.get( USER_HOME_DIR, "dev/fortify" ) );
		this.excludeDirs.add( Paths.get( USER_HOME_DIR, "dev/git/sap" ) );
		this.excludeDirs.add( Paths.get( USER_HOME_DIR, "dev/git/tools" ) );
		this.excludeDirs.add( Paths.get( USER_HOME_DIR, "dev/git/isuru" ) );
		this.excludeDirs.add( Paths.get( USER_HOME_DIR, "dev/hana" ) );
		this.excludeDirs.add( Paths.get( USER_HOME_DIR, "dev/hdbstudio_workspace" ) );
		this.excludeDirs.add( Paths.get( USER_HOME_DIR, "dev/Ruby22-x64" ) );
		this.excludeDirs.add( Paths.get( USER_HOME_DIR, "dev/xsa" ) );
		
		this.excludeDirs.add( Paths.get( USER_HOME_DIR, "dev/archive/Rails" ) );
		this.excludeDirs.add( Paths.get( USER_HOME_DIR, "dev/eclipse_workspaces" ) );
		
		// add any user defined exclude patterns from config file
		this.excludePatterns = new ArrayList<String>();
		this.excludePatterns.add(".metadata");
		this.excludePatterns.add(".settings");
		this.excludePatterns.add(".git");
		this.excludePatterns.add("bin");
		this.excludePatterns.add("node");
		this.excludePatterns.add("node_modules");
		this.excludePatterns.add("target");
		
		// set last backup timestamp
		// read timestamp from config file
		// if timestamp exits, set this time stamp
		// if config timestamp is null, ask user to define timestamp
		
		this.lastBackupTime = Calendar.getInstance();
		this.lastBackupTime.add( Calendar.DAY_OF_MONTH, -180 );
		this.lastBackupTime.set( Calendar.HOUR_OF_DAY, 0 );
		this.lastBackupTime.set( Calendar.MINUTE, 0 );
		this.lastBackupTime.set( Calendar.SECOND, 0 );
		this.lastBackupTime.set( Calendar.MILLISECOND, 0 );
	}
	
	
	public void scanForFileChanges() {
		
		SimpleDateFormat sdf = new SimpleDateFormat( GlobalConstants.TIMESTAMP_FORMAT );
		LOGGER.info("Last Backup Time - " + sdf.format( this.lastBackupTime.getTimeInMillis() ) );
		
		BackupScanner scanner = new BackupScanner( this.lastBackupTime, this.excludeDirs, this.excludePatterns );
		try {
			for( Path p: this.searchDirs ) {
				Files.walkFileTree( p, scanner );
			}
		} catch (IOException e) {
			LOGGER.error("Error while scanning for file changes", e);
		}
		
		for( BackupFileDTO f: scanner.getDeltaDirs() ) {
			LOGGER.debug( f.toString() );
		}
		LOGGER.info("Total Files - " + scanner.getTotalFileCount() );
		LOGGER.info("New or Modified Files - " + scanner.getDeltaDirs().size() );
		
	}

}
