package org.warn.fm.ui;

import java.awt.Component;
import java.text.SimpleDateFormat;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.warn.fm.model.BackupFile;
import org.warn.fm.util.FileManagerUtil;
import org.warn.fm.util.GlobalConstants;

/**
 * https://docs.oracle.com/javase/tutorial/uiswing/components/tree.html#display
 */
public class FileTreeCellRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = 1L;
	
	//private Icon fileIcon = new ImageIcon("src/main/resources/fileIcon.png");

	public Component getTreeCellRendererComponent( JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus ) {
		super.getTreeCellRendererComponent( tree, value, sel, expanded, leaf, row, hasFocus );
		if( leaf ) {
			setIcon( null );
			setToolTipText( getToolTip(value) );
		} else {
			setToolTipText(null); //no tool tip
		}
		return this;
	}
	
	private String getToolTip( Object value ) {
		String toolTip = null;
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		Object obj =  node.getUserObject();
		if( obj instanceof BackupFile ) {
			BackupFile backupFile = (BackupFile) obj;
			SimpleDateFormat sdf = new SimpleDateFormat( GlobalConstants.FULL_TS_FORMAT );
			toolTip = "<html><p>" +
				//backupFile.getDeltaType() + " | " + 
				( backupFile.getCreatedTime()==null ? "" : "Created: " + sdf.format( backupFile.getCreatedTime().toMillis() ) +  "<br>" ) +
				( backupFile.getModifiedTime()==null ? "" : "Modified: " + sdf.format( backupFile.getModifiedTime().toMillis() ) +  "<br>" ) +
				"Size: " + FileManagerUtil.printFileSizeUserFriendly( backupFile.getFileSize() ) +
				"</p></html>";
		}
		return toolTip;
	}
	
}