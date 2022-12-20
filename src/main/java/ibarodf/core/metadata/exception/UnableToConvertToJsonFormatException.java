package ibarodf.core.metadata.exception;

public class UnableToConvertToJsonFormatException extends Exception{
    public UnableToConvertToJsonFormatException(String dataThatCannotBeConverted){
        super("Unable to convert "+dataThatCannotBeConverted+ " to Json Format");
    }
    
}
