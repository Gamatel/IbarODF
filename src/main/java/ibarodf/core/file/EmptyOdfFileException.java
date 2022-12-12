package ibarodf.core.file;

import java.nio.file.Path;

public class EmptyOdfFileException extends Exception {
    public EmptyOdfFileException(Path pathFile) {
        super("The file "+pathFile.getFileName()+" wasn't created by LibreOffice.");
    }

}
