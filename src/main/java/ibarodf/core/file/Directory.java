package ibarodf.core.file;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import ibarodf.core.IbarOdfCore;
import ibarodf.core.file.exception.UnableToAddAllFilesException;
import ibarodf.core.meta.exception.UnableToConvertToJsonFormatException;

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

    //Json Key
    public static final String SUBDIRECTORIES = "Directories";
    public static final String REGULAR_FILES = "Regular files";
    public static final String ODF_FILES = "Odf files";
    public static final String INFORMATIONS = "Informations";
    public static final String WRONG_FILES = "Wrong Files";
    public static final String TOTAL_NUMBER_OF_FILES = "Total";

    
    public Directory(final Path path, boolean isRecursif) throws UnableToAddAllFilesException{
        super(path);
        this.isRecursif = isRecursif;
        try {
            ArrayList<Path> filesPath = getSubFilesPathFromDirectory(path);
            addFiles(filesPath);
        }catch(final Exception e){
            throw new UnableToAddAllFilesException(getFileName());
        }
    }

    public Directory(final Path path) throws UnableToAddAllFilesException{
        this(path, false);
    }


    private void addFiles(final ArrayList<Path> filesPath) throws UnableToAddAllFilesException{
        Iterator<Path> pathIterator = filesPath.iterator();
        Path currentPath;
        while(pathIterator.hasNext()){
            totalNumberOfFile++;
            currentPath = pathIterator.next();
            try{
                if(Files.isDirectory(currentPath)){
                    if(isRecursif){
                        directories.add(new Directory(currentPath, true));
                    }else{
                        regularFiles.add(new RegularFile(currentPath));
                    }
                    numberOfSubDirectories++;
                }else if(IbarOdfCore.isAnOdfFile(currentPath.toString())){
                    odfFiles.add(new OdfFile(currentPath));
                    numberOfOdfFiles++;
                }else{
                    regularFiles.add(new RegularFile(currentPath));
                    numberOfRegularFiles++;
                }
            // }catch(IOException | ZipException | EmptyOdfFileException | UnableToAddMetadataException e){
            }catch(Exception e){
                if(e.getMessage().isEmpty()){
                    wrongFiles.add(new WrongFile(currentPath));
                }else{
                    wrongFiles.add(new WrongFile(currentPath, e.getMessage()));
                }
                numberOfWrongFiles++;
            }
            /* }catch(Exception e){
                throw new UnableToAddAllFilesException(getFileName());
            } */
        }
    }

    /**
     * Get the children of a directory
     * @param directoryPath The path of the directory to be searched
     * @return A list of Path objects.
     */
    public static ArrayList<Path> getSubFilesPathFromDirectory(final Path directoryPath){
        final ArrayList<Path> filesPath = new ArrayList<>();
        final File directory = new File(directoryPath.toString());
        final String[] textPath = directory.list();
        final String separator = IbarOdfCore.getCurrentSystemSeparator();
        try{
            for(final String currentPath : textPath){ 
                filesPath.add(IbarOdfCore.stringToPath(directoryPath +separator+currentPath));
            } 
        }catch(final Exception e){
            System.err.println(e.getMessage());
        }   
        return filesPath;
    }

    private JSONArray informationFileToJson(){
        JSONArray infos = new JSONArray();
        infos.put((new JSONObject()).put(SUBDIRECTORIES, numberOfSubDirectories));
        infos.put((new JSONObject()).put(REGULAR_FILES, numberOfRegularFiles));
        infos.put((new JSONObject()).put(ODF_FILES, numberOfOdfFiles));
        infos.put((new JSONObject()).put(WRONG_FILES, numberOfWrongFiles));
        infos.put((new JSONObject()).put(TOTAL_NUMBER_OF_FILES, totalNumberOfFile));
        return infos;
    }

    /**
     * Returns a JSON representation of the directory. 
     * @return A JSONObject
     */
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
            throw new UnableToConvertToJsonFormatException(getFileName());
        }
    }

    
}