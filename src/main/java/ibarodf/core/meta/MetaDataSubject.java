package ibarodf.core.meta;

import java.text.ParseException;
import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;

import ibarodf.core.meta.exception.ReadOnlyMetaException;

public class MetadataSubject extends MetadataXML {
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
