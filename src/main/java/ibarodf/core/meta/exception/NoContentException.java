package ibarodf.core.meta.exception;

import java.nio.file.Path;

public class NoContentException extends Exception {
    public NoContentException(Path filePath){
        super("Exception : "+filePath.getFileName() + " does not have a content."); 
    }

    
}
