package ibarodf.core.file;

import java.nio.file.Path;


/**
 * A class that represents a file that is not a directory
 */
public class RegularFile extends AbstractGenericFile{
	public RegularFile(Path path){
		super(path);
	}
}
