package ibarodf.core.file.exception;

public class CannotAddHyperLinkException extends CannotAddAllMetadatasException{
    public CannotAddHyperLinkException(String fileName){
        super("Cannot Add Hyperlink for "+ fileName);
    }
    
}
