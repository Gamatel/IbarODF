package ibarodf.core.file;

import java.nio.file.Path;

import org.json.JSONObject;

import ibarodf.core.metadata.exception.UnableToConvertToJsonFormatException;

/**
 * Wrong File is responsible for instantiation of the files 
 * that have met a probleme during the contruction of a directory.
 */
public class WrongFile extends RegularFile {
    private String errorMessage;
    public final static String ERRORMESSAGE = "Error Message"; 
    private static final String DEFAULT_ERROR_MESSAGE = "Cannot get the error Message";

    public WrongFile(Path path, String errorMessage){
        super(path);
        this.errorMessage = errorMessage;
    }

    public WrongFile(Path path){
        this(path, DEFAULT_ERROR_MESSAGE); 
    }

    @Override
    public JSONObject toJonObject() throws UnableToConvertToJsonFormatException{
        JSONObject jsonWrongOdfFile = super.toJonObject();
        jsonWrongOdfFile.put(ERRORMESSAGE, errorMessage);
        return jsonWrongOdfFile;
    }
    
}
