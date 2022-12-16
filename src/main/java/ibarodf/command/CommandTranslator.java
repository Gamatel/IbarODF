package ibarodf.command;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

import ibarodf.core.IbarOdfCore;
import ibarodf.core.file.UnrecognizableTypeFileException;


/**
 * It takes a command line argument and returns the corresponding command
 */

public class CommandTranslator {
    private final String[] command;

    public enum Command {
        DISPLAY_HELP,
        DISPLAY_THE_META_DATA_A_FILE,
        DISPLAY_THE_META_DATA_OF_AN_ODF_FILE,
        CHANGE_THE_TITLE_OF_AN_ODF_FILE,
        CHANGE_THE_SUBJECT_OF_AN_ODF_FILE,
        CHANGE_THE_KEYWORDS_TO_AN_ODF_FILE,
        CHANGE_THE_COMMENTS_OF_AN_ODF_FILE,
        CHANGE_THE_CREATOR_OF_AN_ODF_FILE,
        CHANGE_THE_DESCRIPTION_OF_AN_ODF_FILE,
        DISPLAY_THE_META_DATA_OF_ODF_FILES_IN_A_DIRECTORY,
        DISPLAY_THE_META_DATA_OF_ODF_FILES_IN_A_DIRECTORY_RECURSIVELY
    
    }   

    public CommandTranslator(String[] command){
        this.command = command;
    }

/**
 * `isAskingForHelp` returns true if the command is `Command.DISPLAY_HELP` and false otherwise
 * 
 * @param command The command that the user has entered.
 * @return A boolean value.
 */
    public static boolean isAskingForHelp(Command command){
        return command == Command.DISPLAY_HELP;
    }
    
    /**
     * Returns true if the user ask to display informations and false otherwise
     * 
     * @param command The command that the user has chosen to execute.
     * @return The method is returning a boolean value.
     */
    public static boolean isAskingToDisplayMetadata(Command command){
        return command == Command.DISPLAY_THE_META_DATA_A_FILE||command == Command.DISPLAY_THE_META_DATA_OF_AN_ODF_FILE
        ||command == Command.DISPLAY_THE_META_DATA_OF_ODF_FILES_IN_A_DIRECTORY|| command == Command.DISPLAY_THE_META_DATA_OF_ODF_FILES_IN_A_DIRECTORY_RECURSIVELY;
    }
    
    /**
     * This function returns true if the command is asking to change the description of an ODF file.
     * 
     * @param command The command that the user wants to execute.
     * @return A boolean value.
     */
    public static boolean isAskingToChangeTheDescriptionOfAnOdfFile(Command command){
        return command == Command.CHANGE_THE_DESCRIPTION_OF_AN_ODF_FILE;
    }

    /**
     * It returns true if the command is asking to change the metadata of an ODF file
     * 
     * @param command The command that the user wants to execute.
     * @return The method is returning a boolean value.
     */
    public static boolean isAskingToChangeMetadataOnOdfFile(Command command){
        return command == Command.CHANGE_THE_COMMENTS_OF_AN_ODF_FILE || command == Command.CHANGE_THE_CREATOR_OF_AN_ODF_FILE
        || command == Command.CHANGE_THE_KEYWORDS_TO_AN_ODF_FILE || command == Command.CHANGE_THE_SUBJECT_OF_AN_ODF_FILE ||command == Command.CHANGE_THE_TITLE_OF_AN_ODF_FILE;
    }

   
    private boolean isAskingForHelp(){
        return command.length == 1 && ((command[0].equals("-h") || command[0].equals("--help"))); 
    }
    
    private boolean isAskingToDisplayMetaDataOfAFile(){
        return command.length == 2  && ((command[0].equals("-f") || command[0].equals("--file")));  
    }
    
    private boolean isAskingToOperateOnADirectory(){
        return (command.length == 2 || command.length ==  3) && ((command[0].equals("-d") || command[0].equals("--directory"))); 
    }
    
    private boolean isAskingToDisplayMetaDataOfOdtFilesInADirectory(){
        return command.length == 2 && isAskingToOperateOnADirectory();  
    }

    private boolean isAskingToDisplayMetaDataOfOdtFilesInADirectoryRecursively(){
        return command.length ==  3 && isAskingToOperateOnADirectory() && 
        ((command[2].equals("-r")|| command[2].equals("--recursively")));
    }

    
    private boolean isAskingToOperateOnAFile(){
        return command.length >= 2 && command.length <= 4  && ((command[0].equals("-f") || command[0].equals("--file")));
    }
    
    private boolean isAskingToChangeASpecificMetadataOfAFile(){
        return command.length == 4 && isAskingToOperateOnAFile();
    }
    
    private boolean isAskingToChangeTheTitleOfAFile(){
        return isAskingToChangeASpecificMetadataOfAFile() && ((command[2].equals("-t") || command[2].equals("--title")));
    }
    
