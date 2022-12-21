package ibarodf.gui;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;

public class ShowPicturePanel extends JPanel {
    DisplayPicturePanel overviewPanel;
    DisplayPicturePanel odfImgPanel;

    public ShowPicturePanel() {
       setLayout(new FlowLayout(FlowLayout.CENTER, 70, 0));
       ImageIcon img = new ImageIcon((new File("src/main/resources/img/thumbnail.png")).getPath());
       Dimension dim = new Dimension(200, 280);
       odfImgPanel = new DisplayPicturePanel("Images du fichier", "Agrandir l'image", img, dim);
       overviewPanel = new DisplayPicturePanel("Aperçu du fichier odf", "Agrandir l'aperçu", img, dim);

       add(odfImgPanel);
       add(overviewPanel);
    }
}
