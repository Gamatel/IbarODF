package ibarodf.core.meta;

import java.util.Iterator;
import java.util.List;

import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;

public class MetaDataKeyword extends MetaDataXML{
    public final static String ATTR = "Keyword";
    public MetaDataKeyword(OdfOfficeMeta meta) throws Exception{
        super(meta, ATTR, null);
        setValue(getValue());
    }

    public String getValue(){
        if(getMeta().getKeywords() ==  null){
            return "No Keyword";
        }
        Iterator<String> keywordsIt = getMeta().getKeywords().iterator();
        StringBuffer keywords = new StringBuffer();
        while(keywordsIt.hasNext()){
            keywords.append(keywordsIt.next());
            if(keywordsIt.hasNext()){
                keywords.append(",");
            }
        }
        return keywords.toString();
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

    




}
