package org.warn.fm.ui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.warn.fm.backup.BackupFile;
import org.warn.fm.backup.BackupHelper;
import org.warn.fm.ui.UIContainer;

public class MainActionListener implements ActionListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MainActionListener.class);
	
	private BackupHelper backupHelper;
	private JButton backupBtn;
	private JLabel statusLbl;
	private JTree fileTree;
	
	public MainActionListener( BackupHelper backupHelper, JButton backupBtn, JTree fileTree, JLabel statusLbl ) {
		this.backupHelper = backupHelper;
		this.backupBtn = backupBtn;
		this.statusLbl = statusLbl;
		this.fileTree = fileTree;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		String command = e.getActionCommand();
		switch(command) {
			
			case UIContainer.SCAN_BTN_ACTION:
				LOGGER.debug("Starting scan..");
				this.statusLbl.setText("Scanning directories for new or modified files ...");
				SwingUtilities.invokeLater( new Runnable() {
					public void run() {
						long startTime = System.currentTimeMillis();
						Set<BackupFile> newOrModifiedFiles = backupHelper.scanForFileChanges();
						/*
						Set<BackupFile> newOrModifiedFiles = new TreeSet<BackupFile>();
						//newOrModifiedFiles.add( new BackupFile( Paths.get("C:\\Users\\i830520\\Downloads\\top1"), false, null, null, null ) );
						newOrModifiedFiles.add( new BackupFile( Paths.get("C:\\Users\\i830520\\Downloads\\top1\\a.txt"), false, null, null, null ) );
						//newOrModifiedFiles.add( new BackupFile( Paths.get("C:\\Users\\i830520\\Downloads\\top1\\child1"), true, null, null, null ) );
						newOrModifiedFiles.add( new BackupFile( Paths.get("C:\\Users\\i830520\\Downloads\\top1\\child1\\b.txt"), true, null, null, null ) );
						newOrModifiedFiles.add( new BackupFile( Paths.get("C:\\Users\\i830520\\Downloads\\top1\\child1\\c.txt"), true, null, null, null ) );
						//newOrModifiedFiles.add( new BackupFile( Paths.get("C:\\Users\\i830520\\Downloads\\top1\\child1\\child1_1"), true, null, null, null ) );
						newOrModifiedFiles.add( new BackupFile( Paths.get("C:\\Users\\i830520\\Downloads\\top1\\child1\\child1_1\\c1.txt"), true, null, null, null ) );
						newOrModifiedFiles.add( new BackupFile( Paths.get("C:\\Users\\i830520\\Downloads\\top1\\child1\\child1_1\\c2.txt"), true, null, null, null ) );
						//newOrModifiedFiles.add( new BackupFile( Paths.get("C:\\Users\\i830520\\Downloads\\top1\\child2"), true, null, null, null ) );
						newOrModifiedFiles.add( new BackupFile( Paths.get("C:\\Users\\i830520\\Downloads\\top1\\child2\\d.txt"), true, null, null, null ) );
						newOrModifiedFiles.add( new BackupFile( Paths.get("C:\\Users\\i830520\\Downloads\\top1\\child2\\e.txt"), true, null, null, null ) );
						newOrModifiedFiles.add( new BackupFile( Paths.get("C:\\Users\\i830520\\Downloads\\top1\\child2\\f.txt"), true, null, null, null ) );
						newOrModifiedFiles.add( new BackupFile( Paths.get("C:\\Users\\i830520\\Downloads\\top1\\g.txt"), true, null, null, null ) );
						//newOrModifiedFiles.add( new BackupFile( Paths.get("C:\\Users\\i830520\\Downloads\\top2"), false, null, null, null ) );
						newOrModifiedFiles.add( new BackupFile( Paths.get("C:\\Users\\i830520\\Downloads\\top2\\2a.txt"), false, null, null, null ) );
						//newOrModifiedFiles.add( new BackupFile( Paths.get("C:\\Users\\i830520\\Downloads\\top2\\child1"), true, null, null, null ) );
						newOrModifiedFiles.add( new BackupFile( Paths.get("C:\\Users\\i830520\\Downloads\\top2\\child1\\2b.txt"), true, null, null, null ) );
						*/
						long endTime = System.currentTimeMillis();
						long duration = (endTime - startTime) / 1000;
						LOGGER.debug("Scan completed in " + duration + " second(s)");
						statusLbl.setText("Scan completed in " + duration + " second(s)");
						if( newOrModifiedFiles != null && newOrModifiedFiles.size() > 0 ) {
							backupBtn.setEnabled(true);
							clearTree(fileTree);
							fileTree.setRootVisible(true);
							//DefaultTreeModel treeModel = new DefaultTreeModel( addNodes( null, new File(".") ) );
							DefaultTreeModel treeModel = new DefaultTreeModel( addNodes( newOrModifiedFiles ) );
							fileTree.setModel(treeModel);
							for( int i = 0; i < fileTree.getRowCount(); i++ ) {
								fileTree.expandRow(i);
							}
						}
					}
				});
				break;
			
			case UIContainer.BACKUP_BTN_ACTION:
				LOGGER.debug("Starting backup process..");
				this.statusLbl.setText("Starting backup process..");
				SwingUtilities.invokeLater( new Runnable() {
					public void run() {
						long startTime = System.currentTimeMillis();
						// TODO
						long endTime = System.currentTimeMillis();
						long duration = (endTime - startTime) / 1000;
						LOGGER.debug("Backup process completed in " + duration + " second(s)");
						statusLbl.setText("Backup process completed in " + duration + " second(s)");
					}
				});
				break;
			
		}
	}
	
	
	/** Add nodes from under "dir" into curTop. Highly recursive. */
