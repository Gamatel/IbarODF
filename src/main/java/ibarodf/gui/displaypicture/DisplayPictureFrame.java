package ibarodf.gui.displaypicture;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Dimension;

class DisplayPictureFrame extends JFrame {
    public DisplayPictureFrame(ImageIcon img, Dimension imgDim) {
        super(imgDim.getWidth() + "x" + imgDim.getHeight());
        setVisible(true);
        setSize(imgDim);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel labelImg = new JLabel();
        labelImg.setIcon(img);
        add(labelImg);
    }
}
