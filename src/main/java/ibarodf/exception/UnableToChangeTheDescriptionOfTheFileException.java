package ibarodf.exception;

import java.nio.file.Path;

public class UnableToChangeTheDescriptionOfTheFileException extends Exception {
    public UnableToChangeTheDescriptionOfTheFileException(Path path){
        super("Cannot change the description of the file "+ path.getFileName());
    }
    public UnableToChangeTheDescriptionOfTheFileException(String fileName){
        super("Cannot change the description of the file "+ fileName);
    }
    public UnableToChangeTheDescriptionOfTheFileException(){
        super("Cannot change the description of the current file");
    }
}
