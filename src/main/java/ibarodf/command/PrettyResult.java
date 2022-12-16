package ibarodf.command;

import java.nio.file.Path;
import java.util.Collection;

import javax.json.JsonException;

import org.json.JSONArray;
import org.json.JSONObject;

import ibarodf.core.file.AbstractGenericFile;
import ibarodf.core.file.Directory;
import ibarodf.core.file.OdfFile;
import ibarodf.core.file.RegularFile;
import ibarodf.core.file.WrongFile;
import ibarodf.core.meta.MetadataHyperlink;
import ibarodf.core.meta.MetadataOdfPictures;
import ibarodf.core.meta.MetadataStats;
import ibarodf.core.meta.object.Picture;

public abstract class PrettyResult {
    public static final int DEFAULT_LINE_SIZE_FOR_PRETTY_PRINT = 100;
    public static final String DEFAULT_LINE_SYMBOLE_FOR_PRETTY_PRINT = "-";
    public static final String CLOSED_DIR = "SubSirectory";

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
     */
    public static void prettyDirectory(JSONObject directory, int depth) {
        try {
            ligne(depth);
            print(directory.get(Directory.FILE_NAME).toString(), depth);
            ligne(depth);
            displayRegularFiles(directory, depth + 1);
            displayOdfFiles(directory, depth + 1);
            displaySubDirectories(directory, depth + 1);
            displayWrongFiles(directory, depth + 1);
            prettyInformations(directory, depth);
        } catch (JsonException e) {
        }
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
    private static void displayRegularFiles(JSONObject jsonDirectory, int depth) {
        JSONArray jsonRegularFiles = jsonDirectory.getJSONArray(Directory.REGULAR_FILES);
        JSONObject currentJsonObject;
        for (int index = 0, indexMax = jsonRegularFiles.length(); index < indexMax; index++) {
            currentJsonObject = jsonRegularFiles.getJSONObject(index);
            if (currentJsonObject.get(AbstractGenericFile.MIME_TYPE).equals(AbstractGenericFile.TYPE_DIRECTORY)) {
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
     */
    private static void displayOdfFiles(JSONObject jsonDirectory, int depth) {
        JSONArray jsonOdfFiles = jsonDirectory.getJSONArray(Directory.ODF_FILES);
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
     */
    private static void displaySubDirectories(JSONObject jsonDirectory, int depth) {
        JSONArray jsonDirectories = jsonDirectory.getJSONArray(Directory.SUBDIRECTORIES);
        for (int index = 0, indexMax = jsonDirectories.length(); index < indexMax; index++) {
            prettyDirectory(jsonDirectories.getJSONObject(index), depth + 1);
        }
    }

    private static void displayWrongFiles(JSONObject jsonDirectory, int depth) {
        JSONArray jsonWrongFiles = jsonDirectory.getJSONArray(Directory.WRONG_FILES);
        int numberOfWrongFiles = jsonWrongFiles.length();
        if (numberOfWrongFiles > 0) {
            ligne("/!\\", PrettyResult.DEFAULT_LINE_SIZE_FOR_PRETTY_PRINT / 3, depth);
            for (int index = 0; index < numberOfWrongFiles; index++) {
                prettyWrongFile(jsonWrongFiles.getJSONObject(index), depth + 1);
            }
            ligne("/!\\", PrettyResult.DEFAULT_LINE_SIZE_FOR_PRETTY_PRINT / 3, depth);
        }
    }

    public static void prettyDirectory(JSONObject directory) {
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
        try {
            prettyPrint(RegularFile.FILE_NAME, file.get(RegularFile.FILE_NAME), depth);
            prettyPrint(RegularFile.MIME_TYPE, file.get(RegularFile.MIME_TYPE), depth);
            prettyPrint(RegularFile.SIZE, file.get(RegularFile.SIZE), depth);
        } catch (JsonException e) {
        }
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
        prettyFile(jsonWrongFile, depth + 1);
        prettyPrint(WrongFile.ERRORMESSAGE, jsonWrongFile.get(WrongFile.ERRORMESSAGE), depth + 1);

    }

    /**
     * This function prints the contents of a JSONObject that represents an ODF file
     * 
     * @param odfFile The JSONObject that represents the ODF file.
     * @param depth   the depth of the JSONObject in the JSONObject tree.
     */

    private static void prettyOdfFile(JSONObject odfFile, int depth) {
        try {
            prettyFile(odfFile, depth);
            JSONArray metaJsonArray = odfFile.getJSONArray(OdfFile.METADATA);
            prettyMetadata(metaJsonArray, depth);
        } catch (JsonException e) {
        }
    }

    public static void prettyOdfFile(JSONObject odfFile) {
        prettyOdfFile(odfFile, 0);
    }

    /**
     * It prints the key and value of a JSON object, with a tabulation that depends
     * on the depth of the
     * object
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
    private static void prettyMetadata(JSONArray metadataJsonArray, int depth) {
        JSONObject currentMeta = new JSONObject();
        Collection<String> typeMeta;
        for (int indexArray = 0, indexMaxArray = metadataJsonArray.length(); indexArray < indexMaxArray; indexArray++) {
            currentMeta = metadataJsonArray.getJSONObject(indexArray);
            typeMeta = currentMeta.keySet();
            for (String meta : typeMeta) {
                switch (meta) {
                    case MetadataHyperlink.HYPERLINKS:
                        prettyPrint(MetadataHyperlink.HYPERLINKS, depth);
                        prettyHyperlink(currentMeta, depth + 1);
                        break;
                    case MetadataOdfPictures.PICTURES:
                        prettyPrint(MetadataOdfPictures.PICTURES, depth);
                        prettyPicture(currentMeta, depth + 1);
                        break;
                    case MetadataStats.STATISTICS:
                        prettyPrint(MetadataStats.STATISTICS, depth);
                        prettyStatistique(currentMeta, depth + 1);
                        break;
                    default:
                        prettyObject(currentMeta, depth);

                }
            }
        }
    }

    /**
     * This function prints hyperlink in a pretty way
     * 
     * @param hyperlinkJson The JSONObject that contains the hyperlink information.
     * @param depth         the depth of the current object in the JSON tree
     */
    private static void prettyHyperlink(JSONObject hyperlinkJson, int depth) {
        JSONArray hyperlinksArray = hyperlinkJson.getJSONArray(MetadataHyperlink.HYPERLINKS);
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
        ligne(depth + 1);
        prettyPrint(PrettyResult.CLOSED_DIR, closedDirectory.get(Directory.FILE_NAME), depth + 1);
        ligne(depth + 1);
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
        for (String attribut : attributsObject) {
            prettyPrint(attribut, jsonObject.get(attribut), depth);
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

    private static void prettyPicture(JSONObject pictureJson, int depth) {
        JSONArray pictureArray = pictureJson.getJSONArray(MetadataOdfPictures.PICTURES);
        JSONObject currentPicture;
        Path picturePath;
        int lengthArray = pictureArray.length();
        for (int index = 0; index < lengthArray; index++) {
            currentPicture = pictureArray.getJSONObject(index);
            picturePath = (Path) currentPicture.get(Picture.PATH);
            prettyPrint(MetadataOdfPictures.PICTURES + picturePath.getFileName(), depth);
            prettyObject(currentPicture, depth + 1);
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
    private static void prettyInformations(JSONObject directoryJson, int depth) {
        ligne(depth);
        String endMessage = "In " + directoryJson.get(Directory.FILE_NAME);
        prettyPrint(endMessage, depth);
        JSONArray informations = directoryJson.getJSONArray(Directory.INFORMATIONS);
        for (int index = 0, indexMax = informations.length(); index < indexMax; index++) {
            prettyObject(informations.getJSONObject(index), depth + 1);
        }
        ligne(depth);
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
    private static void prettyStatistique(JSONObject metadataJsonArray, int depth) {
        JSONArray statArray = metadataJsonArray.getJSONArray(MetadataStats.STATISTICS);
        JSONObject currentStat;
        for (int index = 0, indexMax = statArray.length(); index < indexMax; index++) {
            currentStat = statArray.getJSONObject(index);
            prettyObject(currentStat, depth);
        }

    }

}
