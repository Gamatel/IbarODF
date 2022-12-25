package ibarodf.gui;

import ibarodf.core.metadata.exception.NoSuchMetadataException;
import ibarodf.gui.table.TablePanel;
import org.json.JSONObject;

import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * This class represent the Panel that show metadata with TableModel an ShowPicturePanel
 */
public class MetaDataPanel extends JPanel {
    private final TablePanel tablePanel = new TablePanel();
    private final ShowPicturePanel showPicturePanel = new ShowPicturePanel();

    /**
     * MetaDataPanel Constructor
     */
    public MetaDataPanel() {
       setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(tablePanel);
        add(showPicturePanel);
    }

    /**
     * This method change metadata in TablePanel
     * @param dataJson The Object Json that represent whole file
     */
    public void setDataInTable(JSONObject dataJson) {
        tablePanel.setModel(dataJson);

    }

    /**
     * This method change picture in ShowPicturePanel
     * @param dataJson The Object Json that represent whole file
     */
    public void setImgInPicturePanel(JSONObject dataJson) {
        try {
            showPicturePanel.loadFilePicture(dataJson);
        } catch (NoSuchMetadataException e) {
            JOptionPane.showMessageDialog(this, "Une erreur s'est produite lors du chargement des images");
            throw new RuntimeException(e);
        }
    }
}
