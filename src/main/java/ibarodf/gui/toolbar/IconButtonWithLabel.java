package ibarodf.gui.toolbar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

import ibarodf.gui.palette.ColorPalette;

public class IconButtonWithLabel extends JButton implements MouseListener{
	private final Color bgColor = ColorPalette.MAIN_COLOR;	
	private final Color bgMousePressedColor = ColorPalette.SECONDARY_COLOR;	

	public IconButtonWithLabel(String labelText, ImageIcon icon) {
		super(labelText);
		this.setIcon(icon);
		setHorizontalAlignment(SwingConstants.LEFT);

		setInitialDesign();
		addMouseListener(this);

	}

	public IconButtonWithLabel(String labelText, ImageIcon icon,  Dimension preferredSize) {
		this(labelText, icon);
		setPreferredSize(preferredSize);
	}

	private void setInitialDesign() {
		setBackground(bgColor);
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	}

	private void setDesign() {
		setBackground(bgColor);
	}

	private void setDesignMousePressed() {
		setBackground(bgMousePressedColor);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		setDesignMousePressed();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		setDesign();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		setDesignMousePressed();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		setDesign();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		setDesignMousePressed();
	}
}
