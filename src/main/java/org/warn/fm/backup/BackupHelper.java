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
import java.util.Set;
import java.util.TreeSet;

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
	private Set<String> includeFilePatterns;
	private Set<String> excludeDirs;
	private Set<String> excludeDirPatterns;
	private Set<String> excludeFilePatterns;
	private Calendar lastBackupTime;
	
	public BackupHelper( UserConfig userConfig ) {
		
		this.userConfig = userConfig;
		
		// add any user defined paths from config file
		this.includeDirs = new TreeSet<String>( userConfig.getListProperty( ConfigConstants.EL_BACKUP_INCLUDE_DIRS ) );
		
		// add any user defined include patterns from config file
		this.includeFilePatterns = new TreeSet<String>( userConfig.getListProperty( ConfigConstants.EL_BACKUP_INCLUDE_FILE_PATTERNS ) );
		
		// add any user defined exclude paths from config file
		this.excludeDirs = new TreeSet<String>( userConfig.getListProperty( ConfigConstants.EL_BACKUP_EXCLUDE_DIRS ) );
		
		// add any user defined exclude patterns from config file
		this.excludeDirPatterns = new TreeSet<String>( userConfig.getListProperty( ConfigConstants.EL_BACKUP_EXCLUDE_DIR_PATTERNS ) );
		
		// add any user defined exclude patterns from config file
		this.excludeFilePatterns = new TreeSet<String>( userConfig.getListProperty( ConfigConstants.EL_BACKUP_EXCLUDE_FILE_PATTERNS ) );
				
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
	
	
	public BackupScanResult scanForFileChanges( Calendar scanFromDate ) {
		
		long startTime = System.currentTimeMillis();
		
		SimpleDateFormat sdf = new SimpleDateFormat( GlobalConstants.FULL_TS_FORMAT );
		LOGGER.info("Scanning files created or modifiled after " + sdf.format( scanFromDate.getTimeInMillis() ) );
		
		BackupScanner scanner = new BackupScanner( scanFromDate, this.includeFilePatterns, 
				this.excludeDirs, this.excludeDirPatterns, this.excludeFilePatterns );
		EnumSet<FileVisitOption> opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
		try {
			for( String dir: this.includeDirs ) {
				Files.walkFileTree( Paths.get(dir), opts, Integer.MAX_VALUE, scanner );
			}
		} catch (IOException e) {
			LOGGER.error("Error while scanning for file changes", e);
		}
		Set<BackupFile> newOrModifiedFiles = scanner.getNewOrModifiedFiles();
		userConfig.updateConfig( ConfigConstants.EL_LAST_SCAN_TIME, sdf.format( System.currentTimeMillis() ) );
		
		long endTime = System.currentTimeMillis();
		double duration = (endTime - startTime) / 1000;
		LOGGER.info("Total Files - " + scanner.getTotalFileCount() );
		LOGGER.info("New or Modified Files - " + newOrModifiedFiles.size() );
		LOGGER.info("Completed in " + duration + " second(s)..");
		
		BackupScanResult scanResult = new BackupScanResult( newOrModifiedFiles, scanner.getTotalFileCount().get(), duration );
		return scanResult;
	}
	
	public String getlastBackupTime() {
		SimpleDateFormat sdf = new SimpleDateFormat( GlobalConstants.FULL_TS_FORMAT );
		return sdf.format( this.lastBackupTime.getTimeInMillis() );
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
	
	public Set<String> getIncludeFilePatterns() {
		return includeFilePatterns;
	}

	public Set<String> getExcludeDirs() {
		return excludeDirs;
	}

	public Set<String> getExcludeDirPatterns() {
		return excludeDirPatterns;
	}
	
	public Set<String> getExcludeFilePatterns() {
		return excludeFilePatterns;
	}
	
	public void updateIncludeExcludeList( String type, Set<String> updatedList ) {
		
		if( type != null && updatedList != null ) {
			if( type.equals( GlobalConstants.MANAGE_INCLUDE_DIRS ) ) {
				this.includeDirs = updatedList;
				this.userConfig.updateConfig( ConfigConstants.EL_BACKUP_INCLUDE_DIRS, this.includeDirs );
				
			} else if( type.equals( GlobalConstants.MANAGE_INCLUDE_FILE_PATTERNS ) ) {
				this.includeFilePatterns = updatedList;
				this.userConfig.updateConfig( ConfigConstants.EL_BACKUP_INCLUDE_FILE_PATTERNS, this.includeFilePatterns );
				
			} else if( type.equals( GlobalConstants.MANAGE_EXCLUDE_DIRS ) ) {
				this.excludeDirs = updatedList;
				this.userConfig.updateConfig( ConfigConstants.EL_BACKUP_EXCLUDE_DIRS, this.excludeDirs );
				
			} else if( type.equals( GlobalConstants.MANAGE_EXCLUDE_DIR_PATTERNS ) ) {
				this.excludeDirPatterns = updatedList;
				this.userConfig.updateConfig( ConfigConstants.EL_BACKUP_EXCLUDE_DIR_PATTERNS, this.excludeDirPatterns );
			
			} else if( type.equals( GlobalConstants.MANAGE_EXCLUDE_FILE_PATTERNS ) ) {
				this.excludeFilePatterns = updatedList;
				this.userConfig.updateConfig( ConfigConstants.EL_BACKUP_EXCLUDE_FILE_PATTERNS, this.excludeFilePatterns );
			}
		}
	}
	
}
