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
    public static final int NUMBER_SYMBOLE = 100; 

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

    public static void printProperly(String result){
        System.out.println("");
        int depth =0;
        char[] charArray = result.toCharArray();
        for(char singleChar :  charArray){
            switch(singleChar){
                case '{':
                    depth ++;
                    ligne();
                    System.out.print(properTabulation(depth));
                    break;
                case '*':
                    depth ++;
                    System.out.print(properTabulation(depth)+"- click : ");
                    break;
                case '<':
                    depth ++;
                    System.out.print(properTabulation(depth));
                    break;
                case '}': case '>': case']': case '?': 
                    depth--;
                    break;
                case ',' :
                    System.out.print(properTabulation(depth));
                    break;
                case '"':
                    ligne();
                    System.out.print(properTabulation(depth+1));
                    break;
                default:
                    System.out.print(singleChar);
            }
        }
    }


    public static String properTabulation(int numberOfTab){
        StringBuilder tabulation = new StringBuilder("\n");
        while(numberOfTab > 0){
            tabulation.append("\t");
            numberOfTab--;
        }
        return tabulation.toString();
    } 

    public static void ligne(){
        System.out.println("");
        for(int time=0; time<NUMBER_SYMBOLE; time++){
            System.out.print("_");
        }
    }

    public static void main(String[] args) throws Exception{
        System.out.println("JAVA-POO PROJECT: ");
        CommandTranslator askedActionToPerform = new CommandTranslator(args);
        System.out.printf(askedActionToPerform.translate().toString());
    //     try{
    //         Command actionToPerform = askedActionToPerform.translate();
    //         if(actionToPerform == Command.DISPLAY_HELP){
    //             help();
    //         }else if(actionToPerform == Command.REPLACE_THE_DESCRIPTION_OF_AN_ODF_FILE){
    //             changeTheDescriptionOfAnOdtFile(actionToPerform, CommandTranslator.stringToPath(args[1]), args);
    //         }else{
    //             ibarODFCore ibar = new ibarODFCore(actionToPerform, CommandTranslator.stringToPath(args[1]), args);
    //             printProperly(ibar.launchCore().toString());
    //             properTabulation(2);
    //         }
    //     }catch(NotAllowedCommandException e){
    //         System.out.println(e.getMessage());
    //     }catch(FileNotFoundException e){
    //         System.out.println(e.getMessage());
    //     }catch(org.odftoolkit.odfdom.pkg.OdfValidationException e){
    //         System.err.println("Is not  REAL odtFile.");
    //     }
    //     catch(Exception e){
    //         System.err.println("Something went wrong...");
    //     }
    }
}
