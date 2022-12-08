package ibarodf.core.meta;

import java.text.ParseException;
import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;

public class MetaDataSubject extends MetaDataXML {
	public final static String ATTR = "Subject";

	public MetaDataSubject(OdfOfficeMeta meta, String value) {
		super(meta, ATTR, value);
	}

	public void setValue(String value) throws ParseException, ReadOnlyMetaException {
		getMeta().setSubject(value);
		super.setValue(value);
	}
}
