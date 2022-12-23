package ibarodf.gui.toolbar.exception;

import java.nio.file.Path;

public class CurrentDirectoryIsAFile extends Exception {
    public CurrentDirectoryIsAFile(String fileName){
        super(fileName+" is not a directory");
    }
    

    public CurrentDirectoryIsAFile(Path filePath){
        super(filePath.getFileName()+" is not a directory");
    }
}
