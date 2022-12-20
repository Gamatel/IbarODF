package ibarodf.core.metadata.exception;

import java.nio.file.Path;

public class NoPictureException extends Exception{
    public NoPictureException(Path path){
        super("Exception : No picture in " + path.getFileName()+ ".");
    }
}
