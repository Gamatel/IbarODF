package ibarodf.core.meta;

import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import ibarodf.command.Command;
import ibarodf.command.CommandTranslator;
import net.lingala.zip4j.core.ZipFile;



public class MetaDataHandler {
    private Path fileToUnzipPath;
    private Path unzipedFilePath;

    public MetaDataHandler(Path fileToUnzipPath){
        this.fileToUnzipPath = fileToUnzipPath;
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
        System.out.println(fileToUnzipPath.getFileName() + " decompressed : "+ destination);
        return destination;
    }

    public Path getThumbnailPath() throws Exception{
        String separator = FileSystems.getDefault().getSeparator();
        return CommandTranslator.stringToPath(unzipedFilePath.toString()+separator+"Thumbnails"+separator+"thumbnail");
    }

    public Path getUnzipedFilePath(){
        return unzipedFilePath;
    }



}
