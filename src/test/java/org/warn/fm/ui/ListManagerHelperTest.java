package org.warn.fm.ui;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.warn.fm.backup.BackupHelper;
import org.warn.fm.util.GlobalConstants;

public class ListManagerHelperTest {
	
	private BackupHelper bh = Mockito.mock( BackupHelper.class );
	
	@Before
	public void setup() {
		
	}
	
	@Test
	public void testCreateListPanel() throws InterruptedException {
		JPanel includeDirsListPanel = ListManagerHelper.createListPanel( GlobalConstants.MANAGE_INCLUDE_DIRS, bh.getIncludeDirs(), this.bh );
		JFrame frame = new JFrame("Test ListManagerHelperTest");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null); // position to center of screen
		frame.add(includeDirsListPanel);
		frame.pack();
		frame.setVisible(true);
		Thread.sleep(3000);
	}
	
}
