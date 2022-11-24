package ibarodf.core.meta;

import java.text.ParseException;
import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;

public class MetaDataTitle extends MetaDataOdf {
	public final static String attr = "Title";

	public MetaDataTitle(OdfOfficeMeta meta, String value) {
		super(meta, attr, value);
	}

	public void setValue(String value) throws ParseException {
		getMeta().setTitle(value);
		super.setValue(value);
	}
}
