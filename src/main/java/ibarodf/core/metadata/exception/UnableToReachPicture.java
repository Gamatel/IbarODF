package ibarodf.core.metadata.exception;

import java.nio.file.Path;

public class UnableToReachPicture extends Exception{
    public UnableToReachPicture(String filePath){
        super("Unallowed to reach the picture "+filePath);
    }
    
    public UnableToReachPicture(Path filePath){
        super("Unallowed to reach the picture : "+filePath.getFileName());
    }
}
