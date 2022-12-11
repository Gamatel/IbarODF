package ibarodf.core.meta;

import java.nio.file.Path;

public class NoContentException extends Exception {
    public NoContentException(Path filePath){
        super(filePath.getFileName() + " does not have a content."); 
    }

    
}
