package ibarodf.gui.toolbar.exception;

import java.nio.file.Path;

public class CurrentFileIsADirectoryException extends Exception {
    public CurrentFileIsADirectoryException(String fileName){
        super(fileName+" is a directory ");
    }
    public CurrentFileIsADirectoryException(Path filePath){
        super(filePath.getFileName() +" is a directory ");
    }
    
    
}


