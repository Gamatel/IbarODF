package ibarodf.core.meta;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import org.json.JSONObject;

import ibarodf.core.meta.exception.UnableToReachPicture;

public class Picture {
    private final Path picturePath;
    private final long size;
    private final int heigth;
    private final int widgth;

    //Json Key
    public static final String PATH = "Path";   
    public static final String SIZE = "Size (Ko)";  
    public static final String HEIGTH = "heigth";  
    public static final String WIDGTH = "widgth";  

    public Picture(Path picturePath)throws IOException, UnableToReachPicture{
        this.picturePath = picturePath;
        File pictureFile = new File(picturePath.toString());
        size = Files.size(picturePath)/1024;
        BufferedImage bufferedImage = ImageIO.read(pictureFile);
        if(bufferedImage == null){
            throw new UnableToReachPicture(picturePath);
        }
        heigth = bufferedImage.getHeight();
        widgth = bufferedImage.getWidth();
    }

    public JSONObject toJson(){
        JSONObject pictureJson = new JSONObject();
        pictureJson.put(PATH ,picturePath);
        pictureJson.put(SIZE , size);
        pictureJson.put(HEIGTH,heigth);
        pictureJson.put(WIDGTH,widgth);
        return pictureJson; 
    }

    
}
