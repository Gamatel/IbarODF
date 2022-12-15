package ibarodf.gui;

import java.awt.Dimension;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;

public class TreeStructurePanel extends JPanel {
	TreeStructurePanel() {
		super();

		DefaultMutableTreeNode framework = new DefaultMutableTreeNode("Framework");
		DefaultMutableTreeNode front = new DefaultMutableTreeNode("Front-End");
		DefaultMutableTreeNode back = new DefaultMutableTreeNode("Back-End");
		DefaultMutableTreeNode autres = new DefaultMutableTreeNode("Autres");

		framework.add(front);

		DefaultMutableTreeNode angular = new DefaultMutableTreeNode("AngularJS");
		DefaultMutableTreeNode react = new DefaultMutableTreeNode("React.js");
		DefaultMutableTreeNode meteor = new DefaultMutableTreeNode("Meteor.js");
		DefaultMutableTreeNode ember = new DefaultMutableTreeNode("Ember.js ");

		front.add(angular); front.add(react); front.add(meteor); front.add(ember);  
		framework.add(back);

		DefaultMutableTreeNode nodejs = new DefaultMutableTreeNode("NodeJS");
		DefaultMutableTreeNode express = new DefaultMutableTreeNode("Express");
		back.add(nodejs); back.add(express);

		framework.add(autres);

		JTree tree = new JTree(framework);
		tree.setPreferredSize(new Dimension(400, 600));

		add(new JScrollPane(tree));
	}
}
