package ibarodf.core.file.exception;

public class UnableToAddAllFilesException extends Exception {
    public UnableToAddAllFilesException(String directoryName){
        super("Cannot add all files from " +directoryName);
    }    
}
