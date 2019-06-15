package org.warn.fm.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseListener;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import org.warn.fm.backup.BackupHelper;
import org.warn.fm.ui.listeners.ListManagerActionListener;
import org.warn.fm.ui.listeners.ListManagerMouseAdapter;

public class ListPanelBuilder {
	
	public static final String BROWSE_ACTION = "...";
	public static final String ADD_ITEM_ACTION = "Add";
	public static final String REMOVE_ITEM_ACTION = "Remove";
	public static final String COPY_ITEM_ACTION = "Copy";
	
	private static final int MAIN_PANEL_WIDTH = 525;
	private static final int MAIN_PANEL_HEIGHT = 500;
	private static final int LIST_SCROLL_PANE_WIDTH = 400;
	private static final int LIST_SCROLL_PANE_HEIGHT = 350;
	private static final int BROWSE_BTN_WIDTH = 25;
	private static final int BROWSE_BTN_HEIGHT = 25;
	private static final int ADD_ITEM_BTN_WIDTH = 75;
	private static final int ADD_ITEM_BTN_HEIGHT = 25;
	private static final int REMOVE_ITEM_BTN_WIDTH = 75;
	private static final int REMOVE_ITEM_BTN_HEIGHT = 25;
	
	public static JPanel createListPanel( String actionType, Collection<String> listItems, BackupHelper backupHelper ) {
		
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		for( String dir: listItems ) {
			listModel.addElement(dir);
		}
		JList<String> jList = new JList<String>(listModel);
		jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jList.setSelectedIndex(0);
		jList.setVisibleRowCount(15);
		
		JScrollPane listScrollPane = new JScrollPane(jList);
		listScrollPane.setPreferredSize( new Dimension( LIST_SCROLL_PANE_WIDTH, LIST_SCROLL_PANE_HEIGHT ) );
		listScrollPane.setMinimumSize( new Dimension( LIST_SCROLL_PANE_WIDTH, LIST_SCROLL_PANE_HEIGHT ) );
		
		JButton browseBtn = new JButton(BROWSE_ACTION);
		browseBtn.setPreferredSize( new Dimension( BROWSE_BTN_WIDTH, BROWSE_BTN_HEIGHT ) );
		browseBtn.setMinimumSize( new Dimension( BROWSE_BTN_WIDTH, BROWSE_BTN_HEIGHT ) );
		
		JButton addItemBtn = new JButton(ADD_ITEM_ACTION);
		addItemBtn.setEnabled(false);
		addItemBtn.setPreferredSize( new Dimension( ADD_ITEM_BTN_WIDTH, ADD_ITEM_BTN_HEIGHT ) );
		addItemBtn.setMinimumSize( new Dimension( ADD_ITEM_BTN_WIDTH, ADD_ITEM_BTN_HEIGHT ) );
		
		JButton removeItemBtn = new JButton(REMOVE_ITEM_ACTION);
		removeItemBtn.setPreferredSize( new Dimension( REMOVE_ITEM_BTN_WIDTH, REMOVE_ITEM_BTN_HEIGHT ) );
		removeItemBtn.setMinimumSize( new Dimension( REMOVE_ITEM_BTN_WIDTH, REMOVE_ITEM_BTN_HEIGHT ) );
		
		JTextField newItemTxt = new JTextField(25);
		
		JMenuItem copy = new JMenuItem( COPY_ITEM_ACTION );
		JPopupMenu listItemPopupMenu = new JPopupMenu();
		listItemPopupMenu.add(copy);
		
		MouseListener listManagerPopupListener = new ListManagerMouseAdapter( listItemPopupMenu );
		jList.addMouseListener(listManagerPopupListener);
		
		ListManagerActionListener listManagerActionListener = 
				new ListManagerActionListener( actionType, addItemBtn, removeItemBtn, newItemTxt, jList, listModel, backupHelper );
		jList.addListSelectionListener(listManagerActionListener);
		browseBtn.addActionListener(listManagerActionListener);
		addItemBtn.addActionListener(listManagerActionListener);
		removeItemBtn.addActionListener(listManagerActionListener);
		newItemTxt.getDocument().addDocumentListener(listManagerActionListener);
		newItemTxt.addActionListener(listManagerActionListener);
		copy.addActionListener(listManagerActionListener);
		
		
		//Create a panel that uses BoxLayout.
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.add(newItemTxt);
		buttonPane.add(Box.createHorizontalStrut(5));
		buttonPane.add(browseBtn);
		buttonPane.add(Box.createHorizontalStrut(5));
		buttonPane.add(addItemBtn);
		buttonPane.add(Box.createHorizontalStrut(5));
		buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
		buttonPane.add(Box.createHorizontalStrut(5));
		buttonPane.add(removeItemBtn);
		buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		JPanel listPanel = new JPanel();
		listPanel.setPreferredSize( new Dimension( MAIN_PANEL_WIDTH, MAIN_PANEL_HEIGHT ) );
		listPanel.setMinimumSize( new Dimension( MAIN_PANEL_WIDTH, MAIN_PANEL_HEIGHT ) );
		listPanel.add(listScrollPane, BorderLayout.LINE_START);
		listPanel.add(buttonPane, BorderLayout.CENTER);
		
		return listPanel;
	}

}
