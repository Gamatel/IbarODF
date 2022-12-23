package ibarodf.gui.toolbar.exception;

import java.nio.file.Path;

public class CurrentFileIsADirectory extends Exception {
    public CurrentFileIsADirectory(String fileName){
        super(fileName+" is a directory ");
    }
    public CurrentFileIsADirectory(Path filePath){
        super(filePath.getFileName() +" is a directory ");
    }
    
    
}


