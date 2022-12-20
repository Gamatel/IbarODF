package ibarodf.command;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.json.JsonException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ibarodf.core.IbarOdfResultParser;
import ibarodf.core.file.Directory;
import ibarodf.core.file.RegularFile;
import ibarodf.core.file.WrongFile;
import ibarodf.core.metadata.MetadataHyperlink;
import ibarodf.core.metadata.MetadataOdfPictures;
import ibarodf.core.metadata.MetadataStats;
import ibarodf.core.metadata.MetadataThumbnail;
import ibarodf.core.metadata.exception.NoSuchMetadataException;
import ibarodf.core.metadata.object.Picture;

public abstract class PrettyResult {
    public static final int DEFAULT_LINE_SIZE_FOR_PRETTY_PRINT = 100;
    public static final String DEFAULT_LINE_SYMBOLE_FOR_PRETTY_PRINT = "-";
    public static final String CLOSED_DIR = "Sub Directory";

    private static String properTabulation(int numberOfTab) {
        StringBuilder tabulation = new StringBuilder();
        while (numberOfTab > 0) {
            tabulation.append("\t");
            numberOfTab--;
        }
        return tabulation.toString();
    }

    /**
     * It prints a line of a given size and depth
     * 
     * @param ligneSymbole the symbol you want to use to draw the line
     * @param lineSize     the number of times the symbol will be printed
     * @param depth        the depth of the current node in the tree
     */
    public static void ligne(String ligneSymbole, int lineSize, int depth) {
        StringBuffer ligneString = new StringBuffer(properTabulation(depth));
        for (int time = 0; time < lineSize; time++) {
            ligneString.append(ligneSymbole);
        }
        System.out.println(ligneString);
    }

    public static void ligne(int depth) {
        ligne(PrettyResult.DEFAULT_LINE_SYMBOLE_FOR_PRETTY_PRINT, PrettyResult.DEFAULT_LINE_SIZE_FOR_PRETTY_PRINT,
                depth);
    }

    public static void ligne() {
        ligne(0);
    }

    /**
     * It takes a JSONObject representing a directory, and an integer representing
     * the depth of the
     * directory in the file system, and prints out the directory's contents
     *
     * @param directory the JSONObject to display
     * @param depth     the depth of the directory in the tree
     * @throws NoSuchMetadataException
     * @throws JSONException
     */
    public static void prettyDirectory(JSONObject directory, int depth) throws JSONException, NoSuchMetadataException {
        try {
            ligne(depth);
            print(IbarOdfResultParser.getFileName(directory), depth);
            ligne(depth);
            displayRegularFiles(IbarOdfResultParser.getRegularFiles(directory), depth + 1);
            displayOdfFiles(IbarOdfResultParser.getOdfFiles(directory), depth + 1);
            displaySubDirectories(directory, depth + 1);
            displayWrongFiles(IbarOdfResultParser.getWrongFiles(directory), depth + 1);
            prettyEndDirectory(directory, depth); 
        } catch (JsonException e) {
        }
    }



    private static void prettyEndDirectory(JSONObject jsonDirectory, int depth){
        ligne(depth);
        prettyPrint("In " +  IbarOdfResultParser.getFileName(jsonDirectory), depth);
        prettyInformations(jsonDirectory, depth);
        ligne(depth);
    } 

    /**
     * It takes a JSONObject representing a directory, and an integer representing
     * the depth of the
     * directory in the file system, and it prints out all of the directory's
     * regular file
     * 
     * @param jsonDirectory The JSONObject that represents the directory containing
     *                      the regular files.
     * @param depth         the depth of the directory in the tree
     */
    private static void displayRegularFiles(JSONArray jsonRegularFiles, int depth) {
        JSONObject currentJsonObject;
        for (int index = 0, indexMax = jsonRegularFiles.length(); index < indexMax; index++) {
            currentJsonObject = jsonRegularFiles.getJSONObject(index);
            if (IbarOdfResultParser.isDirectory(currentJsonObject)) {
                prettyClosedDirectory(currentJsonObject, depth);
            } else {
                prettyFile(currentJsonObject, depth);
                System.out.println("\n");
            }
        }
    }

    /**
     * It takes a JSONObject representing a directory, and an integer representing
     * the depth of the
     * directory in the file system, and it prints out all of the directory's odf
     * files
     * 
     * @param jsonDirectory The JSONObject that represents the directory containing
     *                      the odf files
     * @param depth         the depth of the directory in the tree
     * @throws NoSuchMetadataException
     * @throws JSONException
     */
    private static void displayOdfFiles(JSONArray jsonOdfFiles, int depth) throws JSONException, NoSuchMetadataException {
        for (int index = 0, indexMax = jsonOdfFiles.length(); index < indexMax; index++) {
            prettyOdfFile(jsonOdfFiles.getJSONObject(index), depth);
            System.out.println("\n");
        }
    }

