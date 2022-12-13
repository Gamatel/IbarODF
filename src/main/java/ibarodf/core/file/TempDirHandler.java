package ibarodf.core.file;


import ibarodf.core.meta.Hyperlink;
import ibarodf.core.meta.MetaDataHyperlink;
import ibarodf.core.meta.Picture;
import ibarodf.core.meta.exception.NoContentException;
import ibarodf.core.meta.exception.NoPictureException;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


import ibarodf.core.AbtractIbarOdfCore;
import net.lingala.zip4j.core.ZipFile;



public class TempDirHandler{
    private final Path fileToUnzipPath;
    private Path unzipedFilePath;
    

    public TempDirHandler(Path fileToUnzipPath) throws IOException, ZipException{
        this.fileToUnzipPath = fileToUnzipPath;
        try{
            unzipedFilePath = unzip();
        }
        catch(IOException e){
            System.err.println(e.getMessage());
        } 
    }

    private Path unzip() throws IOException, ZipException{
        Path destination = Files.createTempDirectory("IBARODF");
        File destinationfile = new File(destination.toString());
        destinationfile.deleteOnExit();
        ZipFile zipFile = new ZipFile(fileToUnzipPath.toString());
        zipFile.extractAll(destination.toString());
        return destination;
    }

    public Path getThumbnailPath() throws Exception{
        String separator = FileSystems.getDefault().getSeparator();
        File thumbnailFile = new File(unzipedFilePath.toString()+separator+"Thumbnails"+separator+"thumbnail.png");
        if(!thumbnailFile.exists()){
            throw new NoPictureException(fileToUnzipPath);
        }
        return AbtractIbarOdfCore.stringToPath(thumbnailFile.getAbsolutePath());
    }

    public Path getPicturesDirectory() throws Exception{
        String separator = FileSystems.getDefault().getSeparator(); 
        File picturesDirectory = new File(unzipedFilePath.toString()+separator+"Pictures");
        if(!picturesDirectory.exists()){
            throw new NoPictureException(fileToUnzipPath);
        }
        return AbtractIbarOdfCore.stringToPath(picturesDirectory.getAbsolutePath());
    }


    public ArrayList<Picture> getPicture() throws Exception{
        ArrayList<Path> picturesPathArrayList = Directory.getSubFilesPathFromDirectory(getPicturesDirectory());
        ArrayList<Picture>  picturesArrayList = new ArrayList<Picture>();
        for(Path picturePath :picturesPathArrayList){
            picturesArrayList.add(new Picture(picturePath));
        }
        return picturesArrayList;
    }

    public Path getContentFile() throws Exception{
        String separator = FileSystems.getDefault().getSeparator(); 
        File contentFile = new File(unzipedFilePath.toString()+separator+"content.xml");
        if(!contentFile.exists()){
            throw new NoContentException(fileToUnzipPath);
        }
        return AbtractIbarOdfCore.stringToPath(contentFile.getAbsolutePath());
    }

    public boolean haveAnMetaXmlFile(){
        String separator = FileSystems.getDefault().getSeparator(); 
        File metaXmlFile = new File(unzipedFilePath.toString()+separator+"meta.xml");
        if(!metaXmlFile.exists()){
            return false;
        }
        return true;
    }

    public NodeList getHyperlinksNodeList() throws Exception{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document contenteDocument = builder.parse(new File(getContentFile().toString()));
        NodeList hyperlinkList = contenteDocument.getElementsByTagName(MetaDataHyperlink.HYPERLINK_TAG);
        return hyperlinkList;
    }

    public ArrayList<Hyperlink> getHyperlink() throws Exception{
        NodeList hyperlinksNodeList = getHyperlinksNodeList();
        Element currentElement;
        Hyperlink hyperlinkToAdd;
        ArrayList<Hyperlink> hyperlinksArray = new ArrayList<Hyperlink>(); 
        for(int index=0, listLength = hyperlinksNodeList.getLength() ; index<listLength; index++){
            currentElement = (Element) hyperlinksNodeList.item(index);
            hyperlinkToAdd = new Hyperlink(currentElement.getAttribute(MetaDataHyperlink.TYPE_TAG),currentElement.getAttribute(MetaDataHyperlink.REFERENCE_TAG), currentElement.getAttribute(MetaDataHyperlink.STYLE_NAME_TAG), currentElement.getAttribute(MetaDataHyperlink.VISITED_STYLE_NAME_TAG));
            hyperlinksArray.add(hyperlinkToAdd);
        }        
        return hyperlinksArray;
    }





    
    
    
}
