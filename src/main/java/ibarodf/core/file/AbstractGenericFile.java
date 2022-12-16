package ibarodf.core.file;

import java.io.File;
import java.nio.file.Path;

import org.json.JSONObject;

import ibarodf.core.IbarOdfCore;
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
    public final static String SIZE =  "Size (Ko)";


    private final Path path;
    private final String fileName;
    private final String mimeType;
    private final long size;


    public AbstractGenericFile(Path path){
        this.path = path;
        File file = path.toFile();
        this.fileName = file.getName();
        this.mimeType = initMIMEType();
        this.size = file.length()/1024 ;
    }

    /**
     * This function returns the Mime Type a file
     * @return The Mime Type variable is being returned.
     */
    public String getMimeType(){
        return mimeType;
    }

    /**
     * This function returns the path of the file
     * @return The File path
     */
    public Path getPath(){
        return path;
    }

    /**
     * This function returns the file name of the file
     * @return The file name.
     */
    public String getFileName(){
        return fileName;
    }

    /**
     * This function returns the size in Ko of the file
     * @return The size of the file.
     */
    public long getSize(){
        return size; 
    }

    /**
     * It returns the MIME type of the file, or "Directory" if the file is a
     * directory
     * @return The MIME type of the file.
     */
    private String initMIMEType(){
		try{
            File file = path.toFile();
            if(file.isDirectory()){
                return TYPE_DIRECTORY;
            }
			return IbarOdfCore.fileType(getPath());
		}catch(Exception e){
		    return UNKNOWN_TYPE;		
        }
    } 

    /**
     * Returns a JSON representation of the file.
     * @return A JSONObject
     * @throws UnableToConvertToJsonFormatException
     */ 
    public JSONObject toJonObject() throws UnableToConvertToJsonFormatException{
        JSONObject genericFile = new JSONObject();
        genericFile.put(PATH, path);
        genericFile.put(FILE_NAME, fileName);
        genericFile.put(MIME_TYPE, mimeType);
        genericFile.put(SIZE, size);
        return genericFile;
    }
    
}
