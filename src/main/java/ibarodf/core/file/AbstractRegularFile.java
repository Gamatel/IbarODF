package ibarodf.core.file;


import java.util.Map;
import java.io.File;
import java.lang.StringBuilder;

import ibarodf.command.CommandTranslator;
import ibarodf.core.meta.AbstractMetaData;
import ibarodf.core.meta.MetaDataRegularFile;
import ibarodf.core.meta.MetaDataTitle;

import java.nio.file.Path;
import java.text.ParseException;
import java.util.HashMap;

public abstract class AbstractRegularFile extends AbstractGenericFile{
	private final HashMap<String, AbstractMetaData> metaDataHM = new HashMap<String, AbstractMetaData>();
	public final static String FILE_TITLE = "File title"; 
	public final static String FILE_MIME_TYPE = "MIME type";

	public AbstractRegularFile(Path path) {
		super(path);
	}
	
	
	public HashMap<String, AbstractMetaData> getMetaData() {
		return metaDataHM;	
	}
	

	public void setMetaData(final String attribut, final String value) throws ParseException {
		metaDataHM.get(attribut).setValue(value);
	}

	public String getMetaData(final String attribut) throws Exception{
		if(!metaDataHM.containsKey(attribut)) 
			throw new IllegalArgumentException(String.format("Attribut %s doesnn't exist", attribut));
		return metaDataHM.get(attribut).getValue();
	}

	@Override
	public StringBuilder displayMetaData() throws Exception{
		StringBuilder metaDataStr = new StringBuilder();
		metaDataStr.append("<");
		String lineStr;
		for (Map.Entry<String, AbstractMetaData> entry: getMetaData().entrySet()) {
			lineStr = String.format("%s: %s,", entry.getKey(), entry.getValue().getValue());
			metaDataStr.append(lineStr);
		}
		metaDataStr.append(">");
		return metaDataStr;
	}

	public void addMetaData(String key, AbstractMetaData metaData){
		metaDataHM.put(key,metaData); 
	}

	public void loadMetaData()  throws Exception{
		addMetaData(AbstractRegularFile.FILE_TITLE,getTitle());
        addMIMEType();
    }

	public MetaDataRegularFile getTitle(){
        File file = new File(getPath().toString());
        return new MetaDataRegularFile(MetaDataTitle.ATTR, file.getName());
    }

    public void addMIMEType(){
		try{
			MetaDataRegularFile metaMimeType = new MetaDataRegularFile(FILE_MIME_TYPE, CommandTranslator.fileType(getPath().toString()));
			addMetaData(AbstractRegularFile.FILE_MIME_TYPE, metaMimeType);
		}catch(Exception e){
			addMetaData(AbstractRegularFile.FILE_MIME_TYPE, new MetaDataRegularFile(FILE_MIME_TYPE,"Unknown"));
		}
    }


}
