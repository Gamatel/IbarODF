package ibarodf.core.meta;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;

import ibarodf.core.meta.exception.UnableToConvertToJsonFormatException;

public class MetadataKeyword extends MetadataXML{
    public final static String ATTR = "Keyword";
    //Json Key
    public final static String KEYWORDS = "Keywords";

    public MetadataKeyword(OdfOfficeMeta meta, List<String> value ) throws Exception{
        super(meta, ATTR, value);
    }

    public void addKeyword(String newKeywords){
        String[] keywords = newKeywords.split(",");
        addKeyword(keywords);
    }

    /**
     * This function takes an array of keywords and adds all of them to the odf File 
     * @param newKeyword The new keyword to add to the page.
     */
    public void addKeyword(String[] newKeyword){
        for(String keyword : newKeyword){
            getMeta().addKeyword(keyword);
        }
    }

    /**
     * This function removes all of the Odf file keywords
     */
    public void deleteKeywords(){
		if(!(getMeta().getKeywords() == null)){
			List<String> keyList = getMeta().getKeywords();
			keyList.removeAll(keyList);
			getMeta().setKeywords(keyList);
		}
    }

    @Override
    public void setValue(String newKeywords){
        deleteKeywords();
        addKeyword(newKeywords);
    }

    @Override
    public JSONObject toJson() throws UnableToConvertToJsonFormatException{
        try {
            JSONArray keywordArray = new JSONArray();
            List<String> keywords = (List<String>)getValue();
            keywordArray.put(keywords);
            return (new JSONObject()).put(KEYWORDS, keywords);
        }catch(ClassCastException e){
            System.err.println(e.getLocalizedMessage());
            throw new UnableToConvertToJsonFormatException(ATTR);
        }
    }
    




}
