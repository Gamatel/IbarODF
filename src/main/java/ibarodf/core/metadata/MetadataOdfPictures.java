package ibarodf.core.metadata;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import ibarodf.core.metadata.exception.UnableToConvertToJsonFormatException;
import ibarodf.core.metadata.object.Picture;


public class MetadataOdfPictures extends AbstractMetadataOdf {
    public final static String ATTR = "Pictures";
    public final static String PICTURES = ATTR;

    public MetadataOdfPictures(ArrayList<Picture> value){
        super(ATTR, value);
    }

    @Override
    public JSONObject toJson() throws UnableToConvertToJsonFormatException{
        ArrayList<Picture> picturesList = (ArrayList<Picture>) getValue();
        if(picturesList.size()!=0){
            JSONArray picturesListJson = new JSONArray();
            for(Picture picture: picturesList){
                picturesListJson.put(picture.toJson());
            }
            return (new JSONObject()).put(PICTURES, picturesListJson);
        }
        return new JSONObject();
    }

}
