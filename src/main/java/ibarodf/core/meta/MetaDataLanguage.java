package ibarodf.core.meta;

import java.text.ParseException;
import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;

public class MetaDataLanguage extends MetaDataOdf {
	public final static String attr = "Language";

	public MetaDataLanguage(OdfOfficeMeta meta, String value) {
		super(meta, attr, value);
	}

	public void setValue(String value) throws ParseException {
		getMeta().setLanguage(value);
		super.setValue(value);
	}
}
