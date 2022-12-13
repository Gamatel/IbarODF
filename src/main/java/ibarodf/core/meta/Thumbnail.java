package ibarodf.core.meta;


import java.nio.file.Path;
import java.text.ParseException;

import ibarodf.core.meta.exception.ReadOnlyMetaException;


public class Thumbnail extends AbstractMetaDataOdf {
    public final static String ATTR = "Thumbnail";
    public final static String THUMBNAIL = ATTR;
    public Thumbnail(Path path){
        super(ATTR, path);
    }

    public void setValue(String value) throws ParseException, ReadOnlyMetaException {
        throw new ReadOnlyMetaException(ATTR);
    }

}
