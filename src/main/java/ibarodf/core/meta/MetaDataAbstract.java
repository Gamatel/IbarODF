package ibarodf.core.meta;

import java.text.ParseException;

public abstract class MetaDataAbstract {

	private final String attribut;
	private String value;

	public MetaDataAbstract(String attribut, String value) {
		this.attribut = attribut;
		this.value = value;
	}

	public String getAttribut() {
		return attribut;
	}

	public void setValue(String value) throws ParseException {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public String toString() {
		return "MetaDataAbstract : { attribut: '" + attribut + "', value: '" + value + "'}";
	}
}
