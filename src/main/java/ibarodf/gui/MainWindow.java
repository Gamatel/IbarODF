package ibarodf.gui;

import ibarodf.gui.toolbar.ToolBar;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;


public class MainWindow extends JFrame {

	final Dimension OVERVIEW_SIZE = new Dimension(210, 280);
	public MainWindow() {
		super("IbarODF");
		setSize(new Dimension(1000,1000));
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		add(new ToolBar(), BorderLayout.NORTH);
		add(new TreeStructurePanel(new Dimension(500, 800)), BorderLayout.WEST);
		add(new MetaDataPanel(), BorderLayout.CENTER);
   }
}
