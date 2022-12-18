package ibarodf.core;

import java.nio.file.Path;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import ibarodf.core.file.AbstractGenericFile;
import ibarodf.core.file.Directory;
import ibarodf.core.file.OdfFile;
import ibarodf.core.file.WrongFile;
import ibarodf.core.meta.MetadataComment;
import ibarodf.core.meta.MetadataCreationDate;
import ibarodf.core.meta.MetadataCreator;
import ibarodf.core.meta.MetadataHyperlink;
import ibarodf.core.meta.MetadataInitialCreator;
import ibarodf.core.meta.MetadataKeyword;
import ibarodf.core.meta.MetadataOdfPictures;
import ibarodf.core.meta.MetadataStats;
import ibarodf.core.meta.MetadataSubject;
import ibarodf.core.meta.MetadataThumbnail;
import ibarodf.core.meta.MetadataTitle;
import ibarodf.core.meta.exception.NoSuchMetadataException;
import ibarodf.core.meta.object.Hyperlink;
import ibarodf.core.meta.object.Picture;

public abstract class IbarOdfResultParser {
    public static String  getFileName(JSONObject abstractGenericFile) {
        return abstractGenericFile.getString(AbstractGenericFile.FILE_NAME);
    }

    public static String getMimeType(JSONObject abstractGenericFile) {
        return abstractGenericFile.getString(AbstractGenericFile.MIME_TYPE);
    }

    public static int getSize(JSONObject abstractGenericFile) {
        return abstractGenericFile.getInt(AbstractGenericFile.SIZE);
    }

    public static Object getPath(JSONObject abstractGenericFile) {
        return abstractGenericFile.get(AbstractGenericFile.PATH);
    }

    public static boolean isDirectory(JSONObject genericFile){
        return getMimeType(genericFile).equals(AbstractGenericFile.TYPE_DIRECTORY);
    }

    public static JSONArray getSubDirectories(JSONObject jsonDirectory) {
        return jsonDirectory.getJSONArray(Directory.SUBDIRECTORIES);
    }

    public static JSONArray getRegularFiles(JSONObject jsonDirectory) {
        return jsonDirectory.getJSONArray(Directory.REGULAR_FILES);
    }

    public static JSONArray getOdfFiles(JSONObject jsonDirectory) {
        return jsonDirectory.getJSONArray(Directory.ODF_FILES);
    }

    public static JSONArray getWrongFiles(JSONObject jsonDirectory) {
        return jsonDirectory.getJSONArray(Directory.WRONG_FILES);
    }

    private static JSONArray getMetadata(JSONObject jsonOdfFile) {
        return jsonOdfFile.getJSONArray(OdfFile.METADATA);
    }

    private static int getMetadataIndex(JSONObject jsonOdfFile, String type) {
        JSONArray haveTheseMetadata = jsonOdfFile.getJSONArray(OdfFile.HAVE_THESE_METADATA);
        int index = 0, indexMax = haveTheseMetadata.length();
        while (index < indexMax && !haveTheseMetadata.get(index).equals(type)) {
            index++;
        }
        return index < indexMax ? index : -1;
    }

    public static List<Object> getCurrentOdfMetadat(JSONObject jsonObject){
        return jsonObject.getJSONArray(OdfFile.HAVE_THESE_METADATA).toList();
    }

    public static JSONObject getMetadataByType(JSONObject jsonOdfFile, String type) throws NoSuchMetadataException {
        int index = getMetadataIndex(jsonOdfFile, type);
        if (index == -1) {
            throw new NoSuchMetadataException(jsonOdfFile, type);
        }
        return getMetadata(jsonOdfFile).getJSONObject(index);
    }

    public static String getTitle(JSONObject odfFile) throws NoSuchMetadataException {
        return getMetadataByType(odfFile, MetadataTitle.TITLE).getString(MetadataTitle.TITLE);
    }

    public static String getCreator(JSONObject odfFile) throws NoSuchMetadataException {
        return getMetadataByType(odfFile, MetadataCreator.CREATOR).getString(MetadataCreator.CREATOR);
    }

    public static String getInitialgetCreator(JSONObject odfFile) throws NoSuchMetadataException {
        return getMetadataByType(odfFile, MetadataInitialCreator.INITIAL_CREATOR)
                .getString(MetadataInitialCreator.INITIAL_CREATOR);
    }

    public static String getSubject(JSONObject odfFile) throws NoSuchMetadataException {
        return getMetadataByType(odfFile, MetadataSubject.SUBJECT).getString(MetadataSubject.SUBJECT);
    }

    public static String getComments(JSONObject odfFile) throws NoSuchMetadataException {
        return getMetadataByType(odfFile, MetadataComment.COMMENTS).getString(MetadataComment.COMMENTS);
    }

