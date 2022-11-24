package ibarodf.core.meta;

import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;
import java.text.ParseException;

public class MetaDataCreator extends MetaDataOdf {
	public final static String attr = "Creator";

	public MetaDataCreator(OdfOfficeMeta meta, String value) {
		super(meta, attr, value);
	}

	public void setValue(String value) throws ParseException {
		getMeta().setCreator(value);
		super.setValue(value);
	}
}
