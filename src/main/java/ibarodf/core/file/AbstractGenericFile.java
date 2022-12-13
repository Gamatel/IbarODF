package ibarodf.core.file;

import java.io.File;
import java.nio.file.Path;

import org.json.JSONObject;

import ibarodf.core.AbtractIbarOdfCore;


public abstract class AbstractGenericFile {
    private final Path path;
    private final String fileName;
    private final String mimeType;


    public final static String PATH = "Path";
    public final static String FILE_NAME = "File Name";
    public final static String MIME_TYPE = "Mime Type";

    public final static String TYPE_DIRECTORY =  "Directory";
    public final static String UNKNOWN_TYPE =  "Unknown";


    public AbstractGenericFile(Path path){
        this.path = path;
        this.fileName = setFileName();
        this.mimeType = setMIMEType();
    }

    private String setFileName(){
        File file = new File(getPath().toString());
        return file.getName();
    }

    private String setMIMEType(){
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


    public String getMimeType(){
        return mimeType;
    }
    public Path getPath(){
        return path;
    }

    public String getFileName(){
        return fileName;
    }

    public JSONObject toJonObject() throws Exception{
        JSONObject genericFile = new JSONObject();
        genericFile.put(PATH, path);
        genericFile.put(FILE_NAME, fileName);
        genericFile.put(MIME_TYPE, mimeType);
        return genericFile;
    } 

    abstract public StringBuilder displayMetaData() throws Exception;
    
}
