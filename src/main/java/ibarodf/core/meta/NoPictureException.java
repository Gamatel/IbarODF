package ibarodf.core.meta;

import java.nio.file.Path;

public class NoPictureException extends Exception{
    public NoPictureException(Path path){
        super("\nNo picture in " + path.getFileName()+ ".");
    }
}
