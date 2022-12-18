package ibarodf.core.meta;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import ibarodf.core.meta.exception.UnableToConvertToJsonFormatException;
import ibarodf.core.meta.object.Hyperlink;

public class MetadataHyperlink extends AbstractMetadataOdf {
    public final static String ATTR = "Hyperlinks";
    public final static String HYPERLINK_TAG = "text:a";
    public final static String REFERENCE_TAG = "xlink:href";
    public final static String TYPE_TAG = "xlink:type";
    public final static String STYLE_NAME_TAG = "text:style-name";
    public final static String VISITED_STYLE_NAME_TAG = "text:visited-style-name";

    //Json Key
    public final static String HYPERLINKS = ATTR;

    public MetadataHyperlink(ArrayList<Hyperlink> value) throws Exception{
        super(ATTR,value);   
    }

    @Override
    public JSONObject toJson() throws UnableToConvertToJsonFormatException{
        try {
            ArrayList<Hyperlink> hyperlinkArray = (ArrayList<Hyperlink>)getValue();
            if(hyperlinkArray.size()!=0){
                JSONArray hyperlinkJson = new JSONArray();
                for(Hyperlink hyperlink : hyperlinkArray ){
                    hyperlinkJson.put(hyperlink.toJson());
                }
                return new JSONObject().put(HYPERLINKS, hyperlinkJson);
            }
            return new JSONObject();
        }catch(Exception e){
            throw new UnableToConvertToJsonFormatException(ATTR);
        }
    }


}
