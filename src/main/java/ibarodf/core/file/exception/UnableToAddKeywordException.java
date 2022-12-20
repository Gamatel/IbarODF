package ibarodf.core.file.exception;

public class UnableToAddKeywordException extends UnableToAddMetadataException {
    public UnableToAddKeywordException(String fileName){
        super("Cannot Add Keyword for "+ fileName);
    }
}
