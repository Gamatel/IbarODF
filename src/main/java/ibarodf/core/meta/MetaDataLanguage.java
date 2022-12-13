package ibarodf.core.meta;

import java.text.ParseException;
import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;

import ibarodf.core.meta.exception.ReadOnlyMetaException;

public class MetaDataLanguage extends MetaDataXML {
	public final static String ATTR = "Language";

	public MetaDataLanguage(OdfOfficeMeta meta, String value) {
		super(meta, ATTR, value);
	}

	public void setValue(String value) throws ParseException, ReadOnlyMetaException {
		getMeta().setLanguage(value);
		super.setValue(value);
	}
}
