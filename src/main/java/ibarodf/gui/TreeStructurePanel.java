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
	public final static String CURRENT_DIRECTORY = ".";

	private JTree tree;
	private DefaultMutableTreeNode root;
	private JSONObject directory;
	private String pathText;
	private final Dimension preferedSize;
	private MetaDataPanel metadataPanel; 

	public TreeStructurePanel(Dimension preferredSize, MetaDataPanel metadataPanel) {
		this(preferredSize, defaultRoot());
		this.metadataPanel = metadataPanel;
	}


	public void setRootAsADirectory(String path){
		try{
			pathText = path;
			Path currentPath = IbarOdfCore.stringToPath(path);
			directory = IbarOdfCore.directoryToJson(currentPath);
			root = new DefaultMutableTreeNode(currentPath.getFileName());
		}catch(FileNotFoundException | UnableToConvertToJsonFormatException e){
			System.err.println(e.getMessage());
		}
	}


	public void setRootAsFile(String path) throws Exception{
		pathText = path;
		Path currentPath = IbarOdfCore.stringToPath(path);
		directory = IbarOdfCore.RegularFileToJson(currentPath);
		root = new DefaultMutableTreeNode(currentPath.getFileName());
	}

	public void refresh(String path) throws Exception{
		Path newRootPath = IbarOdfCore.stringToPath(path);
		JSONObject newRoot = IbarOdfCore.RegularFileToJson(newRootPath);
		if(IbarOdfResultParser.isDirectory(newRoot)){
			setRootAsADirectory(path);
		}else{
			setRootAsFile(path);
		}
		tree = new JTree(root);
		tree.setPreferredSize(preferedSize);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(new SelectionListener());
		setViewportView(tree);
		fillTreeStructure();
	}


	public TreeStructurePanel(Dimension preferredSize, String path){
		super();
		this.preferedSize = preferredSize;
		setPreferredSize(preferredSize);
		setBackground(Color.BLUE);
		setRootAsADirectory(path);
		tree = new JTree(root);
		tree.setPreferredSize(preferredSize);

		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(new SelectionListener());
		setViewportView(tree);
		fillTreeStructure();
	}

	public JTree getTree() {
		return tree;
	}

	public static String defaultRoot() {
		File[] listRoot = File.listRoots();
		return listRoot[0].toString();
	}



	public void fillTreeStructure() {
		fillTreeWithRegularFiles(root, IbarOdfResultParser.getRegularFiles(directory));
		fillTreeWithOdfFiles(root,IbarOdfResultParser.getOdfFiles(directory));
		fillTreeWithWrongFiles(root,IbarOdfResultParser.getWrongFiles(directory));
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
				subDirectoryNode.add(new DefaultMutableTreeNode(CURRENT_DIRECTORY));
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
			path.append(separator).append(parents[index]);
		}
		return path.toString();


	}

	private StringBuilder rootName(String separator){
		StringBuilder racine = new StringBuilder(pathText);
		if(pathText.endsWith(separator)){
			racine.deleteCharAt(pathText.length()-1);
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
					JSONObject currentFileJson = IbarOdfCore.RegularFileToJson(currentNodePath);
					if(IbarOdfResultParser.isOdfFile(currentFileJson)){
						currentFileJson = IbarOdfCore.odfFileToJson(currentNodePath);
					}else if(IbarOdfResultParser.isDirectory(currentFileJson)){
						currentFileJson = IbarOdfCore.directoryToJson(currentNodePath);
					}
					System.out.println(currentFileJson);
					metadataPanel.setDataInTable(currentFileJson);
					metadataPanel.setImgInPicturePanel(currentFileJson);
				
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
