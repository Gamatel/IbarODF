package ibarodf.core.meta;

import java.text.ParseException;

import org.json.JSONObject;

import ibarodf.core.meta.exception.ReadOnlyMetaException;
import ibarodf.core.meta.exception.UnableToConvertToJsonFormatException;

/**
 * This class is an abstract class that represents a metadata
 */
public abstract class AbstractMetadata {

	private final String attribut;
	private Object value;

	public AbstractMetadata(String attribut, Object value) {
		this.attribut = attribut;
		this.value = value;
	}

	/**
	 * This function retuns a String that correspond to the type of the metadata
	 * @see {@link ibarodf.core.meta to see all types of metadata}
	 * @return returns the type of the metadata
	 */
	public String getAttribut() {
		return attribut;
	}
	/**
	 * This function sets the value of the metadata
	 * @param value The value to set the metadata to.
	 */

	public void setValue(String value) throws ParseException, ReadOnlyMetaException {
		this.value = value;
	}

	/**
	 * This function returns the value of the metadata 
	 * @return The value of the metadata
	 */
	public Object getValue(){
		return value;
	}
	
	/**
	 * It return a String cooresponding to the JSON representation of the metadata.
	 * Otherwise, if the metadata hasn't been converted to json format it returns the String `none`
	 * @return A string representation of the metadata
	 */
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
