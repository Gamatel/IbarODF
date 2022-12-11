package ibarodf.core.file;

import java.util.ArrayList;
import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import ibarodf.core.IbarODFCore;








public  class Directory extends AbstractGenericFile {
    private final ArrayList<Directory> directories = new ArrayList<>();
    private final ArrayList<AbstractGenericFile> files = new ArrayList<>();

    public Directory(Path path){
        super(path);
        ArrayList<Path> filesPath = getSubFilesPathFromDirectory(path);
        addFiles(filesPath);
    }


    void addFiles(ArrayList<Path> filesPath){
        for(Path currentPath : filesPath){
            try{
                if(Files.isDirectory(currentPath)){
                    directories.add(new Directory(currentPath));
                }else if(IbarODFCore.isAnOdfFile(currentPath.toString())){
                    OdfFile odtFileToAdd = new OdfFile(currentPath);
                    files.add(odtFileToAdd);
                }else{
                    files.add(new NotOdfFile(currentPath));
                }
            } catch(Exception e){
                System.out.println(e.getMessage());
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
                filesPath.add(IbarODFCore.stringToPath(directoryPath +separator+currentPath));
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
        for(AbstractGenericFile currentFile : files){ 
            metaDataStr.append(currentFile.displayMetaData());
        }
        metaDataStr.append("}}");
        metaDataStr.append(getInformations());
        return metaDataStr;
    }

    public String toString(){
        return "{Directory -- Path : "+ getPath() + " }"; 
    }

    public int getNumberOfDirectories(){
        return directories.size();
    }

    public int getNumberOfFiles(){
        return files.size();
    }

    public String getInformations(){
        String infos = "\"In " + getPath().getFileName() + " :" + getNumberOfDirectories() + " Directories - " +
                getNumberOfFiles() + " Total regular file\"";
        return infos;
    }

    
}