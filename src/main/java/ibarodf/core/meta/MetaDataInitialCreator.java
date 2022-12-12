package ibarodf.core.meta;

import java.text.ParseException;
import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;

public class MetaDataInitialCreator extends MetaDataXML {
	public final static String ATTR = "InitialCreator";
	public final static String INITIALCREATOR = ATTR;

	public MetaDataInitialCreator(OdfOfficeMeta meta){
		super(meta, ATTR, meta.getInitialCreator());
		try{
			if(getValue() == null){
				setValue("No Initial Creator");
			}
		}catch(Exception e){}
	}

	public void setValue(String value) throws ParseException, ReadOnlyMetaException {
		getMeta().setInitialCreator(value);
		super.setValue(value);
	}
}
