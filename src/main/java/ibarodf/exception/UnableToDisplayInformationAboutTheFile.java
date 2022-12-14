package ibarodf.exception;

import java.nio.file.Path;

public class UnableToDisplayInformationAboutTheFile extends Exception {
    public UnableToDisplayInformationAboutTheFile(Path path){
        super("Cannot display the metadata about "+ path.getFileName());
    }

    public UnableToDisplayInformationAboutTheFile(String fileName){
        super("Cannot display the metadata about "+ fileName);
    }
}
