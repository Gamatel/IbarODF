package ibarodf.core.file;
import java.util.ArrayList;
import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import ibarodf.command.*;




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
                }else if(CommandTranslator.isAnOdtFile(currentPath.toString())){
                    files.add(new OdfFile(currentPath));
                }else{
                    files.add(new NotOdtFile(currentPath));
                }
            }catch(Exception e){
                files.add(new NotOdtFile(currentPath));
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
                filesPath.add(CommandTranslator.stringToPath(directoryPath.toString()+separator+currentPath));
            } 
        }catch(Exception e){
            System.err.println(e.getMessage());
        }   
        return filesPath;
    }



    public StringBuilder displayMetaData() throws Exception{
        StringBuilder metaDataStr = new StringBuilder();
        String directoryName = "{"+getPath().getFileName().toString()+":{\n";
        metaDataStr.append(directoryName);
        for(Directory currentDirectory : directories){
            metaDataStr.append(currentDirectory.displayMetaData());
        }
        for(AbstractGenericFile currentFile : files){ 
            metaDataStr.append(currentFile.displayMetaData());
        }
        metaDataStr.append("}\n");
        metaDataStr.append("}\n");
        return metaDataStr;
    }

    public String toString(){
        return "{Directory -- Path : "+ getPath() + " }"; 
    }

    
}