    public static String getCreationDate(JSONObject odfFile) throws NoSuchMetadataException {
        return getMetadataByType(odfFile, MetadataCreationDate.CREATION_DATE)
                .getString(MetadataCreationDate.CREATION_DATE);
    }

    public static Path getThumbnail(JSONObject odfFile)throws NoSuchMetadataException{
        return (Path)getMetadataByType(odfFile, MetadataThumbnail.THUMBNAIL).get(MetadataThumbnail.THUMBNAIL);
    }

    public static List<Object> getKeywords(JSONObject odfFile) throws NoSuchMetadataException {
        return getMetadataByType(odfFile, MetadataKeyword.KEYWORDS).getJSONArray(MetadataKeyword.KEYWORDS).toList();
    }

    public static JSONArray getStatistics(JSONObject odfFile) throws NoSuchMetadataException {
        try{
            return getMetadataByType(odfFile, MetadataStats.STATISTICS).getJSONArray(MetadataStats.STATISTICS);
        }catch(Exception e){
            throw new NoSuchMetadataException(odfFile, MetadataStats.STATISTICS);
        }
    }

    public static JSONArray getHyperlink(JSONObject odfFile) throws NoSuchMetadataException{
        return getMetadataByType(odfFile,MetadataHyperlink.HYPERLINKS).getJSONArray(MetadataHyperlink.HYPERLINKS);
    }

    public static String getHyperlinkVisitedStyleName(JSONObject hyperlink){
        return hyperlink.getString(Hyperlink.VISITED_STYLE_NAME);
    }

    public static String getHyperlinkType(JSONObject hyperlink){
        return hyperlink.getString(Hyperlink.TYPE);
    }
    
    public static Object getHyperlinkReference(JSONObject hyperlink){
        return hyperlink.getString(Hyperlink.REFERENCE);
    }
    
    public static String getHyperlinkStyleName(JSONObject hyperlink){
        return hyperlink.getString(Hyperlink.STYLE_NAME);
    }
    

    public static int getNumberOfSubDirectories(JSONObject jsonDirectory){
        return jsonDirectory.getJSONObject(Directory.INFORMATIONS).getInt(Directory.SUBDIRECTORIES);
    }

    public static int getNumberOfRegularFile(JSONObject jsonDirectory){
        return jsonDirectory.getJSONObject(Directory.INFORMATIONS).getInt(Directory.REGULAR_FILES);
    }

    public static int getNumberOfOdfFiles(JSONObject jsonDirectory){
        return jsonDirectory.getJSONObject(Directory.INFORMATIONS).getInt(Directory.ODF_FILES);
    }
    public static int getNumberOfWrongFiles(JSONObject jsonDirectory){
        return jsonDirectory.getJSONObject(Directory.INFORMATIONS).getInt(Directory.WRONG_FILES);
    }

    public static int getTotalNumberOfFiles(JSONObject jsonDirectory){
        return jsonDirectory.getJSONObject(Directory.INFORMATIONS).getInt(Directory.TOTAL_NUMBER_OF_FILES);
    }

    public static JSONArray getPictures(JSONObject jsonOdfFile) throws NoSuchMetadataException{
        try{
            return getMetadataByType(jsonOdfFile,MetadataOdfPictures.PICTURES).getJSONArray(MetadataOdfPictures.PICTURES);
        }catch(Exception e){
            throw new NoSuchMetadataException(jsonOdfFile, MetadataOdfPictures.PICTURES);
        }
    }
        
    public static Path getPicturesPath(JSONObject jsonPicture){
        return (Path)jsonPicture.get(Picture.PATH);
    }

    public static int getPictureSize(JSONObject jsonPicture){
        return jsonPicture.getInt(Picture.SIZE);
    }
    public static int getPictureHeigth(JSONObject jsonPicture){
        return jsonPicture.getInt(Picture.HEIGTH);
    }
    public static int getPictureWidgth(JSONObject jsonPicture){
        return jsonPicture.getInt(Picture.WIDGTH);
    }


    public static boolean isKeyword(Object object){
        return MetadataKeyword.KEYWORDS.equals(object.toString());
    }
    public static boolean isHyperlink(Object object){
        return MetadataHyperlink.HYPERLINKS.equals(object.toString());
    }
    public static boolean isStatistics(Object object){
        return MetadataStats.STATISTICS.equals(object.toString());
    }
    public static boolean isPictures(Object object){
        return MetadataOdfPictures.PICTURES.equals(object.toString());
    }
    public static boolean isThumbnail(Object object){
        return MetadataThumbnail.THUMBNAIL.equals(object.toString());
    }

    public static String getWrongFileErrorMessage(JSONObject wrongFile){
        return wrongFile.getString(WrongFile.ERRORMESSAGE); 
    }



}
