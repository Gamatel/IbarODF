package ibarodf.core.file.exception;

public class UnableToAddPictureException extends UnableToAddMetadataException {
    public UnableToAddPictureException(String fileName){
        super("Cannot Add Pictures for "+ fileName);
    }
    
}
