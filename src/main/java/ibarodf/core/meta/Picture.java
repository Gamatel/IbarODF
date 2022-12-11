package ibarodf.core.meta;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;

public class Picture {
    private final Path picturePath;
    private final BufferedImage bufferedImage;
    private final long size;
    private final int heigth;
    private final int widgth;

    public Picture(Path picturePath) throws Exception{
        this.picturePath = picturePath;
        File pictureFile = new File(picturePath.toString());
        size = Files.size( picturePath)/1024;
        bufferedImage = ImageIO.read(pictureFile); 
        heigth = bufferedImage.getHeight();
        widgth = bufferedImage.getWidth();
    }

    public BufferedImage getBufferedImage(){
        return bufferedImage;
    }

    public String toString(){   
        return "Path : "+ picturePath + ";Heigth : "+ heigth + ";Size : "+ size + "Ko ;Widgth : "+ widgth + ";";
    }

    
}
