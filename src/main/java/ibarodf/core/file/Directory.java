package ibarodf.core.file;
import java.util.ArrayList;
import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import ibarodf.command.*;
import ibarodf.core.IbarODFCore;
import ibarodf.core.UnrecognizableTypeFileException;





public  class Directory extends AbstractGenericFile {
    private ArrayList<Directory> directories = new ArrayList<Directory>();
    private ArrayList<AbstractGenericFile> files = new ArrayList<AbstractGenericFile>();

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
                    OdfFile odtFileToAdd = IbarODFCore.isAnOdtFile(currentPath.toString())? new OdtFile(currentPath) : new OdfFile(currentPath);
                    files.add(odtFileToAdd);
                }else{
                    files.add(new RegularFile(currentPath));
                }
            }catch(UnrecognizableTypeFileException e){
                files.add(new RegularFile(currentPath));
            }catch(NotAllowedCommandException e){
                files.add(new RegularFile(currentPath));
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
    }


    public static ArrayList<Path> getSubFilesPathFromDirectory(Path directoryPath){
        ArrayList<Path> filesPath = new ArrayList<Path>();
        File directory = new File(directoryPath.toString());
        String[] textPath = directory.list();
        String separator = FileSystems.getDefault().getSeparator();
        try{
            for(String currentPath : textPath){ 
                filesPath.add(IbarODFCore.stringToPath(directoryPath.toString()+separator+currentPath));
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
        StringBuilder infos = new StringBuilder("\"In "+getPath().getFileName()+" :");
        infos.append(getNumberOfDirectories()+" Directories - ");
        infos.append(files.size()+" Total regular file\"");
        return infos.toString();
    }

    
}