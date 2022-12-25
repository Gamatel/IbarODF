package ibarodf.gui.displaypicture;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;
import java.awt.Dimension;
import java.io.File;
import java.util.Collection;
import java.util.HashSet;

/**
 * This class represent a tabbed panel that contains DisplayPicturePanel class
 */
public class DisplayPictureTabbedPanel extends JTabbedPane {
    private Collection<String> imgPathSet;
    private final Dimension imgDim;

    /**
     * @param imgDim The size of the image that will be display
     */
    public DisplayPictureTabbedPanel(Dimension imgDim) {
        super();
        this.imgPathSet = new HashSet<String>();
        this.imgDim = imgDim;

        constructPanelEmpty();
    }

    private void constructPanelEmpty() {
        DisplayPicturePanel imgPanel = new DisplayPicturePanel("Pas d'image", "Agrandir l'image", imgDim);
        add(imgPanel, "Pas d'image");
    }
    private void constructPanel() {
        int i = 0;
        for (String imgPath : imgPathSet) {
            File fileImg = new File(imgPath);
            ImageIcon imgIcon = new ImageIcon(imgPath);
            String labelText1 = fileImg.getName();
            String labelText2 = "Afficher l'image " + (i+1);
            DisplayPicturePanel imgPanel = new DisplayPicturePanel(labelText1, labelText2, imgIcon, imgDim);

            add(imgPanel, "image " + (i+1));
            i++;
        }
    }

    /**
     * This methode take the Path of image in a set and display them
     * @param newImgPathSet Set of image path
     */
    public void loadSetImg(Collection<String> newImgPathSet) {
        removeAll();
        imgPathSet = newImgPathSet;
        if (newImgPathSet != null)
            constructPanel();
        else
           constructPanelEmpty();
    }
}
