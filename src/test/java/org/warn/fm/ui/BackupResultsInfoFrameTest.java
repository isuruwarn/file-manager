package org.warn.fm.ui;

import java.util.Set;

import org.junit.Test;
import org.warn.fm.model.BackupFile;
import org.warn.fm.util.TestUtils;

public class BackupResultsInfoFrameTest {
	
	@Test
	public void testBackupResultsInfoFrame() throws InterruptedException {
		Set<BackupFile> backupFiles = TestUtils.getBackupFileSet( 1000, null );
		new BackupResultsInfoFrame( "Test", backupFiles );
		Thread.sleep(15000);
	}

}
