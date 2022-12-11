package ibarodf.core.meta;

import java.nio.file.Path;
import java.util.ArrayList;


import ibarodf.core.file.Directory;

public class MetaDataOdfPictures extends AbstractMetaDataOdf {
    private final ArrayList<Path> picturesPath;
    public final static String ATTR = "Pictures";

    public MetaDataOdfPictures(Path pathOdtFile){
        super(ATTR, pathOdtFile.toString());
        picturesPath = Directory.getSubFilesPathFromDirectory(pathOdtFile);

    }

    public String getValue() throws Exception{
        StringBuilder pictures = new StringBuilder(super.getValue());
        Picture currentPicture;
        pictures.append("[");
        for(Path currentPicturePath : picturesPath){
            currentPicture = new Picture(currentPicturePath);
            pictures.append(currentPicture.toString());
        }
        pictures.append("]");
        return pictures.toString();
    }

    public ArrayList<Path> getPicturesPath(){
        return picturesPath;
    }

}
