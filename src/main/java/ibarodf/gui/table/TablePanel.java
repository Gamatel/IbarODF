package ibarodf.gui.table;

import ibarodf.gui.palette.ColorPalette;
import ibarodf.gui.palette.FontPalette;
import org.json.JSONObject;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.Color;
import java.awt.Dimension;

/**
 * This class represent a table that will display metadata of file
 */
public class TablePanel extends JScrollPane {
	private static final Color bgHeaderColor = ColorPalette.SECONDARY_COLOR;
	private final static Color headerTextColor = ColorPalette.MAIN_TEXT_COLOR;
	private static final Color bgColumnColor = ColorPalette.BG_COLUMN_COLOR;
	private static final Color textColorColumn = ColorPalette.TEXT_SECONDARY_COLOR;
	private final JTable table;
	private final static Dimension TABLE_DIM = new Dimension(850, 410);

	/**
	 * Constructor of TablePanel
	 */
	public TablePanel() {
		super();

		table = new JTable(new TableModel());

		setInitialDesign();
		setColumnsDesign();
		setHeaderDesign();
		setViewportView(table);
	}

	private void setInitialDesign() {
		setBackground(ColorPalette.MAIN_BG);
		setBorder(BorderFactory.createEmptyBorder(20, 50, 0, 50));
		setPreferredSize(TABLE_DIM);

		table.setBackground(ColorPalette.BG_TABLE_COLOR);
		table.setBorder(BorderFactory.createEmptyBorder());
	}

	private void setColumnsDesign() {
		table.setRowHeight(45);
		table.setFont(FontPalette.TABLE_FONT_CONTENT);

		TableColumn col0 = table.getColumnModel().getColumn(0);
		col0.setCellRenderer(new HeaderColumnRender(bgColumnColor, textColorColumn));

		TableColumn col1 = table.getColumnModel().getColumn(0);
		col1.setCellRenderer(new ValueColumnRender(bgColumnColor, textColorColumn));
	}

	private void setHeaderDesign() {
		JTableHeader header = table.getTableHeader();
		header.setBorder(BorderFactory.createEmptyBorder());
		header.setBackground(bgHeaderColor);
		header.setForeground(headerTextColor);
		header.setPreferredSize(new Dimension(this.getWidth() + 40, 45 ));
		header.setBorder(BorderFactory.createEmptyBorder());
		header.setFont(FontPalette.TABLE_FONT_HEADER);
	}

	/**
	 * Set a new Model for the table ( need to show metadata of a different file )
	 * @param dataJson the JSONObject that represent the file to show metaData
	 */
	public void setModel(JSONObject dataJson) {
		TableModel model = new TableModel(dataJson);
		table.setModel(model);
		SwingUtilities.updateComponentTreeUI(table);
	}

}
