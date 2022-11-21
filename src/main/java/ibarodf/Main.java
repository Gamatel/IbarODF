package ibarodf;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import ibarodf.command.*;


public class Main {

    public static void help(){
        try{
            BufferedReader helpReader = new BufferedReader(new FileReader("src\\main\\java\\ibarodf\\help.txt"));
            String line;
            while((line = helpReader.readLine()) != null){
                System.out.println(line);
            } 
            helpReader.close();
        }catch(IOException e){
            System.err.println("Sorry, cannot reach the help manual.");
        }
    }

    public static void main(String[] args){
        System.out.println("JAVA-POO PROJECT: ");
        CommandTranslator askedActionToPerform = new CommandTranslator(args);
        try{
            Command actionToPerform = askedActionToPerform.translate();
            if(actionToPerform == Command.DISPLAY_HELP){
                help();
            }else{// Path
                System.out.println("Instanciation of IbarOdf :\nAction to perform : "+ actionToPerform);
                System.out.println("on a the file of path : "+ CommandTranslator.stringToPath(args[1]));
                

            }
        }catch(NotAllowedCommandException e){
            System.out.println(e.getMessage());
        }catch(FileNotFoundException e){
            System.out.println(e.getMessage());
        }catch(Exception e){
            System.err.println("Something went wrong...");
            e.printStackTrace();
        }
    }
}
