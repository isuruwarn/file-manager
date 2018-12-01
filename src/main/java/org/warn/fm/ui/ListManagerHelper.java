package org.warn.fm.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import org.warn.fm.backup.BackupHelper;
import org.warn.fm.ui.listeners.ListManagerActionListener;

public class ListManagerHelper {
	
	public static final String ADD_ITEM_ACTION = "Add";
	public static final String REMOVE_ITEM_ACTION = "Remove";
	
	public static final String MANAGE_INCLUDE_DIRS = "Manage Include Directories";
	public static final String MANAGE_INCLUDE_PATTERNS = "Manage Include Patterns";
	public static final String MANAGE_EXCLUDE_DIRS = "Manage Exclude Directories";
	public static final String MANAGE_EXCLUDE_PATTERNS = "Manage Exclude Patterns";
	
	private static final int LIST_SCROLL_PANE_WIDTH = 330;
	private static final int LIST_SCROLL_PANE_HEIGHT = 300;
	private static final int MAIN_PANEL_WIDTH = 350;
	private static final int MAIN_PANEL_HEIGHT = 350;
	
	public static JPanel createListPanel( Collection<String> listItems, BackupHelper backupHelper ) {
		
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		for( String dir: listItems ) {
			listModel.addElement(dir);
		}
		JList<String> list = new JList<String>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.setVisibleRowCount(15);
		
		JScrollPane listScrollPane = new JScrollPane(list);
		listScrollPane.setPreferredSize( new Dimension( LIST_SCROLL_PANE_WIDTH, LIST_SCROLL_PANE_HEIGHT ) );
		listScrollPane.setMinimumSize( new Dimension( LIST_SCROLL_PANE_WIDTH, LIST_SCROLL_PANE_HEIGHT ) );
		
		JButton addItemBtn = new JButton(ADD_ITEM_ACTION);
		addItemBtn.setEnabled(false);

		JButton removeItemBtn = new JButton(REMOVE_ITEM_ACTION);
		JTextField newItemTxt = new JTextField(25);
		
		ListManagerActionListener listManagerActionListener = 
				new ListManagerActionListener( addItemBtn, removeItemBtn, newItemTxt, list, listModel, backupHelper );
		list.addListSelectionListener(listManagerActionListener);
		addItemBtn.addActionListener(listManagerActionListener);
		removeItemBtn.addActionListener(listManagerActionListener);
		newItemTxt.getDocument().addDocumentListener(listManagerActionListener);
		newItemTxt.addActionListener(listManagerActionListener);
		
		//Create a panel that uses BoxLayout.
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.add(newItemTxt);
		buttonPane.add(addItemBtn);
		buttonPane.add(Box.createHorizontalStrut(5));
		buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
		buttonPane.add(Box.createHorizontalStrut(5));
		buttonPane.add(removeItemBtn);
		buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		JPanel listPanel = new JPanel();
		listPanel.setPreferredSize( new Dimension( MAIN_PANEL_WIDTH, MAIN_PANEL_HEIGHT ) );
		listPanel.setMinimumSize( new Dimension( MAIN_PANEL_WIDTH, MAIN_PANEL_HEIGHT ) );
		listPanel.add(listScrollPane, BorderLayout.CENTER);
		listPanel.add(buttonPane, BorderLayout.PAGE_END);
		
		return listPanel;
	}

}
