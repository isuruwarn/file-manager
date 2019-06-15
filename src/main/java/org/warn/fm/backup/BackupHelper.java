package org.warn.fm.backup;

import java.io.File;
import java.io.IOException;
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
import org.warn.fm.model.BackupFile;
import org.warn.fm.model.BackupLogRecord;
import org.warn.fm.model.BackupResult;
import org.warn.fm.model.BackupScanResult;
import org.warn.fm.ui.BackupProgressBarWorker;
import org.warn.fm.util.GlobalConstants;
import org.warn.utils.config.UserConfig;
import org.warn.utils.config.UserConfigJsonUtils;
import org.warn.utils.core.Env;
import org.warn.utils.file.FileOperations;

import lombok.Getter;

public class BackupHelper {
	
	public static final String DOCUMENTS = "Documents";
	public static final String PICTURES = "Pictures";
	public static final String MUSIC = "Music";
	public static final String VIDEOS = "Videos";
	public static final String DOWNLOADS = "Downloads";
	public static final String USER_HOME_DIR = Env.getUserHomeDir();
	
	private static final Logger LOGGER = LoggerFactory.getLogger( BackupHelper.class );
	private final SimpleDateFormat fullTimestampSDF = new SimpleDateFormat( GlobalConstants.FULL_TS_FORMAT );
	
	private UserConfig userConfig;
	
	@Getter
	private Set<String> includeDirs;
	
	@Getter
	private Set<String> includeFilePatterns;
	
	@Getter
	private Set<String> excludeDirs;
	
	@Getter
	private Set<String> excludeDirPatterns;
	
	@Getter
	private Set<String> excludeFilePatterns;
	
	@Getter
	private List<BackupLogRecord> backupLog;
	
	private Calendar lastBackupTime;
	
