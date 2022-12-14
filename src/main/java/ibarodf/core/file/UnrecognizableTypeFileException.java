package ibarodf.core.file;
public class UnrecognizableTypeFileException extends Exception {
    public UnrecognizableTypeFileException(java.nio.file.Path path){
        super("Exception : The type of the file "+ path.getFileName() +" is unrecognizable.");
    }
    
}
