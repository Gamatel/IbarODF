package ibarodf.core.meta;

import java.text.ParseException;

import org.json.JSONObject;

public abstract class AbstractMetaData {

	private final String attribut;
	private Object value;

	public AbstractMetaData(String attribut, Object value) {
		this.attribut = attribut;
		this.value = value;
	}

	public String getAttribut() {
		return attribut;
	}

	public void setValue(String value) throws ParseException, ReadOnlyMetaException {
		this.value = value;
	}

	public Object getValue() throws Exception {
		return value;
	}

	public String toString() {
		return "MetaDataAbstract : { attribut: '" + attribut + "', value: '" + value + "'}";
	}

	public JSONObject toJson() throws Exception{
		return (new JSONObject()).put(attribut, value);
	}

}
