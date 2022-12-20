package ibarodf.gui.toolbar;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JToolBar;
import java.awt.Dimension;
import java.awt.FlowLayout;

public class ToolBar extends JToolBar {
	private final Color bgColor = new Color(156, 39, 176);

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

		setInitialDesign();
	}

	private void setInitialDesign() {
		setBackground(bgColor);
		setBorder(BorderFactory.createEmptyBorder());
	}
}
