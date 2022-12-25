package ibarodf.gui;

import ibarodf.core.IbarOdfResultParser;
import ibarodf.core.metadata.exception.NoSuchMetadataException;
import ibarodf.gui.displaypicture.DisplayPicturePanel;
import ibarodf.gui.displaypicture.DisplayPictureTabbedPanel;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.HashSet;
import java.util.Set;

/**
 * This class represent a panel with DisplayPicturePanel for thumbnails and DisplayPictureTabbedPanel for odf img
 */
public class ShowPicturePanel extends JPanel {
    private final static Dimension IMG_SIZE = new Dimension(200, 260);
    private final DisplayPicturePanel overviewPanel;
    private final DisplayPictureTabbedPanel odfImgPanel;

    boolean isAnOdfFile = false;

    /**
     * Constructor of ShowPicturePanel
     */
    public ShowPicturePanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 70, 0));
        overviewPanel = new DisplayPicturePanel("Aperçu du fichier", "Agrandir l'image", IMG_SIZE);
        odfImgPanel = new DisplayPictureTabbedPanel(IMG_SIZE);

        add(odfImgPanel);
        add(overviewPanel);
    }

    /**
     * This method change picture that will be displayed in ShowPicturePanel
     * @param dataJson the Json Object that represent the whole file
     * @throws NoSuchMetadataException exception that represent that picture array or thumbnails object isn't in dataJson
     */
    public void loadFilePicture(JSONObject dataJson) throws NoSuchMetadataException {
        isAnOdfFile = IbarOdfResultParser.isOdfFile(dataJson);

        loadThumbNails(dataJson);
        loadFileImg(dataJson);
    }

    private void loadThumbNails(JSONObject dataJson) throws NoSuchMetadataException {
        ImageIcon thumbnails;
        if (isAnOdfFile)
            thumbnails = new ImageIcon(IbarOdfResultParser.getThumbnailPath(dataJson).toString());
        else
            thumbnails = null;

        overviewPanel.setImage(thumbnails);
    }

    private void loadFileImg(JSONObject dataJson){
        try {
            if (isAnOdfFile) {
                Set<String> picturePathSet = getPicturePathSet(dataJson);
                odfImgPanel.loadSetImg(picturePathSet);
            } else {
                odfImgPanel.loadSetImg(null);
            }
        } catch (NoSuchMetadataException e) {
            odfImgPanel.loadSetImg(null);
        }
    }

    private Set<String> getPicturePathSet(JSONObject dataJson) throws NoSuchMetadataException {
        Set<String> picturesPathSet = new HashSet<>();
        JSONArray picturesArrayJson = IbarOdfResultParser.getPictures(dataJson);

        int i = 0;
        while (!picturesArrayJson.isNull(i)) {
            JSONObject pictureJson = picturesArrayJson.getJSONObject(i);
            String picturePath = IbarOdfResultParser.getPicturesPath(pictureJson).toString();
            picturesPathSet.add(picturePath);
            i++;
        }

        return picturesPathSet;
    }
}
