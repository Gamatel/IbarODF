package ibarodf.gui.table;

import ibarodf.gui.ColorPalette;
import ibarodf.gui.FontPalette;
import org.json.JSONObject;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.Color;
import java.awt.Dimension;

public class TablePanel extends JScrollPane {
	private static final Color bgHeaderColor = ColorPalette.SECONDARY_COLOR;
	private final static Color headerTextColor = ColorPalette.MAIN_TEXT_COLOR;
	private static final Color bgColumnColor = ColorPalette.BG_COLUMN_COLOR;
	private static final Color textColorColumn = ColorPalette.TEXT_SECONDARY_COLOR;
	private final JTable table;
	private final static Dimension TABLE_DIM = new Dimension(850, 410);

	public TablePanel() {
		super();

		table = new JTable(new TableModel(new JSONObject("{\"Path\":\"/home/alexandre/Bureau/test/test.odt\",\"Metadata\":[{\"Title\":\"Je Suis le nouveau titre\"},{\"Subject\":\"Je Suis un sujet très intéréssant\"},{\"Keywords\":[]},{\"Hyperlinks\":[{\"Visited Style Name\":\"Visited_20_Internet_20_Link\",\"Type\":\"simple\",\"Reference\":\"https://www.google.com/\",\"Style Name\":\"Internet_20_link\"},{\"Visited Style Name\":\"Visited_20_Internet_20_Link\",\"Type\":\"simple\",\"Reference\":\"https://www.cyu.fr/\",\"Style Name\":\"Internet_20_link\"}]},{\"Creation Date\":\"21/11/2022 14:58:49\"},{\"Statistics\":[{\"imageCount\":2},{\"objectCount\":0},{\"pageCount\":1},{\"wordCount\":14},{\"tableCount\":0},{\"nonWhitespaceCharacterCount\":49},{\"characterCount\":61},{\"paragraphCount\":3}]},{\"Thumbnail\":\"/tmp/IBARODF4788630234301104754/Thumbnails/thumbnail.png\"}],\"Mime Type\":\"application/vnd.oasis.opendocument.text\",\"File Name\":\"test.odt\",\"Have \":[\"Title\",\"Subject\",\"Keywords\",\"Hyperlinks\",\"Creation Date\",\"Statistics\",\"Thumbnail\"],\"Size (Ko)\":48}\n"), true));

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
}
