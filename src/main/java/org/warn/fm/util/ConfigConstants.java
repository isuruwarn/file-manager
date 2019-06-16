package org.warn.fm.util;

import org.warn.utils.core.Env;
import org.warn.utils.datetime.DateTimeUtil;

public class ConfigConstants {
	
	public static final String FILEMAN_HOME_DIR_NAME = ".fileman";
	public static final String FILEMAN_CONFIG_FILE = "config.json";
	public static final String FILEMAN_BACKUP_LOG_FILE = FILEMAN_HOME_DIR_NAME + Env.FILE_SEPERATOR + "backuplog.json";
	public static final String APP_PROPERTY_FILE_NAME = "application.properties";
	
	// logging properties
	public static final String FILEMAN_LOG_PROPERTY_NAME = "log.name"; // env variable name used in logback.xml 
	public static final String FILEMAN_LOG_DIR = "logs";
	public static final String FILEMAN_LOG_FILE = Env.USER_HOME_DIR + Env.FILE_SEPERATOR + FILEMAN_HOME_DIR_NAME + Env.FILE_SEPERATOR + 
			FILEMAN_LOG_DIR + Env.FILE_SEPERATOR + "application-" + DateTimeUtil.dateSDF.format( System.currentTimeMillis() ) + ".log";
	public static final String LOGBACK_FILE_OUTPUT_LOGGER = "fout";
	
	// config file property names
	public static final String EL_APP_VERSION = "app.version";
	public static final String EL_LAST_BACKUP_TIME = "lastBackupTime";
	public static final String EL_LAST_SCAN_TIME = "lastScanTime";
	public static final String EL_LAST_BACKUP_LOCATION = "lastBackupLocation";
	public static final String EL_BACKUP_INCLUDE_DIRS = "backupIncludeDirs";
	public static final String EL_BACKUP_INCLUDE_FILE_PATTERNS = "backupIncludeFilePatterns";
	public static final String EL_BACKUP_EXCLUDE_DIRS = "backupExcludeDirs";
	public static final String EL_BACKUP_EXCLUDE_DIR_PATTERNS = "backupExcludeDirPatterns";
	public static final String EL_BACKUP_EXCLUDE_FILE_PATTERNS = "backupExcludeFilePatterns";
	
}
