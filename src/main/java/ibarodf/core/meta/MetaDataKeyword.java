package ibarodf.core.meta;

import java.text.ParseException;
import java.util.Iterator;

import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;

public class MetaDataKeyword extends MetaDataXML{
    public final static String ATTR = "Keyword";
    public MetaDataKeyword(OdfOfficeMeta meta) throws ParseException{
        super(meta, ATTR, null);
        setValue(getKeywords());
    }

    public String getKeywords(){
        if(getMeta().getKeywords() ==  null){
            return "No Keyword";
        }
        Iterator<String> keywordsIt = getMeta().getKeywords().iterator();
        StringBuffer keywords = new StringBuffer();
        while(keywordsIt.hasNext()){
            keywords.append(keywordsIt.next());
            if(keywordsIt.hasNext()){
                keywords.append(", ");
            }
        }
        return keywords.toString();
    }



}
