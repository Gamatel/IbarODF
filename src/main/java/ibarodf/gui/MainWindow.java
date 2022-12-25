package ibarodf.gui;

import ibarodf.gui.toolbar.ToolBar;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;


/**
 * This class represent the whole GUI
 */
public class MainWindow extends JFrame {
	public final static int TREE_STRUCTURE_WIDTH = 500;
	public final static int TREE_STRUCTURE_HEIGHT = 800;

	/**
	 * MainWindow Constructor
	 */
	public MainWindow() {
		super("IbarODF");
		setSize(new Dimension(1000,1000));
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		init_window(); 
	}


   private void init_window() {
	   MetaDataPanel metadataPanel = new MetaDataPanel();
	   TreeStructurePanel filesTree = new TreeStructurePanel(new Dimension(TREE_STRUCTURE_WIDTH, TREE_STRUCTURE_HEIGHT), metadataPanel);
	   ToolBar treeModifierBar = new ToolBar(filesTree);

	   add(treeModifierBar, BorderLayout.NORTH);
	   add(metadataPanel, BorderLayout.CENTER);
	   add(filesTree, BorderLayout.WEST);
   }
}
