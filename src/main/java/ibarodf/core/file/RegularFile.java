package ibarodf.core.file;




import java.lang.StringBuilder;
import java.nio.file.Path;


public class RegularFile extends AbstractGenericFile{
	public RegularFile(Path path){
		super(path);
	}
	
	@Override
	public StringBuilder displayMetaData() throws Exception{
		StringBuilder result = new StringBuilder();
		result.append("<File Name : "+ getFileName()+";");
		result.append("MimeType : "+ getMimeType()+";>");
		return result;
	}
	


}
