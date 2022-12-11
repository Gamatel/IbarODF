package ibarodf.core.meta;

import java.text.ParseException;
import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;

public class MetaDataTitle extends MetaDataXML {
	public final static String ATTR = "Title";

	public MetaDataTitle(OdfOfficeMeta meta) {
		super(meta, ATTR, meta.getTitle());
	}

	public void setValue(String value) throws ParseException, ReadOnlyMetaException {
		getMeta().setTitle(value);
		super.setValue(value);
	}
}
