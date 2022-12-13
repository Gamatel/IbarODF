package ibarodf.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.io.FileReader;


import org.json.JSONObject;

import ibarodf.core.file.Directory;
import ibarodf.core.file.OdfFile;
import ibarodf.core.file.RegularFile;
import ibarodf.core.meta.MetaDataComment;
import ibarodf.core.meta.MetaDataCreator;
import ibarodf.core.meta.MetaDataKeyword;
import ibarodf.core.meta.MetaDataSubject;
import ibarodf.core.meta.MetaDataTitle;

public abstract class AbtractIbarOdfCore {
	public static final String[] descriptionMetaData = {MetaDataTitle.ATTR, MetaDataSubject.ATTR, MetaDataKeyword.ATTR, MetaDataComment.ATTR};
	/* private final Command actionToPerform;
	private final Path fileToOperateOn;
	private ArrayList<String> newValues;
	public IbarODFCore(Command actionToPerform, Path fileToOperateOn, ArrayList<String> newValues) {
		this.actionToPerform = actionToPerform;
		this.fileToOperateOn = fileToOperateOn;
		this.newValues = newValues;
	}

	public IbarODFCore(){
		this.actionToPerform = null;
		this.fileToOperateOn = null;
		this.newValues = null;
	}
	 */
	
	 public static void help(){
		try{
			String fileSeparator = FileSystems.getDefault().getSeparator();
			BufferedReader helpReader = new BufferedReader(new FileReader(new File("ressources" + fileSeparator +"help.txt")));
			String line;
			while((line = helpReader.readLine()) != null){
				System.out.println(line);
			} 
			helpReader.close();
		}catch(IOException e){
			System.err.println("Sorry, cannot reach the help manual.");
		}
	}
	
	/* public IbarODFCore(Command actionToPerform, Path fileToOperateOn) {
		this(actionToPerform, fileToOperateOn, null);
	}

	public StringBuilder displayTheMetaDataOfOdtFileInADirectory() throws Exception{
		Directory directory = new Directory(fileToOperateOn);
		return directory.displayMetaData();
	}
	public StringBuilder displayTheMetaDataOfAFile() throws Exception{
		RegularFile notOdfFile = new RegularFile(fileToOperateOn);
		return notOdfFile.displayMetaData();
	}

	public StringBuilder diplayTheMetatDataOfAnOdtFile() throws Exception{
		OdfFile odfFile = new OdfFile(fileToOperateOn); 
		return odfFile.displayMetaData();
	}
 */
	
	public static void changeTheDescriptionOfAnOdtFile(Path odfFileToOperateOn, ArrayList<String> newValues) throws Exception{
		OdfFile file = new OdfFile(odfFileToOperateOn);
		for(int index =0 , maxIndex = descriptionMetaData.length; index < maxIndex; index++ ){
			file.setMetaData( descriptionMetaData[index], newValues.get(index));
		}
		/* 
		file.setMetaData(MetaDataTitle.ATTR, newValues.indexOf(0));
		file.setMetaData(MetaDataSubject.ATTR, );
		file.setMetaData(MetaDataKeyword.ATTR, newKeyword);
		file.setMetaData(MetaDataComment.ATTR, newComments);
		file.saveChange();
		 */
		file.saveChange();
		System.out.println("Description changed!");

	}

	public static Path stringToPath(String filePath)  throws IOException{
        File file = new File(filePath);
        if(!file.exists()){
            throw new FileNotFoundException();
        }
		return file.toPath().toAbsolutePath().normalize();
    }

    public static String fileType(String filePath) throws IOException, UnrecognizableTypeFileException {
        Path path = stringToPath(filePath);
        String type = Files.probeContentType(path);
        if(type == null){
            throw new UnrecognizableTypeFileException(path);
        }
        return type;
    }

	public static boolean isAnOdfFile(String filePath){
        boolean isOdf= false;
        try{
            String type = fileType(filePath);
            isOdf = !type.equals("application/vnd.oasis.opendocument.formula") && type.contains("application/vnd.oasis.opendocument");
        }catch(UnrecognizableTypeFileException ignored){
        } catch(IOException e){
            System.err.println(e.getMessage());
        }
		return isOdf;
    }
/* 
	public StringBuilder displayMetaData() throws Exception{
		switch(actionToPerform){
			case DISPLAY_THE_META_DATA_A_FILE:
				return displayTheMetaDataOfAFile();
			case DISPLAY_THE_META_DATA_OF_ODF_FILES_IN_A_DIRECTORY:
				return displayTheMetaDataOfOdtFileInADirectory();
			case DISPLAY_THE_META_DATA_OF_AN_ODF_FILE:
				return diplayTheMetatDataOfAnOdtFile();
			default:
				throw new Exception("Something went wrong...");
		}
	} */




