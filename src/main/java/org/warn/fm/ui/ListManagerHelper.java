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

import org.warn.fm.ui.listeners.ListManagerActionListener;

public class ListManagerHelper {
	
	public static JPanel createListPanel( Collection<String> listItems ) {
		
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		for( String dir: listItems ) {
			listModel.addElement(dir);
		}
		JList<String> list = new JList<String>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.setVisibleRowCount(15);
		
		JScrollPane listScrollPane = new JScrollPane(list);
		listScrollPane.setPreferredSize( new Dimension( 300, 200 ) );
		listScrollPane.setMinimumSize( new Dimension( 300, 200 ) );
		
		JButton addItemBtn = new JButton("Add");
		addItemBtn.setEnabled(false);

		JButton removeItemBtn = new JButton("Remove");
		JTextField newItemTxt = new JTextField(25);
		
		ListManagerActionListener listManagerActionListener = 
				new ListManagerActionListener( addItemBtn, removeItemBtn, newItemTxt, list, listModel );
		list.addListSelectionListener(listManagerActionListener);
		addItemBtn.addActionListener(listManagerActionListener);
		removeItemBtn.addActionListener(listManagerActionListener);
		newItemTxt.getDocument().addDocumentListener(listManagerActionListener);
		newItemTxt.addActionListener(listManagerActionListener);
		
		//Create a panel that uses BoxLayout.
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.add(removeItemBtn);
		buttonPane.add(Box.createHorizontalStrut(5));
		buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
		buttonPane.add(Box.createHorizontalStrut(5));
		buttonPane.add(newItemTxt);
		buttonPane.add(addItemBtn);
		buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		JPanel listPanel = new JPanel();
		listPanel.setPreferredSize( new Dimension( 350, 300 ) );
		listPanel.setMinimumSize( new Dimension( 350, 300 ) );
		listPanel.add(listScrollPane, BorderLayout.CENTER);
		listPanel.add(buttonPane, BorderLayout.PAGE_END);
		
		return listPanel;
	}

}
