package ibarodf.gui.toolbar;

import javax.swing.ImageIcon;
import javax.swing.JToolBar;
import java.awt.Dimension;
import java.awt.FlowLayout;

public class ToolBar extends JToolBar {
	public ToolBar() {
		super();
		setLayout(new FlowLayout(FlowLayout.LEFT));
		setFloatable(false);

		Dimension dimBtn = new Dimension(250, 64);

		ImageIcon iconFileOpen = new ImageIcon("src/main/resources/icons/file_open.png");
		IconButtonWithLabel btnFileOpen = new IconButtonWithLabel("Ouvrir un fichier", iconFileOpen, dimBtn);

		ImageIcon iconDirOpen = new ImageIcon("src/main/resources/icons/new_folder.png");
		IconButtonWithLabel btnDirOpen = new IconButtonWithLabel("Ouvrir un dossier", iconDirOpen, dimBtn);

		add(btnFileOpen);
		add(btnDirOpen);
	}
}
