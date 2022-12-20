package ibarodf.core.file.exception;

public class UnableToAddThumbnailException extends UnableToAddMetadataException{
    public UnableToAddThumbnailException(String fileName){
        super("Cannot Add Thumbnail for "+ fileName);
    }
    
}
