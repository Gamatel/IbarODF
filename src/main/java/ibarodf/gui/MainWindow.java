package ibarodf.gui;

import ibarodf.gui.toolbar.ToolBar;
import org.json.JSONObject;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;


public class MainWindow extends JFrame {
	public final static int OVERVIEW_SIZE_WIDTH = 210;
	public final static int OVERVIEW_SIZE_HEIGHT = 280;
	public final static int TREE_STRUCTURE_WIDTH = 500;
	public final static int TREE_STRUCTURE_HEIGHT = 800;

	private TreeStructurePanel filesTree;
	private MetaDataPanel metadataPanel;
	public MainWindow() {
		super("IbarODF");
		setSize(new Dimension(1000,1000));
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		init_window();

		filesTree.getTree().addTreeSelectionListener(treeSelectionEvent -> {
			JSONObject dataJson = filesTree.getCurrentFileJson();

			if (dataJson != null) {
				metadataPanel.setDataInTable(dataJson);
				metadataPanel.setImgInPicturePanel(dataJson);
			}
		});


   }

   private void init_window() {
	   ToolBar treeModifierBar = new ToolBar(filesTree);
	   filesTree = new TreeStructurePanel(new Dimension(TREE_STRUCTURE_WIDTH, TREE_STRUCTURE_HEIGHT));
	   metadataPanel = new MetaDataPanel();

	   add(treeModifierBar, BorderLayout.NORTH);
	   add(metadataPanel, BorderLayout.CENTER);
	   add(filesTree, BorderLayout.WEST);
   }
}
