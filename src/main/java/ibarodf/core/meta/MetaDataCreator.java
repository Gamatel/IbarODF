package ibarodf.core.meta;

import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;
import java.text.ParseException;

public class MetaDataCreator extends MetaDataXML {
	public final static String ATTR = "Creator";

	public MetaDataCreator(OdfOfficeMeta meta) {
		super(meta, ATTR, meta.getCreator());
	}

	public void setValue(String value) throws ParseException {
		getMeta().setCreator(value);
		super.setValue(value);
	}
}
