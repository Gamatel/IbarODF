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

/**
 * This class is used to parse the JSONObject returned by the IbarOdfResultParser
 */
public abstract class IbarOdfResultParser {
    /**
     * This function returns the file name of the file that is passed in as a parameter
     * @param abstractGenericFile This is the JSONObject that contains the file information.
     * @return The file name of the file.
     */
    public static String  getFileName(JSONObject abstractGenericFile) {
        return abstractGenericFile.getString(AbstractGenericFile.FILE_NAME);
    }

    /**
     * This function returns the mime type of a file
     * @param abstractGenericFile The JSONObject that represents the file.
     * @return A string
     */
    public static String getMimeType(JSONObject abstractGenericFile) {
        return abstractGenericFile.getString(AbstractGenericFile.MIME_TYPE);
    }

    /**
     * It returns the size of a file
     * @param abstractGenericFile The JSONObject that contains the file information.
     * @return The size of the file.
     */
    public static int getSize(JSONObject abstractGenericFile) {
        return abstractGenericFile.getInt(AbstractGenericFile.SIZE);
    }

    /**
     * Retuns the Path of the parsed file
     * @param abstractGenericFile The JSONObject that contains the file information.
     * @return The value of the key "path" in the JSONObject abstractGenericFile.
     */
    public static Object getPath(JSONObject abstractGenericFile) {
        return abstractGenericFile.get(AbstractGenericFile.PATH);
    }

   /**
    * It checks if the parsed file is a directory
    * @param genericFile The file object that you want to check.
    * @return A boolean value.
    */
    public static boolean isDirectory(JSONObject genericFile){
        return getMimeType(genericFile).equals(AbstractGenericFile.TYPE_DIRECTORY);
    }

    /**
     * This function returns the subdirectories of the parsed directory
     * 
     * @param jsonDirectory The JSONObject that represents the directory.
     * @return A JSONArray of subdirectories.
     */
    public static JSONArray getSubDirectories(JSONObject jsonDirectory) {
        return jsonDirectory.getJSONArray(Directory.SUBDIRECTORIES);
    }

    /**
     * This function returns the JSONArray of regular files in the given directory.
     * @param jsonDirectory The JSONObject that represents the directory.
     * @return A JSONArray of JSONObjects.
     */
    public static JSONArray getRegularFiles(JSONObject jsonDirectory) {
        return jsonDirectory.getJSONArray(Directory.REGULAR_FILES);
    }

    /**
     * This function returns the JSONArray of ODF files in the given directory
     * @param jsonDirectory The JSONObject that represents the directory.
     * @return A JSONArray of the regular file.
     */
    public static JSONArray getOdfFiles(JSONObject jsonDirectory) {
        return jsonDirectory.getJSONArray(Directory.ODF_FILES);
    }
    /**
     * It returns the wrong files of a directory
     * @param jsonDirectory The JSONObject that contains the directory information.
     * @return A JSONArray of the wrong files.
     */
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

    /**
     * This function takes a JSONObject as an argument and returns a list of all the metadata that the
     * ODF file has.
     * @param jsonObject The ODF file.
     * @return A list of objects.
     */
    public static List<Object> getCurrentOdfMetadat(JSONObject jsonObject){
        return jsonObject.getJSONArray(OdfFile.HAVE_THESE_METADATA).toList();
    }

    /**
     * It returns the metadata of a given type from a JSON object representing an ODF file
     * 
     * @param jsonOdfFile the JSONObject that represents the ODF file
     * @param type the type of metadata you want to get
     * @return A JSONObject
     */
    public static JSONObject getMetadataByType(JSONObject jsonOdfFile, String type) throws NoSuchMetadataException {
        int index = getMetadataIndex(jsonOdfFile, type);
        if (index == -1) {
            throw new NoSuchMetadataException(jsonOdfFile, type);
        }
        return getMetadata(jsonOdfFile).getJSONObject(index);
    }

    /**
     * It returns the title of the document
     * 
     * @param odfFile The JSONObject that contains the metadata
     * @return The title of the document.
     */
    public static String getTitle(JSONObject odfFile) throws NoSuchMetadataException {
        return getMetadataByType(odfFile, MetadataTitle.TITLE).getString(MetadataTitle.TITLE);
    }

    /**
     * > This function returns the creator of the ODF file
     * 
     * @param odfFile The JSONObject that contains the metadata
     * @return The creator of the document.
     */
    public static String getCreator(JSONObject odfFile) throws NoSuchMetadataException {
        return getMetadataByType(odfFile, MetadataCreator.CREATOR).getString(MetadataCreator.CREATOR);
    }

    /**
     * This function gets the initial creator of the document
     * 
     * @param odfFile The JSONObject that contains the metadata
     * @return The initial creator of the document.
     */
    public static String getInitialgetCreator(JSONObject odfFile) throws NoSuchMetadataException {
        return getMetadataByType(odfFile, MetadataInitialCreator.INITIAL_CREATOR)
                .getString(MetadataInitialCreator.INITIAL_CREATOR);
    }

