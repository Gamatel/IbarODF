package ibarodf.core.metadata;

import java.text.ParseException;
import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;

import ibarodf.core.metadata.exception.ReadOnlyMetaException;

public class MetadataInitialCreator extends AbstractMetadataXML {
	public final static String ATTR = "InitialCreator";
	public final static String INITIAL_CREATOR = ATTR;

	public MetadataInitialCreator(OdfOfficeMeta meta){
		super(meta, ATTR, meta.getInitialCreator());
	}

	@Override
	public void setValue(String value) throws ParseException, ReadOnlyMetaException {
		getMeta().setInitialCreator(value);
		super.setValue(value);
	}
}
