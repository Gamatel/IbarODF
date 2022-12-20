package ibarodf.core.metadata;

import java.text.ParseException;
import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;

import ibarodf.core.metadata.exception.ReadOnlyMetaException;

public class MetadataTitle extends AbstractMetadataXML {
	public final static String ATTR = "Title";
	public final static String TITLE = ATTR;

	public MetadataTitle(OdfOfficeMeta meta) {
		super(meta, ATTR, meta.getTitle());
	}

	@Override
	public void setValue(String value) throws ParseException, ReadOnlyMetaException {
		getMeta().setTitle(value);
		super.setValue(value);
	}
}
