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
        ArrayList<Path> filesPath = getFilesPath();
        addFiles(filesPath);
    }


    void addFiles(ArrayList<Path> filesPath){
        try{
            for(Path currentPath : filesPath){
                if(Files.isDirectory(currentPath)){
                    directories.add(new Directory(currentPath));
                }else if(CommandTranslator.isAnOdtFile(currentPath.toString())){
                    files.add(new OdfFile(currentPath));
                }else{
                    files.add(new NotOdtFile(currentPath));
                }
            }
        }catch(Exception e){
            System.err.println(e.getMessage());
        }
    }


    public ArrayList<Path> getFilesPath(){
        ArrayList<Path> filesPath = new ArrayList<Path>();
        String[] textPath;
        File directory = new File(getPath().toString());
        textPath = directory.list();
        try{
            for(String currentPath : textPath){ 
                filesPath.add(CommandTranslator.stringToPath(getPath().toString()+FileSystems.getDefault().getSeparator()+currentPath));
            } 
        }catch(Exception e){
            System.err.println(e.getMessage());
        }   
        return filesPath;
    }



    public StringBuilder displayMetaData() throws Exception{
        StringBuilder metaDataStr = new StringBuilder();
        String directoryName = getPath().getFileName().toString()+":{\n";
        metaDataStr.append(directoryName);
        for(Directory currentDirectory : directories){
            metaDataStr.append(currentDirectory.displayMetaData());
        }
        for(AbstractGenericFile currentFile : files){ 
            metaDataStr.append(currentFile.displayMetaData());
        }
        metaDataStr.append("}\n");
        return metaDataStr;
    }

    public String toString(){
        return "{Directory -- Path : "+ getPath() + " }"; 
    }

    
}