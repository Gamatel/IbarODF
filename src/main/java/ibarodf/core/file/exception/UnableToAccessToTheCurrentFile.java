package ibarodf.core.file.exception;

import java.nio.file.Path;

public class UnableToAccessToTheCurrentFile extends Exception {
    public UnableToAccessToTheCurrentFile(Path filePath){
        super("Unable to acces to the file "+ filePath.getFileName());
    }
    
    public UnableToAccessToTheCurrentFile(String fileName){
        super("Unable to acces to the file "+ fileName);
    }
}
