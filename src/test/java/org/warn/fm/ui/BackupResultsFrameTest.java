package org.warn.fm.ui;

import java.util.Calendar;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.warn.fm.model.BackupFile;
import org.warn.fm.model.BackupResult;
import org.warn.fm.util.TestUtils;

public class BackupResultsFrameTest {
	
	@Before
	public void setup() {
		
	}
	
	@Test
	public void testSuccessfulBackup() throws InterruptedException {
		
		Set<BackupFile> savedBackupFiles = TestUtils.getBackupFileSet( 1000, BackupFile.SAVED );
		Set<BackupFile> failedBackupFiles = TestUtils.getBackupFileSet( 0, BackupFile.FAILED );
		BackupResult backupResult = new BackupResult( Calendar.getInstance(), savedBackupFiles, failedBackupFiles, 
				1000, 10, "C:\\Users\\i830520\\Downloads\\MIIDETCCAfkCAQAwgcsxCzAJBgNVBAYTAkRFMRwwGgYDVQQKDBNTQVAgVHJ1c3QgQ29tbXVuaXR5MRUwEwYDVQQLDAxJb1QgU2Vydml", 1073741824L );
		new BackupResultsFrame( backupResult );
		
		Thread.sleep(3000);
	}
	
	@Test
	public void testBackupFailedForSomeFiles() throws InterruptedException {
		
		Set<BackupFile> savedBackupFiles = TestUtils.getBackupFileSet( 900, BackupFile.SAVED );
		Set<BackupFile> failedBackupFiles = TestUtils.getBackupFileSet( 100, BackupFile.FAILED );
		BackupResult backupResult = new BackupResult( Calendar.getInstance(), savedBackupFiles, failedBackupFiles, 
				1000, 10, "C:\\Users\\i830520\\Downloads", 1073741824L );
		new BackupResultsFrame( backupResult );
		
		Thread.sleep(3000);
	}
	
	@Test
	public void testBackupFailed() throws InterruptedException {
		
		Set<BackupFile> savedBackupFiles = TestUtils.getBackupFileSet( 0, BackupFile.SAVED );
		Set<BackupFile> failedBackupFiles = TestUtils.getBackupFileSet( 1000, BackupFile.FAILED );
		BackupResult backupResult = new BackupResult( Calendar.getInstance(), savedBackupFiles, failedBackupFiles, 
				1000, 0, "C:\\Users\\i830520\\Downloads", 0L );
		new BackupResultsFrame( backupResult );
		
		Thread.sleep(3000);
	}

}
