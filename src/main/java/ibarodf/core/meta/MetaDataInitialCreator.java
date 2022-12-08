package ibarodf.core.meta;

import java.text.ParseException;
import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;

public class MetaDataInitialCreator extends MetaDataXML {
	public final static String ATTR = "InitialCreator";

	public MetaDataInitialCreator(OdfOfficeMeta meta, String value) {
		super(meta, ATTR, value);
	}

	public void setValue(String value) throws ParseException, ReadOnlyMetaException {
		getMeta().setInitialCreator(value);
		super.setValue(value);
	}
}
