package ibarodf.core.file;

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

import ibarodf.core.meta.MetadataTitle;
import ibarodf.core.meta.MetadataThumbnail;
import ibarodf.core.meta.exception.NoPictureException;
import ibarodf.core.meta.exception.UnableToConvertToJsonFormatException;
import ibarodf.core.meta.object.Hyperlink;
import ibarodf.core.meta.object.Picture;
import net.lingala.zip4j.exception.ZipException;
import ibarodf.core.meta.MetadataCreator;
import ibarodf.core.meta.MetadataHyperlink;
import ibarodf.core.meta.MetadataInitialCreator;
import ibarodf.core.meta.MetadataKeyword;
import ibarodf.core.meta.MetadataOdfPictures;
import ibarodf.core.file.exception.UnableToAddMetadataException;
import ibarodf.core.file.exception.UnableToAddHyperLinkException;
import ibarodf.core.file.exception.UnableToAddKeywordException;
import ibarodf.core.file.exception.UnableToAddPictureException;
import ibarodf.core.file.exception.UnableToAddStatisticsException;
import ibarodf.core.file.exception.UnableToAddThumbnailException;
import ibarodf.core.file.exception.UnableToLoadOdfDocumentException;
import ibarodf.core.file.exception.UnableToSaveChangesException;
import ibarodf.core.file.exception.EmptyOdfFileException;
import ibarodf.core.meta.AbstractMetadata;
import ibarodf.core.meta.MetadataComment;
import ibarodf.core.meta.MetadataCreationDate;
import ibarodf.core.meta.MetadataStats;
import ibarodf.core.meta.MetadataSubject;

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

	public OdfFile(Path path) throws IOException, ZipException, EmptyOdfFileException, UnableToLoadOdfDocumentException,
			UnableToAddMetadataException {
		super(path);
		try {
			tempDirHandler = new TempDirHandler(path);
			tempDirHandler.haveAnMetaXmlFile();
			loadMetaData();
		} catch (EmptyOdfFileException e) {
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
		} catch (ibarodf.core.meta.exception.ReadOnlyMetaException e) {
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
			JSONArray metadataArray = new JSONArray();
			Collection<String> keys = metadata.keySet();
			for (String key : keys) {
				metadataArray.put(metadata.get(key).toJson());
			}
			odfFileJson.put(METADATA, metadataArray);
			return odfFileJson;
		} catch (UnableToConvertToJsonFormatException e) {
			throw new UnableToConvertToJsonFormatException(getFileName());
		}
	}

}
