package ibarodf.core.file.exception;

public class CannotAddThumbnailException extends CannotAddAllMetadatasException{
    public CannotAddThumbnailException(String fileName){
        super("Cannot Add Thumbnail for "+ fileName);
    }
    
}
