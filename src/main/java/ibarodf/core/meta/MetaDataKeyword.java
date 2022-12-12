package ibarodf.core.meta;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;

public class MetaDataKeyword extends MetaDataXML{
    public final static String ATTR = "Keyword";
    //Json Key
    public final static String KEYWORDS = "Keywords";

    public MetaDataKeyword(OdfOfficeMeta meta, List<String> value ) throws Exception{
        super(meta, ATTR, value);
    }

    public void addKeyword(String newKeywords){
        String[] keywords = newKeywords.split(",");
        for(String keyword : keywords){
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

    public JSONObject toJson() throws Exception{
        JSONArray keywordArray = new JSONArray();
        List<String> keywords = (List<String>) getValue();
        keywordArray.put(keywords);
        return (new JSONObject()).put(KEYWORDS, keywords);
    }
    




}
