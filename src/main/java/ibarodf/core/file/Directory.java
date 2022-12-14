package ibarodf.core.file;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import ibarodf.core.AbtractIbarOdfCore;
import ibarodf.core.file.exception.UnableToAddAllFilesException;
import ibarodf.core.file.exception.UnableToAddMetadataException;
import ibarodf.core.meta.exception.UnableToConvertToJsonFormatException;
import ibarodf.core.file.exception.EmptyOdfFileException;
import net.lingala.zip4j.exception.ZipException;

/**
 * This class represents a directory. It contains a list of directories, a list of regular files
 * and a list of ODF files
 */

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
    
    public Directory(final Path path) throws UnableToAddAllFilesException{
        super(path);
        try {
            final ArrayList<Path> filesPath = getSubFilesPathFromDirectory(path);
            addFiles(filesPath);
        }catch(final Exception e){
            System.out.println("ça merde dans le constructeur du Directory");
        }
    }

    private void addFiles(final ArrayList<Path> filesPath) throws UnableToAddAllFilesException{
        Iterator<Path> pathIterator = filesPath.iterator();
        Path currentPath;
        while(pathIterator.hasNext()){
            currentPath = pathIterator.next();
            try{
                if(Files.isDirectory(currentPath)){
                    directories.add(new Directory(currentPath));
                }else if(AbtractIbarOdfCore.isAnOdfFile(currentPath.toString())){
                    final OdfFile odtFileToAdd = new OdfFile(currentPath);
                    odfFiles.add(odtFileToAdd);
                }else{
                    regularFiles.add(new RegularFile(currentPath));
                }
            }catch(IOException | ZipException | EmptyOdfFileException | UnableToAddMetadataException e){
                regularFiles.add(new RegularFile(currentPath));
            }catch(final Exception e){
                throw new UnableToAddAllFilesException(getFileName());
            }
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
        final String separator = AbtractIbarOdfCore.getCurrentSystemSeparator();
        try{
            for(final String currentPath : textPath){ 
                filesPath.add(AbtractIbarOdfCore.stringToPath(directoryPath +separator+currentPath));
            } 
        }catch(final Exception e){
            System.err.println(e.getMessage());
        }   
        return filesPath;
    }

    /**
     * Returns a JSON representation of the directory. 
     * @return A JSONObject
     */
    public JSONObject toJonObject() throws UnableToConvertToJsonFormatException{  
        try {
            final JSONObject directoryJson = super.toJonObject();
            final JSONArray regularFilesJson = new JSONArray();
            final JSONArray odfFilesJson = new JSONArray();
            final JSONArray directoriesJson = new JSONArray();
            Iterator<Directory> DirectoryIt = directories.iterator();
            Iterator<OdfFile> odfFileIt = odfFiles.iterator();
            Iterator<RegularFile> regularFileIt = regularFiles.iterator();
            while(DirectoryIt.hasNext()){
                directoriesJson.put(DirectoryIt.next().toJonObject());
            }
            while(regularFileIt.hasNext()){ 
                regularFilesJson.put(regularFileIt.next().toJonObject());
            }
            while(odfFileIt.hasNext()){
                odfFilesJson.put(odfFileIt.next().toJonObject());
            }
            directoryJson.put(REGULAR_FILES, regularFilesJson);
            directoryJson.put(ODF_FILES, odfFilesJson);
            directoryJson.put(SUBDIRECTORIES, directoriesJson);
            return  directoryJson;
        }catch(UnableToConvertToJsonFormatException e){
            throw new UnableToConvertToJsonFormatException(getFileName());
        }
    }

    
}