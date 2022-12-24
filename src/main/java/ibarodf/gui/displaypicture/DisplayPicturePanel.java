package ibarodf.gui.displaypicture;

import ibarodf.gui.palette.FontPalette;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class DisplayPicturePanel extends JPanel {
    private final static ImageIcon NO_THUMBNAILS_IMG = new ImageIcon("src/main/resources/img/no_img.png");
    private final static Dimension DIM = new Dimension(400, 350);
    private final JLabel labelPicture;
    private final JButton button;
    private final Dimension imgSize;

    public DisplayPicturePanel(String labelText, String btnText, Dimension imgSize) {
        super();
        this.imgSize = imgSize;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setPreferredSize(DIM);


        JLabel label = new JLabel(labelText);
        label.setFont(FontPalette.DISPLAY_PICTURE_FONT_LABEL);
        button = new JButton(btnText);

        labelPicture = new JLabel();
        labelPicture.setSize(new Dimension(200, 200));
        labelPicture.setBackground(Color.BLUE);
        setImage((ImageIcon) null);

        label.setAlignmentX(CENTER_ALIGNMENT);
        labelPicture.setAlignmentX(CENTER_ALIGNMENT);
        button.setAlignmentX(CENTER_ALIGNMENT);

        add(label);
        add(labelPicture);
        add(button);
    }

    public DisplayPicturePanel(String labelText, String btnText, ImageIcon icon, Dimension imgSize) {
        this(labelText, btnText, imgSize);
        setImage(icon);
    }

    private ImageIcon getResizedImage(ImageIcon icon, Dimension size) {
        int width = (int) size.getWidth();
        int height = (int) size.getHeight();
        Image imgIcon = icon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);

        return new ImageIcon(imgIcon);
    }

    // public void setImage(File fileImg) {
    //     if (fileImg != null) {
    //         ImageIcon rawImg = new ImageIcon(fileImg.getPath());
    //         labelPicture.setIcon(getResizedImage(rawImg, imgSize));
    //         button.setEnabled(true);
    //     } else {
    //         labelPicture.setIcon(getResizedImage(NO_THUMBNAILS_IMG, imgSize));
    //         button.setEnabled(false);
    //     }
    // }

    public void setImage(ImageIcon rawImg) {
        if (rawImg != null) {
            labelPicture.setIcon(getResizedImage(rawImg, imgSize));
            button.setEnabled(true);
        } else {
            labelPicture.setIcon(getResizedImage(NO_THUMBNAILS_IMG, imgSize));
            button.setEnabled(false);
        }
    }

}
