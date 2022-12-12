package ibarodf.core.meta;

import java.text.ParseException;
import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;

public class MetaDataTitle extends MetaDataXML {
	public final static String ATTR = "Title";
	public final static String TITLE = ATTR ;

	public MetaDataTitle(OdfOfficeMeta meta) {
		super(meta, ATTR, meta.getTitle());
		try{
			if(getValue() == null){
				setValue("No title");
			}
		}catch(Exception e){}
	}

	public void setValue(String value) throws ParseException, ReadOnlyMetaException {
		getMeta().setTitle(value);
		super.setValue(value);
	}
}
