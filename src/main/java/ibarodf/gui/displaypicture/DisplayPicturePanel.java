package ibarodf.gui.displaypicture;

import ibarodf.gui.palette.FontPalette;

import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;

/**
 * This class manage how to display a picture, with her label and her button to make a better display
 */
public class DisplayPicturePanel extends JPanel {
    private final static ImageIcon NO_THUMBNAILS_IMG = new ImageIcon("src/main/resources/img/no_img.png");
    private final static Dimension DIM = new Dimension(400, 350);
    private final JLabel labelPicture;
    private final JButton button;
    private final Dimension imgSize;
    private ImageIcon initialPicture;

    /**
     * DisplayPicture Panel Constructor 1
     * @param labelText The text show in the top of the component
     * @param btnText The text show in the button
     * @param imgSize The size of the image that will be display
     */
    public DisplayPicturePanel(String labelText, String btnText, Dimension imgSize) {
        super();
        this.imgSize = imgSize;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setPreferredSize(DIM);


        JLabel label = new JLabel(labelText);
        label.setFont(FontPalette.DISPLAY_PICTURE_FONT_LABEL);
        button = new JButton(btnText);
        button.addActionListener(actionEvent -> displayImgInNewWindows());

        labelPicture = new JLabel();
        labelPicture.setSize(new Dimension(200, 200));
        labelPicture.setBackground(Color.BLUE);
        setImage(null);

        label.setAlignmentX(CENTER_ALIGNMENT);
        labelPicture.setAlignmentX(CENTER_ALIGNMENT);
        button.setAlignmentX(CENTER_ALIGNMENT);

        add(label);
        add(labelPicture);
        add(button);
    }

    /**
     * DisplayPicture Panel Constructor 2
     * @param labelText The text show in the top of the component
     * @param btnText The text show in the button
     * @param imgSize The size of the image that will be display
     * @param icon The Icon that will be display in the component
     */
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

    /**
     * setImage method change the component image
     * @param rawImg The image that will be display
     */
    public void setImage(ImageIcon rawImg) {
        if (rawImg != null) {
            initialPicture = rawImg;
            labelPicture.setIcon(getResizedImage(rawImg, imgSize));
            button.setEnabled(true);
        } else {
            labelPicture.setIcon(getResizedImage(NO_THUMBNAILS_IMG, imgSize));
            button.setEnabled(false);
        }
    }

    private void displayImgInNewWindows() {
        JFrame imgFrame = new DisplayPictureFrame(initialPicture, getImgDim());
    }

    private Dimension getImgDim() {
       final int height = initialPicture.getIconHeight();
       final int width = initialPicture.getIconWidth();

       return new Dimension(width, height);
    }
}
