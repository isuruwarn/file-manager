package org.warn.fm.ui.listeners;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.warn.fm.backup.BackupHelper;
import org.warn.fm.ui.ListManagerHelper;
import org.warn.fm.ui.UIContainer;
import org.warn.utils.swing.UICommons;

/** 
 * Based on:
 *   https://docs.oracle.com/javase/tutorial/uiswing/examples/components/ListDemoProject/src/components/ListDemo.java
 * 
 */
public class ListManagerActionListener implements ActionListener, ListSelectionListener, DocumentListener {
	
	private boolean alreadyEnabled = false;
	private String actionType;
	private JButton addItemBtn;
	private JButton removeItemBtn;
	private JTextField newItemTxt;
	private JList<String> jList;
	private DefaultListModel<String> listModel;
	private BackupHelper backupHelper;
	
	public ListManagerActionListener( String actionType, JButton addItemBtn, JButton removeItemBtn, JTextField newItemTxt, JList<String> jList, 
			DefaultListModel<String> listModel, BackupHelper backupHelper ) {
		this.actionType = actionType;
		this.addItemBtn = addItemBtn;
		this.removeItemBtn = removeItemBtn;
		this.newItemTxt = newItemTxt;
		this.jList = jList;
		this.listModel = listModel;
		this.backupHelper = backupHelper;
	}

	//Required by ActionListener.
	public void actionPerformed(ActionEvent e) {
		
		String command = e.getActionCommand();
		switch(command) {
			
			case ListManagerHelper.BROWSE_ACTION:
				if( this.actionType.equals( UIContainer.MANAGE_INCLUDE_DIRS_ACTION ) || this.actionType.equals( UIContainer.MANAGE_EXCLUDE_DIRS_ACTION )
						|| this.actionType.equals( UIContainer.MANAGE_EXCLUDE_DIR_PATTERNS_ACTION ) ) {
					UICommons.chooseDirectory( newItemTxt );
				} else {
					UICommons.chooseFile( newItemTxt );
				}
				break;
				
			case ListManagerHelper.ADD_ITEM_ACTION:
				String name = this.newItemTxt.getText();
		
				//User didn't type in a unique name...
				if (name.equals("") || alreadyInList(name)) {
					Toolkit.getDefaultToolkit().beep();
					this.newItemTxt.requestFocusInWindow();
					this.newItemTxt.selectAll();
					return;
				}
				
				int index = this.jList.getSelectedIndex(); //get selected index
				if (index == -1) { //no selection, so insert at beginning
					index = 0;
				} else {		   //add after the selected item
					index++;
				}
		
				this.listModel.insertElementAt( name, index );
				//If we just wanted to add to the end, we'd do this:
				//listModel.addElement(newItemTxt.getText());
		
				//Reset the text field.
				this.newItemTxt.requestFocusInWindow();
				this.newItemTxt.setText("");
		
				//Select the new item and make it visible.
				this.jList.setSelectedIndex(index);
				this.jList.ensureIndexIsVisible(index);
				
				//this.backupHelper.updateIncludeExcludeList( this.actionType, generateSetFromListModel() );
				this.backupHelper.addToIncludeExcludeList( this.actionType, name );
				
				break;
			
			case ListManagerHelper.REMOVE_ITEM_ACTION:
				//This method can be called only if
				//there's a valid selection
				//so go ahead and remove whatever's selected.
				name = this.jList.getSelectedValue();
				int index2 = this.jList.getSelectedIndex();
				this.listModel.remove(index2);

				int size = this.listModel.getSize();

				if (size == 0) { //Nobody's left, disable firing.
					this.removeItemBtn.setEnabled(false);

				} else { //Select an index.
					if (index2 == this.listModel.getSize()) {
						//removed item in last position
						index2--;
					}
					this.jList.setSelectedIndex(index2);
					this.jList.ensureIndexIsVisible(index2);
				}
				
				this.backupHelper.updateIncludeExcludeList( this.actionType, generateSetFromListModel() );
				//this.backupHelper.addToIncludeExcludeList( this.actionType, name );
				
				break;
			
			case ListManagerHelper.COPY_ITEM_ACTION:
				String value = this.jList.getSelectedValue();
				StringSelection stringSelection = new StringSelection(value);
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(stringSelection, null);
		}
	}
	
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting() == false) {

			if (this.jList.getSelectedIndex() == -1) {
				//No selection, disable fire button.
				this.removeItemBtn.setEnabled(false);

			} else {
				//Selection, enable the fire button.
				this.removeItemBtn.setEnabled(true);
			}
		}
	}

	//This method tests for string equality. You could certainly
	//get more sophisticated about the algorithm.  For example,
	//you might want to ignore white space and capitalization.
	protected boolean alreadyInList(String name) {
		return this.listModel.contains(name);
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
		if (!this.alreadyEnabled) {
			this.addItemBtn.setEnabled(true);
		}
	}

	private boolean handleEmptyTextField(DocumentEvent e) {
		if (e.getDocument().getLength() <= 0) {
			this.addItemBtn.setEnabled(false);
			this.alreadyEnabled = false;
			return true;
		}
		return false;
	}
	
	private Set<String> generateSetFromListModel() {
		Set<String> updatedList = new HashSet<String>();
		for( int i=0; i<this.listModel.size(); i++ ) {
			updatedList.add(  this.listModel.get(i) );
		}
		return updatedList;
	}
}
