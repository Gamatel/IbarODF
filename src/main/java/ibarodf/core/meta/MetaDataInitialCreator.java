package ibarodf.core.meta;

import java.text.ParseException;
import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;

public class MetaDataInitialCreator extends MetaDataXML {
	public final static String ATTR = "InitialCreator";

	public MetaDataInitialCreator(OdfOfficeMeta meta){
		super(meta, ATTR, meta.getInitialCreator());
	}

	public void setValue(String value) throws ParseException {
		getMeta().setInitialCreator(value);
		super.setValue(value);
	}
}
