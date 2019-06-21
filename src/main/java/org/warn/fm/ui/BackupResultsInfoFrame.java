package org.warn.fm.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.Calendar;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.warn.fm.model.BackupFile;
import org.warn.utils.datetime.DateTimeUtil;
import org.warn.utils.file.FileHelper;
import org.warn.utils.file.FileOperations;

public class BackupResultsInfoFrame implements ActionListener {
	
	private static final String[] FILE_INFO_TABLE_COLUMNS = { 
			"", "File Name", "Location", "Status", "Size", "Created Date", "Modified Date" };
	
	private static final String COLUMN_SEPARATOR = ",";
	private static final String ROW_SEPARATOR = "\n";
	private static final String FILE_MENU_ACTION = "File";
	private static final String SAVE_MENU_ITEM_ACTION = "Save";
	
	private static final int FILE_INFO_PANEL_WIDTH = 900;
	private static final int FILE_INFO_PANEL_HEIGHT = 600;
	private static final int FILE_INFO_SCROLL_PANE_WIDTH = 880;
	private static final int FILE_INFO_SCROLL_PANE_HEIGHT = 580;
	
	private String title;
	private String[][] data;
	
	public BackupResultsInfoFrame( String title, Set<BackupFile> backupFiles ) {
		
		this.title = title;
		
		final AtomicInteger i = new AtomicInteger(0);
		data = new String[backupFiles.size()][7];
		backupFiles.stream().forEachOrdered( f -> {
			data[i.get()][0] = String.valueOf( i.get()+1 );
			data[i.get()][1] = f.toString();
			data[i.get()][2] = f.getPath().toString();
			data[i.get()][3] = f.getStatusMessage();
			data[i.get()][4] = FileHelper.printFileSizeUserFriendly( f.getFileSize() );
			data[i.get()][5] = DateTimeUtil.fullTimestampSDF.format( f.getCreatedTime().toMillis() );
			data[i.get()][6] = DateTimeUtil.fullTimestampSDF.format( f.getModifiedTime().toMillis() );
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
		table.getColumnModel().getColumn(4).setPreferredWidth(75);
		table.getColumnModel().getColumn(4).setMinWidth(75);
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
		
		JMenuItem menuItemSave = new JMenuItem(SAVE_MENU_ITEM_ACTION);
		menuItemSave.addActionListener(this);
		
		JMenu fileMenu = new JMenu(FILE_MENU_ACTION);
		fileMenu.add(menuItemSave);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		
		JPanel fileInfoPanel = new JPanel();
		fileInfoPanel.setPreferredSize( new Dimension( FILE_INFO_PANEL_WIDTH, FILE_INFO_PANEL_HEIGHT ) );
		fileInfoPanel.setMinimumSize( new Dimension( FILE_INFO_PANEL_WIDTH, FILE_INFO_PANEL_HEIGHT ) );
		fileInfoPanel.add(fileInfoScrollPane);
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
		frame.setJMenuBar(menuBar);
		frame.add( fileInfoPanel );
		frame.setLocationRelativeTo(null); // position to center of screen
		frame.pack();
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		String command = e.getActionCommand();
		
		switch(command) {
		
			case SAVE_MENU_ITEM_ACTION:
				
				StringBuilder csvOutput = new StringBuilder();
				
				// add headers
				for( String columnName: FILE_INFO_TABLE_COLUMNS ) {
					csvOutput.append( columnName + COLUMN_SEPARATOR );
				}
				csvOutput.append(ROW_SEPARATOR);
				
				// add data
				for( String[] row: data ) {
					for( String value: row ) {
						csvOutput.append( value + COLUMN_SEPARATOR );
					}
					csvOutput.append(ROW_SEPARATOR);
				}
				
				final String suggestedFilename = "filemanager_" + title.toLowerCase().replaceAll(" ", "_") 
						+ "_" + DateTimeUtil.dateSDF.format( Calendar.getInstance().getTimeInMillis() ) + ".csv";
				
				// write to file
				JFileChooser fc = new JFileChooser();
				fc.setSelectedFile( new File( suggestedFilename ) );
				fc.setFileSelectionMode( JFileChooser.FILES_ONLY );
				int returnVal = fc.showSaveDialog(null);
				if( returnVal == JFileChooser.APPROVE_OPTION ) {
					FileOperations.write( csvOutput.toString(), fc.getSelectedFile().getAbsolutePath() );
				}
				
				break;
		}
	}

}
