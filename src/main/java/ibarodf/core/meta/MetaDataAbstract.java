package ibarodf.core.meta;

public abstract class MetaDataAbstract {

	private String attribut;
	private String value;

	public MetaDataAbstract(String attribut, String value) {
		this.attribut = attribut;
		this.value = value;
	}

	public void setAttribut(String attribut) {
		this.attribut = attribut;
	}

	public String getAttribut() {
		return attribut;
	}

	public void setValue(String value) throws LockMetaDataException {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public String toString() {
		return "MetaDataAbstract : { attribut: '" + attribut + "', value: '" + value + "'}";
	}
}
