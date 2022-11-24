package ibarodf.core.meta;

import java.text.ParseException;
import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;

public class MetaDataInitialCreator extends MetaDataOdf {
	public final static String attr = "InitialCreator";

	public MetaDataInitialCreator(OdfOfficeMeta meta, String value) {
		super(meta, attr, value);
	}

	public void setValue(String value) throws ParseException {
		getMeta().setInitialCreator(value);
		super.setValue(value);
	}
}
