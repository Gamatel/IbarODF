package ibarodf.gui;

import java.awt.Dimension;
import java.awt.BorderLayout;
import javax.swing.JFrame;


public class MainWindow extends JFrame {

   public MainWindow() {
      super("IbarODF");
       setSize(new Dimension(500,500));
       setVisible(true);
       setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	  add(new ToolBar(), BorderLayout.NORTH);
	  add(new TreeStructurePanel(new Dimension(500, 800)), BorderLayout.WEST);
	  add(new MetaDataPanel(), BorderLayout.CENTER);
   }
}
