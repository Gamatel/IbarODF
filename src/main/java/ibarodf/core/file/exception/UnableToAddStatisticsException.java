package ibarodf.core.file.exception;

public class UnableToAddStatisticsException extends UnableToAddMetadataException{
    public UnableToAddStatisticsException(String fileName){
        super("Cannot Add Statisctics for "+ fileName);
    }    
}
