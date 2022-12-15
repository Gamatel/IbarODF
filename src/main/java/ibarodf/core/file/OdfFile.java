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


import ibarodf.core.meta.MetaDataTitle;
import ibarodf.core.meta.Picture;
import ibarodf.core.meta.Thumbnail;
import ibarodf.core.meta.exception.NoPictureException;
import ibarodf.core.meta.exception.UnableToConvertToJsonFormatException;
import net.lingala.zip4j.exception.ZipException;
import ibarodf.core.meta.MetaDataCreator;
import ibarodf.core.meta.MetaDataHyperlink;
import ibarodf.core.meta.MetaDataInitialCreator;
import ibarodf.core.meta.MetaDataKeyword;
import ibarodf.core.meta.MetaDataOdfPictures;
import ibarodf.core.meta.MetaDataSubject;
import ibarodf.core.file.exception.UnableToAddMetadataException;
import ibarodf.core.file.exception.UnableToAddHyperLinkException;
import ibarodf.core.file.exception.UnableToAddKeywordException;
import ibarodf.core.file.exception.UnableToAddPictureException;
import ibarodf.core.file.exception.UnableToAddStatisticsException;
import ibarodf.core.file.exception.UnableToAddThumbnailException;
import ibarodf.core.file.exception.UnableToLoadOdfDocumentException;
import ibarodf.core.file.exception.UnableToSaveChangesException;
import ibarodf.core.file.exception.EmptyOdfFileException;
import ibarodf.core.meta.AbstractMetaData;
import ibarodf.core.meta.Hyperlink;
import ibarodf.core.meta.MetaDataComment;
import ibarodf.core.meta.MetaDataCreationDate;
import ibarodf.core.meta.MetaDataStats;


/**
 * The class OdfFile is a subclass of RegularFile. It contains a LinkedHashMap of AbstractMetaData
 * 
 */
public class OdfFile extends RegularFile {
	private OdfDocument odf; 
	private OdfOfficeMeta meta; 
	private final TempDirHandler tempDirHandler; 
	private final LinkedHashMap<String, AbstractMetaData> metaDataHM = new LinkedHashMap<String, AbstractMetaData>();

	//Json Key
	public static final String METADATAS = "Metadata"; 


	public OdfFile(Path path) throws IOException, ZipException, EmptyOdfFileException, UnableToLoadOdfDocumentException, UnableToAddMetadataException {
		super(path);
		try {
			tempDirHandler = new TempDirHandler(path);
			tempDirHandler.haveAnMetaXmlFile();
			loadMetaData();
		}catch(EmptyOdfFileException e){
			throw new UnableToAddMetadataException(getPath(), e.getMessage());
		}
	}	

	TempDirHandler getTempDirHandler(){
		return tempDirHandler;
	}
	
	private void loadMetaData() throws UnableToLoadOdfDocumentException, UnableToAddMetadataException{
		try{
			odf = OdfDocument.loadDocument(getPath().toString());
			OdfMetaDom metaDom = odf.getMetaDom();
			meta = new OdfOfficeMeta(metaDom);
			initAllMeta();
		}catch(Exception e){
			throw new UnableToLoadOdfDocumentException(getFileName());
		}
	}

	/**
	 * This function returns a LinkedHashMap of the metadata of the current object
	 * @return A LinkedHashMap of String and AbstractMetaData.
	 */
	public LinkedHashMap<String, AbstractMetaData> getMetaData() {
		return metaDataHM;	
	}
	
	/**
	 * It sets the value of a metadata
	 * @param attribut the name of the metadata to be set
	 * @param value The value to set the attribute to.
	 */

