package ibarodf.gui.toolbar.exception;

import java.nio.file.Path;

public class CurrentDirectoryIsAFileException extends Exception {
    public CurrentDirectoryIsAFileException(String fileName){
        super(fileName+" is not a directory");
    }
    

    public CurrentDirectoryIsAFileException(Path filePath){
        super(filePath.getFileName()+" is not a directory");
    }
}
