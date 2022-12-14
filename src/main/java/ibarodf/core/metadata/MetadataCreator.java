package ibarodf.core.metadata;

import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;

import ibarodf.core.metadata.exception.ReadOnlyMetaException;

import java.text.ParseException;

public class MetadataCreator extends AbstractMetadataXML {
	public final static String ATTR = "Creator";
	public final static String CREATOR = ATTR;

	public MetadataCreator(OdfOfficeMeta meta) {
		super(meta, ATTR, meta.getCreator());
	}

	@Override
	public void setValue(String value) throws ParseException, ReadOnlyMetaException {
		getMeta().setCreator(value);
		super.setValue(value);
	}
}
