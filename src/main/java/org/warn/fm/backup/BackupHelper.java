package org.warn.fm.backup;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.warn.fm.config.ConfigConstants;
import org.warn.fm.util.GlobalConstants;
import org.warn.utils.config.UserConfig;
import org.warn.utils.core.Env;

public class BackupHelper {
	
	public static final String DOCUMENTS = "Documents";
	public static final String PICTURES = "Pictures";
	public static final String MUSIC = "Music";
	public static final String VIDEOS = "Videos";
	public static final String DOWNLOADS = "Downloads";
	public static final String USER_HOME_DIR = Env.getUserHomeDir();
	
	private static final Logger LOGGER = LoggerFactory.getLogger( BackupHelper.class );
	
	private UserConfig userConfig;
	private Set<String> includeDirs;
	private Set<String> excludeDirs;
	private Set<String> includePatterns;
	private Set<String> excludePatterns;
	private Calendar lastBackupTime;
	
	public BackupHelper( UserConfig userConfig ) {
		
		this.userConfig = userConfig;
		this.includeDirs = new HashSet<String>();
		
		// add default paths
		this.includeDirs.add( Paths.get( USER_HOME_DIR, DOCUMENTS ).toString() );
		this.includeDirs.add( Paths.get( USER_HOME_DIR, PICTURES ).toString() );
		this.includeDirs.add( Paths.get( USER_HOME_DIR, MUSIC ).toString() );
		this.includeDirs.add( Paths.get( USER_HOME_DIR, VIDEOS ).toString() );
		this.includeDirs.add( Paths.get( USER_HOME_DIR, DOWNLOADS ).toString() );
		
		// add any user defined paths from config file
		addElements( userConfig.getListProperty( ConfigConstants.EL_BACKUP_INCLUDE_DIRS ), this.includeDirs );
		this.includeDirs.add( Paths.get( USER_HOME_DIR, "dev" ).toString() );
		this.includeDirs.add( Paths.get( USER_HOME_DIR, "OneDrive - SAP SE", "Documents" ).toString() );
		
		// add any user defined exclude paths from config file
		this.excludeDirs = new HashSet<String>();
		addElements( userConfig.getListProperty( ConfigConstants.EL_BACKUP_EXCLUDE_DIRS ), this.excludeDirs );
		this.excludeDirs.add( Paths.get( USER_HOME_DIR, "dev/android" ).toString() );
		this.excludeDirs.add( Paths.get( USER_HOME_DIR, "dev/apache-httpd" ).toString() );
		this.excludeDirs.add( Paths.get( USER_HOME_DIR, "dev/apache-jmeter-4.0" ).toString() );
		this.excludeDirs.add( Paths.get( USER_HOME_DIR, "dev/apache-maven-3.3.9" ).toString() );
		this.excludeDirs.add( Paths.get( USER_HOME_DIR, "dev/apache-tomee-plus-7.0.4" ).toString() );
		this.excludeDirs.add( Paths.get( USER_HOME_DIR, "dev/eclipse" ).toString() );
		this.excludeDirs.add( Paths.get( USER_HOME_DIR, "dev/fortify" ).toString() );
		this.excludeDirs.add( Paths.get( USER_HOME_DIR, "dev/git/sap" ).toString() );
		this.excludeDirs.add( Paths.get( USER_HOME_DIR, "dev/git/tools" ).toString() );
		this.excludeDirs.add( Paths.get( USER_HOME_DIR, "dev/git/isuru" ).toString() );
		this.excludeDirs.add( Paths.get( USER_HOME_DIR, "dev/hana" ).toString() );
		this.excludeDirs.add( Paths.get( USER_HOME_DIR, "dev/hdbstudio_workspace" ).toString() );
		this.excludeDirs.add( Paths.get( USER_HOME_DIR, "dev/Ruby22-x64" ).toString() );
		this.excludeDirs.add( Paths.get( USER_HOME_DIR, "dev/xsa" ).toString() );
		this.excludeDirs.add( Paths.get( USER_HOME_DIR, "dev/archive/Rails" ).toString() );
		this.excludeDirs.add( Paths.get( USER_HOME_DIR, "dev/eclipse_workspaces" ).toString() );
		
		// add any user defined exclude patterns from config file
		this.excludePatterns = new HashSet<String>();
		addElements( userConfig.getListProperty( ConfigConstants.EL_BACKUP_EXCLUDE_PATTERNS ), this.excludePatterns );
		this.excludePatterns.add(".metadata");
		this.excludePatterns.add(".settings");
		this.excludePatterns.add(".git");
		this.excludePatterns.add("bin");
		this.excludePatterns.add("node");
		this.excludePatterns.add("node_modules");
		this.excludePatterns.add("target");
		
		// add any user defined include patterns from config file
		this.includePatterns = new HashSet<String>();
		addElements( userConfig.getListProperty( ConfigConstants.EL_BACKUP_INCLUDE_PATTERNS ), this.includePatterns );
		
		// check for last backup timestamp in config file
		String strLastBackupTime = userConfig.getProperty( ConfigConstants.EL_LAST_BACKUP_TIME );
		if( strLastBackupTime != null && !strLastBackupTime.isEmpty() ) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat( GlobalConstants.FULL_TS_FORMAT );
				Date date = sdf.parse( strLastBackupTime );
				this.lastBackupTime = Calendar.getInstance();
				this.lastBackupTime.setTime(date);
			} catch( ParseException e ) {
				LOGGER.error( "Error while passing saved backup timestamp! Using default backup timestamp", e );
				setDefaultBackupTimestamp();
			}
		} else {
			// if config timestamp is null, set default timestamp
			setDefaultBackupTimestamp();
		}
	}
	
	
	public Set<BackupFile> scanForFileChanges() {
		
		long startTime = System.currentTimeMillis();
		
		SimpleDateFormat sdf = new SimpleDateFormat( GlobalConstants.FULL_TS_FORMAT );
		LOGGER.info("Last Backup Time - " + sdf.format( this.lastBackupTime.getTimeInMillis() ) );
		
		BackupScanner scanner = new BackupScanner( this.lastBackupTime, this.excludeDirs, this.excludePatterns );
		EnumSet<FileVisitOption> opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
		try {
			for( String dir: this.includeDirs ) {
				Files.walkFileTree( Paths.get(dir), opts, Integer.MAX_VALUE, scanner );
			}
		} catch (IOException e) {
			LOGGER.error("Error while scanning for file changes", e);
		}
		
		Set<BackupFile> newOrModifiedFiles = scanner.getNewOrModifiedFiles();
		newOrModifiedFiles.stream().forEach( f -> {
			LOGGER.debug( f.toString() );
		});
		LOGGER.info("Total Files - " + scanner.getTotalFileCount() );
		LOGGER.info("New or Modified Files - " + newOrModifiedFiles.size() );
		
		long endTime = System.currentTimeMillis();
		userConfig.updateConfig( ConfigConstants.EL_LAST_BACKUP_TIME, sdf.format( this.lastBackupTime.getTimeInMillis() ) );
		LOGGER.info("Completed in " + (endTime - startTime) / 1000 + " second(s)..");
		
		return newOrModifiedFiles;
	}
	
	public String getlastBackupTime() {
		SimpleDateFormat sdf = new SimpleDateFormat( GlobalConstants.SCAN_FROM_TS_FORMAT );
		return sdf.format( this.lastBackupTime.getTimeInMillis() );
	}
	
	private void addElements( List<String> list, Set<String> set ) {
		if( list != null ) {
			for( String s: list ) {
				set.add(s);
			}
		}
	}
	
	private void setDefaultBackupTimestamp() {
		this.lastBackupTime = Calendar.getInstance();
		this.lastBackupTime.add( Calendar.MONTH, -6 );
		this.lastBackupTime.set( Calendar.HOUR_OF_DAY, 0 );
		this.lastBackupTime.set( Calendar.MINUTE, 0 );
		this.lastBackupTime.set( Calendar.SECOND, 0 );
		this.lastBackupTime.set( Calendar.MILLISECOND, 0 );
	}

	public Set<String> getIncludeDirs() {
		return includeDirs;
	}

	public void saveIncludeDirs(Set<String> includeDirs) {
		this.includeDirs = includeDirs;
		userConfig.updateConfig( ConfigConstants.EL_BACKUP_INCLUDE_DIRS, includeDirs );
	}

	public Set<String> getExcludeDirs() {
		return excludeDirs;
	}

	public void saveExcludeDirs(Set<String> excludeDirs) {
		this.excludeDirs = excludeDirs;
		userConfig.updateConfig( ConfigConstants.EL_BACKUP_EXCLUDE_DIRS, excludeDirs );
	}

	public Set<String> getIncludePatterns() {
		return includePatterns;
	}

	public void saveIncludePatterns(Set<String> includePatterns) {
		this.includePatterns = includePatterns;
		userConfig.updateConfig( ConfigConstants.EL_BACKUP_INCLUDE_PATTERNS, includePatterns );
	}

	public Set<String> getExcludePatterns() {
		return excludePatterns;
	}

	public void saveExcludePatterns(Set<String> excludePatterns) {
		this.excludePatterns = excludePatterns;
		userConfig.updateConfig( ConfigConstants.EL_BACKUP_EXCLUDE_PATTERNS, excludePatterns );
	}
	
}
