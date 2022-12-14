package ibarodf.core.file;

import java.io.File;
import java.nio.file.Path;

import org.json.JSONObject;

import ibarodf.core.AbtractIbarOdfCore;
import ibarodf.core.meta.exception.UnableToConvertToJsonFormatException;


/**
 * This class represents a file.
 */
public abstract class AbstractGenericFile {
    public final static String PATH = "Path";
    public final static String FILE_NAME = "File Name";
    public final static String MIME_TYPE = "Mime Type";
    public final static String TYPE_DIRECTORY =  "Directory";
    public final static String UNKNOWN_TYPE =  "Unknown";

    private final Path path;
    private final String fileName;
    private final String mimeType;


    public AbstractGenericFile(Path path){
        this.path = path;
        this.fileName = initFileName();
        this.mimeType = initMIMEType();
    }

    /**
     * This function returns the Mime Type a file
     * 
     * @return The Mime Type variable is being returned.
     */
    public String getMimeType(){
        return mimeType;
    }

    /**
     * This function returns the path of the file
     * 
     * @return The path is being returned.
     */
    public Path getPath(){
        return path;
    }

    /**
     * This function returns the file name of the file that is being read
     * 
     * @return The file name.
     */
    public String getFileName(){
        return fileName;
    }

    
    private String initFileName(){
        File file = new File(getPath().toString());
        return file.getName();
    }

    private String initMIMEType(){
		try{
            File file = new File(path.toString());
            if(file.isDirectory()){
                return TYPE_DIRECTORY;
            }
			return AbtractIbarOdfCore.fileType(getPath().toString());
		}catch(Exception e){
		    return UNKNOWN_TYPE;		
        }
    } 
    /**
     * Returns a JSON representation of the file.
     * 
     * @return A JSONObject
     */
    public JSONObject toJonObject() throws UnableToConvertToJsonFormatException{
        JSONObject genericFile = new JSONObject();
        genericFile.put(PATH, path);
        genericFile.put(FILE_NAME, fileName);
        genericFile.put(MIME_TYPE, mimeType);
        return genericFile;
    }
    
}
