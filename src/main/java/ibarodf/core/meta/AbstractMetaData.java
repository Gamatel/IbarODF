package ibarodf.core.meta;

import java.text.ParseException;

import org.json.JSONObject;

import ibarodf.core.meta.exception.ReadOnlyMetaException;
import ibarodf.core.meta.exception.UnableToConvertToJsonFormatException;

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

	public Object getValue(){
		return value;
	}
	
    public String toString(){   
        try{
			return toJson().toString();
		}catch(UnableToConvertToJsonFormatException e){   
			return "none";
		}
	}


	public JSONObject toJson() throws UnableToConvertToJsonFormatException{
		return (new JSONObject()).put(attribut, value);
	}

}
