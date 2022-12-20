package ibarodf.core.metadata;

import java.text.ParseException;
import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;

import ibarodf.core.metadata.exception.ReadOnlyMetaException;

public class MetadataLanguage extends AbstractMetadataXML {
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
