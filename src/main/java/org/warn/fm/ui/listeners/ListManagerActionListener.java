package org.warn.fm.ui.listeners;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ListManagerActionListener implements ActionListener, ListSelectionListener, DocumentListener {
	
	private boolean alreadyEnabled = false;
	private JButton addItemBtn;
	private JButton removeItemBtn;
	private JTextField newItemTxt;
	private JList<String> list;
	DefaultListModel<String> listModel;
	
	public ListManagerActionListener( JButton addItemBtn, JButton removeItemBtn, JTextField newItemTxt, JList<String> list, 
			DefaultListModel<String> listModel ) {
		this.addItemBtn = addItemBtn;
		this.removeItemBtn = removeItemBtn;
		this.newItemTxt = newItemTxt;
		this.list = list;
		this.listModel = listModel;
	}

	//Required by ActionListener.
	public void actionPerformed(ActionEvent e) {
		
		String command = e.getActionCommand();
		switch(command) {
			
			case "Add":
				String name = newItemTxt.getText();
		
				//User didn't type in a unique name...
				if (name.equals("") || alreadyInList(name)) {
					Toolkit.getDefaultToolkit().beep();
					newItemTxt.requestFocusInWindow();
					newItemTxt.selectAll();
					return;
				}
		
				int index = list.getSelectedIndex(); //get selected index
				if (index == -1) { //no selection, so insert at beginning
					index = 0;
				} else {		   //add after the selected item
					index++;
				}
		
				listModel.insertElementAt(newItemTxt.getText(), index);
				//If we just wanted to add to the end, we'd do this:
				//listModel.addElement(newItemTxt.getText());
		
				//Reset the text field.
				newItemTxt.requestFocusInWindow();
				newItemTxt.setText("");
		
				//Select the new item and make it visible.
				list.setSelectedIndex(index);
				list.ensureIndexIsVisible(index);
				
				break;
			
			case "Remove":
				//This method can be called only if
				//there's a valid selection
				//so go ahead and remove whatever's selected.
				int index2 = list.getSelectedIndex();
				listModel.remove(index2);

				int size = listModel.getSize();

				if (size == 0) { //Nobody's left, disable firing.
					removeItemBtn.setEnabled(false);

				} else { //Select an index.
					if (index2 == listModel.getSize()) {
						//removed item in last position
						index2--;
					}

					list.setSelectedIndex(index2);
					list.ensureIndexIsVisible(index2);
				}
				break;
		}
	}
	
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting() == false) {

			if (list.getSelectedIndex() == -1) {
				//No selection, disable fire button.
				removeItemBtn.setEnabled(false);

			} else {
				//Selection, enable the fire button.
				removeItemBtn.setEnabled(true);
			}
		}
	}

	//This method tests for string equality. You could certainly
	//get more sophisticated about the algorithm.  For example,
	//you might want to ignore white space and capitalization.
	protected boolean alreadyInList(String name) {
		return listModel.contains(name);
	}

	//Required by DocumentListener.
	public void insertUpdate(DocumentEvent e) {
		enableButton();
	}

	//Required by DocumentListener.
	public void removeUpdate(DocumentEvent e) {
		handleEmptyTextField(e);
	}

	//Required by DocumentListener.
	public void changedUpdate(DocumentEvent e) {
		if (!handleEmptyTextField(e)) {
			enableButton();
		}
	}

	private void enableButton() {
		if (!alreadyEnabled) {
			addItemBtn.setEnabled(true);
		}
	}

	private boolean handleEmptyTextField(DocumentEvent e) {
		if (e.getDocument().getLength() <= 0) {
			addItemBtn.setEnabled(false);
			alreadyEnabled = false;
			return true;
		}
		return false;
	}
}
