package ibarodf.core.metadata;

import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;

public abstract class AbstractMetadataXML extends AbstractMetadata {
	private final OdfOfficeMeta meta;

	public AbstractMetadataXML(OdfOfficeMeta meta, String attribut, Object value) {
		super(attribut, value);
		this.meta = meta;
	}

	public OdfOfficeMeta getMeta() {
		return meta;
	}

	
}
