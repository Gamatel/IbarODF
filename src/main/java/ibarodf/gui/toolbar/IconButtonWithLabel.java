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

/**
 * This class represent a button with his icon
 */
public class IconButtonWithLabel extends JButton implements MouseListener{
	private final Color bgColor = ColorPalette.MAIN_COLOR;	
	private final Color bgMousePressedColor = ColorPalette.SECONDARY_COLOR;

	/**
	 * Constructor of iconButtonWithLabel
	 * @param labelText The text that will be displayed in the label
	 * @param icon The icon that will be displayed next to the button
	 */
	public IconButtonWithLabel(String labelText, ImageIcon icon) {
		super(labelText);
		this.setIcon(icon);
		setHorizontalAlignment(SwingConstants.LEFT);
		setInitialDesign();
		addMouseListener(this);

	}

	/**
	 * Constructor of iconButtonWithLabel
	 * @param labelText The text that will be displayed in the label
	 * @param icon The icon that will be displayed next to the button
	 * @param preferredSize The preferred Dimension for the whole component
	 */
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
