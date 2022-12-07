package ibarodf.core.file;

import ibarodf.core.meta.NoPictureException;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import ibarodf.core.IbarODFCore;
import net.lingala.zip4j.core.ZipFile;



public class TempDirHandler {
    private final Path fileToUnzipPath;
    private Path unzipedFilePath;

    public TempDirHandler(Path fileToUnzipPath){
        this.fileToUnzipPath = fileToUnzipPath;
        try{
            unzipedFilePath = unzip();
        }
        catch(IOException e){
            System.err.println(e.getMessage());
        } 
    }

    private Path unzip() throws IOException{
        Path destination = Files.createTempDirectory("IBARODF");
        File destinationfile = new File(destination.toString());
        destinationfile.deleteOnExit();
        try {
            ZipFile zipFile = new ZipFile(fileToUnzipPath.toString());
            zipFile.extractAll(destination.toString());
        } catch (ZipException e) {
            System.err.println("DECOMPRESSION ERROR");
        }
        return destination;
    }

    public Path getThumbnailPath() throws Exception{
        String separator = FileSystems.getDefault().getSeparator();
        File thumbnailFile = new File(unzipedFilePath.toString()+separator+"Thumbnails"+separator+"thumbnail.png");
        if(!thumbnailFile.exists()){
            throw new NoPictureException(fileToUnzipPath);
        }
        return IbarODFCore.stringToPath(thumbnailFile.getAbsolutePath());
    }

    public Path getPicturesDirectory() throws Exception{
        String separator = FileSystems.getDefault().getSeparator(); 
        File picturesDirectory = new File(unzipedFilePath.toString()+separator+"Pictures");
        if(!picturesDirectory.exists()){
            throw new NoPictureException(fileToUnzipPath);
        }
        return IbarODFCore.stringToPath(picturesDirectory.getAbsolutePath());
    }
}