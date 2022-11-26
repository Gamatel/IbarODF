package ibarodf.core.file;

import java.nio.file.Path;


public class NotOdtFile extends AbstractRegularFile{
    public NotOdtFile(Path path){
        super(path);
        try{
            loadMetaData();
        }catch(Exception e){
            e.getMessage();
        }
    }

}
