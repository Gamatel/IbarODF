package ibarodf.command;
public class NotAnOdfFileException extends Exception {
    public NotAnOdfFileException(String file){
        super("\tThe file "+ file +" is not an ODT file.");
    }
    
}
