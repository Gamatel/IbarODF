package ibarodf.core.meta;

import java.text.ParseException;
import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;

public class MetaDataSubject extends MetaDataXML {
	public final static String ATTR = "Subject";
	public final static String SUBJECT = ATTR;

	public MetaDataSubject(OdfOfficeMeta meta) {
		super(meta, ATTR,meta.getSubject());
		try{
			if(getValue() == null){
				setValue("No Subject");
			}
		}catch(Exception e){}
	}

	public void setValue(String value) throws ParseException, ReadOnlyMetaException {
		getMeta().setSubject(value);
		super.setValue(value);
	}
}