    /**
     * It displays the subdirectories of the given directory
     * 
     * @param jsonDirectory The JSONObject that represents the directory you want to
     *                      display.
     * @param depth         The depth of the directory.
     * @throws NoSuchMetadataException
     * @throws JSONException
     */
    private static void displaySubDirectories(JSONObject jsonDirectory, int depth) throws JSONException, NoSuchMetadataException {
        JSONArray jsonDirectories = jsonDirectory.getJSONArray(Directory.SUBDIRECTORIES);
        for (int index = 0, indexMax = jsonDirectories.length(); index < indexMax; index++) {
            prettyDirectory(jsonDirectories.getJSONObject(index), depth+1);
        }
    }

    private static void displayWrongFiles(JSONArray jsonWrongFiles, int depth) {
        int numberOfWrongFiles = jsonWrongFiles.length();
        if (numberOfWrongFiles > 0) {
            ligne("/!\\", PrettyResult.DEFAULT_LINE_SIZE_FOR_PRETTY_PRINT / 3, depth);
            for (int index = 0; index < numberOfWrongFiles; index++) {
                prettyWrongFile(jsonWrongFiles.getJSONObject(index), depth + 1);
            }
            ligne("/!\\", PrettyResult.DEFAULT_LINE_SIZE_FOR_PRETTY_PRINT / 3, depth);
        }
    }

    public static void prettyDirectory(JSONObject directory) throws JSONException, NoSuchMetadataException {
        prettyDirectory(directory, 0);
    }

    /**
     * It takes a JSONObject and an integer as parameters, and prints the name, mime
     * type, and size of the
     * file
     * 
     * @param file  The JSONObject that represents the file.
     * @param depth the depth of the file in the tree
     */
    private static void prettyFile(JSONObject file, int depth) {
        prettyPrint(RegularFile.FILE_NAME, IbarOdfResultParser.getFileName(file), depth);
        prettyPrint(RegularFile.MIME_TYPE, IbarOdfResultParser.getMimeType(file), depth);
        prettyPrint(RegularFile.SIZE, IbarOdfResultParser.getSize(file), depth);
    }

    public static void prettyFile(JSONObject file) {
        prettyFile(file, 0);
    }

    /**
     * This function prints the contents of a JSONObject that represents a wrong
     * file
     * 
     * @param jsonWrongFile the JSONObject that represents the wrong file
     * @param depth         depth the depth of the file in the tree
     */
    private static void prettyWrongFile(JSONObject jsonWrongFile, int depth) {
        prettyPrint(Directory.WRONG_FILES, depth);
        if(IbarOdfResultParser.isDirectory(jsonWrongFile)){
            prettyClosedDirectory(jsonWrongFile, depth+1);
        }else{
            prettyFile(jsonWrongFile, depth+1);
        }
        prettyPrint(WrongFile.ERRORMESSAGE, IbarOdfResultParser.getWrongFileErrorMessage(jsonWrongFile), depth + 1);

    }

    /**
     * This function prints the contents of a JSONObject that represents an ODF file
     * 
     * @param odfFile The JSONObject that represents the ODF file.
     * @param depth   the depth of the JSONObject in the JSONObject tree.
     * @throws NoSuchMetadataException
     */

    private static void prettyOdfFile(JSONObject odfFile, int depth) throws NoSuchMetadataException {
        try {
            prettyFile(odfFile, depth);
            prettyMetadata(odfFile, depth);
        } catch (JsonException e) {
        }
    }

    public static void prettyOdfFile(JSONObject odfFile) throws NoSuchMetadataException {
        prettyOdfFile(odfFile, 0);
    }

    /**
     * It prints the key and value of a JSON object, with a tabulation that depends
     * on the depth of the object
     * 
     * @param key   The key of the JSON object
     * @param value The value of the key.
     * @param depth The depth of the current object in the file system.
     */
    private static void prettyPrint(String key, Object value, int depth) {
        String tabulation = properTabulation(depth);
        System.out.println(tabulation + key + " : " + value);
    }

    private static void prettyPrint(String key, int depth) {
        prettyPrint(key, "", depth);
    }

    /**
     * It prints a string with a tabulation that depends on the depth of the node
     * 
     * @param toPrint The string to print
     * @param depth   The depth of the current node.
     */
    private static void print(String toPrint, int depth) {
        String tabulation = properTabulation(depth);
        System.out.println(tabulation + toPrint);
    }

    /**
     * It takes a JSONArray representing the metadata of an odf file and an integer
     * as parameters. It loops through
     * the metadata array, and for each metadata depending on the type,
     * it calls a function to print it prettily.
     * 
     * @param metadataJsonArray the metadata of the odf file
     * @param depth             the depth in the file tree of the file
     */
    private static void prettyMetadata(JSONObject odfFile, int depth) throws NoSuchMetadataException{
        List<Object> currentMetadata = IbarOdfResultParser.getCurrentOdfMetadata(odfFile);
        Iterator<Object> currentMetadataIt = currentMetadata.iterator();
        Object currentMeta;
        while(currentMetadataIt.hasNext()){    
            currentMeta = currentMetadataIt.next();
            if(IbarOdfResultParser.isHyperlinkKey(currentMeta)){
                prettyHyperlink(IbarOdfResultParser.getHyperlink(odfFile), depth);
            }else if(IbarOdfResultParser.isPicturesKey(currentMeta)){
                prettyPicture(IbarOdfResultParser.getPictures(odfFile), depth);
            }else if(IbarOdfResultParser.isStatisticsKey(currentMeta)){
                prettyStatistique(IbarOdfResultParser.getJsonArrayOfStatistics(odfFile), depth);
            }else if(IbarOdfResultParser.isThumbnailKey(currentMeta)){
                prettyThumbnail(odfFile, depth); 
            }else{
                prettyObject(IbarOdfResultParser.getMetadataByType(odfFile, currentMeta.toString()), depth);
            }
        }
    }


