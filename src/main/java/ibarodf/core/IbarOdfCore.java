package ibarodf.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.github.andrewoma.dexx.collection.HashMap;

import ibarodf.core.file.AbstractGenericFile;
import ibarodf.core.file.Directory;
import ibarodf.core.file.OdfFile;
import ibarodf.core.file.RegularFile;
import ibarodf.core.file.UnrecognizableTypeFileException;
import ibarodf.core.meta.MetadataComment;
import ibarodf.core.meta.MetadataCreator;
import ibarodf.core.meta.MetadataKeyword;
import ibarodf.core.meta.MetadataSubject;
import ibarodf.core.meta.MetadataTitle;
import ibarodf.core.meta.exception.UnableToConvertToJsonFormatException;

/**
 * It's a class that contains static methods that allows you : 
 * <ol>
 * <li>to visualise all the main metadata of an Odf file and to change the ones that are setable
 * (such as its title, subject, keywords, creator, description...)</li>
 * <li>to visualise the contente of a directory</li>
 * </ol>
 */
public abstract class IbarOdfCore {
	public static final String[] descriptionMetaData = { MetadataTitle.ATTR, MetadataSubject.ATTR, MetadataKeyword.ATTR,
			MetadataComment.ATTR };

	private static void changeTheDescriptionOfAnOdtFile(Path odfFileToOperateOn, ArrayList<String> newValues)
			throws Exception {
		String[] description = IbarOdfCore.descriptionMetaData;
		OdfFile file = new OdfFile(odfFileToOperateOn);
		for (int index = 0, maxIndex = description.length; index < maxIndex; index++) {
			file.setMetaData(description[index], newValues.get(index));
		}
		file.saveChange();
		System.out.println("Description changed!");
	}

	/**
	 * It takes a file path, and four strings, and changes the title, subject, keywords, and comments of
	 * the file.
	 * 
	 * @param filePath The path to the file you want to change the description of.
	 * @param newTitle The new title of the document.
	 * @param newSubject The new subject of the document.
	 * @param newKeywords The new keywords for the file.
	 * @param newComments The new comment to be set.
	 * @throws Exception
	 */
	public static void changeTheDescriptionOfAnOdtFile(Path filePath, String newTitle, String newSubject, String newKeywords, String newComments) throws Exception {
		ArrayList<String> newValues = new ArrayList<String>();
		newValues.add(newTitle);
		newValues.add(newSubject);
		newValues.add(newKeywords);
		newValues.add(newComments);
		changeTheDescriptionOfAnOdtFile(filePath, newValues);

	}

	/**
	 * It takes a string, checks if it's a valid file path, and returns a normalized absolute path
	 * 
	 * @param filePath The path to the file you want to read.
	 * @return A Path object
	 */
	public static Path stringToPath(String filePath) throws FileNotFoundException {
		File file = new File(filePath);
		if (!file.exists()) {
			throw new FileNotFoundException();
		}
		return file.toPath().toAbsolutePath().normalize();
	}

	
	/**
	 * It returns the MIME type of a file
	 * 
	 * @param filePath The path to the file you want to check.
	 * @return The file type of the file at the given path.
	 */
	public static String fileType(Path filePath) throws IOException, UnrecognizableTypeFileException {
		String type = Files.probeContentType(filePath);
		if (type == null) {
			throw new UnrecognizableTypeFileException(filePath);
		}
		return type;
	}
	
	public static String fileType(String filePath) throws IOException, UnrecognizableTypeFileException {
		return fileType(IbarOdfCore.stringToPath(filePath));
	}

	/**
	 * It returns true if the file is an ODF file, and false otherwise
	 * 
	 * @param filePath The path to the file you want to check.
	 * @return A boolean value.
	 */
	public static boolean isAnOdfFile(String filePath) throws UnrecognizableTypeFileException, IOException {
		boolean isOdf = false;
		String type = fileType(filePath);
		isOdf = !type.equals("application/vnd.oasis.opendocument.formula") && !type.equals("application/vnd.oasis.opendocument.text-template")
				&& type.contains("application/vnd.oasis.opendocument");
		return isOdf;
	}

	public static boolean isAnOdfFile(Path filePath) throws UnrecognizableTypeFileException, IOException{
		return isAnOdfFile(filePath.toString()); 
	}

	/**
	 * It returns the system separator of the current system
	 * 
	 * @return The current system separator.
	 */
	public static String getCurrentSystemSeparator() {
		return FileSystems.getDefault().getSeparator();
	}


	/**
	 * This function changes the title of the file
	 * 
	 * @param path The path to the ODF file.
	 * @param newTitle The new title of the file.
	 */
 	public static void changeTheTitleOfAnOdfFile(Path path, String newTitle) throws Exception {
		OdfFile file = new OdfFile(path);
		file.setMetaData(MetadataTitle.ATTR, newTitle);
		file.saveChange();
	}


	/**
	 * this function changes the subject of an ODF file.
	 * 
	 * @param path The path to the ODF file.
	 * @param newSubject The new subject you want to set.
	*/
	public static void changeTheSubjectOfAnOdfFile(Path path, String newSubject) throws Exception {
		OdfFile file = new OdfFile(path);
		file.setMetaData(MetadataSubject.ATTR, newSubject);
		file.saveChange();
	}

