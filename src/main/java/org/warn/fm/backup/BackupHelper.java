package org.warn.fm.backup;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
		
		/*
		----------------------------------------------------------------------------
		Approach 1: Single threaded scanning, iterate through each include directory
		Result: Average of 9.25 seconds / 28013 files
		----------------------------------------------------------------------------
		*/
//		BackupScanner scanner = new BackupScanner( scanFromDate, this.includeFilePatterns, 
//				this.excludeDirs, this.excludeDirPatterns, this.excludeFilePatterns );
//		EnumSet<FileVisitOption> opts = EnumSet.of( FileVisitOption.FOLLOW_LINKS );
//		try {
//			for( String dir: this.includeDirs ) {
//				Files.walkFileTree( Paths.get(dir), opts, Integer.MAX_VALUE, scanner );
//			}
//		} catch( IOException e ) {
//			LOGGER.error("Error while scanning for file changes", e);
//		}
//		Set<BackupFile> newOrModifiedFiles = scanner.getNewOrModifiedFiles();
//		int totalFileCount = scanner.getTotalFileCount().get();
		
		/*
		-----------------------------------------------------------------------------
		Approach 2: Multi-threaded scanning with FixedThreadPool using Callable task,
					one thread per include directory
		Result: Average of 7 seconds / 28013 files
		-----------------------------------------------------------------------------
		*/
		int totalFileCount = 0;
		Set<BackupFile> newOrModifiedFiles = new HashSet<BackupFile>();
		List<Future<BackupScanner>> futures = new ArrayList<>();
		ExecutorService service = Executors.newFixedThreadPool( this.includeDirs.size() );
		for( String rootDir: this.includeDirs ) {
			BackupScannerCallable task = new BackupScannerCallable( rootDir, 
					new BackupScanner( scanFromDate, this.includeFilePatterns, this.excludeDirs, this.excludeDirPatterns, this.excludeFilePatterns ) 
					);
			Future<BackupScanner> f = service.submit(task);
			futures.add(f);
		}

		for( Future<BackupScanner> f: futures ) {
			try {
				BackupScanner scanner = f.get(); // blocking operation
				newOrModifiedFiles.addAll( scanner.getNewOrModifiedFiles() );
				totalFileCount += scanner.getTotalFileCount().get();
			} catch( InterruptedException | ExecutionException e ) {
				LOGGER.error("Error while completing file scan task", e);
			}
		}
		
		long endTime = System.currentTimeMillis();
		double duration = (endTime - startTime) / 1000;
		LOGGER.info("Total Files - " + totalFileCount );
		LOGGER.info("New or Modified Files - " + newOrModifiedFiles.size() );
		LOGGER.info("Scan completed in " + duration + " second(s)..");
		
		userConfig.updateConfig( ConfigConstants.EL_LAST_SCAN_TIME, sdf.format( endTime ) );
		BackupScanResult scanResult = new BackupScanResult( newOrModifiedFiles, totalFileCount, duration );
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
	
	public void addToIncludeExcludeList( String type, String item ) {
		
		if( type != null && item != null ) {
			
			if( type.equals( GlobalConstants.MANAGE_INCLUDE_DIRS ) ) {
				
				this.includeDirs.add(item);
				this.userConfig.updateConfig( ConfigConstants.EL_BACKUP_INCLUDE_DIRS, this.includeDirs );
				
				if( this.excludeDirs.contains(item) ) {
					this.excludeDirs.remove(item);
					this.userConfig.updateConfig( ConfigConstants.EL_BACKUP_EXCLUDE_DIRS, this.excludeDirs );
				}
				
			} else if( type.equals( GlobalConstants.MANAGE_INCLUDE_FILE_PATTERNS ) ) {
				
				Path p = Paths.get(item);
				String fileName = p.getFileName().toString();
				String fileExtension = fileName.substring( fileName.indexOf(".") );
				this.includeFilePatterns.add( fileExtension );
				this.userConfig.updateConfig( ConfigConstants.EL_BACKUP_INCLUDE_FILE_PATTERNS, this.includeFilePatterns );
				
				if( this.excludeFilePatterns.contains(fileExtension) ) {
					this.excludeFilePatterns.remove(fileExtension);
					this.userConfig.updateConfig( ConfigConstants.EL_BACKUP_EXCLUDE_FILE_PATTERNS, this.excludeFilePatterns );
				}
				
			} else if( type.equals( GlobalConstants.MANAGE_EXCLUDE_DIRS ) ) {
				
				this.excludeDirs.add(item);
				this.userConfig.updateConfig( ConfigConstants.EL_BACKUP_EXCLUDE_DIRS, this.excludeDirs );
				
				if( this.includeDirs.contains(item) ) {
					this.includeDirs.remove(item);
					this.userConfig.updateConfig( ConfigConstants.EL_BACKUP_INCLUDE_DIRS, this.includeDirs );
				}
				
			} else if( type.equals( GlobalConstants.MANAGE_EXCLUDE_DIR_PATTERNS ) ) {
				
				Path p = Paths.get(item);
				this.excludeDirPatterns.add( p.getFileName().toString() );
				this.userConfig.updateConfig( ConfigConstants.EL_BACKUP_EXCLUDE_DIR_PATTERNS, this.excludeDirPatterns );
			
			} else if( type.equals( GlobalConstants.MANAGE_EXCLUDE_FILE_PATTERNS ) ) {
				
				Path p = Paths.get(item);
				String fileName = p.getFileName().toString();
				String fileExtension = fileName.substring( fileName.indexOf(".") );
				this.excludeFilePatterns.add( fileExtension );
				this.userConfig.updateConfig( ConfigConstants.EL_BACKUP_EXCLUDE_FILE_PATTERNS, this.excludeFilePatterns );
				
				if( this.includeFilePatterns.contains(fileExtension) ) {
					this.includeFilePatterns.remove(fileExtension);
					this.userConfig.updateConfig( ConfigConstants.EL_BACKUP_INCLUDE_FILE_PATTERNS, this.includeFilePatterns );
				}
			}
		}
	}
	
}
