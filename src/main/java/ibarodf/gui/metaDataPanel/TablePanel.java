package ibarodf.gui.metaDataPanel;

import javax.swing.JTable;
import javax.swing.JScrollPane;


public class TablePanel extends JScrollPane {
	final String[] header = {"Nom", "Prenom", "Sexe"};

	public TablePanel() {
		super();

		Object[][] data = {
			{"Jean", "Herad", "Masculin"},
			{"Jean", "Herad", "Masculin"},
			{"Jean", "Herad", "Masculin"}
		};

		JTable table = new JTable(data, header);
		setViewportView(table);
	}
}
