package ibarodf.core.meta;

import java.nio.file.Path;
import java.util.ArrayList;


import ibarodf.core.file.Directory;

public class MetaDataOdtPictures extends MetaDataRegularFile {
    private ArrayList<Path> picturesPath;
    public final static String ATTR = "Pictures";

    public MetaDataOdtPictures(Path pathOdtFile){
        super(ATTR, pathOdtFile.toString());
        picturesPath = Directory.getSubFilesPathFromDirectory(pathOdtFile);

    }
    
    public int getNumberOfPictures(){
        return picturesPath.size();
    }

    public String getValue() throws Exception{
        StringBuilder pictures = new StringBuilder(super.getValue());
        for(Path currentPicturePath : picturesPath){
            pictures.append("*"+currentPicturePath+"?,");
        }
        return pictures.toString();

    }

    public ArrayList<Path> getPicturesPath(){
        return picturesPath;
    }
}
