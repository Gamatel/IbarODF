package ibarodf.core.file;

import java.io.File;
import java.nio.file.Path;

import org.json.JSONObject;

import ibarodf.core.IbarODFCore;


public abstract class AbstractGenericFile {
    private final Path path;
    private final String fileName;
    private final String mimeType;

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
			return IbarODFCore.fileType(getPath().toString());
		}catch(Exception e){
		    return "Unknown";		
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

    abstract public JSONObject toJonObject() throws Exception; 
    abstract public StringBuilder displayMetaData() throws Exception;
    
}
