package ibarodf.core.file.exception;

public class CannotAddStatisticsException extends CannotAddAllMetadatasException{
    public CannotAddStatisticsException(String fileName){
        super("Cannot Add Statisctics for "+ fileName);
    }    
}
