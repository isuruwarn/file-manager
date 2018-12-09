package org.warn.fm.ui.listeners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;

public class ListManagerMouseAdapter extends MouseAdapter {
	
	private JPopupMenu listItemPopupMenu;
	
	public ListManagerMouseAdapter( JPopupMenu listItemPopupMenu ) {
		this.listItemPopupMenu = listItemPopupMenu;
	}
	
	public void mousePressed(MouseEvent e) {
		showPopup(e);
	}

	public void mouseReleased(MouseEvent e) {
		showPopup(e);
	}

	private void showPopup(MouseEvent e) {
		if( e.isPopupTrigger() ) {
			this.listItemPopupMenu.show( e.getComponent(), e.getX(), e.getY() );
		}
	}

}
