package ibarodf.core.file;

import java.nio.file.Path;


public abstract class AbstractGenericFile {
    private final Path path;

    public AbstractGenericFile(Path path){
        this.path = path;
    }

    public Path getPath(){
        return path;
    }

    abstract StringBuilder displayMetaData() throws Exception;
    
}
