package ibarodf.command;
public class UnrecognizableTypeFileException extends Exception {
    public UnrecognizableTypeFileException(java.nio.file.Path path){
        super("\n/!\\The type of the file at "+ path.toString() +" is unrecognizable.");
    }
    
}
