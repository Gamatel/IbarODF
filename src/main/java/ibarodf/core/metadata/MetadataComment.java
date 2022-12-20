package ibarodf.core.metadata;

import java.text.ParseException;
import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;

import ibarodf.core.metadata.exception.ReadOnlyMetaException;

public class MetadataComment extends AbstractMetadataXML {
	public final static String ATTR = "Comments";
	public final static String COMMENTS = ATTR;
	public MetadataComment(OdfOfficeMeta meta) {
		super(meta, ATTR, meta.getDescription());
	}

	@Override
	public void setValue(String value) throws ParseException, ReadOnlyMetaException {
		getMeta().setDescription(value);
		super.setValue(value);
	}
}
