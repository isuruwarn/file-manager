package org.warn.fm.model;

import org.junit.Test;

public class BackupResultTest {
	
	@Test( expected = NullPointerException.class )
	public void testNonNullConstructors() {
		new BackupResult(null, null, 0, 0, null, 0);
	}

}
