package ibarodf.core.file.exception;

public class UnableToSaveChangesException extends Exception {
    public UnableToSaveChangesException(String fileName){
        super("Couldn't save the changes for "+ fileName);
    }
    
}
