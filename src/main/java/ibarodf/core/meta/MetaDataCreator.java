package ibarodf.core.meta;

import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;
import java.text.ParseException;

public class MetaDataCreator extends MetaDataXML {
	public final static String ATTR = "Creator";
	public final static String CREATOR = ATTR;

	public MetaDataCreator(OdfOfficeMeta meta) {
		super(meta, ATTR, meta.getCreator());
		try{
			if(getValue() == null){
				setValue("No Creator");
			}
		}catch(Exception e){}
	}

	public void setValue(String value) throws ParseException, ReadOnlyMetaException {
		getMeta().setCreator(value);
		super.setValue(value);
	}
}
