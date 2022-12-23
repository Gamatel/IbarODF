package ibarodf.gui.toolbar.exception;

import java.nio.file.Path;

public class WrongFileException extends Exception {
    public WrongFileException(String fileName){
        super(fileName+" is a wrong file");
    }

    public WrongFileException(Path fileName){
        super(fileName.getFileName()+" is a wrong file");
    }

    
}
