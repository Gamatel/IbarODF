package ibarodf.command;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

import org.odftoolkit.odfdom.type.Length;

import ibarodf.core.AbtractIbarOdfCore;



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
        DISPLAY_THE_META_DATA_OF_ODF_FILES_IN_A_DIRECTORY
    
    }   



    public CommandTranslator(String[] command){
        this.command = command;
    }

    public static boolean isAskingForHelp(Command command){
        return command == Command.DISPLAY_HELP;
    }
    
    public static boolean isAskingToDisplayMetadata(Command command){
        return (command == Command.DISPLAY_THE_META_DATA_A_FILE) ||( command == Command.DISPLAY_THE_META_DATA_OF_AN_ODF_FILE) ||(command == Command.DISPLAY_THE_META_DATA_OF_ODF_FILES_IN_A_DIRECTORY);
    }
    
    public static boolean isAskingToChangeTheDescriptionOfAnOdfFile(Command command){
        return command == Command.CHANGE_THE_DESCRIPTION_OF_AN_ODF_FILE;
    }

    public static boolean isAskingToChangeMetadataOnOdfFile(Command command){
        return command == Command.CHANGE_THE_COMMENTS_OF_AN_ODF_FILE || command == Command.CHANGE_THE_CREATOR_OF_AN_ODF_FILE
        || command == Command.CHANGE_THE_KEYWORDS_TO_AN_ODF_FILE || command == Command.CHANGE_THE_SUBJECT_OF_AN_ODF_FILE ||command == Command.CHANGE_THE_TITLE_OF_AN_ODF_FILE;
    }

    public void testProperLength()throws UnallowedCommandException {
        if(command.length <= 0){
            throw new UnallowedCommandException();
        }else if((command.length == 1 && !isAskingForHelp()) || command.length>4){
            throw new UnallowedCommandException();
        }else if((command.length == 3 && !isAskingToChangeTheDescriptionOfAFile())){
            throw new UnallowedCommandException();
        }
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

    private boolean isAskingToChangeTheKeywordsOfAFile(){
        return isAskingToOperateOnAFile() && (command[2].equals("-k") || command[2].equals("--keyword"));
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
        }else if(isAskingToOperateOnAFile()){
            throw new UnallowedCommandException(" current file is a directory.");            
        }
        throw new UnallowedCommandException("cannot perform such operation on a directory.");
    }

    /**
     * It takes a command line argument and returns the corresponding command
     * 
     * @return A Command object.
     * @throws UnallowedCommandException
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public Command translate() throws UnallowedCommandException, FileNotFoundException{
        testProperLength();
        if(isAskingForHelp()){
            return Command.DISPLAY_HELP;
        }
        String filePath = command[1];
        Command askedCommand;
        /* if(!file.exists()){
            throw new FileNotFoundException("the file "+filePath+ " does not exist");
        }else  */
        if(Files.isDirectory(AbtractIbarOdfCore.stringToPath(filePath))){
            askedCommand = actionToPerformOnADirectory();
        }else if(AbtractIbarOdfCore.isAnOdfFile(filePath)){
            askedCommand = actionToPerformOnAnOdfFile();
        }else if(isAskingToOperateOnAFile()){
            askedCommand =  actionToPerformOnFile(); 
        }else{
            throw new UnallowedCommandException("unknown command.");
        }return askedCommand;
    }

    
}
