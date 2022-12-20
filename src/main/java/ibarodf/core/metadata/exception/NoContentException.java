package ibarodf.core.metadata.exception;

import java.nio.file.Path;

/**
 * It's signal if the current Odf file doesn't have an content.xml, in which case
 * it's impossible to get some Metadata 
 */
public class NoContentException extends Exception {
    public NoContentException(Path filePath){
        super("Exception : "+filePath.getFileName() + " does not have a content."); 
    }

    
}
