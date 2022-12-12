package ibarodf;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Scanner;

import javax.json.JsonException;

import org.json.JSONArray;
import org.json.JSONObject;

import ibarodf.command.*;
import ibarodf.core.Command;
import ibarodf.core.IbarODFCore;
import ibarodf.core.file.Directory;
import ibarodf.core.file.OdfFile;
import ibarodf.core.meta.MetaDataHyperlink;
import ibarodf.core.meta.MetaDataOdfPictures;
import ibarodf.core.meta.MetaDataStats;

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
        System.out.println("\nEnter the new title :");
        title = scanEntry.nextLine();
        System.out.println("Enter the new subject :");
        subject = scanEntry.nextLine();
        System.out.println("Enter the new keywords :");
        keywords = scanEntry.nextLine();
        System.out.println("Enter the new comments :");
        comments = scanEntry.nextLine();
        scanEntry.close();
        IbarODFCore ibar = new IbarODFCore(actionToPerform, fileToOperateOn, args);
        ibar.replaceTheDescriptionOfAnOdtFile(title, subject, keywords, comments);
    }

    public static void printProperly(String result){
        System.out.println();
        int depth =0;
        char[] charArray = result.toCharArray();
        for(char singleChar :  charArray){
            switch(singleChar){
                case '{':
                    depth ++;
                    ligne();
                    System.out.print(properTabulation(depth));
                    break;
                case '<': case '[':
                    depth ++;
                    System.out.print(properTabulation(depth));
                    break;
                case '}': case '>': case ']': 
                    depth--;
                    break;
                case ';' :
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
        System.out.println();
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
        System.out.println();
        for(int time=0; time<NUMBER_SYMBOLE; time++){
            System.out.print("-");
        }
        System.out.println();
    }

    public static void prettyDirectory(JSONObject directory){
        try {
            ligne();
            System.out.println(Directory.DIRECTORY_NAME + " : " +directory.getString(Directory.DIRECTORY_NAME));
            ligne();
            JSONArray jsonRegularFiles = directory.getJSONArray(Directory.REGULAR_FILES);
            JSONArray jsonOdfFiles = directory.getJSONArray(Directory.ODF_FILES);
            JSONArray jsonDirectories = directory.getJSONArray(Directory.SUBDIRECTORIES);
            for(int index=0, indexMax = jsonRegularFiles.length(); index<indexMax ; index++ ){
                prettyFile(jsonRegularFiles.getJSONObject(index));
                System.out.println("\n");
            }
            for(int index=0, indexMax = jsonOdfFiles.length(); index<indexMax ; index++ ){
                prettyOdfFile(jsonOdfFiles.getJSONObject(index));
                System.out.println("\n");
            }
            for(int index=0, indexMax = jsonDirectories.length(); index<indexMax ; index++ ){
                prettyDirectory(jsonDirectories.getJSONObject(index));
            }
            ligne();
            System.out.println("In " + directory.getString(Directory.DIRECTORY_NAME));
            System.out.println(directory.getJSONArray(Directory.CONTENT_OF_DIRECTORY));
            ligne();
        }catch(JsonException e){}
    }

    public static void prettyFile(JSONObject file){
        try {   
            prettyObject(file);
        }catch(JsonException e){}
    }

    public static void prettyOdfFile(JSONObject odfFile){
        try {      
            System.out.println(OdfFile.FILE_NAME + ":"+  odfFile.get(OdfFile.FILE_NAME));
            System.out.println(OdfFile.MIME_TYPE + ":"+  odfFile.get(OdfFile.MIME_TYPE));
            JSONArray metaJsonArray = odfFile.getJSONArray(OdfFile.METADATAS);
            prettyMetadata(metaJsonArray);
        }catch(JsonException e){}
    }


    public static void prettyMetadata(JSONArray metadataJsonArray){
        JSONObject currentMeta = new JSONObject();
        Collection<String> typeMeta;
        for(int indexArray =0, indexMaxArray = metadataJsonArray.length(); indexArray < indexMaxArray; indexArray++){
            currentMeta = metadataJsonArray.getJSONObject(indexArray);
            typeMeta = currentMeta.keySet();
            for(String  meta : typeMeta){
                switch(meta){
                    case MetaDataHyperlink.HYPERLINKS:
                        prettyHyperlink(currentMeta);
                        break;
                    case MetaDataOdfPictures.PICTURES:
                        prettyPicture(currentMeta);
                        break;
                    case MetaDataStats.STATISTICS:
                        prettyStatistique(currentMeta);
                        break;
                    default :
                        prettyObject(currentMeta);

                }
            }
        }
    }

    public static void prettyHyperlink(JSONObject HyperlinkJson){   
        JSONArray hyperlinksArray = HyperlinkJson.getJSONArray(MetaDataHyperlink.HYPERLINKS);
        JSONObject currentLink;
        for(int index= 0, indexMax = hyperlinksArray.length(); index<indexMax; index++){
            currentLink = hyperlinksArray.getJSONObject(index);
            prettyObject(currentLink);
        }     
    }




    public static void prettyObject(JSONObject jsonObject){
        Collection<String> attributsObject = jsonObject.keySet();
        for(String attribut : attributsObject){
            System.out.println(attribut + ":"+  jsonObject.get(attribut));
        }
    }

    public static void prettyPicture(JSONObject pictureJson){
        JSONArray pictureArray = pictureJson.getJSONArray(MetaDataOdfPictures.PICTURES);
        JSONObject currentPicture;
        for(int index= 0, indexMax =pictureArray.length(); index<indexMax; index++){
            currentPicture = pictureArray.getJSONObject(index);
            prettyObject(currentPicture);;
        }     
    }

    
    public static void prettyStatistique(JSONObject metadataJsonArray){
        JSONArray statArray = metadataJsonArray.getJSONArray(MetaDataStats.STATISTICS);
        JSONObject currentStat;
        for(int index= 0, indexMax = statArray.length(); index< indexMax; index++){
            currentStat = statArray.getJSONObject(index);
            prettyObject(currentStat);
        }

    }

    

    public static void main(String[] args){
        CommandTranslator askedActionToPerform = new CommandTranslator(args);
        try{
            Command actionToPerform = askedActionToPerform.translate();
            if(actionToPerform == Command.DISPLAY_HELP){
                help();
            }else if(actionToPerform == Command.REPLACE_THE_DESCRIPTION_OF_AN_ODF_FILE){
                changeTheDescriptionOfAnOdtFile(actionToPerform, IbarODFCore.stringToPath(args[1]), args);
            }else if(actionToPerform == Command.DISPLAY_THE_META_DATA_A_FILE){
                IbarODFCore ibar = new IbarODFCore(actionToPerform, IbarODFCore.stringToPath(args[1]), args);
                prettyFile(ibar.toJson());
            }else if(actionToPerform == Command.DISPLAY_THE_META_DATA_OF_AN_ODF_FILE){
                IbarODFCore ibar = new IbarODFCore(actionToPerform, IbarODFCore.stringToPath(args[1]), args);
                prettyOdfFile(ibar.toJson());    
            }
            else if(IbarODFCore.wantToDisplayMetadata(actionToPerform)){
                IbarODFCore ibar = new IbarODFCore(actionToPerform, IbarODFCore.stringToPath(args[1]), args);
                prettyDirectory(ibar.toJson());
                // System.out.println(ibar.toJson());
            }
            else{
                IbarODFCore ibar = new IbarODFCore(actionToPerform, IbarODFCore.stringToPath(args[1]), args);
                printProperly(ibar.launchCore().toString());
            }
         }catch(NotAllowedCommandException | FileNotFoundException e){
            System.out.println(e.getLocalizedMessage());
         } catch(org.odftoolkit.odfdom.pkg.OdfValidationException e){
            System.err.println("Is not  REAL odtFile.");
         }
         catch(Exception e){
            System.err.println("\nSomething went wrong...");
            System.err.println(e.getLocalizedMessage());
            
         }
    }
}
