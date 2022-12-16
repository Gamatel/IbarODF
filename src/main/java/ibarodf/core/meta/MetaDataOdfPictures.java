package ibarodf.core.meta;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import ibarodf.core.meta.exception.UnableToConvertToJsonFormatException;
import ibarodf.core.meta.object.Picture;


public class MetadataOdfPictures extends AbstractMetadataOdf {
    public final static String ATTR = "Pictures";
    public final static String PICTURES = ATTR;

    public MetadataOdfPictures(ArrayList<Picture> value){
        super(ATTR, value);
    }

    @Override
    public JSONObject toJson() throws UnableToConvertToJsonFormatException{
        ArrayList<Picture> picturesList = (ArrayList<Picture>) getValue();
            JSONArray picturesListJson = new JSONArray();
            for(Picture picture: picturesList){
                picturesListJson.put(picture.toJson());
            }
        return (new JSONObject()).put(PICTURES, picturesListJson);
    }

}
