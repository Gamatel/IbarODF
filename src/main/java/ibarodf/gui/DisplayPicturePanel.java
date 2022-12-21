package ibarodf.gui;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class DisplayPicturePanel extends JPanel {
    private final JLabel labelPicture;

    public DisplayPicturePanel(String labelText, String btnText) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel label = new JLabel(labelText);
        JButton button = new JButton(btnText);

        labelPicture = new JLabel();
        labelPicture.setSize(new Dimension(200, 200));
        labelPicture.setBackground(Color.BLUE);

        add(label);
        add(labelPicture);
        add(button);
        setBackground(Color.BLUE);
    }
    public DisplayPicturePanel(String labelText, String btnText, ImageIcon icon, Dimension imgSize) {
        this(labelText, btnText);
        labelPicture.setIcon(getResizedImage(icon, imgSize));
    }

    public DisplayPicturePanel(String btnText, String labelText, ImageIcon icon, Dimension imgSize, Dimension dim) {
        this(labelText, btnText, icon, imgSize);
        setPreferredSize(dim);
    }

    private ImageIcon getResizedImage(ImageIcon icon, Dimension size) {
        int width = (int) size.getWidth();
        int height = (int) size.getHeight();
        Image imgIcon = icon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
        return new ImageIcon(imgIcon);
    }

    public void setImage(File fileImg) {
        labelPicture.setIcon(new ImageIcon(fileImg.getPath()));
    }

    public void setImage(ImageIcon img) {
        labelPicture.setIcon(img);
    }
}
