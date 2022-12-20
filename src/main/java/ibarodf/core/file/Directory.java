package ibarodf.core.file;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import ibarodf.core.IbarOdfCore;
import ibarodf.core.file.exception.EmptyOdfFileException;
import ibarodf.core.file.exception.UnableToAddMetadataException;
import ibarodf.core.file.exception.UnableToLoadOdfDocumentException;
import ibarodf.core.file.exception.UnrecognizableTypeFileException;
import ibarodf.core.metadata.exception.UnableToConvertToJsonFormatException;
import net.lingala.zip4j.exception.ZipException;

/**
 * This class represents a directory. It contains a list of directories, a list of regular files
 * and a list of ODF files
 */

public  class Directory extends RegularFile {
    private final ArrayList<Directory> directories = new ArrayList<>();
    private final ArrayList<OdfFile> odfFiles = new ArrayList<>();
    private final ArrayList<RegularFile> regularFiles = new ArrayList<>();
    private final ArrayList<WrongFile> wrongFiles = new ArrayList<>();
    private final boolean isRecursif;
    
    private int numberOfSubDirectories = 0;
    private int numberOfRegularFiles = 0;
    private int numberOfOdfFiles = 0;
    private int numberOfWrongFiles = 0;
    private int totalNumberOfFile=0;

    
    // Json Keys
    public static final String SUBDIRECTORIES = "Directories";
    public static final String REGULAR_FILES = "Regular files";
    public static final String ODF_FILES = "Odf files";
    public static final String INFORMATIONS = "Informations";
    public static final String WRONG_FILES = "Wrong Files";
    public static final String TOTAL_NUMBER_OF_FILES = "Total";
    public static final String[] ALL_INFORMATIONS = {SUBDIRECTORIES, REGULAR_FILES, ODF_FILES, WRONG_FILES, TOTAL_NUMBER_OF_FILES};
    
    public Directory(Path path, boolean isRecursif) throws FileNotFoundException{
        super(path);
        this.isRecursif = isRecursif;
        ArrayList<Path>filesPath = getSubFilesPathFromDirectory(path);
        addFiles(filesPath);
    }
    
    public Directory(Path path) throws FileNotFoundException{
        this(path, false);
    }


    /**
     * It takes an ArrayList of Paths, and for each path, it checks if it's a directory, an ODF file, or a
     * regular file, and adds it to the appropriate ArrayList
     * @param filesPath ArrayList of files Path
     */
    private void addFiles(ArrayList<Path> filesPath){
        Iterator<Path> pathIterator = filesPath.iterator();
        Path currentPath; 
        while(pathIterator.hasNext()){
            totalNumberOfFile++;
            currentPath = pathIterator.next();
            try{
                if(Files.isDirectory(currentPath)){
                    addADirectory(currentPath);
                }else{
                    addAfile(currentPath);
                }
            }catch(UnrecognizableTypeFileException e){
                regularFiles.add(new RegularFile(currentPath));
                numberOfRegularFiles ++;
            }catch(Exception e){
                addWrongFile(currentPath, e);
            }
        }
    }

    private void addAfile(Path odfFilePath) throws UnableToLoadOdfDocumentException, UnrecognizableTypeFileException, IOException, ZipException, EmptyOdfFileException, UnableToAddMetadataException{
        if(IbarOdfCore.isAnOdfFile(odfFilePath)){
            odfFiles.add(new OdfFile(odfFilePath));
            numberOfOdfFiles++;
        }else{
            regularFiles.add(new RegularFile(odfFilePath));
            numberOfRegularFiles++;
        }
    }

    private void addWrongFile(Path wrongFilePath, Exception e){
        numberOfWrongFiles++;
        if(e.getMessage() == null){
            wrongFiles.add(new WrongFile(wrongFilePath));
        }else{
            wrongFiles.add(new WrongFile(wrongFilePath, e.getMessage()));
        }
    }

    private void addADirectory(Path pathDirectory) throws FileNotFoundException{
        if(isRecursif){
            directories.add(new Directory(pathDirectory, true));
        }else{
            regularFiles.add(new RegularFile(pathDirectory));
        }
        numberOfSubDirectories++;
    }



    /**
     * This function gets the children of a directory
     * @param directoryPath The path of the directory to be searched
     * @return A list of Path corresponding to the children of the search directory.
     */
    public static ArrayList<Path> getSubFilesPathFromDirectory(Path directoryPath) throws FileNotFoundException{
        ArrayList<Path> filesPath = new ArrayList<Path>();
        File directory = new File(directoryPath.toString());
        String[] textPath = directory.list();
        String separator = IbarOdfCore.getCurrentSystemSeparator();
        Path childPath;
            for(String currentPath : textPath){ 
                childPath = IbarOdfCore.stringToPath(directoryPath +separator+currentPath);
                if(childPath.toFile().canExecute()){
                    filesPath.add(childPath);
                }
            } 
          
        return filesPath; 
    }

  /**
   * It creates a JSONArray containing the informations regarding the current directory.
   * @return A JSONArray object.
   */
    private JSONObject informationFileToJson(){
        JSONObject infos = new JSONObject();
        infos.put(SUBDIRECTORIES, numberOfSubDirectories);
        infos.put(REGULAR_FILES, numberOfRegularFiles);
        infos.put(ODF_FILES, numberOfOdfFiles);
        infos.put(WRONG_FILES, numberOfWrongFiles);
        infos.put(TOTAL_NUMBER_OF_FILES, totalNumberOfFile);
        return infos;
    }

    @Override
    public JSONObject toJonObject() throws UnableToConvertToJsonFormatException{  
        try {
            final JSONObject directoryJson = super.toJonObject();

            final JSONArray directoriesJson = new JSONArray();
            final JSONArray regularFilesJson = new JSONArray();
            final JSONArray odfFilesJson = new JSONArray();
            final JSONArray wrongFileJson = new JSONArray();

            Iterator<Directory> directoryIt = directories.iterator();
            Iterator<RegularFile> regularFileIt = regularFiles.iterator();
            Iterator<OdfFile> odfFileIt = odfFiles.iterator();
            Iterator<WrongFile> wrongFileIt = wrongFiles.iterator();

            while(directoryIt.hasNext()){
                directoriesJson.put(directoryIt.next().toJonObject());
            }
            while(regularFileIt.hasNext()){ 
                regularFilesJson.put(regularFileIt.next().toJonObject());
            }
            while(odfFileIt.hasNext()){
                odfFilesJson.put(odfFileIt.next().toJonObject());
            }
            while(wrongFileIt.hasNext()){
                wrongFileJson.put(wrongFileIt.next().toJonObject());
            }

            directoryJson.put(SUBDIRECTORIES, directoriesJson);
            directoryJson.put(REGULAR_FILES, regularFilesJson);
            directoryJson.put(ODF_FILES, odfFilesJson);
            directoryJson.put(WRONG_FILES, wrongFileJson);
            directoryJson.put(INFORMATIONS, informationFileToJson());
            return  directoryJson;
        }catch(UnableToConvertToJsonFormatException e){
            System.err.println(e.getLocalizedMessage());
            throw new UnableToConvertToJsonFormatException(getFileName());
        }
    }
    
}