	/**
	 * This function changes the keywords of an Odf file
	 * @param path The path to the ODF file.
	 * @param newKeywords The new keywords you want to set.
	 */
	public static void changeTheKeywordsOfAnOdfFile(Path path, String newKeywords) throws Exception {
		OdfFile file = new OdfFile(path);
		file.setMetaData(MetadataKeyword.ATTR, newKeywords);
		file.saveChange();
	}

	/**
	 * This function changes the comments of en Odf file
	 * @param path The path to the ODF file.
	 * @param newComments The new comments you want to set.
	 */
	public static void changeTheCommentsOfAnOdfFile(Path path, String newComments) throws Exception {
		OdfFile file = new OdfFile(path);
		file.setMetaData(MetadataComment.ATTR, newComments);
		file.saveChange();
	}

	/**
	 * This function changes the creator of en Odf file
	 * @param path The path to the ODF file.
	 * @param newCreator The new creator you want to set.
	 */
	public static void changeTheCreatorOfAnOdfFile(Path path, String newCreator) throws Exception {
		OdfFile file = new OdfFile(path);
		file.setMetaData(MetadataCreator.ATTR, newCreator);
		file.saveChange();
	}
	/**
	 * It takes a directory path and a boolean, and returns a JSON object 
	 * that corresponds to the Json representation of a directory
	 * @param directoryPath The path to the directory you want to convert to JSON.
	 * @param recursif if true, the directory will be scanned recursively.
	 * @return A JSONObject
	 */
	public static JSONObject directoryToJson(Path directoryPath, boolean recursif) throws FileNotFoundException, UnableToConvertToJsonFormatException{
		Directory directory = new Directory(directoryPath, recursif);
		return directory.toJonObject();
	}

	public static JSONObject directoryToJson(Path directoryPath) throws FileNotFoundException, UnableToConvertToJsonFormatException {
		return directoryToJson(directoryPath, false);
	}

	/**
	 * This function takes a path to an ODF file and returns a JSON object that represents the file
	 * 
	 * @param path The path to the ODF file.
	 * @return A JSONObject
	 */
	public static JSONObject odfFileToJson(Path path) throws Exception {
		OdfFile file = new OdfFile(path);
		return file.toJonObject();
	}

	/**
	 * This function takes a path to a file and returns a JSONObject that represents the file
	 * 
	 * @param path The path to the file you want to convert to JSON.
	 * @return A JSONObject
	 */
	public static JSONObject RegularFileToJson(Path path) throws Exception {
		RegularFile file = new RegularFile(path);
		return file.toJonObject();

	}








	//Start JSON PARSER

	//ABOUT ABTRACT GENERIC FILES 
	public static Object getPath(JSONObject abstractGenericFile){
		return abstractGenericFile.get(AbstractGenericFile.PATH);
	}

	public static Object getMimeType(JSONObject abstractGenericFile){
		return abstractGenericFile.get(AbstractGenericFile.MIME_TYPE);
	}

	public static Object getSize(JSONObject abstractGenericFile){
		return abstractGenericFile.get(AbstractGenericFile.SIZE);
	}

	public static Object getFileName(JSONObject abstractGenericFile){
		return abstractGenericFile.get(AbstractGenericFile.FILE_NAME);
	}
	
	
	
	
	
	
	
	
	//ABOUT DIRECTORY
	private static JSONArray getDirectoryJsonChildrenByType(JSONObject JsonDirectory, String type){
		return JsonDirectory.getJSONArray(type);
	}

	public static JSONArray getOdfFiles(JSONObject jsonDirectory){
		return getDirectoryJsonChildrenByType(jsonDirectory, Directory.ODF_FILES);
	} 

	public static JSONArray getRegularFiles(JSONObject jsonDirectory){
		return getDirectoryJsonChildrenByType(jsonDirectory, Directory.REGULAR_FILES);
	} 

	public static JSONArray getSubDirectories(JSONObject jsonDirectory){
		return getDirectoryJsonChildrenByType(jsonDirectory, Directory.SUBDIRECTORIES);
		
	} 
	public static JSONArray getWrongFiles(JSONObject jsonDirectory){
		return getDirectoryJsonChildrenByType(jsonDirectory, Directory.WRONG_FILES);
	} 

	public static boolean isADirectory(JSONObject abstractGenericFile){
		return getMimeType(abstractGenericFile).equals(AbstractGenericFile.TYPE_DIRECTORY);
	} 

	private static HashMap<Object,Object> getDirectoryInformatiosByType(JSONObject jsonDirectory){
		JSONArray jsonInformations = jsonDirectory.getJSONArray(Directory.INFORMATIONS);
		HashMap<Object,Object> informationsHashMap = new HashMap<Object,Object>();
		JSONObject currentObject; 
		String currentInformation;
		for(int index=0, indexMax = jsonInformations.length(); index<indexMax; index++){
			currentObject = jsonInformations.getJSONObject(index);
			currentInformation =  Directory.ALL_INFORMATIONS[index];
			informationsHashMap.put( currentInformation , currentObject.get(currentInformation));
		} 
		return informationsHashMap;
	} 

	//ABOUT ODF Files











}
