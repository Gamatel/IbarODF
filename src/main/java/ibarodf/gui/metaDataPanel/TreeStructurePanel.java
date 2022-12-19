package ibarodf.gui.metaDataPanel;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;

public class TreeStructurePanel extends JScrollPane {
	JTree tree;
	DefaultMutableTreeNode root;
	public TreeStructurePanel(Dimension preferredSize) {
		super();
		setPreferredSize(preferredSize);
		setBackground(Color.BLUE);

		root = new DefaultMutableTreeNode();

		tree = new JTree(root);
		tree.setPreferredSize(preferredSize);

		setViewportView(tree);
		fillTreeStructure();
	}

	void fillTreeStructure() {
		DefaultMutableTreeNode front = new DefaultMutableTreeNode("Front-End");
		DefaultMutableTreeNode back = new DefaultMutableTreeNode("Back-End");
		DefaultMutableTreeNode autres = new DefaultMutableTreeNode("Autres");

		root.add(front);

		DefaultMutableTreeNode angular = new DefaultMutableTreeNode("AngularJS");
		DefaultMutableTreeNode react = new DefaultMutableTreeNode("React.js");
		DefaultMutableTreeNode meteor = new DefaultMutableTreeNode("Meteor.js");
		DefaultMutableTreeNode ember = new DefaultMutableTreeNode("Ember.js ");

		front.add(angular); front.add(react); front.add(meteor); front.add(ember);
		root.add(back);

		DefaultMutableTreeNode nodejs = new DefaultMutableTreeNode("NodeJS");
		DefaultMutableTreeNode express = new DefaultMutableTreeNode("Express");
		back.add(nodejs); back.add(express);

		root.add(autres);
		DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
		model.reload(root);
	}
}
