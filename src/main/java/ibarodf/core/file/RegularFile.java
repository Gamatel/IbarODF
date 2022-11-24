package ibarodf.core.file;

import java.util.HashMap;
import java.text.ParseException;
import ibarodf.core.meta.MetaDataAbstract;

public abstract class RegularFile {

	private String path;
	private final HashMap<String, MetaDataAbstract> metaDataHM = new HashMap<String, MetaDataAbstract>();

	public RegularFile(String path) {
		this.path = path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public abstract void setMetaData(final String attribut, final String value) throws ParseException;
	public abstract void loadMetaData() throws Exception;
	public abstract String displayMetaData();

	public HashMap<String, MetaDataAbstract> getMetaData() {
		return metaDataHM;	
	}
}
