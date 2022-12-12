package ibarodf.core.meta;

import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;

public class MetaDataXML extends AbstractMetaData {
	private final OdfOfficeMeta meta;

	public MetaDataXML(OdfOfficeMeta meta, String attribut, Object value) {
		super(attribut, value);
		this.meta = meta;
	}

	public OdfOfficeMeta getMeta() {
		return meta;
	}

	
}
