package ibarodf.core.meta;

import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import net.lingala.zip4j.core.ZipFile;



public class MetaDataHandler {
    private Path fileToUnzipPath;
    private Path unzipedFilePath;

    public MetaDataHandler(Path filePath){
        this.fileToUnzipPath = filePath;
        try{
            unzipedFilePath = unzip();
        }
        catch(IOException e){
            System.err.println(e.getMessage());
        } 
    }

    private Path unzip() throws IOException {
        Path destination = Files.createTempDirectory("IBARODF");
        File destinationfile = new File(destination.toString());
        destinationfile.deleteOnExit();
        try {
             ZipFile zipFile = new ZipFile(fileToUnzipPath.toString());
             zipFile.extractAll(destination.toString());
        } catch (ZipException e) {
            e.printStackTrace();
        }
        System.out.println("Path : "+ destination);
        return destination;
    }

    public Path getUnzipedFilePath(){
        return unzipedFilePath;
    }



}
