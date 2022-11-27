package ibarodf;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

import ibarodf.command.*;
import ibarodf.core.ibarODFCore;

public class MainCli {

    public static void help(){
        try{
            BufferedReader helpReader = new BufferedReader(new FileReader("help.txt"));
            String line;
            while((line = helpReader.readLine()) != null){
                System.out.println(line);
            } 
            helpReader.close();
        }catch(IOException e){
            System.err.println("Sorry, cannot reach the help manual.");
        }
    }

    public static void changeTheDescriptionOfAnOdtFile(Command actionToPerform, Path fileToOperateOn, String[] args) throws Exception{
        Scanner scanEntry = new Scanner(System.in);
        String title, subject, keywords, comments;
        System.out.println("Enter the new title :");
        title = scanEntry.nextLine();
        System.out.println("Enter the new subject :");
        subject = scanEntry.nextLine();
        System.out.println("Enter the new keywords :");
        keywords = scanEntry.nextLine();
        System.out.println("Enter the new comments :");
        comments = scanEntry.nextLine();
        scanEntry.close();
        ibarODFCore ibar = new ibarODFCore(actionToPerform, fileToOperateOn, args);
        ibar.replaceTheDescriptionOfAnOdtFile(title, subject, keywords, comments);
    }


    public static void main(String[] args){
        System.out.println("JAVA-POO PROJECT: ");
        CommandTranslator askedActionToPerform = new CommandTranslator(args);
        try{
            Command actionToPerform = askedActionToPerform.translate();
            if(actionToPerform == Command.DISPLAY_HELP){
                help();
            }else if(actionToPerform == Command.REPLACE_THE_DESCRIPTION_OF_AN_ODT_FILE){
                changeTheDescriptionOfAnOdtFile(actionToPerform, CommandTranslator.stringToPath(args[1]), args);
            }else{
                ibarODFCore ibar = new ibarODFCore(actionToPerform, CommandTranslator.stringToPath(args[1]), args);
                System.out.println(ibar.launchCore());
            }
        }catch(NotAllowedCommandException e){
            System.out.println(e.getMessage());
        }catch(FileNotFoundException e){
            System.out.println(e.getMessage());
        }catch(org.odftoolkit.odfdom.pkg.OdfValidationException e){
            System.err.println("Is not  REAL odtFile.");
        }
        catch(Exception e){
            System.err.println("Something went wrong...");
        }
    }
}
