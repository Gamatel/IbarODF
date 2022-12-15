package ibarodf.command;

import java.nio.file.Path;
import java.util.Collection;

import javax.json.JsonException;

import org.json.JSONArray;
import org.json.JSONObject;

import ibarodf.core.file.AbstractGenericFile;
import ibarodf.core.file.Directory;
import ibarodf.core.file.OdfFile;
import ibarodf.core.file.RegularFile;
import ibarodf.core.file.WrongFile;
import ibarodf.core.meta.MetaDataHyperlink;
import ibarodf.core.meta.MetaDataOdfPictures;
import ibarodf.core.meta.MetaDataStats;
import ibarodf.core.meta.Picture;

public abstract class PrettyResult {
    public static final int DEFAULT_LINE_SIZE_FOR_PRETTY_PRINT = 100; 
    public static final String DEFAULT_LINE_SYMBOLE_FOR_PRETTY_PRINT = "-";
    public static final String CLOSED_DIR = "SubSirectory"; 
    

    public static String properTabulation(int numberOfTab){
        StringBuilder tabulation = new StringBuilder();
        while(numberOfTab > 0){
            tabulation.append("\t");
            numberOfTab--;
        }
        return tabulation.toString();
    } 

    public static void ligne(String ligneSymbole, int lineSize, int depth){
        StringBuffer ligneString = new StringBuffer(properTabulation(depth));
        for(int time=0; time<lineSize; time++){
            ligneString.append(ligneSymbole);
        }
       System.out.println(ligneString);
    }

    public static void ligne(int depth){
        ligne(PrettyResult.DEFAULT_LINE_SYMBOLE_FOR_PRETTY_PRINT, PrettyResult.DEFAULT_LINE_SIZE_FOR_PRETTY_PRINT, depth);
    }

    public static void ligne(){
        ligne(0);
    }

    public static void prettyDirectory(JSONObject directory, int depth){
        try {
            ligne(depth);
            print(directory.get(Directory.FILE_NAME).toString(), depth);
            ligne(depth);
            displayRegularFiles(directory, depth+1);
            displayOdfFiles(directory, depth+1);
            displaySubDirectories(directory, depth+1);
            displayWrongFiles(directory, depth+1);
            prettyInformations(directory, depth);
        }catch(JsonException e){}
    }

    private static void displayRegularFiles(JSONObject jsonDirectory, int depth){
        JSONArray jsonRegularFiles = jsonDirectory.getJSONArray(Directory.REGULAR_FILES);
        JSONObject currentJsonObject;
        for(int index=0, indexMax = jsonRegularFiles.length(); index<indexMax ; index++ ){
            currentJsonObject = jsonRegularFiles.getJSONObject(index);
            if(currentJsonObject.get(AbstractGenericFile.MIME_TYPE).equals(AbstractGenericFile.TYPE_DIRECTORY)){
                prettyClosedDirectory(currentJsonObject, depth);
            }else{
                prettyFile(currentJsonObject, depth);
                System.out.println("\n");
            }
        }

    }

    private static void displayOdfFiles(JSONObject jsonDirectory, int depth){
        JSONArray jsonOdfFiles = jsonDirectory.getJSONArray(Directory.ODF_FILES);
        for(int index=0, indexMax = jsonOdfFiles.length(); index<indexMax ; index++ ){
            prettyOdfFile(jsonOdfFiles.getJSONObject(index), depth);
            System.out.println("\n");
        }
    }

    private static void displaySubDirectories(JSONObject jsonDirectory, int depth){
        JSONArray jsonDirectories = jsonDirectory.getJSONArray(Directory.SUBDIRECTORIES);
        for(int index=0, indexMax = jsonDirectories.length(); index<indexMax ; index++ ){
            prettyDirectory(jsonDirectories.getJSONObject(index), depth+1);
        }
    }

    private static void displayWrongFiles(JSONObject jsonDirectory, int depth){
        JSONArray jsonWrongFiles = jsonDirectory.getJSONArray(Directory.WRONG_FILES);
        int numberOfWrongFiles = jsonWrongFiles.length();
        if(numberOfWrongFiles > 0){
            ligne("/!\\", PrettyResult.DEFAULT_LINE_SIZE_FOR_PRETTY_PRINT/3,  depth);
            for(int index=0 ; index<numberOfWrongFiles ; index++ ){
                prettyWrongFile(jsonWrongFiles.getJSONObject(index), depth+1);
            }
            ligne("/!\\",  PrettyResult.DEFAULT_LINE_SIZE_FOR_PRETTY_PRINT/3, depth);
        }
    }




    public static void prettyDirectory(JSONObject directory){
        prettyDirectory(directory,0);
    }


    private static void prettyFile(JSONObject file, int depth){
        try {   
            prettyPrint(RegularFile.FILE_NAME , file.get(RegularFile.FILE_NAME) , depth);
            prettyPrint(RegularFile.MIME_TYPE  , file.get(RegularFile.MIME_TYPE) , depth);
            prettyPrint(RegularFile.SIZE  , file.get(RegularFile.SIZE) , depth);
        }catch(JsonException e){}
    }

