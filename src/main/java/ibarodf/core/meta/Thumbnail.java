package ibarodf.core.meta;


import java.nio.file.Path;

import ibarodf.command.CommandTranslator;

public class Thumbnail extends AbstractMetaData {
    public final static String ATTR = "Thumbnail";
    public Thumbnail(Path path){
        super(ATTR, path.toString());
    }
    
    public String getValue() throws Exception{
        StringBuilder pathText = new StringBuilder(); 
        Path path = CommandTranslator.stringToPath(super.getValue()); 
        pathText.append("\""+path+"\"");
        return pathText.toString();
    }


}
