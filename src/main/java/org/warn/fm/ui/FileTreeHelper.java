package org.warn.fm.ui;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.warn.fm.backup.BackupFile;

public class FileTreeHelper {
	
	private Map<String, DefaultMutableTreeNode> dirMap;
	
	public FileTreeHelper() {
		dirMap = new TreeMap<String, DefaultMutableTreeNode>();
	}
	
	public void clearTree( JTree tree ) {
		if( tree!= null ) {
			DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
			root.removeAllChildren();
			model.reload();
		}
	}
	
	public DefaultMutableTreeNode addNodes( Set<BackupFile> newOrModifiedFiles ) {
		dirMap.clear();
		for( BackupFile f: newOrModifiedFiles ) {
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(f);
			addParentDir( f.getPath(), node );
		}
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("..");
		dirMap.keySet().stream().forEachOrdered( key -> {
			DefaultMutableTreeNode node = dirMap.get(key);
			if( node.getParent() == null ) {
				root.add(node);
			}
		});
		return root;
	}
	
	private void addParentDir( Path path, DefaultMutableTreeNode childNode ) {
		if( path != null && path.getParent() != null ) {
			String strParentPath = path.getParent().toString();
			if( strParentPath != null ) {
				DefaultMutableTreeNode parentNode = dirMap.get(strParentPath);
				if( parentNode == null ) {
					parentNode = new DefaultMutableTreeNode( strParentPath );
					dirMap.put( strParentPath, parentNode );
					addParentDir( Paths.get(strParentPath), parentNode );
				}
				parentNode.add(childNode);
			}
		}
	}
	
	public static Object getSelectedUserObject( JTree fileTree ) {
		Object obj = null;
		TreePath selectionPath = fileTree.getSelectionPath();
		if( selectionPath != null ) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectionPath.getLastPathComponent();
			obj = node.getUserObject();			
		}
		return obj;
	}

}
