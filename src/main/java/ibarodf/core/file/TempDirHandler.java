package ibarodf.core.file;

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

import ibarodf.core.IbarOdfCore;
import ibarodf.core.file.exception.EmptyOdfFileException;
import ibarodf.core.file.exception.UnableToReachHyperlinkException;
import ibarodf.core.meta.MetadataHyperlink;
import ibarodf.core.meta.exception.NoContentException;
import ibarodf.core.meta.exception.NoPictureException;
import ibarodf.core.meta.exception.UnableToReachPicture;
import ibarodf.core.meta.object.Hyperlink;
import ibarodf.core.meta.object.Picture;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

/**
 * The TempDirHandler class unzip an odf file.
 * It 's responsable for making the basics operations (such as looking for
 * its pictures, hyperlink...) on the unzip version of the file.
 */
public class TempDirHandler {
    private final Path fileToUnzipPath;
    private Path unzipedFilePath;

    public TempDirHandler(Path fileToUnzipPath) throws IOException, ZipException {
        this.fileToUnzipPath = fileToUnzipPath;
        try {
            unzipedFilePath = unzip();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    /**
     * It creates a temporary directory and unzips the file ODF file to
     * that directory
     * @return A Path object.
     * @throws IOException
     * @throws ZipException
     */
    private Path unzip() throws IOException, ZipException {
        Path destination = Files.createTempDirectory("IBARODF");
        File destinationfile = new File(destination.toString());
        destinationfile.deleteOnExit();
        ZipFile zipFile = new ZipFile(fileToUnzipPath.toString());
        zipFile.extractAll(destination.toString());
        return destination;
    }

    /**
     * It returns the path to the thumbnail of the unzip odf file
     * @return the Path Object
     * @throws NoPictureException
     * @throws IOException
     */
    public Path getThumbnailPath() throws NoPictureException, IOException {
        String separator = FileSystems.getDefault().getSeparator();
        File thumbnailFile = new File(
                unzipedFilePath.toString() + separator + "Thumbnails" + separator + "thumbnail.png");
        if (!thumbnailFile.exists()) {
            throw new NoPictureException(fileToUnzipPath);
        }
        return IbarOdfCore.stringToPath(thumbnailFile.getAbsolutePath());
    }

    /**
     * It returns the path to the directory containing the pictures of the ODF file
     * @return A Path Object
     * @throws NoPictureException
     * @throws IOException
     */
    private Path getPicturesDirectory() throws NoPictureException, IOException {
        String separator = FileSystems.getDefault().getSeparator();
        File picturesDirectory = new File(unzipedFilePath.toString() + separator + "Pictures");
        if (!picturesDirectory.exists()) {
            throw new NoPictureException(fileToUnzipPath);
        }
        return IbarOdfCore.stringToPath(picturesDirectory.getAbsolutePath());
    }

    /**
     * It returns an ArrayList of Picture objects, which are created from the paths
     * of the pictures in the pictures directory
     * @return An ArrayList of Picture objects.
     * @throws IOException
     * @throws NoPictureException 
     * @throws UnableToReachPicture 
     */
    public ArrayList<Picture> getPictures() throws IOException, NoPictureException, UnableToReachPicture {
        ArrayList<Path> picturesPathArrayList = Directory.getSubFilesPathFromDirectory(getPicturesDirectory());
        ArrayList<Picture> picturesArrayList = new ArrayList<Picture>();
        for (Path picturePath : picturesPathArrayList) {
            picturesArrayList.add(new Picture(picturePath));
        }
        return picturesArrayList;
    }

    /**
     * It returns the path to the content.xml file
     * @return A Path object.
     * @throws NoContentException
     * @throws IOException 
     */
    private Path getContentFile() throws NoContentException, IOException {
        String separator = FileSystems.getDefault().getSeparator();
        File contentFile = new File(unzipedFilePath.toString() + separator + "content.xml");
        if (!contentFile.exists()) {
            throw new NoContentException(fileToUnzipPath);
        }
        return IbarOdfCore.stringToPath(contentFile.getAbsolutePath());
    }

    /**
     * It checks if the file meta.xml exists in the unzipped file
     * @throws EmptyOdfFileException
     */
    public void haveAnMetaXmlFile() throws EmptyOdfFileException {
        String separator = FileSystems.getDefault().getSeparator();
        File metaXmlFile = new File(unzipedFilePath.toString() + separator + "meta.xml");
        if (!metaXmlFile.exists()) {
            throw new EmptyOdfFileException(fileToUnzipPath);
        }
    }

    /**
     * It parses the contente.xml of the ODF file and returns a list of
     * all the nodes representing a hyperlink
     * @return A NodeList of all the hyperlinks in the content.xml file.
     * @throws  UnableToReachHyperlinkException
     */
    public NodeList getHyperlinksNodeList() throws UnableToReachHyperlinkException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document contenteDocument = builder.parse(new File(getContentFile().toString()));
            NodeList hyperlinkList = contenteDocument.getElementsByTagName(MetadataHyperlink.HYPERLINK_TAG);
            return hyperlinkList;
        } catch (Exception e) {
            throw new UnableToReachHyperlinkException(fileToUnzipPath.getFileName().toString());
        }
    }

    /**
     * It gets a list of hyperlinks from the XML file, and then creates a new Hyperlink object for each
     * one, and adds it to an ArrayList
     * @return An ArrayList of Hyperlink objects.
     * @throws UnableToReachHyperlinkException
     */
    public ArrayList<Hyperlink> getHyperlink() throws UnableToReachHyperlinkException {
        NodeList hyperlinksNodeList = getHyperlinksNodeList();
        Element currentElement;
        Hyperlink hyperlinkToAdd;
        ArrayList<Hyperlink> hyperlinksArray = new ArrayList<Hyperlink>();
        for (int index = 0, listLength = hyperlinksNodeList.getLength(); index < listLength; index++) {
            currentElement = (Element) hyperlinksNodeList.item(index);
            hyperlinkToAdd = new Hyperlink(currentElement.getAttribute(MetadataHyperlink.TYPE_TAG),
                    currentElement.getAttribute(MetadataHyperlink.REFERENCE_TAG),
                    currentElement.getAttribute(MetadataHyperlink.STYLE_NAME_TAG),
                    currentElement.getAttribute(MetadataHyperlink.VISITED_STYLE_NAME_TAG));
            hyperlinksArray.add(hyperlinkToAdd);
        }
        return hyperlinksArray;
    }

}
