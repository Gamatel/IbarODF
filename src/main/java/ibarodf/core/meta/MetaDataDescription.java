package ibarodf.core.meta;

import java.text.ParseException;
import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;

public class MetaDataDescription extends MetaDataOdf {
	public final static String attr = "Description";
	public MetaDataDescription(OdfOfficeMeta meta, String value) {
		super(meta, attr, value);
	}

	public void setValue(String value) throws ParseException {
		getMeta().setDescription(value);
		super.setValue(value);
	}
}
