package ibarodf.core.meta;

import java.text.ParseException;
import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;

import ibarodf.core.meta.exception.ReadOnlyMetaException;

public class MetadataLanguage extends MetadataXML {
	public final static String ATTR = "Language";

	public MetadataLanguage(OdfOfficeMeta meta, String value) {
		super(meta, ATTR, value);
	}

	@Override
	public void setValue(String value) throws ParseException, ReadOnlyMetaException {
		getMeta().setLanguage(value);
		super.setValue(value);
	}
}
