package ibarodf.gui.table;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Color;
import java.awt.Component;

class HeaderColumnRender extends DefaultTableCellRenderer {
	private final Color bg, fg;

	public HeaderColumnRender(Color bg, Color fg) {
		super();
		this.bg = bg;
		this.fg = fg;
	}

	public Component getTableCellRendererComponent(JTable table, Object
			value, boolean isSelected, boolean hasFocus, int row, int column) {

		Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		setInitialDesign(cell);
		return cell;
	}
	private void setInitialDesign(Component cell) {
		cell.setBackground(bg);
		cell.setForeground(fg);
		cell.setEnabled(false);
		cell.setFocusable(false);
		setBorder(BorderFactory.createEmptyBorder());
	}
}