    private boolean isAskingToAddASubjectToAFile(){
        return isAskingToChangeASpecificMetadataOfAFile() && ((command[2].equals("-s") || command[2].equals("--subject")));
    }

    private boolean isAskingToChangeTheKeywordsOfAFile(){
        return isAskingToChangeASpecificMetadataOfAFile() && ((command[2].equals("-k") || command[2].equals("--keyword")));
    }

    private boolean isAskingToChangeTheCommentsOfAFile(){
        return isAskingToChangeASpecificMetadataOfAFile() && ((command[2].equals("-co") || command[2].equals("--comments")));
    }

    private boolean isAskingToChangeTheCreatorOfAFile(){
        return isAskingToChangeASpecificMetadataOfAFile() && ((command[2].equals("-cr") || command[2].equals("--creator")));
    }

    private boolean isAskingToChangeTheDescriptionOfAFile(){
        return isAskingToOperateOnAFile() && command.length == 3  && (command[2].equals("-de") || command[2].equals("--description"));
    }    
    

    private Command actionToPerformOnAnOdfFile() throws UnallowedCommandException{
        if(isAskingToDisplayMetaDataOfAFile()){
            return Command.DISPLAY_THE_META_DATA_OF_AN_ODF_FILE;
        }else if(isAskingToChangeTheTitleOfAFile()){
            return Command.CHANGE_THE_TITLE_OF_AN_ODF_FILE;
        }else if(isAskingToAddASubjectToAFile()){
            return Command.CHANGE_THE_SUBJECT_OF_AN_ODF_FILE;
        }else if(isAskingToChangeTheKeywordsOfAFile()){
            return Command.CHANGE_THE_KEYWORDS_TO_AN_ODF_FILE;
        }else if(isAskingToChangeTheCommentsOfAFile()){
            return Command.CHANGE_THE_COMMENTS_OF_AN_ODF_FILE;
        }else if(isAskingToChangeTheCreatorOfAFile()){
            return Command.CHANGE_THE_CREATOR_OF_AN_ODF_FILE;
        }else if(isAskingToChangeTheDescriptionOfAFile()){
            return Command.CHANGE_THE_DESCRIPTION_OF_AN_ODF_FILE;
        }
        throw new UnallowedCommandException("unknown command.");
    }

    private Command actionToPerformOnFile() throws UnallowedCommandException{
        if(isAskingToDisplayMetaDataOfAFile()) {
            return Command.DISPLAY_THE_META_DATA_A_FILE;
        }
        throw new UnallowedCommandException("cannot perform such operation on a non ODT file.");
    }

    private Command actionToPerformOnADirectory() throws UnallowedCommandException{
        if(isAskingToDisplayMetaDataOfOdtFilesInADirectory()){
            return Command.DISPLAY_THE_META_DATA_OF_ODF_FILES_IN_A_DIRECTORY;
        }else if(isAskingToDisplayMetaDataOfOdtFilesInADirectoryRecursively()){
            return Command.DISPLAY_THE_META_DATA_OF_ODF_FILES_IN_A_DIRECTORY_RECURSIVELY;
        }else if(isAskingToOperateOnAFile()){
            throw new UnallowedCommandException(" current file is a directory.");            
        }
        throw new UnallowedCommandException("cannot perform such operation on a directory.");
    }

    private boolean isValidLength() throws UnallowedCommandException{
        if(command.length ==0 || command.length >=4){
            throw new UnallowedCommandException("Illegal argument");
        }if(command.length == 1 && !isAskingForHelp()){
            throw new UnallowedCommandException("Unknown command");
        }
        return true;
    }

    /**
     * It takes a command line argument and returns the corresponding command
     * 
     * @return A Command object.
     * @throws UnallowedCommandException
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public Command translate() throws UnallowedCommandException, FileNotFoundException, IOException{
        try{
            isValidLength(); 
            if(isAskingForHelp()){
                return Command.DISPLAY_HELP;
            }
            String filePath = command[1];
            Command askedCommand;
            File file = new File(filePath);
            if(!file.exists()){
                throw new FileNotFoundException("the file "+filePath+ " does not exist");
            }else 
            if(Files.isDirectory(IbarOdfCore.stringToPath(filePath))){
                askedCommand = actionToPerformOnADirectory();
            }else if(IbarOdfCore.isAnOdfFile(filePath)){
                askedCommand = actionToPerformOnAnOdfFile();
            }else if(isAskingToOperateOnAFile()){
                askedCommand =  actionToPerformOnFile(); 
            }else{
                throw new UnallowedCommandException("unknown command.");
            }return askedCommand;
        }catch(UnrecognizableTypeFileException e){
            return Command.DISPLAY_THE_META_DATA_A_FILE;
        }

    }
    
}
