package ibarodf.core.file;

import java.nio.file.Path;

public class NotOdfFile extends RegularFile{
    public NotOdfFile(Path path) throws Exception{
        super(path); 
        loadMetaData();
    }    
}