    /**
     * It takes a JSONObject as an argument, and returns a String
     * 
     * @param odfFile the JSONObject that contains the metadata
     * @return The subject of the document.
     */
    public static String getSubject(JSONObject odfFile) throws NoSuchMetadataException {
        return getMetadataByType(odfFile, MetadataSubject.SUBJECT).getString(MetadataSubject.SUBJECT);
    }

    /**
     * It returns the comments of the ODF file
     * 
     * @param odfFile The JSONObject that contains the ODF file's metadata.
     * @return The comments of the document.
     */
    public static String getComments(JSONObject odfFile) throws NoSuchMetadataException {
        return getMetadataByType(odfFile, MetadataComment.COMMENTS).getString(MetadataComment.COMMENTS);
    }

    /**
     * > This function returns the creation date of the ODF file
     * 
     * @param odfFile The JSONObject that contains the metadata
     * @return The creation date of the document.
     */
    public static String getCreationDate(JSONObject odfFile) throws NoSuchMetadataException {
        return getMetadataByType(odfFile, MetadataCreationDate.CREATION_DATE)
                .getString(MetadataCreationDate.CREATION_DATE);
    }

    /**
     * > This function returns the thumbnail of the ODF file
     * 
     * @param odfFile The JSONObject that contains the metadata
     * @return A Path object.
     */
    public static Path getThumbnail(JSONObject odfFile)throws NoSuchMetadataException{
        return (Path)getMetadataByType(odfFile, MetadataThumbnail.THUMBNAIL).get(MetadataThumbnail.THUMBNAIL);
    }

    /**
     * > This function returns a list of keywords from the ODF file
     * 
     * @param odfFile The JSONObject that contains the metadata
     * @return A list of keywords.
     */
    public static List<Object> getKeywords(JSONObject odfFile) throws NoSuchMetadataException {
        return getMetadataByType(odfFile, MetadataKeyword.KEYWORDS).getJSONArray(MetadataKeyword.KEYWORDS).toList();
    }

    /**
     * "Get the statistics metadata from the ODF file."
     * 
     * The function is a static method of the `MetadataStats` class. It takes a single parameter, an ODF
     * file, and returns a JSON array of statistics
     * 
     * @param odfFile The JSONObject that represents the ODF file.
     * @return A JSONArray of statistics.
     */
    public static JSONArray getStatistics(JSONObject odfFile) throws NoSuchMetadataException {
        try{
            return getMetadataByType(odfFile, MetadataStats.STATISTICS).getJSONArray(MetadataStats.STATISTICS);
        }catch(Exception e){
            throw new NoSuchMetadataException(odfFile, MetadataStats.STATISTICS);
        }
    }

    /**
     * This function returns a JSONArray of all the hyperlinks in the document
     * 
     * @param odfFile the JSONObject that contains the metadata
     * @return A JSONArray of JSONObjects.
     */
    public static JSONArray getHyperlink(JSONObject odfFile) throws NoSuchMetadataException{
        return getMetadataByType(odfFile,MetadataHyperlink.HYPERLINKS).getJSONArray(MetadataHyperlink.HYPERLINKS);
    }

    /**
     * Returns the style name of the visited hyperlink
     * 
     * @param hyperlink The hyperlink object.
     * @return The style name of the hyperlink when it has been visited.
     */
    public static String getHyperlinkVisitedStyleName(JSONObject hyperlink){
    
        return hyperlink.getString(Hyperlink.VISITED_STYLE_NAME);
    }

    /**
     * This function returns the type of the hyperlink
     * 
     * @param hyperlink The hyperlink object.
     * @return The type of hyperlink.
     */
    public static String getHyperlinkType(JSONObject hyperlink){
        return hyperlink.getString(Hyperlink.TYPE);
    }
    
    /**
     * This function takes a JSONObject and returns the value of the REFERENCE key
     * 
     * @param hyperlink The hyperlink object
     * @return A string
     */
    public static Object getHyperlinkReference(JSONObject hyperlink){
        return hyperlink.getString(Hyperlink.REFERENCE);
    }
    
    /**
     * This function returns the style name of the hyperlink
     * 
     * @param hyperlink The JSONObject that represents the hyperlink.
     * @return The style name of the hyperlink.
     */
    public static String getHyperlinkStyleName(JSONObject hyperlink){
        return hyperlink.getString(Hyperlink.STYLE_NAME);
    }
    

    /**
     * This function returns the number of subdirectories of a directory
     * 
     * @param jsonDirectory The JSONObject of the directory you want to get the number of
     * subdirectories from.
     * @return The number of subdirectories in the directory.
     */
    public static int getNumberOfSubDirectories(JSONObject jsonDirectory){
        return jsonDirectory.getJSONObject(Directory.INFORMATIONS).getInt(Directory.SUBDIRECTORIES);
    }

