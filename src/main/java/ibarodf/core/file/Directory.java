package ibarodf.core.file;

import java.util.ArrayList;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import ibarodf.core.AbtractIbarOdfCore;
import ibarodf.core.file.exception.CannotAddAllFilesException;
import ibarodf.core.file.exception.CannotAddAllMetadatasException;
import ibarodf.core.file.exception.EmptyOdfFileException;
import net.lingala.zip4j.exception.ZipException;








public  class Directory extends AbstractGenericFile {
    private final ArrayList<Directory> directories = new ArrayList<>();
    private final ArrayList<OdfFile> odfFiles = new ArrayList<>();
    private final ArrayList<RegularFile> regularFiles = new ArrayList<>();

    //Json Key
    public static final String SUBDIRECTORIES = "Directories";
    public static final String REGULAR_FILES = "Regular files";
    public static final String ODF_FILES = "Odf files";
    public static final String CONTENT_OF_DIRECTORY = "In";
    public static final String NUMBER_OF = "Number of ";
    
    public Directory(Path path) throws CannotAddAllFilesException{
        super(path);
        try {
            ArrayList<Path> filesPath = getSubFilesPathFromDirectory(path);
            addFiles(filesPath);
        }catch(ArrayStoreException e){
            System.out.println("C'est Array store Exception");
        }
    }


    void addFiles(ArrayList<Path> filesPath) throws CannotAddAllFilesException{
        for(Path currentPath : filesPath){
            try{
                if(Files.isDirectory(currentPath)){
                    directories.add(new Directory(currentPath));
                }else if(AbtractIbarOdfCore.isAnOdfFile(currentPath.toString())){
                    OdfFile odtFileToAdd = new OdfFile(currentPath);
                    odfFiles.add(odtFileToAdd);
                }else{
                    regularFiles.add(new RegularFile(currentPath));
                }
            }catch(IOException | ZipException | EmptyOdfFileException | CannotAddAllMetadatasException e){
                regularFiles.add(new RegularFile(currentPath));
            }catch(Exception e){
                throw new CannotAddAllFilesException(getFileName());
            }
        }
    }


    public static ArrayList<Path> getSubFilesPathFromDirectory(Path directoryPath){
        ArrayList<Path> filesPath = new ArrayList<>();
        File directory = new File(directoryPath.toString());
        String[] textPath = directory.list();
        String separator = FileSystems.getDefault().getSeparator();
        try{
            for(String currentPath : textPath){ 
                filesPath.add(AbtractIbarOdfCore.stringToPath(directoryPath +separator+currentPath));
            } 
        }catch(Exception e){
            System.err.println(e.getMessage());
        }   
        return filesPath;
    }



    public StringBuilder displayMetaData() throws Exception{
        StringBuilder metaDataStr = new StringBuilder();
        String directoryName = "{"+getPath().getFileName().toString()+"{";
        metaDataStr.append(directoryName);
        for(Directory currentDirectory : directories){
            metaDataStr.append(currentDirectory.displayMetaData());
        }
        for(RegularFile currentRegularFile : regularFiles){ 
            metaDataStr.append(currentRegularFile.displayMetaData());
        }
        for(OdfFile currentOdfFile : odfFiles){
            metaDataStr.append(currentOdfFile.displayMetaData());
        }
        metaDataStr.append("}}");
        return metaDataStr;
    }

    public String toString(){
        return "{Directory -- Path : "+ getPath() + " }"; 
    }

    public int getNumberOfDirectories(){
        return directories.size();
    }

    public int getNumberOfRegularFiles(){
        return regularFiles.size();
    }

    public int getNumberOfFiles(){
        return odfFiles.size();
    }

    public JSONObject toJonObject() throws Exception{  
        JSONObject directoryJson = super.toJonObject();

        JSONArray regularFilesJson = new JSONArray();
        JSONArray odfFilesJson = new JSONArray();
        JSONArray directoriesJson = new JSONArray();
        for(Directory currentDirectory : directories){
            directoriesJson.put(currentDirectory.toJonObject());
        }
        for(RegularFile currentRegularFile : regularFiles){ 
            regularFilesJson.put(currentRegularFile.toJonObject());
        }
        for(OdfFile currentOdfFile : odfFiles){
            odfFilesJson.put(currentOdfFile.toJonObject());
        }
        directoryJson.put(REGULAR_FILES, regularFilesJson);
        directoryJson.put(ODF_FILES, odfFilesJson);
        directoryJson.put(SUBDIRECTORIES, directoriesJson);
        return  directoryJson;
    }

    
}