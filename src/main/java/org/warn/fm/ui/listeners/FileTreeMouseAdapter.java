package org.warn.fm.ui.listeners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;
import javax.swing.JTree;

import org.warn.fm.model.BackupFile;
import org.warn.fm.ui.FileTreeHelper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FileTreeMouseAdapter extends MouseAdapter {
	
	private JPopupMenu addToDirsPopupMenu;
	private JPopupMenu addToFilesPopupMenu;
	
	public void mousePressed(MouseEvent e) {
		showPopup(e);
	}

	public void mouseReleased(MouseEvent e) {
		showPopup(e);
	}

	private void showPopup(MouseEvent e) {
		if( e.isPopupTrigger() ) {
			JTree fileTree = (JTree) e.getComponent();
			Object obj = FileTreeHelper.getSelectedUserObject(fileTree);
			if( obj instanceof BackupFile ) { // file node
				this.addToFilesPopupMenu.show( fileTree, e.getX(), e.getY() );
			} else {
				this.addToDirsPopupMenu.show( fileTree, e.getX(), e.getY() );
			}
		}
	}

}
