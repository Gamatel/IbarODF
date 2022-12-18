package ibarodf.core.meta;

import java.text.ParseException;
import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;

import ibarodf.core.meta.exception.ReadOnlyMetaException;

public class MetadataInitialCreator extends MetadataXML {
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
