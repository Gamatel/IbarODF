package ibarodf.core.meta;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class MetaDataHyperlink extends AbstractMetaDataOdf {
    public final static String ATTR = "Hyperlinks";
    public final static String HYPERLINK_TAG = "text:a";
    public final static String REFERENCE_TAG = "xlink:href";
    public final static String TYPE_TAG = "xlink:type";
    public final static String STYLE_NAME_TAG = "text:style-name";
    public final static String VISITED_STYLE_NAME_TAG = "text:visited-style-name";

    //Json Key
    public final static String HYPERLINKS = ATTR;


    public MetaDataHyperlink(ArrayList<Hyperlink> value) throws Exception{
        super(ATTR,value);   
    }

    public JSONObject toJson() throws Exception{
        ArrayList<Hyperlink> hyperlinkArray = (ArrayList<Hyperlink>)getValue();
        JSONArray hyperlinkJson = new JSONArray();
        for(Hyperlink hyperlink : hyperlinkArray ){
            hyperlinkJson.put(hyperlink.toJson());
        }
        return  (new JSONObject()).put(HYPERLINKS, hyperlinkJson);
    }


}
