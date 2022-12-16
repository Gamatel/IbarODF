package ibarodf.core.meta;

import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;

public class MetadataXML extends AbstractMetadata {
	private final OdfOfficeMeta meta;

	public MetadataXML(OdfOfficeMeta meta, String attribut, Object value) {
		super(attribut, value);
		this.meta = meta;
	}

	public OdfOfficeMeta getMeta() {
		return meta;
	}

	
}