	public static void changeTheTitleOfAnOdfFile(Path path, String newTitle) throws Exception{
		OdfFile file = new OdfFile(path);
		file.setMetaData(MetaDataTitle.ATTR, newTitle);
		file.saveChange();
		System.out.println("Title changed!");

	}


	public static void changeTheSubjectOfAnOdfFile(Path path, String newSubject) throws Exception{
		OdfFile file = new OdfFile(path);
		file.setMetaData(MetaDataSubject.ATTR, newSubject);
		file.saveChange();
		System.out.println("Subject changed!");
	}

	public static void changeTheKeywordsOfAnOdfFile(Path path, String newKeywords) throws Exception{
		OdfFile file = new OdfFile(path);
		file.setMetaData(MetaDataKeyword.ATTR, newKeywords);
		file.saveChange();
		System.out.println("Keywords changed!");
	}

	public static void changeTheCommentsOfAnOdfFile(Path path, String newComments) throws Exception{
		OdfFile file = new OdfFile(path);
		file.setMetaData(MetaDataComment.ATTR, newComments);
		file.saveChange();
		System.out.println("Comments changed!");
	}

	public static void changeTheCreatorOfAnOdfFile(Path path, String newCreator) throws Exception{
		OdfFile file = new OdfFile(path);
		file.setMetaData(MetaDataCreator.ATTR, newCreator);
		file.saveChange();
		System.out.println("Creator changed!");
	}



/* 		
	public void operationOnOdfFile() throws Exception{
		OdfFile file = new OdfFile(fileToOperateOn);
		switch(actionToPerform){
			case CHANGE_THE_TITLE_OF_AN_ODF_FILE:
				file.setMetaData(MetaDataTitle.ATTR, newValues.get(0));
				System.out.println("Title changed!");
				break;
    		case CHANGE_THE_SUBJECT_OF_AN_ODF_FILE:
				file.setMetaData(MetaDataSubject.ATTR,  newValues.get(0));
				System.out.println("Subject added!");
				break;
			case REPLACE_THE_KEYWORDS_TO_AN_ODF_FILE:
				file.setMetaData(MetaDataKeyword.ATTR, newValues.get(0));
				System.out.println("Keywords changed!");
				break;
    		case CHANGE_THE_COMMENTS_OF_AN_ODF_FILE:
				file.setMetaData(MetaDataComment.ATTR,  newValues.get(0));
				System.out.println("Commentschanged!");
				break;
			case CHANGE_THE_CREATOR_OF_AN_ODF_FILE:
				file.setMetaData(MetaDataCreator.ATTR,  newValues.get(0));
				System.out.println("Creator changed!");
				break;
			case REPLACE_THE_DESCRIPTION_OF_AN_ODF_FILE:
				replaceTheDescriptionOfAnOdtFile(newValues);	
				System.out.println("Description changed!");
				break;
			default:
				throw new Exception("Something went wrong...");
		}
		file.saveChange();
	}
 */




	public static boolean wantToDisplayMetadata(Command actionToPerform){
		return actionToPerform == Command.DISPLAY_THE_META_DATA_OF_AN_ODF_FILE || actionToPerform ==  Command.DISPLAY_THE_META_DATA_A_FILE || actionToPerform == Command.DISPLAY_THE_META_DATA_OF_ODF_FILES_IN_A_DIRECTORY;
	}


/* 
	public StringBuilder launchCore() throws Exception{
		return wantToDisplayMetadata(actionToPerform)? displayMetaData() : operationOnOdfFile();
	} */

	public static JSONObject directoryToJson(Path directoryPath) throws Exception{
		Directory directory = new Directory(directoryPath);
		return directory.toJonObject();
	}

	public static JSONObject odfFileToJson(Path path) throws Exception{
		OdfFile file = new OdfFile(path);
		return file.toJonObject();
	}

	
	public static JSONObject RegularFileToJson(Path path) throws Exception{
		RegularFile file = new RegularFile(path);
		return file.toJonObject();
		
	}


/* 	public JSONObject toJson() throws Exception{
		AbstractGenericFile file;
		switch(actionToPerform){
			case DISPLAY_THE_META_DATA_A_FILE:
				file = new RegularFile(fileToOperateOn);
				break;
			case DISPLAY_THE_META_DATA_OF_AN_ODF_FILE:
				file = new OdfFile(fileToOperateOn);
				break;
			case DISPLAY_THE_META_DATA_OF_ODF_FILES_IN_A_DIRECTORY:
				file = new Directory(fileToOperateOn); 
				break;
			default :
				throw new Exception("Error in Json formatage!");
		}
		return file.toJonObject();
	}
 */	

}
