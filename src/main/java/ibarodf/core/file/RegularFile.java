package ibarodf.core.file;



import org.json.JSONObject;

import java.lang.StringBuilder;
import java.nio.file.Path;


public class RegularFile extends AbstractGenericFile{
	public static final String FILE_NAME = "Name";
	public static final String MIME_TYPE = "Mime Type";


	public RegularFile(Path path) throws Exception{
		super(path);
	}
	
	@Override
	public StringBuilder displayMetaData() throws Exception{
		StringBuilder result = new StringBuilder();
		result.append("<File Name : "+ getFileName()+";");
		result.append("MimeType : "+ getMimeType()+";>");
		return result;
	}

	public JSONObject toJonObject() throws Exception{
		JSONObject fileJson = new JSONObject();
		fileJson.put(FILE_NAME, getFileName());
		fileJson.put(MIME_TYPE, getMimeType());
		return fileJson;
	}
	


}
