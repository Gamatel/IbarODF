package ibarodf.core.file;


import java.util.Map;
import java.lang.StringBuilder;
import ibarodf.core.meta.AbstractMetaData;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.HashMap;

public abstract class AbstractRegularFile extends AbstractGenericFile{
	private final HashMap<String, AbstractMetaData> metaDataHM = new HashMap<String, AbstractMetaData>();

	public AbstractRegularFile(Path path) {
		super(path);
	}
	
	
	public HashMap<String, AbstractMetaData> getMetaData() {
		return metaDataHM;	
	}
	
	abstract void loadMetaData()  throws Exception;

	public void setMetaData(final String attribut, final String value) throws ParseException {
		metaDataHM.get(attribut).setValue(value);
	}

	public String getMetaData(final String attribut) {
		if(!metaDataHM.containsKey(attribut)) 
			throw new IllegalArgumentException(String.format("Attribut %s doesnn't exist", attribut));
		return metaDataHM.get(attribut).getValue();
	}

	@Override
	public StringBuilder displayMetaData() {
		StringBuilder metaDataStr = new StringBuilder();
		metaDataStr.append("{");
		for (Map.Entry<String, AbstractMetaData> entry: getMetaData().entrySet()) {
			String lineStr = String.format("%s: %s,\n", entry.getKey(), entry.getValue().getValue());
			metaDataStr.append(lineStr);
		}
		metaDataStr.append("}");
		return metaDataStr;
	}

	public void addMetaData(String key, AbstractMetaData metaData){
		metaDataHM.put(key,metaData); 
	}


}
