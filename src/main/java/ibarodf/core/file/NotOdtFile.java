package ibarodf.core.file;

import java.io.File;
import java.nio.file.Path;

import ibarodf.command.CommandTranslator;
import ibarodf.core.meta.MetaDataOfNotOdtFile;
import ibarodf.core.meta.MetaDataTitle;

public class NotOdtFile extends AbstractRegularFile{

    public NotOdtFile(Path path){
        super(path);
        try{
            loadMetaData();
        }catch(Exception e){
            e.getMessage();
        }
    }


    public void loadMetaData()  throws Exception{
        addMetaData(MetaDataTitle.ATTR, getTitle());
        addMetaData("MIME_Type", getMIMEType());
        
    }

    public MetaDataOfNotOdtFile getTitle(){
        File file = new File(getPath().toString());
        return new MetaDataOfNotOdtFile(MetaDataTitle.ATTR, file.getName());
    }

    public MetaDataOfNotOdtFile getMIMEType() throws Exception{
        return new MetaDataOfNotOdtFile("MIME_type", CommandTranslator.fileType(getPath().toString()));
    }






    
}
