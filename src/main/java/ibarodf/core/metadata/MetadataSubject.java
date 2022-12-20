package ibarodf.core.metadata;

import java.text.ParseException;
import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;

import ibarodf.core.metadata.exception.ReadOnlyMetaException;

public class MetadataSubject extends AbstractMetadataXML {
	public final static String ATTR = "Subject";
	public final static String SUBJECT = ATTR;

	public MetadataSubject(OdfOfficeMeta meta) {
		super(meta, ATTR,meta.getSubject());
	}

	@Override
	public void setValue(String value) throws ParseException, ReadOnlyMetaException {
		getMeta().setSubject(value);
		super.setValue(value);
	}
}
