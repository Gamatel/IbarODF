package ibarodf.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import ibarodf.core.file.Directory;
import ibarodf.core.file.OdfFile;
import ibarodf.core.file.OdtFile;
import ibarodf.core.file.RegularFile;
import ibarodf.core.meta.MetaDataComment;
import ibarodf.core.meta.MetaDataCreator;
import ibarodf.core.meta.MetaDataSubject;
import ibarodf.core.meta.MetaDataTitle;

public class IbarODFCore {
	private Command actionToPerform;
	private Path fileToOperateOn;
	private String[] args;
	public IbarODFCore(Command actionToPerform, Path fileToOperateOn, String[] args) {
		this.actionToPerform = actionToPerform;
		this.fileToOperateOn = fileToOperateOn;
		this.args = args;
	}

	public StringBuilder displayTheMetaDataOfOdtFileInADirectory() throws Exception{
		Directory directory = new Directory(fileToOperateOn);
		return directory.displayMetaData();
	}

	public StringBuilder displayTheMetaDataOfAFile() throws Exception{
		RegularFile notOdtFile = new RegularFile(fileToOperateOn);
		return notOdtFile.displayMetaData();
	}

	public StringBuilder diplayTheMetatDataOfAnOdtFile() throws Exception{
		OdfFile odtFile = new OdfFile(fileToOperateOn); 
		return odtFile.displayMetaData();
	}

	public void replaceTheDescriptionOfAnOdtFile(String newTitle, String newSubject, String newKeyword, String newComments) throws Exception{
		OdfFile file = new OdfFile(fileToOperateOn);
		file.setMetaData(MetaDataTitle.ATTR, newTitle);
		file.setMetaData(MetaDataSubject.ATTR, newSubject);
		System.out.println("Have to implemente adding a keyword!!!");
		// file.setMetaData(MetaDataKeyword.ATTR, args[3]);
		file.setMetaData(MetaDataComment.ATTR, newComments);
		file.saveChange();
		System.out.println("Description changed!");

	}

	public static Path stringToPath(String filePath) throws FileNotFoundException, IOException{
        File file = new File(filePath);
        if(!file.exists()){
            throw new FileNotFoundException();
        }
        Path path = file.toPath().toAbsolutePath().normalize();
        return path;
    }

    public static String fileType(String filePath) throws FileNotFoundException, IOException, UnrecognizableTypeFileException {
        Path path = stringToPath(filePath);
        String type = Files.probeContentType(path);
        if(type == null){
            throw new UnrecognizableTypeFileException(path);
        }
        return type;
    
    }


	public static boolean isAnOdtFile(String filePath){
        boolean isOdt= false;
       try{ 
            String type = fileType(filePath.toString());
            isOdt = type.equals("application/vnd.oasis.opendocument.text"); 
        }catch(UnrecognizableTypeFileException e){
        }catch(FileNotFoundException e){
            System.err.println(e.getMessage());
        }catch(IOException e){
            System.err.println(e.getMessage());
        }
        return isOdt; 
    }

	public static boolean isAnOdfFile(String filePath){
        boolean isOdf= false;
        try{
            String type = fileType(filePath.toString());
            isOdf = type.contains("application/vnd.oasis.opendocument");
        }catch(UnrecognizableTypeFileException e){
        }catch(FileNotFoundException e){
            System.err.println(e.getMessage());
        }catch(IOException e){
            System.err.println(e.getMessage());
        }
        return isOdf;
    }

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
	}

	public StringBuilder operationOnOdfFile() throws Exception{
		OdfFile file = isAnOdtFile(fileToOperateOn.toString())? new OdtFile(fileToOperateOn) : new OdfFile(fileToOperateOn);
		StringBuilder msg = new StringBuilder();
		switch(actionToPerform){
			case CHANGE_THE_TITLE_OF_AN_ODF_FILE:
				file.setMetaData(MetaDataTitle.ATTR, args[3]);
				msg.append("Title changed!");
				break;
    		case CHANGE_THE_SUBJECT_OF_AN_ODF_FILE:
				file.setMetaData(MetaDataSubject.ATTR, args[3]);
				msg.append("Subject added!");
				break;
    		case ADD_A_KEYWORD_TO_AN_ODF_FILE:
				System.out.println("Have to implement the adding of keywords");
				// file.setMetaData(MetaDataKeyword.ATTR, args[3]);
				msg.append("Keyword added!");
				break;
    		case CHANGE_THE_COMMENTS_OF_AN_ODF_FILE:
				file.setMetaData(MetaDataComment.ATTR, args[3]);
				msg.append("Comments changed!");
				break;
			case CHANGE_THE_CREATOR_OF_AN_ODF_FILE:
				file.setMetaData(MetaDataCreator.ATTR, args[3]);
				msg.append("Creator changed!");
				break;
			case REPLACE_THE_DESCRIPTION_OF_AN_ODF_FILE:
				msg.append("To change the description of "+ fileToOperateOn.getFileName() +" call replaceTheDescriptionOfAnOdtFile(String newTitle, String newSubject, String newKeyword, String newComments).");
				break;
			default:
				throw new Exception("Something went wrong...");
		}
		file.saveChange();
		return msg;
	}

	public boolean wantToDisplayMetadata(){
		return actionToPerform == Command.DISPLAY_THE_META_DATA_OF_AN_ODF_FILE || actionToPerform ==  Command.DISPLAY_THE_META_DATA_A_FILE || actionToPerform == Command.DISPLAY_THE_META_DATA_OF_ODF_FILES_IN_A_DIRECTORY;
	}


	public StringBuilder launchCore() throws Exception{
		return wantToDisplayMetadata()? displayMetaData() : operationOnOdfFile();
	}

}
