package ibarodf.core;

import java.nio.file.Path;

import org.apache.jena.util.Metadata;

import ibarodf.command.Command;
import ibarodf.core.file.Directory;
import ibarodf.core.file.NotOdtFile;
import ibarodf.core.file.OdfFile;
import ibarodf.core.meta.MetaDataDescription;
import ibarodf.core.meta.MetaDataSubject;
import ibarodf.core.meta.MetaDataTitle;

public class ibarODFCore {
	private Command actionToPerform;
	private Path fileToOperateOn;
	private String[] args;
	public ibarODFCore(Command actionToPerform, Path fileToOperateOn, String[] args) {
		this.actionToPerform = actionToPerform;
		this.fileToOperateOn = fileToOperateOn;
		this.args = args;
	}

	public StringBuilder displayTheMetaDataOfOdtFileInADirectory(){
		Directory directory = new Directory(fileToOperateOn);
		return directory.displayMetaData();
	}

	public StringBuilder displayTheMetaDataOfAFile(){
		NotOdtFile notOdtFile = new NotOdtFile(fileToOperateOn);
		return notOdtFile.displayMetaData();
	}

	public StringBuilder diplayTheMetatDataOfAnOdtFile() throws Exception{
		OdfFile odtFile = new OdfFile(fileToOperateOn); 
		return odtFile.displayMetaData();
	}


	public void changeTheTitleOfAnOdtFile() throws Exception{
		OdfFile file = new OdfFile(fileToOperateOn);
		file.setMetaData(MetaDataTitle.ATTR, args[3]);
		file.saveChange();
	}

	public void addASubjectToAnOdtFile() throws Exception{
		OdfFile file = new OdfFile(fileToOperateOn);
		file.setMetaData(MetaDataTitle.ATTR, args[3]);
		file.saveChange();
	}

	public void addAKeywordToAnOdtFile() throws Exception{

	}



	public StringBuilder displayMetaData() throws Exception{
		switch(actionToPerform){
			case DISPLAY_THE_META_DATA_OF_A_FILE:
				return displayTheMetaDataOfAFile();
			case DISPLAY_THE_META_DATA_OF_ODT_FILES_IN_A_DIRECTORY:
				return displayTheMetaDataOfOdtFileInADirectory();
			case DISPLAY_THE_META_DATA_OF_AN_ODT_FILE:
				return diplayTheMetatDataOfAnOdtFile();
			default:
				throw new Exception("Something went wrong...");
		}
	}

	public StringBuilder operationOnOdtFile() throws Exception{
		OdfFile file = new OdfFile(fileToOperateOn);
		StringBuilder msg = new StringBuilder();
		switch(actionToPerform){
			case CHANGE_THE_TITLE_OF_AN_ODT_FILE:
				file.setMetaData(MetaDataTitle.ATTR, args[3]);
				msg.append("Title changed!");
				break;
    		case ADD_A_NEW_SUBJECT_TO_AN_ODT_FILE:
				file.setMetaData(MetaDataSubject.ATTR, args[3]);
				msg.append("Subject added!");
				break;
    		case ADD_A_KEYWORD_TO_AN_ODT_FILE:
				System.out.println("Have to implemente adding a keyword!!!");
				// file.setMetaData(MetaDataKeyword.ATTR, args[3]);
				msg.append("Keyword added!");
				break;
    		case REPLACE_THE_DESCRIPTION_OF_AN_ODT_FILE:
				file.setMetaData(MetaDataDescription.ATTR, args[3]);
				msg.append("Description changed!");
				break;
			default:
				throw new Exception("Something went wrong...");
		}
		file.saveChange();
		return msg;
	}

	public boolean wantToDisplayMetadata(){
		return actionToPerform == Command.DISPLAY_THE_META_DATA_OF_AN_ODT_FILE || actionToPerform ==  Command.DISPLAY_THE_META_DATA_OF_A_FILE || actionToPerform == Command.DISPLAY_THE_META_DATA_OF_ODT_FILES_IN_A_DIRECTORY; 
	}


	public StringBuilder launchCore() throws Exception{
		return wantToDisplayMetadata()? displayMetaData() : operationOnOdtFile();

	}

}
