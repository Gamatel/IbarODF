package ibarodf.core.meta.exception;

public class NoStatisticsException extends Exception{
    public NoStatisticsException(){
        super("Exception : Current file does not have statistics.");
    }
    
}
