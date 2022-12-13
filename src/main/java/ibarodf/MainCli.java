package ibarodf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
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
import ibarodf.core.file.RegularFile;
import ibarodf.core.meta.MetaDataHyperlink;
import ibarodf.core.meta.MetaDataOdfPictures;
import ibarodf.core.meta.MetaDataStats;
import ibarodf.core.meta.Picture;

public class MainCli {
    public static final int NUMBER_SYMBOLE = 100; 

    public static void help(){
        try{
            String fileSeparator = FileSystems.getDefault().getSeparator();
            BufferedReader helpReader = new BufferedReader(new FileReader(new File("ressources" + fileSeparator +"help.txt")));
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

    public static String properTabulation(int numberOfTab){
        StringBuilder tabulation = new StringBuilder();
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

    public static void prettyDirectory(JSONObject directory, int depth){
        try {
            ligne();
            prettyPrint(Directory.FILE_NAME, directory.get(Directory.FILE_NAME), depth);
            ligne();
            JSONArray jsonRegularFiles = directory.getJSONArray(Directory.REGULAR_FILES);
            JSONArray jsonOdfFiles = directory.getJSONArray(Directory.ODF_FILES);
            JSONArray jsonDirectories = directory.getJSONArray(Directory.SUBDIRECTORIES);
            for(int index=0, indexMax = jsonRegularFiles.length(); index<indexMax ; index++ ){
                prettyFile(jsonRegularFiles.getJSONObject(index), depth);
                System.out.println("\n");
            }
            for(int index=0, indexMax = jsonOdfFiles.length(); index<indexMax ; index++ ){
                prettyOdfFile(jsonOdfFiles.getJSONObject(index), depth);
                System.out.println("\n");
            }
            for(int index=0, indexMax = jsonDirectories.length(); index<indexMax ; index++ ){
                prettyDirectory(jsonDirectories.getJSONObject(index), depth+1);
            }
            prettyInformations(directory, depth);
        }catch(JsonException e){}
    }

    public static void prettyFile(JSONObject file, int depth){
        try {   
            prettyPrint(RegularFile.FILE_NAME , file.get(RegularFile.FILE_NAME) , depth);
            prettyPrint(RegularFile.MIME_TYPE  , file.get(RegularFile.MIME_TYPE) , depth);
        }catch(JsonException e){}
    }

    public static void prettyOdfFile(JSONObject odfFile, int depth){
        try {      
            prettyFile(odfFile, depth);            
            JSONArray metaJsonArray = odfFile.getJSONArray(OdfFile.METADATAS);
            prettyMetadata(metaJsonArray, depth);
        }catch(JsonException e){}
    }


    public static void prettyPrint(String key, Object value, int depth){
        String tabulation = properTabulation(depth);
        System.out.println(tabulation + key + " : "+ value);

    }

    public static void prettyMetadata(JSONArray metadataJsonArray, int depth){
        JSONObject currentMeta = new JSONObject();
        Collection<String> typeMeta;
        for(int indexArray =0, indexMaxArray = metadataJsonArray.length(); indexArray < indexMaxArray; indexArray++){
            currentMeta = metadataJsonArray.getJSONObject(indexArray);
            typeMeta = currentMeta.keySet();
            for(String  meta : typeMeta){
                switch(meta){
                    case MetaDataHyperlink.HYPERLINKS:
                        prettyPrint(MetaDataHyperlink.HYPERLINKS, "", depth);
                        prettyHyperlink(currentMeta, depth+1);
                        break;
                    case MetaDataOdfPictures.PICTURES:
                        prettyPrint(MetaDataOdfPictures.PICTURES, "", depth);
                        prettyPicture(currentMeta, depth+1);
                        break;
                    case MetaDataStats.STATISTICS:
                        prettyPrint(MetaDataStats.STATISTICS, "", depth);
                        prettyStatistique(currentMeta, depth+1);
                        break;
                    default :
                        prettyObject(currentMeta, depth);

                }
            }
        }
    }

    public static void prettyHyperlink(JSONObject HyperlinkJson,  int depth){
        String tabulation = properTabulation(depth);
        JSONArray hyperlinksArray = HyperlinkJson.getJSONArray(MetaDataHyperlink.HYPERLINKS);
        JSONObject currentLink;
        int lengthArray = hyperlinksArray.length();
        if(lengthArray==0){System.out.println(tabulation+ "No hyperlink");}
        for(int index= 0 ; index<lengthArray; index++){
            currentLink = hyperlinksArray.getJSONObject(index);
            prettyPrint("Hyperlink ", "", depth);
            prettyObject(currentLink, depth+1);
        }     
    }




    public static void prettyObject(JSONObject jsonObject, int depth){
        Collection<String> attributsObject = jsonObject.keySet();
        for(String attribut : attributsObject){
            prettyPrint(attribut, jsonObject.get(attribut) , depth);
        }
    }

    public static void prettyPicture(JSONObject pictureJson, int depth){
        String tabulation = properTabulation(depth);
        JSONArray pictureArray = pictureJson.getJSONArray(MetaDataOdfPictures.PICTURES);
        JSONObject currentPicture;
        Path picturePath;
        int lengthArray = pictureArray.length();
        if(lengthArray==0){System.out.println(tabulation+ "No picture");}
        for(int index= 0; index<lengthArray; index++){
            currentPicture = pictureArray.getJSONObject(index);
            picturePath = (Path) currentPicture.get(Picture.PATH); 
            prettyPrint("Picture "+ picturePath.getFileName(), "", depth);
            prettyObject(currentPicture, depth+1);
        }
    }

    public static void prettyInformations(JSONObject directoryJson, int depth){
        String endMessage = "In " + directoryJson.get(Directory.FILE_NAME);
        ligne();
        prettyPrint(endMessage, "", depth);
        prettyPrint("Number Of Directory",( directoryJson.getJSONArray(Directory.SUBDIRECTORIES)).length(), depth+1);
        prettyPrint("Number Of Files", (directoryJson.getJSONArray(Directory.REGULAR_FILES)).length(), depth+1);
        prettyPrint("Number Of Odf Files",  (directoryJson.getJSONArray(Directory.ODF_FILES)).length(), depth+1);


        ligne();
    } 

    
    public static void prettyStatistique(JSONObject metadataJsonArray, int depth){
        JSONArray statArray = metadataJsonArray.getJSONArray(MetaDataStats.STATISTICS);
        JSONObject currentStat;
        for(int index= 0, indexMax = statArray.length(); index< indexMax; index++){
            currentStat = statArray.getJSONObject(index);
            prettyObject(currentStat,depth);
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
                prettyFile(ibar.toJson(), 0);
            }else if(actionToPerform == Command.DISPLAY_THE_META_DATA_OF_AN_ODF_FILE){
                IbarODFCore ibar = new IbarODFCore(actionToPerform, IbarODFCore.stringToPath(args[1]), args);
                prettyOdfFile(ibar.toJson(), 0);    
            }
            else if(IbarODFCore.wantToDisplayMetadata(actionToPerform)){
                IbarODFCore ibar = new IbarODFCore(actionToPerform, IbarODFCore.stringToPath(args[1]), args);
                prettyDirectory(ibar.toJson(), 0);
                // System.out.println(ibar.toJson());
            }
            else{
                IbarODFCore ibar = new IbarODFCore(actionToPerform, IbarODFCore.stringToPath(args[1]), args);
                System.out.println(ibar.launchCore());
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
