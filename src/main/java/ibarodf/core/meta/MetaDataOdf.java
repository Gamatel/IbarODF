package ibarodf.core.meta;

import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;

public class MetaDataOdf extends MetaDataAbstract {
	private final OdfOfficeMeta meta;

	public MetaDataOdf(OdfOfficeMeta meta, String attribut, String value) {
		super(attribut, value);
		this.meta = meta;
	}

	public OdfOfficeMeta getMeta() {
		return meta;
	}
}