//	private DefaultMutableTreeNode addNodes(DefaultMutableTreeNode curTop, File dir) {
//		String curPath = dir.getPath();
//		DefaultMutableTreeNode curDir = new DefaultMutableTreeNode(curPath);
//		if (curTop != null) { // should only be null at root
//			curTop.add(curDir);
//		}
//		Vector ol = new Vector();
//		String[] tmp = dir.list();
//		for (int i = 0; i < tmp.length; i++)
//			ol.addElement(tmp[i]);
//		Collections.sort(ol, String.CASE_INSENSITIVE_ORDER);
//		File f;
//		Vector files = new Vector();
//		// Make two passes, one for Dirs and one for Files. This is #1.
//		for (int i = 0; i < ol.size(); i++) {
//			String thisObject = (String) ol.elementAt(i);
//			String newPath;
//			if (curPath.equals("."))
//			newPath = thisObject;
//			else
//			newPath = curPath + File.separator + thisObject;
//			if ((f = new File(newPath)).isDirectory())
//			addNodes(curDir, f);
//			else
//			files.addElement(thisObject);
//		}
//		// Pass two: for files.
//		for (int fnum = 0; fnum < files.size(); fnum++)
//			curDir.add(new DefaultMutableTreeNode(files.elementAt(fnum)));
//		return curDir;
//	}
	
//	private DefaultMutableTreeNode addNodes( Set<BackupFile> newOrModifiedFiles ) {
//		String currentDirPath = null;
//		DefaultMutableTreeNode currentDir = null;
//		DefaultMutableTreeNode root = new DefaultMutableTreeNode("..");
//		for( BackupFile f: newOrModifiedFiles ) {
//			String filePath = f.getPath().toString();
//			String dirPath = filePath;
//			if( filePath.lastIndexOf( File.separator) != -1 ) {
//				dirPath = filePath.substring( 0, filePath.lastIndexOf( File.separator) );
//			}
//			if( currentDirPath == null || !currentDirPath.equals(dirPath) ) {
//				currentDirPath = dirPath;
//				currentDir = new DefaultMutableTreeNode( Paths.get(dirPath) );
//				root.add(currentDir);
//			}
//			currentDir.add( new DefaultMutableTreeNode( f.getPath() ) ); 
//		}
//		return root;
//	}
	
	private Map<String, DefaultMutableTreeNode> dirMap = new TreeMap<String, DefaultMutableTreeNode>();
	private DefaultMutableTreeNode addNodes( Set<BackupFile> newOrModifiedFiles ) {
		for( BackupFile f: newOrModifiedFiles ) {
//			String strParentPath = f.getPath().getParent().toString();
//			DefaultMutableTreeNode parentNode = dirMap.get(strParentPath);
//			if( parentNode == null ) {
//				parentNode = new DefaultMutableTreeNode( strParentPath );
//				dirMap.put( strParentPath, parentNode );
//			}
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(f);
//			parentNode.add(node);
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
	
	private void clearTree( JTree tree ) {
		if( tree!= null ) {
			DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
			root.removeAllChildren();
			model.reload();
		}
	}
	
	
}
