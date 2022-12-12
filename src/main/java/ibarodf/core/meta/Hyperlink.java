package ibarodf.core.meta;

import org.json.JSONObject;

public class Hyperlink {

    public final String reference;
    public final String type;
    public final String styleName;
    public final String visitedStyleName;

    //JSON key

    public static final String REFERENCE = "Reference";
    public static final String TYPE = "Type" ;
    public static final String STYLE_NAME = "Style Name";
    public static final String VISITED_STYLE_NAME = "Visited Style Name";
    
    
    public Hyperlink(String type, String  reference, String  styleName, String  visitedStyleName){
        this. reference =  reference;
        this.type = type;
        this.styleName = styleName;
        this.visitedStyleName = visitedStyleName;
    }

    public JSONObject toJson(){
        JSONObject hyperLinkJson = new JSONObject();
        hyperLinkJson.put(TYPE, type);
        hyperLinkJson.put(REFERENCE, reference);
        hyperLinkJson.put(STYLE_NAME, styleName);
        hyperLinkJson.put(VISITED_STYLE_NAME, visitedStyleName);
        return hyperLinkJson;
    }

    
}
