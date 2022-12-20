package ibarodf.exception;

import java.nio.file.Path;

public class UnableToMakeAskedChangesException extends Exception {
    public UnableToMakeAskedChangesException(String filePath){
        super("Cannot make the asked change on the file "+ filePath);
    }

    public UnableToMakeAskedChangesException(Path filePath){
        super("Cannot make asked changes on the file "+ filePath.getFileName());
    }
    
    
}
