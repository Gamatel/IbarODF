package ibarodf.core.file.exception;
public class UnrecognizableTypeFileException extends Exception {
    public UnrecognizableTypeFileException(java.nio.file.Path path){
        super("Exception : The type of the file "+ path.getFileName() +" is unrecognizable.");
    }
    
}
