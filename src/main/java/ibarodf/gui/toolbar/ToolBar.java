package ibarodf.gui.toolbar;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JToolBar;

import ibarodf.gui.TreeStructurePanel;

import java.awt.Dimension;
import java.awt.FlowLayout;

/**
 * This class reprensent a ToolBar with two IconButtonWithLabel
 */
public class ToolBar extends JToolBar {
	private final Color bgColor = new Color(156, 39, 176);
	public final static int WIDTH_TOOL_BAR_BUTTON = 250;
	public final static int HEIGTH_TOOL_BAR_BUTTON = 64;


	/**
	 * ToolBar Constructor
	 * @param treeToPerformActionOn the TreeStructurePanel
	 */
	public ToolBar(TreeStructurePanel treeToPerformActionOn) {
		super();
		setLayout(new FlowLayout(FlowLayout.LEFT));
		setFloatable(false);
		Dimension dimBtn = new Dimension(WIDTH_TOOL_BAR_BUTTON, HEIGTH_TOOL_BAR_BUTTON);
		OpenDirectoryButton btnDirOpen= new OpenDirectoryButton(dimBtn, treeToPerformActionOn);
		OpenFileButton btnFileOpen= new OpenFileButton(dimBtn, treeToPerformActionOn);

		add(btnFileOpen);
		add(btnDirOpen);

		setInitialDesign();
	}

	private void setInitialDesign() {
		setBackground(bgColor);
		setBorder(BorderFactory.createEmptyBorder());
	}
}
