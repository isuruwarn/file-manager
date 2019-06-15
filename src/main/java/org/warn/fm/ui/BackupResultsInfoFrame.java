package org.warn.fm.ui;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.text.SimpleDateFormat;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.warn.fm.model.BackupFile;
import org.warn.fm.util.FileManagerUtil;
import org.warn.fm.util.GlobalConstants;

public class BackupResultsInfoFrame {
	
	private static final SimpleDateFormat fullTimestampSDF = new SimpleDateFormat( GlobalConstants.FULL_TS_FORMAT );
	
	private static final String[] FILE_INFO_TABLE_COLUMNS = { 
			"", "File Name", "Location", "Status", "Size", "Created Date", "Modified Date" };
	
	private static final int FILE_INFO_PANEL_WIDTH = 900;
	private static final int FILE_INFO_PANEL_HEIGHT = 600;
	private static final int FILE_INFO_SCROLL_PANE_WIDTH = 880;
	private static final int FILE_INFO_SCROLL_PANE_HEIGHT = 580;
	
	public BackupResultsInfoFrame( String title, Set<BackupFile> backupFiles ) {
		
		final AtomicInteger i = new AtomicInteger(0);
		String[][] data = new String[backupFiles.size()][7];
		backupFiles.stream().forEachOrdered( f -> {
			data[i.get()][0] = String.valueOf( i.get()+1 );
			data[i.get()][1] = f.toString();
			data[i.get()][2] = f.getPath().toString();
			data[i.get()][3] = f.getStatusMessage();
			data[i.get()][4] = FileManagerUtil.printFileSizeUserFriendly( f.getFileSize() );
			data[i.get()][5] = fullTimestampSDF.format( f.getCreatedTime().toMillis() );
			data[i.get()][6] = fullTimestampSDF.format( f.getModifiedTime().toMillis() );
			i.incrementAndGet();
		});
		
		final JTable table = new JTable( data, FILE_INFO_TABLE_COLUMNS );
		table.setColumnSelectionAllowed(false);
		table.setRowSelectionAllowed(false);
		table.setCellSelectionEnabled(false);
		table.setAutoCreateRowSorter(true);
		
		// set index column width
		table.getColumnModel().getColumn(0).setPreferredWidth(50);
		table.getColumnModel().getColumn(0).setMaxWidth(100);
		
		// set size column width
		table.getColumnModel().getColumn(4).setPreferredWidth(50);
		table.getColumnModel().getColumn(4).setMinWidth(50);
		table.getColumnModel().getColumn(4).setMaxWidth(150);

		// set created date column width
		table.getColumnModel().getColumn(5).setPreferredWidth(150);
		table.getColumnModel().getColumn(5).setMinWidth(150);
		table.getColumnModel().getColumn(5).setMaxWidth(300);
		
		// set modified date column width
		table.getColumnModel().getColumn(6).setPreferredWidth(150);
		table.getColumnModel().getColumn(6).setMinWidth(150);
		table.getColumnModel().getColumn(6).setMaxWidth(300);
		
		JScrollPane fileInfoScrollPane = new JScrollPane(table);
		fileInfoScrollPane.setPreferredSize( new Dimension( FILE_INFO_SCROLL_PANE_WIDTH, FILE_INFO_SCROLL_PANE_HEIGHT ) );
		fileInfoScrollPane.setMinimumSize( new Dimension( FILE_INFO_SCROLL_PANE_WIDTH, FILE_INFO_SCROLL_PANE_HEIGHT ) );
		fileInfoScrollPane.createVerticalScrollBar();
		fileInfoScrollPane.createHorizontalScrollBar();
		
		JPanel fileInfoPanel = new JPanel();
		fileInfoPanel.setPreferredSize( new Dimension( FILE_INFO_PANEL_WIDTH, FILE_INFO_PANEL_HEIGHT ) );
		fileInfoPanel.setMinimumSize( new Dimension( FILE_INFO_PANEL_WIDTH, FILE_INFO_PANEL_HEIGHT ) );
		fileInfoPanel.add( fileInfoScrollPane );
		fileInfoPanel.addComponentListener( new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				int width = fileInfoPanel.getWidth();
				int height = fileInfoPanel.getHeight();
				fileInfoScrollPane.setPreferredSize( new Dimension( width, height ) );
				fileInfoScrollPane.setSize( new Dimension( width, height ) );
				fileInfoScrollPane.setLocation(0, 0);
				table.updateUI();
			}
		});
		
		JFrame frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setPreferredSize( new Dimension( FILE_INFO_PANEL_WIDTH, FILE_INFO_PANEL_HEIGHT ) );
		frame.setMinimumSize( new Dimension( FILE_INFO_PANEL_WIDTH, FILE_INFO_PANEL_HEIGHT ) );
		frame.add( fileInfoPanel );
		frame.setLocationRelativeTo(null); // position to center of screen
		frame.pack();
		frame.setVisible(true);
	}

}
