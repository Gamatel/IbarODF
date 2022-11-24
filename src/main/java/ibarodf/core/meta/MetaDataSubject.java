package ibarodf.core.meta;

import java.text.ParseException;
import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;

public class MetaDataSubject extends MetaDataOdf {
	public final static String attr = "Subject";

	public MetaDataSubject(OdfOfficeMeta meta, String value) {
		super(meta, attr, value);
	}

	public void setValue(String value) throws ParseException {
		getMeta().setSubject(value);
		super.setValue(value);
	}
}
