package ibarodf.command.exception;
public class UnallowedCommandException extends Exception{
    public UnallowedCommandException(String[] command){
        super("The command" + command + " does not exists\nPlease check the help manual (-h or --help)");
    }
    public UnallowedCommandException(String typeOfError){
        super("Unallowed command : "+typeOfError+ "\nPlease check the help manual  (-h or --help)");
    }
    public UnallowedCommandException(){
        super("Unknown command \nPlease check the help manual  (-h or --help)");
    }
    
}
