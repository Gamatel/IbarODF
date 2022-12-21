package ibarodf.gui;

import ibarodf.gui.table.TablePanel;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class MetaDataPanel extends JPanel {
    public MetaDataPanel() {
       setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new TablePanel());
        add(new ShowPicturePanel());
    }
}
