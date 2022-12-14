package ibarodf.core.file;

import java.nio.file.Path;


/**
 * A regular file is a file that is not a directory
 */
public class RegularFile extends AbstractGenericFile{
	public RegularFile(Path path){
		super(path);
	}
}
