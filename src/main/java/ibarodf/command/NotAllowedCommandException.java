package ibarodf.command;
public class NotAllowedCommandException extends Exception{
    public NotAllowedCommandException(String[] command){
        super("\tEntering an unallowed command.\nPlease check the help manual (-h or --help).");
    }
    public NotAllowedCommandException(String typeOfError){
        super("\tUnallowed command : "+typeOfError+ "\nPlease check the help manual  (-h or --help).");
    }
    
}
