package ibarodf.command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Scanner;
import java.util.stream.Stream;

import ibarodf.command.CommandTranslator.Command;
import ibarodf.command.exception.UnallowedCommandException;
import ibarodf.core.IbarOdfCore;
import ibarodf.exception.UnableToChangeTheDescriptionOfTheFileException;
import ibarodf.exception.UnableToDisplayInformationAboutTheFile;
import ibarodf.exception.UnableToMakeAskedChangesException;

public class MainCli {
    
    /**
     * It asks the user to enter a new title, subject, keyword and comment, to change the description of
     * an ODF file
     * 
     * @param filePath The path of the ODF file to be modified.
     */
    public static void changeTheDescription(Path filePath) throws UnableToChangeTheDescriptionOfTheFileException{
        try{
            PrettyResult.ligne();
            String newTitle, newSubject, newKeyword, newComment;     
            Scanner scanEntry = new Scanner(System.in);
            System.out.println("Enter the new title");
            newTitle = scanEntry.nextLine();
            System.out.println("Enter the new subject");
            newSubject = scanEntry.nextLine();
            System.out.println("Enter the new keyword");
            newKeyword = scanEntry.nextLine();
            System.out.println("Enter the new comment");
            newComment = scanEntry.nextLine();
            scanEntry.close();
            PrettyResult.ligne();
            IbarOdfCore.changeTheDescriptionOfAnOdtFile(filePath, newTitle, newSubject, newKeyword, newComment);
            System.out.println("Description changed!");
        }catch(Exception e){
            throw new UnableToChangeTheDescriptionOfTheFileException(filePath);
        }
    }

    /**
     *  It displays metadata about either a file or a directory
     * 
     * @param command The command that was given to the program.
     * @param filePath The path to the file or directory
     * @throws FileNotFoundException
     */
    public static void displayMetadata(Command command, Path filePath) throws UnableToDisplayInformationAboutTheFile, FileNotFoundException{
        try{ 
            switch(command){
                case DISPLAY_THE_META_DATA_A_FILE:
                    PrettyResult.ligne();
                    // System.out.println(IbarOdfCore.RegularFileToJson(filePath));
                    PrettyResult.prettyFile(IbarOdfCore.RegularFileToJson(filePath));
                    PrettyResult.ligne();
                    break;
                case DISPLAY_THE_META_DATA_OF_AN_ODF_FILE: 
                    PrettyResult.ligne();
                    // System.out.println(IbarOdfCore.odfFileToJson(filePath));
                    PrettyResult.prettyOdfFile(IbarOdfCore.odfFileToJson(filePath));
                    PrettyResult.ligne();
                    break;
                case DISPLAY_THE_META_DATA_OF_ODF_FILES_IN_A_DIRECTORY:
                    // System.out.println(IbarOdfCore.directoryToJson(filePath));
                    PrettyResult.prettyDirectory(IbarOdfCore.directoryToJson(filePath));
                    // IbarOdfCore.directoryToJson(filePath, true);
                    break;
                case DISPLAY_THE_META_DATA_OF_ODF_FILES_IN_A_DIRECTORY_RECURSIVELY:
                    // System.out.println(IbarOdfCore.directoryToJson(filePath, true));
                    PrettyResult.prettyDirectory(IbarOdfCore.directoryToJson(filePath, true)); 
                    // IbarOdfCore.directoryToJson(filePath, true);
                    break;
                default:
                    System.err.println("Error : Wasn't asking to display information about "+ filePath.getFileName());
            }
        }catch(FileNotFoundException e){
            throw new FileNotFoundException("Cannot find "+ filePath);
        }catch(Exception e){
            throw new UnableToDisplayInformationAboutTheFile(filePath);
        }
    } 


    /**
     * It changes the asked metadata of an ODF file
     * 
     * @param command The command to execute.
     * @param filePath The path to the file you want to change the metadata of.
     * @param newValue The new value to be set
     */
    public static void changeMetadata(Command command, Path filePath, String newValue) throws UnableToMakeAskedChangesException{    
        try{
            switch(command){
                case CHANGE_THE_TITLE_OF_AN_ODF_FILE:
                    IbarOdfCore.changeTheTitleOfAnOdfFile(filePath, newValue);
                    System.out.println("Title changed!");
                    break;
                case CHANGE_THE_SUBJECT_OF_AN_ODF_FILE:
                    IbarOdfCore.changeTheSubjectOfAnOdfFile(filePath, newValue);
                    System.out.println("Subject changed!");
                    break; 
                case CHANGE_THE_KEYWORDS_TO_AN_ODF_FILE:
                    IbarOdfCore.changeTheKeywordsOfAnOdfFile(filePath, newValue);
                    System.out.println("Keywords changed!");
                    break;
                case CHANGE_THE_COMMENTS_OF_AN_ODF_FILE:
                    IbarOdfCore.changeTheCommentsOfAnOdfFile(filePath, newValue);
                    System.out.println("Comments changed!");
                    break; 
                case CHANGE_THE_CREATOR_OF_AN_ODF_FILE:
                    IbarOdfCore.changeTheCreatorOfAnOdfFile(filePath,newValue);
                    System.out.println("Creator changed!");
                    break; 
                default :
                    System.err.println("Error : Wasn't asking to make any change on "+ filePath.getFileName());
            }
        }catch(Exception e){
            throw new UnableToMakeAskedChangesException(filePath);
        }
    }

    /**
     * It reads the help file and prints it to the console
     */
    public static void help(){
		try{
            BufferedReader helpReader = new BufferedReader(new FileReader(new File("src/main/resources/help.txt")));
			String line;
			while((line = helpReader.readLine()) != null){
				System.out.println(line);
			} 
			helpReader.close();
		}catch(Exception e){
            System.err.println(e.getMessage());
			System.err.println("Sorry, cannot reach the help manual.");
		}
	}

    public static void main(String[] args){
        CommandTranslator askedActionToPerform = new CommandTranslator(args);
        try{
            Command actionToPerform = askedActionToPerform.translate();
            if(CommandTranslator.isAskingForHelp(actionToPerform)){
                help();
            }else{
                Path path = IbarOdfCore.stringToPath(args[1]);
                if(CommandTranslator.isAskingToDisplayMetadata(actionToPerform)){
                    displayMetadata(actionToPerform,path); 
                }else if(CommandTranslator.isAskingToChangeTheDescriptionOfAnOdfFile(actionToPerform)){
                    changeTheDescription(path);
                }else if(CommandTranslator.isAskingToChangeMetadataOnOdfFile(actionToPerform)){
                    changeMetadata(actionToPerform, path, args[3]);
                }
            }
        }catch(UnableToChangeTheDescriptionOfTheFileException | UnableToDisplayInformationAboutTheFile| UnableToMakeAskedChangesException e ){
            System.err.println(e.getMessage());
            e.getStackTrace(); 
        }catch(UnallowedCommandException | FileNotFoundException e ){
            e.getStackTrace(); 
            System.err.println(e.getMessage());
        }catch(Exception e){
            e.getStackTrace(); 
            System.err.println(e.getMessage());
        }
    }
}
