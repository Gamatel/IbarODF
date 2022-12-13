package ibarodf.core.file.exception;

public class CannotAddAllFilesException extends Exception {
    public CannotAddAllFilesException(String directoryName){
        super("Cannot add all files from " +directoryName);
    }    
}
