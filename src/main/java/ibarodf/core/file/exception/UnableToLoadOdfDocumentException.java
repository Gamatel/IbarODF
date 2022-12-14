package ibarodf.core.file.exception;

public class UnableToLoadOdfDocumentException extends UnableToAddMetadataException{
    public UnableToLoadOdfDocumentException(String fileName){
        super("Cannot load OdfDocument for "+ fileName);
    }
    
}
