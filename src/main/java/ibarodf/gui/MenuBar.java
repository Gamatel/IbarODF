package ibarodf.gui;

import javax.swing.JMenuBar;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class MenuBar extends JMenuBar {
	public MenuBar() {
		super();

		JButton btnOpenFile = new JButton(new ImageIcon("src/main/resources/icons/new_folder.png"));
		btnOpenFile.setToolTipText("Ouvrir un nouveau dossier");
		add(btnOpenFile);

		JButton btnOpenDir = new JButton(new ImageIcon("src/main/resources/icons/file_open.png"));
		btnOpenDir.setToolTipText("Ouvrir un nouveau fichier");
		add(btnOpenDir);
	}
}
