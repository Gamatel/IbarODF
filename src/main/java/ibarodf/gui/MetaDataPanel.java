package ibarodf.gui;

import javax.swing.JTable;
import javax.swing.JScrollPane;


public class MetaDataPanel extends JScrollPane {
	final String[] header = {"Nom", "Prenom", "Sexe"};

	public MetaDataPanel() {
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
