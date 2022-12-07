package ibarodf.core.meta;

import java.text.ParseException;
import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;

public class MetaDataComment extends MetaDataXML {
	public final static String ATTR = "Comments";
	public MetaDataComment(OdfOfficeMeta meta, String value) {
		super(meta, ATTR, value);
	}

	public void setValue(String value) throws ParseException {
		getMeta().setDescription(value);
		super.setValue(value);
	}
}