    public static void prettyThumbnail(JSONObject OdfFile, int depth) throws NoSuchMetadataException{
        Path thumbnailPath = IbarOdfResultParser.getThumbnailPath(OdfFile);
        prettyPrint(MetadataThumbnail.THUMBNAIL,thumbnailPath, depth);
    }

    /**
     * This function prints hyperlink in a pretty way
     * 
     * @param hyperlinkJson The JSONObject that contains the hyperlink information.
     * @param depth         the depth of the current object in the JSON tree
     */
    private static void prettyHyperlink(JSONArray hyperlinksArray, int depth) {
        JSONObject currentLink;
        int lengthArray = hyperlinksArray.length();
        for (int index = 0; index < lengthArray; index++) {
            currentLink = hyperlinksArray.getJSONObject(index);
            prettyPrint(MetadataHyperlink.HYPERLINKS, depth);
            prettyObject(currentLink, depth + 1);
        }
    }

    /**
     * This function prints a closed directory
     * 
     * @param closedDirectory the directory to be printed
     * @param depth           the depth of the directory in the tree
     */
    private static void prettyClosedDirectory(JSONObject closedDirectory, int depth) {
        ligne(depth);
        prettyPrint(PrettyResult.CLOSED_DIR, closedDirectory.get(Directory.FILE_NAME), depth);
        ligne(depth);
    }

    /**
     * It takes a JSONObject and a depth, and then prints out the key and value of
     * each key-value pair
     * in the JSONObject
     * 
     * @param jsonObject The Json representation of an abstract generic file
     * @param depth      the depth of the current object in file system
     */
    private static void prettyObject(JSONObject jsonObject, int depth) {
        Collection<String> attributsObject = jsonObject.keySet();
        Iterator<String> attributIt = attributsObject.iterator();
        String currentAttribut; 
        while(attributIt.hasNext()) {
            currentAttribut = attributIt.next();
            prettyPrint(currentAttribut, jsonObject.get(currentAttribut), depth);
        }
    }

    /**
     * It prints the name of each picture in the ODF file, and then prints the
     * details of each picture
     * 
     * @param pictureJson the JSONObject that represents the odf file and that
     *                    contains the pictures
     * @param depth       the depth of the current ODF file in the tree.
     */

    private static void prettyPicture(JSONArray Pictures, int depth) {
        prettyPrint(MetadataOdfPictures.PICTURES, depth);
        JSONObject currentPicture;
        Path picturePath;
        int lengthArray = Pictures.length();
        for (int index = 0; index < lengthArray; index++) {
            currentPicture = Pictures.getJSONObject(index);
            picturePath = (Path) currentPicture.get(Picture.PATH);
            prettyPrint(MetadataOdfPictures.PICTURES + picturePath.getFileName(), depth+1);
            prettyObject(currentPicture, depth + 2);
        }
    }

    /**
     * It takes a JSONObject that represents the current diretory and an integer as
     * parameters,
     * and prints informations regarding it's content (number of files,
     * subdirectories, wrong files...)
     * 
     * @param directoryJson the JSONObject that contains the informations
     * @param depth         the depth of the directory
     */
    private static void prettyInformations(JSONObject directory, int depth) {
        prettyPrint(Directory.SUBDIRECTORIES, IbarOdfResultParser.getNumberOfSubDirectories(directory), depth+1);
        prettyPrint(Directory.REGULAR_FILES, IbarOdfResultParser.getNumberOfRegularFile(directory), depth+1);
        prettyPrint(Directory.ODF_FILES, IbarOdfResultParser.getNumberOfOdfFiles(directory), depth+1);
        prettyPrint(Directory.WRONG_FILES, IbarOdfResultParser.getNumberOfWrongFiles(directory), depth+1);
        prettyPrint(Directory.TOTAL_NUMBER_OF_FILES, IbarOdfResultParser.getTotalNumberOfFiles(directory), depth);
    }

    /**
     * It takes a JSONObject that represents the metadata of an ODF file
     * and an integer as parameters. It then gets the statistics of the ODF files
     * and
     * loops through them to print them in a pretty manner
     * 
     * @param metadataJsonArray The JSONObject that contains the metadata.
     * @param depth             the depth of the current ODF file in the file system
     */
    private static void prettyStatistique(JSONArray metadataJsonArray, int depth) {
        prettyPrint(MetadataStats.STATISTICS,depth);
        JSONObject currentStat;
        for (int index = 0, indexMax = metadataJsonArray.length(); index < indexMax; index++) {
            currentStat = metadataJsonArray.getJSONObject(index);
           prettyObject(currentStat, depth+1);
        }

    }


    






}
