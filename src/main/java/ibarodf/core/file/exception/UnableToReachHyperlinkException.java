package ibarodf.core.file.exception;

public class UnableToReachHyperlinkException extends Exception {
    public UnableToReachHyperlinkException(String fileName){
        super("Cannot reach hyperlink for "+ fileName);
    }
    
}
