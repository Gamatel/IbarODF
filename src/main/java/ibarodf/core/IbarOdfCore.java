package ibarodf.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;


import org.json.JSONObject;

import ibarodf.core.file.Directory;
import ibarodf.core.file.OdfFile;
import ibarodf.core.file.RegularFile;
import ibarodf.core.file.UnrecognizableTypeFileException;
import ibarodf.core.file.exception.UnableToAccessToTheCurrentFile;
import ibarodf.core.meta.MetaDataComment;
import ibarodf.core.meta.MetaDataCreator;
import ibarodf.core.meta.MetaDataKeyword;
import ibarodf.core.meta.MetaDataSubject;
import ibarodf.core.meta.MetaDataTitle;

public abstract class IbarOdfCore {
	public static final String[] descriptionMetaData = {MetaDataTitle.ATTR, MetaDataSubject.ATTR, MetaDataKeyword.ATTR, MetaDataComment.ATTR};
	
	
	private static void  changeTheDescriptionOfAnOdtFile(Path odfFileToOperateOn, ArrayList<String> newValues) throws Exception{
		String[] description = IbarOdfCore.descriptionMetaData;
		OdfFile file = new OdfFile(odfFileToOperateOn);
		for(int index =0 , maxIndex = description.length; index < maxIndex; index++ ){
			file.setMetaData( description[index], newValues.get(index));
		}
		file.saveChange();
		System.out.println("Description changed!");
	}
	
	public static void changeTheDescriptionOfAnOdtFile(Path filePath, String newTitle, String newSubject, String newKeywords, String newComments) throws Exception{
		ArrayList<String> newValues = new ArrayList<String>();
		newValues.add(newTitle);
		newValues.add(newSubject);
		newValues.add(newKeywords);
		newValues.add(newComments);
		changeTheDescriptionOfAnOdtFile(filePath, newValues);

	}


	
    public static boolean isAllowedToAccesToTheCurrentFile(Path pathDirectory) throws UnableToAccessToTheCurrentFile {
        File directory =  pathDirectory.toFile();
        if(!directory.canExecute() || !directory.canRead()|| !directory.canWrite()){
            throw new UnableToAccessToTheCurrentFile(pathDirectory);
        }
		return true;
    }


	public static Path stringToPath(String filePath) throws FileNotFoundException{
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

	public static String fileType(Path filePath) throws IOException, UnrecognizableTypeFileException{
		return fileType(filePath.toString());
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

	public static String getCurrentSystemSeparator(){
		return FileSystems.getDefault().getSeparator(); 
	}

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

	public static JSONObject directoryToJson(Path directoryPath, boolean recursif) throws Exception{
		Directory directory = new Directory(directoryPath, recursif);
		return directory.toJonObject();
	}
	public static JSONObject directoryToJson(Path directoryPath) throws Exception{
		return directoryToJson(directoryPath, false);
	}

	public static JSONObject odfFileToJson(Path path) throws Exception{
		OdfFile file = new OdfFile(path);
		return file.toJonObject();
	}

	
	public static JSONObject RegularFileToJson(Path path) throws Exception{
		RegularFile file = new RegularFile(path);
		return file.toJonObject();
		
	}

}
