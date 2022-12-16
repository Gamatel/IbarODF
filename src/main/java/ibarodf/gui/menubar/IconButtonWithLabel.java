package ibarodf.gui.menubar;

import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

public class IconButtonWithLabel extends JButton {
	public IconButtonWithLabel(String labelText, ImageIcon icon) {
		super(labelText);
		this.setIcon(icon);
		setHorizontalAlignment(SwingConstants.LEFT);
	}
	public IconButtonWithLabel(String labelText, ImageIcon icon, Dimension preferredSize) {
		this(labelText, icon);
		setPreferredSize(preferredSize);
	}
}
