package ibarodf.core;
public class UnrecognizableTypeFileException extends Exception {
    public UnrecognizableTypeFileException(java.nio.file.Path path){
        super("\tThe type of the file "+ path.getFileName() +" is unrecognizable.");
    }
    
}
