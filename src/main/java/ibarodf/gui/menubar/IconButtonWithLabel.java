package ibarodf.gui.menubar;

import java.awt.FlowLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

public class IconButtonWithLabel extends JPanel  {
	private final ImageIcon icon;
	private final JLabel label;
	private JButton button;

	public IconButtonWithLabel(String labelText, ImageIcon icon) {
		super();
		label = new JLabel(labelText);
		this.icon = icon;
		constructButton();
	}

	public IconButtonWithLabel(String labelText, ImageIcon icon, Dimension preferredSize) {
		this(labelText, icon);
		setPreferredSize(preferredSize);
	}

	private void constructButton() {
		setLayout(new FlowLayout(FlowLayout.LEFT));

		button = new JButton();
		button.setIcon(icon);

		add(button);
		add(label);
	}

	public JButton getButton() {
		return button;
	}
}
