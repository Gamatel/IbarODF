package ibarodf.core.file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedHashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.dom.OdfMetaDom;
import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;
import java.text.ParseException;

import net.lingala.zip4j.exception.ZipException;
import ibarodf.core.file.exception.UnableToAddMetadataException;
import ibarodf.core.file.exception.UnableToAddHyperLinkException;
import ibarodf.core.file.exception.UnableToAddKeywordException;
import ibarodf.core.file.exception.UnableToAddPictureException;
import ibarodf.core.file.exception.UnableToAddStatisticsException;
import ibarodf.core.file.exception.UnableToAddThumbnailException;
import ibarodf.core.file.exception.UnableToLoadOdfDocumentException;
import ibarodf.core.file.exception.UnableToSaveChangesException;
import ibarodf.core.metadata.AbstractMetadata;
import ibarodf.core.metadata.MetadataComment;
import ibarodf.core.metadata.MetadataCreationDate;
import ibarodf.core.metadata.MetadataCreator;
import ibarodf.core.metadata.MetadataHyperlink;
import ibarodf.core.metadata.MetadataInitialCreator;
import ibarodf.core.metadata.MetadataKeyword;
import ibarodf.core.metadata.MetadataOdfPictures;
import ibarodf.core.metadata.MetadataStats;
import ibarodf.core.metadata.MetadataSubject;
import ibarodf.core.metadata.MetadataThumbnail;
import ibarodf.core.metadata.MetadataTitle;
import ibarodf.core.metadata.exception.NoPictureException;
import ibarodf.core.metadata.exception.UnableToConvertToJsonFormatException;
import ibarodf.core.metadata.object.Hyperlink;
import ibarodf.core.metadata.object.Picture;
import ibarodf.core.file.exception.EmptyOdfFileException;

/**
 * The class OdfFile is a subclass of RegularFile. This stores the main metadata of Odf files
 */
public class OdfFile extends RegularFile {
	private OdfDocument odf;
	private OdfOfficeMeta meta;
	private final TempDirHandler tempDirHandler;
	private final LinkedHashMap<String, AbstractMetadata> metadata = new LinkedHashMap<String, AbstractMetadata>();

	// Json Key
	public static final String METADATA = "Metadata";
	public static final String HAVE_THESE_METADATA = "Have ";

	public OdfFile(Path path) throws IOException, ZipException, EmptyOdfFileException, UnableToLoadOdfDocumentException,UnableToAddMetadataException, FileNotFoundException{
		super(path);
		if(!path.toFile().exists()){
			throw new FileNotFoundException("The file "+ path.getFileName());
		}
		try {
			tempDirHandler = new TempDirHandler(path);
			if(tempDirHandler.haveAnMetaXmlFile()){
				loadMetaData();
			}
		} catch (Exception e) {
			throw new UnableToAddMetadataException(getPath(), e.getMessage());
		}
	}

	/**
	 * This function returns the TempDirHandler object that is used to makes
	 * operations on the unziped version of the ODF file
	 * 
	 * @see {@link ibarodf.core.file.TempDirHandler}
	 * @return The TempDirHandler object.
	 */
	TempDirHandler getTempDirHandler() {
		return tempDirHandler;
	}

	/**
	 * It loads the ODF document and initializes the metadata
	 * 
	 * @throws UnableToLoadOdfDocumentException
	 * @throws UnableToAddMetadataException
	 */
	private void loadMetaData() throws UnableToLoadOdfDocumentException, UnableToAddMetadataException {
		try {
			odf = OdfDocument.loadDocument(getPath().toString());
			OdfMetaDom metaDom = odf.getMetaDom();
			meta = new OdfOfficeMeta(metaDom);
			initAllMetadata();
		} catch (Exception e) {
			throw new UnableToLoadOdfDocumentException(getFileName());
		}
	}

	/**
	 * This function returns a LinkedHashMap of the metadata of the current object
	 * 
	 * @return A LinkedHashMap of String and AbstractMetaData.
	 */
	public LinkedHashMap<String, AbstractMetadata> getMetaData() {
		return metadata;
	}

