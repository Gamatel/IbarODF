package ibarodf.core.meta;

import java.text.ParseException;
import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;

public class MetaDataComment extends MetaDataXML {
	public final static String ATTR = "Comments";
	public final static String COMMENTS = ATTR;
	public MetaDataComment(OdfOfficeMeta meta) {
		super(meta, ATTR, meta.getDescription());
		try{
			if(getValue() == null){
				setValue("No Comment");
			}
		}catch(Exception e){}
	}

	public void setValue(String value) throws ParseException, ReadOnlyMetaException {
		getMeta().setDescription(value);
		super.setValue(value);
	}
}