    public static void prettyFile(JSONObject file){
        prettyFile(file, 0);
    }

    private static void prettyWrongFile(JSONObject jsonError, int depth){
        prettyPrint(Directory.WRONG_FILES, depth);
        prettyFile(jsonError,depth+1);
        prettyPrint(WrongFile.ERRORMESSAGE, jsonError.get(WrongFile.ERRORMESSAGE), depth+1);

    }

    private static void prettyOdfFile(JSONObject odfFile, int depth){
        try {      
            prettyFile(odfFile, depth);            
            JSONArray metaJsonArray = odfFile.getJSONArray(OdfFile.METADATAS);
            prettyMetadata(metaJsonArray, depth);
        }catch(JsonException e){}
    }

    public static void prettyOdfFile(JSONObject odfFile){
        prettyOdfFile(odfFile,0);
    }


    private static void prettyPrint(String key, Object value, int depth){
        String tabulation = properTabulation(depth);
        System.out.println(tabulation + key + " : "+ value);

    }
    private static void prettyPrint(String key, int depth){
        prettyPrint(key, "", depth); 
    }

    private static void print(String toPrint, int depth){
        String tabulation = properTabulation(depth);
        System.out.println(tabulation + toPrint);
    }

    private static void prettyMetadata(JSONArray metadataJsonArray, int depth){
        JSONObject currentMeta = new JSONObject();
        Collection<String> typeMeta;
        for(int indexArray =0, indexMaxArray = metadataJsonArray.length(); indexArray < indexMaxArray; indexArray++){
            currentMeta = metadataJsonArray.getJSONObject(indexArray);
            typeMeta = currentMeta.keySet();
            for(String  meta : typeMeta){
                switch(meta){
                    case MetaDataHyperlink.HYPERLINKS:
                        prettyPrint(MetaDataHyperlink.HYPERLINKS, depth);
                        prettyHyperlink(currentMeta, depth+1);
                        break;
                    case MetaDataOdfPictures.PICTURES:
                        prettyPrint(MetaDataOdfPictures.PICTURES, depth);
                        prettyPicture(currentMeta, depth+1);
                        break;
                    case MetaDataStats.STATISTICS:
                        prettyPrint(MetaDataStats.STATISTICS, depth);
                        prettyStatistique(currentMeta, depth+1);
                        break;
                    default :
                        prettyObject(currentMeta, depth);

                }
            }
        }
    }



    private static void prettyHyperlink(JSONObject hyperlinkJson,  int depth){
        String tabulation = properTabulation(depth);
        JSONArray hyperlinksArray = hyperlinkJson.getJSONArray(MetaDataHyperlink.HYPERLINKS);
        JSONObject currentLink;
        int lengthArray = hyperlinksArray.length();
        if(lengthArray==0){System.out.println(tabulation+ "No hyperlink");}
        for(int index= 0 ; index<lengthArray; index++){
            currentLink = hyperlinksArray.getJSONObject(index);
            prettyPrint("Hyperlink ", depth);
            prettyObject(currentLink, depth+1);
        }     
    }

    private static void prettyClosedDirectory(JSONObject closedDirectory, int depth){
        ligne(depth+1);
        prettyPrint(PrettyResult.CLOSED_DIR, closedDirectory.get(Directory.FILE_NAME) , depth+1);
        ligne(depth+1);
    }

    private static void prettyObject(JSONObject jsonObject, int depth){
        Collection<String> attributsObject = jsonObject.keySet();
        for(String attribut : attributsObject){
            prettyPrint(attribut, jsonObject.get(attribut) , depth);
        }
    }


    private static void prettyPicture(JSONObject pictureJson, int depth){
        String tabulation = properTabulation(depth);
        JSONArray pictureArray = pictureJson.getJSONArray(MetaDataOdfPictures.PICTURES);
        JSONObject currentPicture;
        Path picturePath;
        int lengthArray = pictureArray.length();
        if(lengthArray==0){System.out.println(tabulation+ "No picture");}
        for(int index= 0; index<lengthArray; index++){
            currentPicture = pictureArray.getJSONObject(index);
            picturePath = (Path) currentPicture.get(Picture.PATH); 
            prettyPrint("Picture "+ picturePath.getFileName(), depth);
            prettyObject(currentPicture, depth+1);
        }
    }




    private static void prettyInformations(JSONObject directoryJson, int depth){
        ligne(depth);
        String endMessage = "In " + directoryJson.get(Directory.FILE_NAME);
        prettyPrint(endMessage, depth);
        JSONArray informations = directoryJson.getJSONArray(Directory.INFORMATIONS);
        for(int index=0, indexMax = informations.length(); index < indexMax; index++){
            prettyObject(informations.getJSONObject(index), depth+1);
        }

        ligne(depth);
    } 


    private static void prettyStatistique(JSONObject metadataJsonArray, int depth){
        JSONArray statArray = metadataJsonArray.getJSONArray(MetaDataStats.STATISTICS);
        JSONObject currentStat;
        for(int index= 0, indexMax = statArray.length(); index< indexMax; index++){
            currentStat = statArray.getJSONObject(index);
            prettyObject(currentStat,depth);
        }

    }




}
