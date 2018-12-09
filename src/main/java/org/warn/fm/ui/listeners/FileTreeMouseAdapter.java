package org.warn.fm.ui.listeners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.warn.fm.backup.BackupFile;

public class FileTreeMouseAdapter extends MouseAdapter {
	
	private JPopupMenu addToDirsPopupMenu;
	private JPopupMenu addToFilesPopupMenu;
	
	public FileTreeMouseAdapter( JPopupMenu addToDirsPopupMenu, JPopupMenu addToFilesPopupMenu ) {
		this.addToDirsPopupMenu = addToDirsPopupMenu;
		this.addToFilesPopupMenu = addToFilesPopupMenu;
	}
	
	public void mousePressed(MouseEvent e) {
		showPopup(e);
	}

	public void mouseReleased(MouseEvent e) {
		showPopup(e);
	}

	private void showPopup(MouseEvent e) {
		if( e.isPopupTrigger() ) {
			JTree fileTree = (JTree) e.getComponent();
			TreePath selectionPath = fileTree.getSelectionPath();
			if( selectionPath != null ) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectionPath.getLastPathComponent();
				Object obj = node.getUserObject();
				if( obj instanceof BackupFile ) { // file node
					this.addToFilesPopupMenu.show( fileTree, e.getX(), e.getY() );
				} else {
					this.addToDirsPopupMenu.show( fileTree, e.getX(), e.getY() );
				}
			}
		}
	}

}
