package ibarodf.core.meta;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;

import ibarodf.core.meta.exception.UnableToConvertToJsonFormatException;

public class MetaDataKeyword extends MetaDataXML{
    public final static String ATTR = "Keyword";
    //Json Key
    public final static String KEYWORDS = "Keywords";

    public MetaDataKeyword(OdfOfficeMeta meta, List<String> value ) throws Exception{
        super(meta, ATTR, value);
    }

    public void addKeyword(String newKeywords){
        String[] keywords = newKeywords.split(",");
        addKeyword(keywords);
        
    }

    public void addKeyword(String[] newKeyword){
        for(String keyword : newKeyword){
            getMeta().addKeyword(keyword);
        }
    }

    public void deleteKeywords(){
		if(!(getMeta().getKeywords() == null)){
			List<String> keyList = getMeta().getKeywords();
			keyList.removeAll(keyList);
			getMeta().setKeywords(keyList);
		}
    }

    public void setValue(String newKeywords){
        deleteKeywords();
        addKeyword(newKeywords);
    }

    public JSONObject toJson() throws UnableToConvertToJsonFormatException{
        try {
            JSONArray keywordArray = new JSONArray();
            List<String> keywords = (List<String>) getValue();
            keywordArray.put(keywords);
            return (new JSONObject()).put(KEYWORDS, keywords);
        }catch(Exception e){
            throw new UnableToConvertToJsonFormatException(ATTR);
        }
    }
    




}
