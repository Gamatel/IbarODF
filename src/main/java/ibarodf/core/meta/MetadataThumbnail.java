package ibarodf.core.meta;


import java.nio.file.Path;
import java.text.ParseException;

import ibarodf.core.meta.exception.ReadOnlyMetaException;


public class MetadataThumbnail extends AbstractMetadataOdf {
    public final static String ATTR = "Thumbnail";
    public final static String THUMBNAIL = ATTR;
    public MetadataThumbnail(Path path){
        super(ATTR, path);
    }

    @Override
    public void setValue(String value) throws ParseException, ReadOnlyMetaException {
        throw new ReadOnlyMetaException(ATTR);
    }

}
