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
        for(Path currentPicturePath : picturesPath){
            pictures.append("*").append(currentPicturePath).append("?,");
        }
        return pictures.toString();
    }


}
