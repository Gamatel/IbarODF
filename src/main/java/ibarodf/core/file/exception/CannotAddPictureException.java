package ibarodf.core.file.exception;

public class CannotAddPictureException extends CannotAddAllMetadatasException {
    public CannotAddPictureException(String fileName){
        super("Cannot Add Pictures for "+ fileName);
    }
    
}