	public void setMetaData(final String attribut, final String value) throws ParseException {
		try {
			metaDataHM.get(attribut).setValue(value);
		} catch (ibarodf.core.meta.exception.ReadOnlyMetaException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * This methode gets the value of the metadata  
	 * @param attribut The name of the attribute you want to get the value of.
	 * @return The value of the attribute.
	 */
	public Object getMetaData(final String attribut){
		if(!metaDataHM.containsKey(attribut)) 
			throw new IllegalArgumentException(String.format("Attribut %s doesn't exist", attribut));
		return metaDataHM.get(attribut).getValue();
	}

	private void addMetaData(String key, AbstractMetaData metaData){
		metaDataHM.put(key,metaData); 
	}

	
	private void initAllMeta() throws UnableToAddMetadataException{
		addMetaData(MetaDataTitle.ATTR, new MetaDataTitle(meta));
		addMetaData(MetaDataCreator.ATTR, new MetaDataCreator(meta));
		addMetaData(MetaDataInitialCreator.ATTR, new MetaDataInitialCreator(meta));
		addMetaData(MetaDataSubject.ATTR, new MetaDataSubject(meta));		
		addMetaData(MetaDataComment.ATTR, new MetaDataComment(meta));
		addKeyword();
		addHyperlink();
		addCreationDate();
		addDocStats();
		addThumbnail();
		addPictures();
	}


	private void addKeyword() throws UnableToAddKeywordException{
		try{
		addMetaData(MetaDataKeyword.ATTR, new MetaDataKeyword(meta, meta.getKeywords()));
		}catch(Exception e){
			throw new UnableToAddKeywordException(getFileName());
		}
	} 

	private void addCreationDate() {
		Calendar creationDateInXML = meta.getCreationDate();
		Calendar creationDate = creationDateInXML == null ? Calendar.getInstance() : creationDateInXML;
		String creationDateStr = MetaDataCreationDate.CalendarToFormattedString(creationDate);
		addMetaData(MetaDataCreationDate.ATTR, new MetaDataCreationDate(meta, creationDateStr));
	}

	private void addDocStats() throws UnableToAddStatisticsException {
		try{
			addMetaData(MetaDataStats.ATTR, new MetaDataStats(meta, MetaDataStats.getStatistics(meta)));
		}catch(Exception e){
			throw new UnableToAddStatisticsException(getFileName());
		}
	}
	
	private void addPictures() throws UnableToAddPictureException{
		try{
			ArrayList<Picture> picturesDirectoryPath = tempDirHandler.getPictures();
			addMetaData(MetaDataOdfPictures.ATTR, new MetaDataOdfPictures(picturesDirectoryPath));
		}catch(NoPictureException e){
			addMetaData(MetaDataOdfPictures.ATTR, new MetaDataOdfPictures(new ArrayList<Picture>()));
		}catch(Exception e){
			throw new UnableToAddPictureException(getFileName());
		}
	}
	
	private void addThumbnail() throws UnableToAddThumbnailException{
		try{
			Path thumbnailPath = getTempDirHandler().getThumbnailPath();
			addMetaData(Thumbnail.ATTR, new Thumbnail(thumbnailPath));
		}catch(Exception e){
			throw new UnableToAddThumbnailException(getFileName());
		}
	}


	private void addHyperlink() throws UnableToAddHyperLinkException{
		try{
			ArrayList<Hyperlink> hyperlinkList = tempDirHandler.getHyperlink();
			addMetaData(MetaDataHyperlink.ATTR,new MetaDataHyperlink(hyperlinkList));
		}catch(Exception e){
			throw new UnableToAddHyperLinkException(getFileName());
		}
	}

	/**
	 * This function saves the changes made to the odf file
	 * @throws UnableToSaveChangesException
	 */
	public void saveChange() throws UnableToSaveChangesException {
		try {
			odf.save(getPath().toString());
		}catch(Exception e){
			throw new UnableToSaveChangesException(getFileName());
		}
	}

	/**
	* It gives a JSON reprensentation of the odf file.
 	* @return A JSONObject
 	*/
	public JSONObject toJonObject() throws UnableToConvertToJsonFormatException {
		try {
			JSONObject odfFileJson = super.toJonObject();
			JSONArray metadataArray = new JSONArray();
			Collection<String> keys = metaDataHM.keySet();
			for(String key: keys){
				metadataArray.put(metaDataHM.get(key).toJson());
			}
			odfFileJson.put(METADATAS, metadataArray);
			return odfFileJson;
		}catch(UnableToConvertToJsonFormatException e){
			throw new  UnableToConvertToJsonFormatException(getFileName());
		}
	}

}










