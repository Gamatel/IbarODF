package ibarodf.core.file.exception;

import java.nio.file.Path;

public class EmptyOdfFileException extends Exception {
    public EmptyOdfFileException(Path pathFile) {
        super("Exception : The file "+pathFile.getFileName()+" wasn't created by LibreOffice.");
    }

}
