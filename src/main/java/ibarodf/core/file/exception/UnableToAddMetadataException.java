package ibarodf.core.file.exception;

import java.nio.file.Path;

public class UnableToAddMetadataException extends Exception {
    public UnableToAddMetadataException(String errorMessage){
        super(errorMessage);
    }

    public UnableToAddMetadataException(Path filePath){
        super("Couldn't add metadata for the file " + filePath.getFileName());
    }

    public UnableToAddMetadataException(Path filePath, String cause){
        super("Couldn't add metadata for the file " + filePath.getFileName() + " Cause - "+ cause);
    }
    
}
