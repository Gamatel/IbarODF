package ibarodf.core.file.exception;

public class UnableToAddHyperLinkException extends UnableToAddMetadataException{
    public UnableToAddHyperLinkException(String fileName){
        super("Cannot Add Hyperlink for "+ fileName);
    }
    
}
