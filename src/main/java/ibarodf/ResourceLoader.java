package ibarodf;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ResourceLoader {
    public static String MSG_ERROR = "un a eu lieu lors du chargement des ressources";

    public static ImageIcon loadImgFromResource(String path) throws IOException {
        InputStream is = new BufferedInputStream(Objects.requireNonNull(ResourceLoader.class.getClassLoader().getResourceAsStream(path)));
        Image image = ImageIO.read(is);
        return new ImageIcon(image);
    }

    public static InputStreamReader loadTextFromResource(String path) {
        InputStream is = new BufferedInputStream(Objects.requireNonNull(ResourceLoader.class.getClassLoader().getResourceAsStream(path)));
        return new InputStreamReader(is, StandardCharsets.UTF_8);
    }
}
