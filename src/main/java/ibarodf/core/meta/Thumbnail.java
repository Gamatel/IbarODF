package ibarodf.core.meta;


import java.nio.file.Path;

import ibarodf.core.IbarODFCore;

public class Thumbnail extends AbstractMetaDataOdf {
    public final static String ATTR = "Thumbnail";
    public Thumbnail(Path path){
        super(ATTR, path.toString());
    }
    
    public String getValue() throws Exception{
        StringBuilder pathText = new StringBuilder(); 
        Path path = IbarODFCore.stringToPath(super.getValue()); 
        pathText.append("*"+path+"?");
        return pathText.toString();
    }


}
