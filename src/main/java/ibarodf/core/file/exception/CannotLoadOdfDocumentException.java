package ibarodf.core.file.exception;

public class CannotLoadOdfDocumentException extends CannotAddAllMetadatasException{
    public CannotLoadOdfDocumentException(String fileName){
        super("Cannot load OdfDocument for "+ fileName);
    }
    
}
