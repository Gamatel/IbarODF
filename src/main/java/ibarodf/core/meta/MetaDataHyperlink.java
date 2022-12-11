package ibarodf.core.meta; 

public class MetaDataHyperlink extends AbstractMetaDataOdf {
    public final static String ATTR = "Hyperlink";
    public final static String HYPERLINK_TAG = "text:a";
    public final static String REFERENCE_TAG = "xlink:href";
    public final static String TYPE_TAG = "xlink:type";
    public final static String STYLE_NAME_TAG = "text:style-name";
    public final static String VISITED_STYLE_NAME_TAG = "text:visited-style-name";

    public MetaDataHyperlink(String value) throws Exception{
        super(ATTR,value);   
    }


}
