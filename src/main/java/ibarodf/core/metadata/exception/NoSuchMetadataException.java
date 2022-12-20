package ibarodf.core.metadata.exception;

public class NoSuchMetadataException extends Exception {
    public NoSuchMetadataException(Object object, String type){
        super("The file "+ object+ " does not have the metadata about its "+ type);

    }
    
}