	/**
	 * It sets the value of a metadata
	 * 
	 * @param attribut the name of the metadata to be set
	 * @param value    The value to set the attribute to.
	 * @throws ParseException
	 */
	public void setMetaData(final String attribut, final String value) throws ParseException {
		try {
			metadata.get(attribut).setValue(value);
		} catch (ibarodf.core.metadata.exception.ReadOnlyMetaException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * This methode gets the value of the metadata
	 * 
	 * @param attribut The name of the attribute you want to get the value of.
	 * @return The value of the attribute.
	 * @throws IllegalArgumentException
	 */
	public Object getMetaData(final String attribut) throws IllegalArgumentException {
		if (!metadata.containsKey(attribut))
			throw new IllegalArgumentException(String.format("Attribut %s doesn't exist", attribut));
		return metadata.get(attribut).getValue();
	}

	/**
	 * This function adds a metadata to the ODF file
	 * 
	 * @param key      The key that correspond to the type of metadata.
	 * @param metaData The metadata object to be added to the hash map.
	 */
	private void addMetadata(String key, AbstractMetadata metaData) {
		metadata.put(key, metaData);
	}

	/**
	 * It adds all the metadata to the ODF file
	 * 
	 * @throws UnableToAddMetadataException
	 */
	private void initAllMetadata() throws UnableToAddMetadataException {
		addMetadata(MetadataTitle.ATTR, new MetadataTitle(meta));
		addMetadata(MetadataCreator.ATTR, new MetadataCreator(meta));
		addMetadata(MetadataInitialCreator.ATTR, new MetadataInitialCreator(meta));
		addMetadata(MetadataSubject.ATTR, new MetadataSubject(meta));
		addMetadata(MetadataComment.ATTR, new MetadataComment(meta));
		addKeyword();
		addHyperlink();
		addCreationDate();
		addDocStats();
		addThumbnail();
		addPictures();
	}

	/**
	 * This function adds a keyword to the metadata of the file
	 * 
	 * @throws UnableToAddKeywordException
	 */
	private void addKeyword() throws UnableToAddKeywordException {
		try {
			addMetadata(MetadataKeyword.ATTR, new MetadataKeyword(meta, meta.getKeywords()));
		} catch (Exception e) {
			throw new UnableToAddKeywordException(getFileName());
		}
	}

	/**
	 * If the creation date is null and it that case set it to the current date.
	 * Then add it to the metatData of the file
	 */
	private void addCreationDate() {
		Calendar creationDateInXML = meta.getCreationDate();
		Calendar creationDate = creationDateInXML == null ? Calendar.getInstance() : creationDateInXML;
		String creationDateStr = MetadataCreationDate.CalendarToFormattedString(creationDate);
		addMetadata(MetadataCreationDate.ATTR, new MetadataCreationDate(meta, creationDateStr));
	}

	/**
	 * This function adds the statistics of the Odf files to the its metadata
	 * 
	 * @throws UnableToAddStatisticsException
	 */
	private void addDocStats() throws UnableToAddStatisticsException {
		try {
			addMetadata(MetadataStats.ATTR, new MetadataStats(meta, MetadataStats.getStatistics(meta)));
		} catch (Exception e) {
			throw new UnableToAddStatisticsException(getFileName());
		}
	}

	/**
	 * It adds the pictures of the ODF file to its the metadata
	 * @throws UnableToAddPictureException
	 */
	private void addPictures() throws UnableToAddPictureException {
		try {
			ArrayList<Picture> picturesDirectoryPath = tempDirHandler.getPictures();
			addMetadata(MetadataOdfPictures.ATTR, new MetadataOdfPictures(picturesDirectoryPath));
		} catch (NoPictureException e) {
			addMetadata(MetadataOdfPictures.ATTR, new MetadataOdfPictures(new ArrayList<Picture>()));
		} catch (Exception e) {
			throw new UnableToAddPictureException(getFileName());
		}
	}

	/**
	 * It adds the thumbnail of the ODF file to its metadata
	 * 
	 * @throws UnableToAddThumbnailException
	 */
	private void addThumbnail() throws UnableToAddThumbnailException {
		try {
			Path thumbnailPath = getTempDirHandler().getThumbnailPath();
			addMetadata(MetadataThumbnail.ATTR, new MetadataThumbnail(thumbnailPath));
		} catch (Exception e) {
			throw new UnableToAddThumbnailException(getFileName());
		}
	}

	/**
	 * It the hyperlinks of the file to its metadata
	 * 
	 * @throws UnableToAddHyperLinkException
	 */

	private void addHyperlink() throws UnableToAddHyperLinkException {
		try {
			ArrayList<Hyperlink> hyperlinkList = tempDirHandler.getHyperlink();
			addMetadata(MetadataHyperlink.ATTR, new MetadataHyperlink(hyperlinkList));
		} catch (Exception e) {
			throw new UnableToAddHyperLinkException(getFileName());
		}
	}

	/**
	 * This function saves the changes made to the odf file
	 * 
	 * @throws UnableToSaveChangesException
	 */
	public void saveChange() throws UnableToSaveChangesException {
		try {
			odf.save(getPath().toString());
		} catch (Exception e) {
			throw new UnableToSaveChangesException(getFileName());
		}
	}

	@Override
	public JSONObject toJonObject() throws UnableToConvertToJsonFormatException {
		try {
			JSONObject odfFileJson = super.toJonObject();
			JSONArray metadataKeys = new JSONArray();
			JSONArray metadataArray = new JSONArray();
			Collection<String> keys = metadata.keySet();
			JSONObject objectToAdd;
			for (String key : keys) {
				objectToAdd = metadata.get(key).toJson();
				if(!objectToAdd.isEmpty()){
					metadataArray.put(objectToAdd);
					metadataKeys.put(key);
				}
			}
			odfFileJson.put(METADATA, metadataArray);
			odfFileJson.put(HAVE_THESE_METADATA, metadataKeys);
			return odfFileJson;
		} catch (UnableToConvertToJsonFormatException e) {
			System.err.println(e.getLocalizedMessage());
			throw new UnableToConvertToJsonFormatException(getFileName());
		}
	}

}
