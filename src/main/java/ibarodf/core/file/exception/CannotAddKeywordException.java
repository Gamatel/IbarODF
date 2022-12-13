package ibarodf.core.file.exception;

public class CannotAddKeywordException extends CannotAddAllMetadatasException {
    public CannotAddKeywordException(String fileName){
        super("Cannot Add Keyword for "+ fileName);
    }
}