    /**
     * This function returns the number of regular files in a directory
     * 
     * @param jsonDirectory the JSONObject that represents the directory
     * @return The number of regular files in the directory.
     */
    public static int getNumberOfRegularFile(JSONObject jsonDirectory){
        return jsonDirectory.getJSONObject(Directory.INFORMATIONS).getInt(Directory.REGULAR_FILES);
    }

    /**
     * This function returns the number of ODF files in a directory
     * 
     * @param jsonDirectory the JSONObject that contains the directory informations
     * @return The number of ODF files in the directory.
     */
    public static int getNumberOfOdfFiles(JSONObject jsonDirectory){
        return jsonDirectory.getJSONObject(Directory.INFORMATIONS).getInt(Directory.ODF_FILES);
    }
    /**
     * This function returns the number of wrong files in a directory
     * 
     * @param jsonDirectory The JSONObject that contains the directory informations
     * @return The number of wrong files in the directory.
     */
    public static int getNumberOfWrongFiles(JSONObject jsonDirectory){
        return jsonDirectory.getJSONObject(Directory.INFORMATIONS).getInt(Directory.WRONG_FILES);
    }

    /**
     * This function returns the total number of files in a directory
     * 
     * @param jsonDirectory The JSONObject that contains the directory informations.
     * @return The total number of files in the directory.
     */
    public static int getTotalNumberOfFiles(JSONObject jsonDirectory){
        return jsonDirectory.getJSONObject(Directory.INFORMATIONS).getInt(Directory.TOTAL_NUMBER_OF_FILES);
    }

    /**
     * It returns a JSONArray of pictures from a JSONObject representing an ODF file
     * 
     * @param jsonOdfFile The JSONObject that contains the metadata.
     * @return A JSONArray of pictures.
     */
    public static JSONArray getPictures(JSONObject jsonOdfFile) throws NoSuchMetadataException{
        try{
            return getMetadataByType(jsonOdfFile,MetadataOdfPictures.PICTURES).getJSONArray(MetadataOdfPictures.PICTURES);
        }catch(Exception e){
            throw new NoSuchMetadataException(jsonOdfFile, MetadataOdfPictures.PICTURES);
        }
    }
        
    /**
     * This function takes a JSONObject and returns a Path
     * 
     * @param jsonPicture The JSONObject that contains the picture information.
     * @return A Path object.
     */
    public static Path getPicturesPath(JSONObject jsonPicture){
        return (Path)jsonPicture.get(Picture.PATH);
    }

    /**
     * This function takes a JSONObject and returns the size of the picture
     * 
     * @param jsonPicture The JSONObject that contains the picture information.
     * @return The size of the picture.
     */
    public static int getPictureSize(JSONObject jsonPicture){
        return jsonPicture.getInt(Picture.SIZE);
    }
    /**
     * This function returns the height of a picture
     * 
     * @param jsonPicture The JSONObject that contains the picture information.
     * @return The height of the picture.
     */
    public static int getPictureHeigth(JSONObject jsonPicture){
        return jsonPicture.getInt(Picture.HEIGTH);
    }
    /**
     * This function returns the width of a picture
     * 
     * @param jsonPicture The JSONObject that contains the picture information.
     * @return The width of the picture.
     */
    public static int getPictureWidgth(JSONObject jsonPicture){
        return jsonPicture.getInt(Picture.WIDGTH);
    }


    /**
     * It checks if the object is a keyword
     * 
     * @param object The object to check.
     * @return A boolean
     */
    public static boolean isKeywordKey(Object object){
        return MetadataKeyword.KEYWORDS.equals(object.toString());
    }
   
    /**
     * If the object is the key for hyperlink, return true
     * 
     * @param object The object to be checked.
     * @return A boolean
     */
    public static boolean isHyperlinkKey(Object object){
        return MetadataHyperlink.HYPERLINKS.equals(object.toString());
    }
    /**
     * > This function checks if the object is the key for statistics object
     * 
     * @param object The object to be tested.
     * @return A boolean
     */
    public static boolean isStatisticsKey(Object object){
        return MetadataStats.STATISTICS.equals(object.toString());
    }
    /**
     * > This function checks if the object is the key for picture
     * 
     * @param object The object to be tested.
     * @return A boolean
     */
    public static boolean isPicturesKey(Object object){
        return MetadataOdfPictures.PICTURES.equals(object.toString());
    }
    /**
     * If the object is the key for the thumbnail, return true
     * 
     * @param object The object to be checked.
     * @return A boolean
     */
    public static boolean isThumbnailKey(Object object){
        return MetadataThumbnail.THUMBNAIL.equals(object.toString());
    }

    /**
     * This function returns the error message of a wrong file
     * 
     * @param wrongFile The JSONObject that contains the wrong file information.
     * @return A string
     */
    public static String getWrongFileErrorMessage(JSONObject wrongFile){
        return wrongFile.getString(WrongFile.ERRORMESSAGE); 
    }



}
