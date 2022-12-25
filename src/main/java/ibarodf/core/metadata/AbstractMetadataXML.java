package ibarodf.core.metadata;

import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;

/**
 * This class is an abstract class that represents a metadata that use OdfOffciceMeta to manager metadata value
 */
public abstract class AbstractMetadataXML extends AbstractMetadataOdf {
	private final OdfOfficeMeta meta;

	public AbstractMetadataXML(OdfOfficeMeta meta, String attribut, Object value) {
		super(attribut, value);
		this.meta = meta;
	}

	public OdfOfficeMeta getMeta() {
		return meta;
	}

	
}
