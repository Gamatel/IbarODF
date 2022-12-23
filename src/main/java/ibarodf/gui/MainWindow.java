package ibarodf.gui;

import ibarodf.gui.toolbar.ToolBar;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;


public class MainWindow extends JFrame {
	public final static int OVERVIEW_SIZE_WIDTH = 210;
	public final static int OVERVIEW_SIZE_HEIGHT = 280;
	public final static int TREE_STRUCTURE_WIDTH = 500;
	public final static int TREE_STRUCTURE_HEIGHT = 800;

	final Dimension OVERVIEW_SIZE = new Dimension( OVERVIEW_SIZE_WIDTH, OVERVIEW_SIZE_HEIGHT);
	public MainWindow() {
		super("IbarODF");
		setSize(new Dimension(1000,1000));
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		try{
			TreeStructurePanel filesTree = new TreeStructurePanel(new Dimension(TREE_STRUCTURE_WIDTH, TREE_STRUCTURE_HEIGHT)); 
			ToolBar treeModifierBar = new ToolBar(filesTree);
			add(treeModifierBar, BorderLayout.NORTH);
			add(filesTree, BorderLayout.WEST);
		}catch(Exception e){
			System.err.println(e.getMessage());
		}
		add(new MetaDataPanel(), BorderLayout.CENTER);
   }
}
