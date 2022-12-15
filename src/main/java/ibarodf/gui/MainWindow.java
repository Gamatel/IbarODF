package ibarodf.gui;

import java.awt.Dimension;
import java.awt.BorderLayout;
import javax.swing.JFrame;


public class MainWindow extends JFrame {

   public MainWindow() {
      super("IbarODF");

	  setJMenuBar(new MenuBar());
	  add(new TreeStructurePanel(), BorderLayout.WEST);
	  add(new MetaDataPanel(), BorderLayout.CENTER);
      setSize(new Dimension(500,500));
      setVisible(true);
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
   }
}
