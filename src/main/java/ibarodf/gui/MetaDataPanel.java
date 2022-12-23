package ibarodf.gui;

import ibarodf.core.metadata.exception.NoSuchMetadataException;
import ibarodf.gui.table.TablePanel;
import org.json.JSONObject;

import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MetaDataPanel extends JPanel {
    private final TablePanel tablePanel = new TablePanel();
    private final ShowPicturePanel showPicturePanel = new ShowPicturePanel();
    public MetaDataPanel() {
       setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(tablePanel);
        add(showPicturePanel);
    }

    public void setDataInTable(JSONObject dataJson) {
        tablePanel.setModel(dataJson);

    }

    public void setImgInPicturePanel(JSONObject dataJson) {
        try {
            showPicturePanel.loadFilePicture(dataJson);
        } catch (NoSuchMetadataException e) {
            JOptionPane.showMessageDialog(this, "Une erreur s'est produite lors du chargement des images");
            throw new RuntimeException(e);
        }
    }
}
