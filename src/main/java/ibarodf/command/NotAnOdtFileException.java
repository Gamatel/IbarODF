package ibarodf.command;
public class NotAnOdtFileException extends Exception {
    public NotAnOdtFileException(String file){
        super("\n/!\\The file "+ file +" is not an ODT file.");
    }
    
}
