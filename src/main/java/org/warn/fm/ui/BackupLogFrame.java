package org.warn.fm.ui;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.warn.fm.model.BackupLogRecord;
import org.warn.utils.datetime.DateTimeUtil;

public class BackupLogFrame {
	
	private static final String TITLE = "Backup Log";
	private static final String[] BACKUP_LOG_TABLE_COLUMNS = { 
			"", "BackupTime", "Backup Status", "Total Files", "Saved Files", "Saved File Size", "Failed Files", "Duration (s)", "Backup Location" };
	
	private static final int BACKUP_LOG_PANEL_WIDTH = 900;
	private static final int BACKUP_LOG_PANEL_HEIGHT = 600;
	private static final int BACKUP_LOG_SCROLL_PANE_WIDTH = 880;
	private static final int BACKUP_LOG_SCROLL_PANE_HEIGHT = 580;
	
	public BackupLogFrame( List<BackupLogRecord> backupLogs ) {
		
		String[][] data;
		if( backupLogs != null ) {
			final AtomicInteger i = new AtomicInteger(0);
			data = new String[backupLogs.size()][9];
			backupLogs.stream().forEachOrdered( f -> {
				data[i.get()][0] = String.valueOf( i.get()+1 );
				data[i.get()][1] = DateTimeUtil.fullTimestampSDF.format( f.getLastBackupTime().getTimeInMillis() );
				data[i.get()][2] = f.getBackupStatus().toString();
				data[i.get()][3] = String.valueOf( f.getTotalFileCount() );
				data[i.get()][4] = String.valueOf( f.getSavedFileCount() );
				data[i.get()][5] = String.valueOf( f.getSavedFileSize() );
				data[i.get()][6] = String.valueOf( f.getFailedFileCount() );
				data[i.get()][7] = String.valueOf( f.getDuration() );
				data[i.get()][8] = f.getBackupLocation();
				i.incrementAndGet();
			});
		} else {
			data = new String[0][9];
		}
		
		final JTable table = new JTable( data, BACKUP_LOG_TABLE_COLUMNS );
		table.setColumnSelectionAllowed(false);
		table.setRowSelectionAllowed(false);
		table.setCellSelectionEnabled(false);
		table.setAutoCreateRowSorter(true);
		
		// index column width
		table.getColumnModel().getColumn(0).setPreferredWidth(50);
		table.getColumnModel().getColumn(0).setMaxWidth(100);
		
		// backup date column width
		table.getColumnModel().getColumn(1).setPreferredWidth(150);
		table.getColumnModel().getColumn(1).setMinWidth(150);
		table.getColumnModel().getColumn(1).setMaxWidth(300);
		
		// backup status column width
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setMinWidth(100);
		table.getColumnModel().getColumn(2).setMaxWidth(150);
		
		// total file count width
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
		table.getColumnModel().getColumn(3).setMaxWidth(100);
		
		// saved file count width
		table.getColumnModel().getColumn(4).setPreferredWidth(100);
		table.getColumnModel().getColumn(4).setMaxWidth(100);
		
		// saved file size column width
		table.getColumnModel().getColumn(5).setPreferredWidth(100);
		table.getColumnModel().getColumn(5).setMinWidth(100);
		table.getColumnModel().getColumn(5).setMaxWidth(150);

		// failed file count column width
		table.getColumnModel().getColumn(6).setPreferredWidth(100);
		table.getColumnModel().getColumn(6).setMaxWidth(100);
		
		// duration column width
		table.getColumnModel().getColumn(7).setPreferredWidth(100);
		table.getColumnModel().getColumn(7).setMinWidth(100);
		table.getColumnModel().getColumn(7).setMaxWidth(150);
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize( new Dimension( BACKUP_LOG_SCROLL_PANE_WIDTH, BACKUP_LOG_SCROLL_PANE_HEIGHT ) );
		scrollPane.setMinimumSize( new Dimension( BACKUP_LOG_SCROLL_PANE_WIDTH, BACKUP_LOG_SCROLL_PANE_HEIGHT ) );
		scrollPane.createVerticalScrollBar();
		scrollPane.createHorizontalScrollBar();
		
		JPanel panel = new JPanel();
		panel.setPreferredSize( new Dimension( BACKUP_LOG_PANEL_WIDTH, BACKUP_LOG_PANEL_HEIGHT ) );
		panel.setMinimumSize( new Dimension( BACKUP_LOG_PANEL_WIDTH, BACKUP_LOG_PANEL_HEIGHT ) );
		panel.add( scrollPane );
		panel.addComponentListener( new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				int width = panel.getWidth();
				int height = panel.getHeight();
				scrollPane.setPreferredSize( new Dimension( width, height ) );
				scrollPane.setSize( new Dimension( width, height ) );
				scrollPane.setLocation(0, 0);
				table.updateUI();
			}
		});
		
		JFrame frame = new JFrame(TITLE);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setPreferredSize( new Dimension( BACKUP_LOG_PANEL_WIDTH, BACKUP_LOG_PANEL_HEIGHT ) );
		frame.setMinimumSize( new Dimension( BACKUP_LOG_PANEL_WIDTH, BACKUP_LOG_PANEL_HEIGHT ) );
		frame.add( panel );
		frame.setLocationRelativeTo(null); // position to center of screen
		frame.pack();
		frame.setVisible(true);
	}

}