	public BackupHelper( UserConfig userConfig ) {
		
		this.userConfig = userConfig;
		
		// add any user defined paths from config file
		this.includeDirs = new TreeSet<String>( this.userConfig.getListProperty( ConfigConstants.EL_BACKUP_INCLUDE_DIRS ) );
		
		// add any user defined include patterns from config file
		this.includeFilePatterns = new TreeSet<String>( this.userConfig.getListProperty( ConfigConstants.EL_BACKUP_INCLUDE_FILE_PATTERNS ) );
		
		// add any user defined exclude paths from config file
		this.excludeDirs = new TreeSet<String>( this.userConfig.getListProperty( ConfigConstants.EL_BACKUP_EXCLUDE_DIRS ) );
		
		// add any user defined exclude patterns from config file
		this.excludeDirPatterns = new TreeSet<String>( this.userConfig.getListProperty( ConfigConstants.EL_BACKUP_EXCLUDE_DIR_PATTERNS ) );
		
		// add any user defined exclude patterns from config file
		this.excludeFilePatterns = new TreeSet<String>( this.userConfig.getListProperty( ConfigConstants.EL_BACKUP_EXCLUDE_FILE_PATTERNS ) );
				
		// check for last backup timestamp in config file
		String strLastBackupTime = this.userConfig.getProperty( ConfigConstants.EL_LAST_BACKUP_TIME );
		if( strLastBackupTime != null && !strLastBackupTime.isEmpty() ) {
			try {
				Date date = this.fullTimestampSDF.parse( strLastBackupTime );
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
		
		loadBackupLog();
	}

	public BackupScanResult scanForFileChanges( Calendar scanFromDate ) {
		
		long startTime = System.currentTimeMillis();
		LOGGER.info("Scanning files created or modifiled after " + this.fullTimestampSDF.format( scanFromDate.getTimeInMillis() ) );
		
		String strLastBackupLocation = this.userConfig.getProperty( ConfigConstants.EL_LAST_BACKUP_LOCATION );
		if( strLastBackupLocation != null && !strLastBackupLocation.isEmpty() ) {
			this.excludeDirs.add( strLastBackupLocation );
		}
		
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
		long newOrModifiedFileSize = 0;
		Set<BackupFile> newOrModifiedFiles = new HashSet<BackupFile>();
		List<Future<BackupScanner>> futures = new ArrayList<>();
		ExecutorService service = Executors.newFixedThreadPool( this.includeDirs.size() );
		for( String rootDir: this.includeDirs ) {
			BackupScanner scanner = new BackupScanner( scanFromDate, this.includeFilePatterns, this.excludeDirs, this.excludeDirPatterns, this.excludeFilePatterns );
			BackupScannerCallable task = new BackupScannerCallable( rootDir, scanner );
			Future<BackupScanner> f = service.submit(task);
			futures.add(f);
		}

		for( Future<BackupScanner> f: futures ) {
			try {
				BackupScanner scanner = f.get(); // blocking operation
				newOrModifiedFiles.addAll( scanner.getNewOrModifiedFiles() );
				totalFileCount += scanner.getTotalFileCount().get();
				newOrModifiedFileSize += scanner.getNewOrModifiedFileSize().get();
			} catch( InterruptedException | ExecutionException e ) {
				LOGGER.error("Error while completing file scan task", e);
			}
		}
		
		/*
		
		TODO - 	https://www.baeldung.com/java-fork-join
				https://www.baeldung.com/thread-pool-java-and-guava
		-----------------------------------------------------------------------------
		Approach 3: Multi-threaded scanning with ForkJoinPool using RecursiveAction task
		IMPLEMENTATION NOT COMPLETE
		Result: Average of   seconds /   files
		-----------------------------------------------------------------------------
		*/
//		AtomicInteger atomicTotalFileCount = new AtomicInteger(0);
//		Set<BackupFile> newOrModifiedFiles = new HashSet<BackupFile>();
//		ForkJoinPool pool = new ForkJoinPool();
//		for( String rootDir: this.includeDirs ) {
//			BackupScanner scanner = new BackupScanner( scanFromDate, this.includeFilePatterns, this.excludeDirs, this.excludeDirPatterns, this.excludeFilePatterns );
//			BackupScannerFork f = new BackupScannerFork( rootDir, scanner, newOrModifiedFiles, atomicTotalFileCount );
//			pool.invoke(f);
//		}
//		int totalFileCount = atomicTotalFileCount.get();
		
		long endTime = System.currentTimeMillis();
		double duration = (endTime - startTime) / 1000;
		LOGGER.info("Total Files - " + totalFileCount );
		LOGGER.info("New or Modified Files - " + newOrModifiedFiles.size() );
		LOGGER.info("New or Modified File Size - " + newOrModifiedFileSize );
		LOGGER.info("Scan completed in " + duration + " second(s)..");
		
		userConfig.updateConfig( ConfigConstants.EL_LAST_SCAN_TIME, this.fullTimestampSDF.format( endTime ) );
		if( strLastBackupLocation != null ) {
			this.excludeDirs.remove(strLastBackupLocation);
		}
		
		return new BackupScanResult( newOrModifiedFiles, totalFileCount, newOrModifiedFileSize, duration );
	}
	
	public BackupResult backup( Set<BackupFile> backupFiles, String backupLocation, BackupProgressBarWorker task ) {
		
		if( backupFiles == null || backupLocation == null || backupLocation.isEmpty() ) {
			return null;
		}
		
		long startTime = System.currentTimeMillis();
		int totalFiles = backupFiles.size();
		int completedCount = 0;
		
		this.userConfig.updateConfig( ConfigConstants.EL_LAST_BACKUP_LOCATION, backupLocation );
		LOGGER.info("Backup Location - " + backupLocation);
		
		long savedFileSize = 0;
		Set<BackupFile> savedFiles = new TreeSet<BackupFile>();
		Set<BackupFile> failedFiles = new TreeSet<BackupFile>();
		
		for( BackupFile f: backupFiles ) {
			
			if( !task.isCancelled() ) {
				
				LOGGER.debug("BackupFile[{}]={}", completedCount, f.getPath() );
				final String source = f.getPath().toString();
				final String target = backupLocation + File.separator + source.replace(":", "");
				final String s = target.replace( File.separator + f.getPath().getFileName().toString(), "" );
				FileOperations.checkOrCreateDir(s);
				
				try {
					Path p = FileOperations.copy( f.getPath(), Paths.get(target) );
					if( p == null ) {
						f.setStatusMessage( BackupFile.FAILED );
						failedFiles.add(f);
					} else {
						f.setStatusMessage( BackupFile.SAVED );
						savedFiles.add(f);
						savedFileSize += f.getFileSize();
					}
				} catch( IOException e ) {
					f.setStatusMessage( BackupFile.FAILED + " - " + e.getMessage() );
					failedFiles.add(f);
				}
				
				completedCount++;
				int progress = (int) ( (float) completedCount / totalFiles * 100 );
				task.setTaskBarProgress(progress);
			}
		
		}
		
		this.lastBackupTime = Calendar.getInstance();
		this.userConfig.updateConfig( ConfigConstants.EL_LAST_BACKUP_TIME, this.fullTimestampSDF.format( this.lastBackupTime.getTime() ) );
		
		long endTime = System.currentTimeMillis();
		long duration = (endTime - startTime) / 1000;
		
		BackupResult backupResult = new BackupResult( savedFiles, failedFiles, totalFiles, duration, backupLocation, savedFileSize );
		updateBackupLog( backupResult );
		
		return backupResult;
	}
	
	public String getlastBackupTime() {
		return this.fullTimestampSDF.format( this.lastBackupTime.getTimeInMillis() );
	}
	
	private void setDefaultBackupTimestamp() {
		this.lastBackupTime = Calendar.getInstance();
		this.lastBackupTime.add( Calendar.MONTH, -6 );
		this.lastBackupTime.set( Calendar.HOUR_OF_DAY, 0 );
		this.lastBackupTime.set( Calendar.MINUTE, 0 );
		this.lastBackupTime.set( Calendar.SECOND, 0 );
		this.lastBackupTime.set( Calendar.MILLISECOND, 0 );
	}
	
	private void loadBackupLog() {
//		this.backupLog = new ArrayList<BackupLogRecord>();
//		final ObjectMapper mapper = new ObjectMapper();
//		final List<String> backupLogsJsonData = UserConfigJsonUtils.loadListFromHomeDir( BackupLogRecord.class, ConfigConstants.FILEMAN_BACKUP_LOG_FILE );
//		if( backupLogsJsonData != null )
//			backupLogsJsonData.forEach( jsonStr -> {
//				try {
//					BackupLogRecord r = mapper.readValue( jsonStr, BackupLogRecord.class );
//					this.backupLog.add(r);
//				} catch( IOException  e ) {
//					LOGGER.error("Error while loading Backup Log", e);
//				}
//			});
//		JavaType type = FileManagerUtil.mapper.getTypeFactory().constructCollectionType( List.class, BackupLogRecord.class );
//		this.backupLog = UserConfigJsonUtils.loadListFromHomeDir( type, ConfigConstants.FILEMAN_BACKUP_LOG_FILE );
		this.backupLog = UserConfigJsonUtils.loadListFromHomeDir( BackupLogRecord.class, ConfigConstants.FILEMAN_BACKUP_LOG_FILE );
	}
	
	private void updateBackupLog( BackupResult backupResult ) {
		
		BackupLogRecord r = BackupLogRecord.builder()
									.lastBackupTime( this.lastBackupTime )
									.backupStatus( backupResult.getBackupStatus() )
									.totalFileCount( backupResult.getTotalFileCount() )
									.savedFileCount( backupResult.getSavedFiles().size() )
									.savedFileSize( backupResult.getSavedFileSize() )
									.failedFileCount( backupResult.getFailedFiles().size() )
									.duration( backupResult.getDuration() )
									.backupLocation( backupResult.getBackupLocation() )
									.build();
		this.backupLog.add(r);
		
//		final List<String> backupLogsJsonData =  new ArrayList<String>( this.backupLog.size() );
//		final ObjectMapper mapper = new ObjectMapper();
//		this.backupLog.forEach( backupLogRecord -> {
//			try {
//				backupLogsJsonData.add( mapper.writeValueAsString(backupLogRecord) );
//			} catch( IOException e) {
//				LOGGER.error("Error while updating Backup Log", e);
//			}
//		});
		
		UserConfigJsonUtils.updateListInHomeDir( this.backupLog, ConfigConstants.FILEMAN_BACKUP_LOG_FILE );
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
