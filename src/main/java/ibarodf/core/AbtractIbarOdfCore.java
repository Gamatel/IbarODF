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
	
	
	public static void changeTheDescriptionOfAnOdtFile(Path odfFileToOperateOn, ArrayList<String> newValues) throws Exception{
		OdfFile file = new OdfFile(odfFileToOperateOn);
		for(int index =0 , maxIndex = descriptionMetaData.length; index < maxIndex; index++ ){
			file.setMetaData( descriptionMetaData[index], newValues.get(index));
		}
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

	public static boolean wantToDisplayMetadata(Command actionToPerform){
		return actionToPerform == Command.DISPLAY_THE_META_DATA_OF_AN_ODF_FILE || actionToPerform ==  Command.DISPLAY_THE_META_DATA_A_FILE || actionToPerform == Command.DISPLAY_THE_META_DATA_OF_ODF_FILES_IN_A_DIRECTORY;
	}

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

}
