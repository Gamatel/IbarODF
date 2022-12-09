package ibarodf.command;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

import ibarodf.core.Command;
import ibarodf.core.IbarODFCore;



public class CommandTranslator {
    private final String[] command;
    public CommandTranslator(String[] command){
        this.command = command;
    }

    private boolean isAskingForHelp(){
        return command.length==1 && (command[0].equals("-h") || command[0].equals("--help")); 
    }
    
    private boolean isAskingToDisplayMetaDataOfAFile(){
        return command.length ==2 && (command[0].equals("-f") || command[0].equals("--file"));  
    }
    
    private boolean isAskingToDisplayMetaDataOfOdtFilesInADirectory(){
        return command.length ==2 && (command[0].equals("-d") || command[0].equals("--directory"));  
    }
    
    
    private boolean isAskingToOperateOnAFile(){
        return (command.length >= 2 && command.length <= 4) && (command[0].equals("-f") || command[0].equals("--file"));
    }
    
    private boolean isAskingToChangeTheTitleOfAFile(){
        return isAskingToOperateOnAFile() && (command[2].equals("-t") || command[2].equals("--title"));
    }
    
    private boolean isAskingToAddASubjectToAFile(){
        return isAskingToOperateOnAFile() && (command[2].equals("-s") || command[2].equals("--subject"));
    }
    
    private boolean isAskingToAddAKeywordToAFile(){
        return isAskingToOperateOnAFile() && (command[2].equals("-Ak") || command[2].equals("--add-keyword"));
    }

    private boolean isAskingToReplaceTheKeywordsOfAFile(){
        return isAskingToOperateOnAFile() && (command[2].equals("-Rk") || command[2].equals("--replace-keyword"));
    }

    private boolean isAskingToChangeTheCommentsOfAFile(){
        return isAskingToOperateOnAFile() && (command[2].equals("-co") || command[2].equals("--comments"));
    }

    private boolean isAskingToChangeTheCreatorOfAFile(){
        return isAskingToOperateOnAFile() && (command[2].equals("-cr") || command[2].equals("--creator"));
    }

    private boolean isAskingToChangeTheDescriptionOfAFile(){
        return isAskingToOperateOnAFile() && (command[2].equals("-de") || command[2].equals("--description"));
    }
    



    public Command actionToPerformOnAnOdfFile() throws NotAllowedCommandException{
        if(isAskingToDisplayMetaDataOfAFile()){
            return Command.DISPLAY_THE_META_DATA_OF_AN_ODF_FILE;
        }else if(isAskingToChangeTheTitleOfAFile()){
            return Command.CHANGE_THE_TITLE_OF_AN_ODF_FILE;
        }else if(isAskingToAddASubjectToAFile()){
            return Command.CHANGE_THE_SUBJECT_OF_AN_ODF_FILE;
        }else if(isAskingToAddAKeywordToAFile()){
            return Command.ADD_A_KEYWORD_TO_AN_ODF_FILE;
        }else if(isAskingToReplaceTheKeywordsOfAFile()){
            return Command.REPLACE_KEYWORDS_TO_AN_ODF_FILE;
        }else if(isAskingToChangeTheCommentsOfAFile()){
            return Command.CHANGE_THE_COMMENTS_OF_AN_ODF_FILE;
        }else if(isAskingToChangeTheCreatorOfAFile()){
            return Command.CHANGE_THE_CREATOR_OF_AN_ODF_FILE;
        }else if(isAskingToChangeTheDescriptionOfAFile()){
            return Command.REPLACE_THE_DESCRIPTION_OF_AN_ODF_FILE;
        }
        throw new NotAllowedCommandException("unknown command.");
    }

    public Command actionToPerformOnFile() throws NotAllowedCommandException{
        if(isAskingToDisplayMetaDataOfAFile()) {
            return Command.DISPLAY_THE_META_DATA_A_FILE;
        }
        throw new NotAllowedCommandException("cannot perform such operation on a non ODT file.");
    }

    public Command actionToPerformOnADirectory() throws NotAllowedCommandException{
        if(isAskingToDisplayMetaDataOfOdtFilesInADirectory()){
            return Command.DISPLAY_THE_META_DATA_OF_ODF_FILES_IN_A_DIRECTORY;
        }else if(isAskingToOperateOnAFile()){
            throw new NotAllowedCommandException(" current file is a directory.");            
        }
        throw new NotAllowedCommandException("cannot perform such operation on a directory.");
    }

    public Command translate() throws NotAllowedCommandException, IOException{
        if(command.length == 0){
            throw new NotAllowedCommandException("no arguments.");
        }else if(isAskingForHelp()){
            return Command.DISPLAY_HELP;
        }else if(command.length==1 || command.length>4){
            throw new NotAllowedCommandException("unknown command.");
        }
        String filePath = command[1];
        File file = new File(filePath);
        Command askedCommand = null;
        if(!file.exists()){
            throw new FileNotFoundException("the file "+filePath+ " does not exist");
        }else if(Files.isDirectory(IbarODFCore.stringToPath(filePath))){
            askedCommand = actionToPerformOnADirectory();
        }else if(IbarODFCore.isAnOdfFile(filePath)){
            askedCommand = actionToPerformOnAnOdfFile();
        }else if(isAskingToOperateOnAFile()){
            askedCommand =  actionToPerformOnFile(); 
        }
        if(askedCommand==null){throw new NotAllowedCommandException("unknown command.");}
        return askedCommand;

    }

    
}
