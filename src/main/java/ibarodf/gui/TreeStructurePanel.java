package ibarodf.gui;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;

import org.json.JSONArray;
import org.json.JSONObject;


import ibarodf.core.IbarOdfCore;
import ibarodf.core.IbarOdfResultParser;
import ibarodf.core.metadata.exception.UnableToConvertToJsonFormatException;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;

public class TreeStructurePanel extends JScrollPane {
	public final static String TO_DELETE = ".";

	JTree tree;
	DefaultMutableTreeNode root;
	JSONObject currentFile;
	String currentPathText;
	Dimension preferedSize;


	public TreeStructurePanel(Dimension preferredSize) throws FileNotFoundException{
		this(preferredSize, defaultRoot());
	}



	public void setRootAsADirectory(String path){
		try{
			currentPathText = path;
			Path currentPath = IbarOdfCore.stringToPath(path);
			currentFile = IbarOdfCore.directoryToJson(currentPath);
			root = new DefaultMutableTreeNode(currentPath.getFileName());
			displayRoot(root);
			fillTreeStructure();
		}catch(FileNotFoundException | UnableToConvertToJsonFormatException e){
			System.err.println(e.getMessage());
		}
	}


	public void setRootAsFile(String path) throws Exception{
		currentPathText = path;
		Path currentPath = IbarOdfCore.stringToPath(path);
		currentFile = IbarOdfCore.RegularFileToJson(currentPath);
		root = new DefaultMutableTreeNode(currentPath.getFileName());
		displayRoot(root);
	}

	public void refresh(String path) throws Exception{
		Path newRootPath = IbarOdfCore.stringToPath(path);
		JSONObject newRoot = IbarOdfCore.RegularFileToJson(newRootPath);
		if(IbarOdfResultParser.isDirectory(newRoot)){
			setRootAsADirectory(path);
		}else{
			setRootAsFile(path);
		}
	}

	public void displayRoot(DefaultMutableTreeNode root){
		tree = new JTree(root);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(new SelectionListener());
		setViewportView(tree);
	}

	public TreeStructurePanel(Dimension preferredSize, String path){
		super();
		this.preferedSize = preferredSize;
		setPreferredSize(preferredSize);
		setBackground(Color.BLUE);
		setRootAsADirectory(path);
		tree = new JTree(root);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(new SelectionListener());
		setViewportView(tree);
	}


	public static String defaultRoot() throws FileNotFoundException{
		File[] listRoot = File.listRoots();
		return listRoot[0].toString();
	}

	public void fillTreeStructure() {
		fillTreeWithRegularFiles(root, IbarOdfResultParser.getRegularFiles(currentFile));
		fillTreeWithOdfFiles(root,IbarOdfResultParser.getOdfFiles(currentFile));
		fillTreeWithWrongFiles(root,IbarOdfResultParser.getWrongFiles(currentFile));
		DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
		model.reload(root);
	}



	public void fillTreeWithRegularFiles(DefaultMutableTreeNode root, JSONArray regularFiles){
		DefaultMutableTreeNode subDirectoryNode; 
		JSONObject currentRegularFile;
		for(int index=0, indexMax = regularFiles.length(); index<indexMax; index++){
			currentRegularFile = regularFiles.getJSONObject(index);
			subDirectoryNode = new DefaultMutableTreeNode(IbarOdfResultParser.getFileName(currentRegularFile)); 
			root.add(subDirectoryNode);
			if(IbarOdfResultParser.isDirectory(currentRegularFile)){
				subDirectoryNode.add(new DefaultMutableTreeNode(TO_DELETE));
			}
		}
	}

	public void fillTreeWithOdfFiles(DefaultMutableTreeNode root, JSONArray odfFiles){
		JSONObject currentOdfFile;
		for(int index=0, indexMax= odfFiles.length(); index<indexMax; index++){
			currentOdfFile = odfFiles.getJSONObject(index);
			root.add(new DefaultMutableTreeNode(IbarOdfResultParser.getFileName(currentOdfFile)));
		}
	}


	public void fillTreeWithWrongFiles(DefaultMutableTreeNode root, JSONArray wrongFiles){
		JSONObject currentWrongFile;
		for(int index=0, indexMax= wrongFiles.length(); index<indexMax; index++){
			currentWrongFile = wrongFiles.getJSONObject(index);
			root.add(new DefaultMutableTreeNode(IbarOdfResultParser.getFileName(currentWrongFile)));
		}
	}

	private String recreatePath(DefaultMutableTreeNode node){
		String separator = IbarOdfCore.getCurrentSystemSeparator();
		TreeNode[] parents = node.getPath();
		StringBuilder path = new StringBuilder(rootName(separator));
		for(int index=1, indexMax = parents.length; index<indexMax; index++){
			path.append(separator+parents[index]);
		}
		return path.toString();


	}

	private StringBuilder rootName(String separator){
		StringBuilder racine = new StringBuilder(currentPathText);
		if(currentPathText.endsWith(separator)){
			racine.deleteCharAt(currentPathText.length()-1);
		}
		return racine;
	}



	private class SelectionListener implements TreeSelectionListener {

		public void valueChanged(TreeSelectionEvent se) {
			JTree tree = (JTree) se.getSource();
			Object pathComponent = tree.getLastSelectedPathComponent(); 
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) pathComponent;
			String currentPath = recreatePath(selectedNode);
			try {
				Path currentNodePath = IbarOdfCore.stringToPath(currentPath);
				if(selectedNode.isLeaf()) {
					JSONObject currentFile = IbarOdfCore.RegularFileToJson(currentNodePath);
					if(IbarOdfResultParser.isOdfFile(currentFile)){
						currentFile = IbarOdfCore.odfFileToJson(currentNodePath);
					}else if(IbarOdfResultParser.isDirectory(currentFile)){
						currentFile = IbarOdfCore.directoryToJson(currentNodePath);
					}
					System.out.println(currentFile); 
				}else{
					JSONObject currentSubDirectory = IbarOdfCore.directoryToJson(currentNodePath);
					fillTreeWithRegularFiles(selectedNode, IbarOdfResultParser.getRegularFiles(currentSubDirectory));
					fillTreeWithOdfFiles(selectedNode,IbarOdfResultParser.getOdfFiles(currentSubDirectory));
					fillTreeWithWrongFiles(selectedNode,IbarOdfResultParser.getWrongFiles(currentSubDirectory));
				}
			}catch(Exception e){
				JOptionPane.showMessageDialog(getParent(), currentPath+" does not exist or is inaccessible!" ,"Can't access",JOptionPane.ERROR_MESSAGE);
			}
		}
	}	

}
