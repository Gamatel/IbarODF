package ibarodf.core.meta;

import java.text.ParseException;
import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;

public class MetaDataComment extends MetaDataXML {
	public final static String ATTR = "Comments";
	public MetaDataComment(OdfOfficeMeta meta) {
		super(meta, ATTR, meta.getDescription());
	}

	public void setValue(String value) throws ParseException, ReadOnlyMetaException {
		getMeta().setDescription(value);
		super.setValue(value);
	}
}
