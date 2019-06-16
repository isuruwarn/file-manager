package org.warn.fm.config;

import org.warn.utils.core.Env;

public class ConfigConstants {
	
	public static final String APP_PROPERTY_FILE_NAME = "application.properties";
	public static final String FILEMAN_HOME_DIR_NAME = ".fileman";
	public static final String FILEMAN_CONFIG_FILE = "config.json";
	public static final String FILEMAN_BACKUP_LOG_FILE = FILEMAN_HOME_DIR_NAME + Env.FILE_SEPERATOR + "backuplog.json";
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
