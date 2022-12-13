package ibarodf.core.file;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

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
import net.lingala.zip4j.exception.ZipException;
import ibarodf.core.meta.MetaDataCreator;
import ibarodf.core.meta.MetaDataHyperlink;
import ibarodf.core.meta.MetaDataInitialCreator;
import ibarodf.core.meta.MetaDataKeyword;
import ibarodf.core.meta.MetaDataOdfPictures;
import ibarodf.core.meta.MetaDataSubject;
import ibarodf.core.file.exception.CannotAddAllMetadatasException;
import ibarodf.core.file.exception.CannotAddHyperLinkException;
import ibarodf.core.file.exception.CannotAddKeywordException;
import ibarodf.core.file.exception.CannotAddPictureException;
import ibarodf.core.file.exception.CannotAddStatisticsException;
import ibarodf.core.file.exception.CannotAddThumbnailException;
import ibarodf.core.file.exception.CannotLoadOdfDocumentException;
import ibarodf.core.file.exception.EmptyOdfFileException;
import ibarodf.core.meta.AbstractMetaData;
import ibarodf.core.meta.Hyperlink;
import ibarodf.core.meta.MetaDataComment;
import ibarodf.core.meta.MetaDataCreationDate;
import ibarodf.core.meta.MetaDataStats;


public class OdfFile extends RegularFile {
	private OdfDocument odf; 
	private OdfOfficeMeta meta; 
	private final TempDirHandler tempDirHandler; 
	private final LinkedHashMap<String, AbstractMetaData> metaDataHM = new LinkedHashMap<String, AbstractMetaData>();

	//Json Key
	public static final String METADATAS = "Metadata"; 


	public OdfFile(Path path) throws IOException, ZipException, EmptyOdfFileException, CannotLoadOdfDocumentException, CannotAddAllMetadatasException {
		super(path);
		tempDirHandler = new TempDirHandler(path);
		if(tempDirHandler.haveAnMetaXmlFile()){
			loadMetaData();
		}else{
			throw new EmptyOdfFileException(path);
		}	
	}	

	TempDirHandler getTempDirHandler(){
		return tempDirHandler;
	}
	
	public void loadMetaData() throws CannotLoadOdfDocumentException, CannotAddAllMetadatasException{
		try{
			odf = OdfDocument.loadDocument(getPath().toString());
			OdfMetaDom metaDom = odf.getMetaDom();
			meta = new OdfOfficeMeta(metaDom);
		}catch(Exception e){
			throw new CannotLoadOdfDocumentException(getFileName());
		}
		initAllMeta();
	}

	public LinkedHashMap<String, AbstractMetaData> getMetaData() {
		return metaDataHM;	
	}
	

	public void setMetaData(final String attribut, final String value) throws ParseException {
		try {
			metaDataHM.get(attribut).setValue(value);
		} catch (ibarodf.core.meta.exception.ReadOnlyMetaException e) {
			throw new RuntimeException(e);
		}
	}

	public Object getMetaData(final String attribut) throws Exception{
		if(!metaDataHM.containsKey(attribut)) 
			throw new IllegalArgumentException(String.format("Attribut %s doesn't exist", attribut));
		return metaDataHM.get(attribut).getValue();
	}

	@Override
	public StringBuilder displayMetaData() throws Exception{
		StringBuilder metaDataStr = new StringBuilder();
		metaDataStr.append("<");
		String lineStr;
		for (Map.Entry<String, AbstractMetaData> entry: getMetaData().entrySet()) {
			lineStr = String.format("%s: %s;",entry.getKey(), entry.getValue().getValue());
			metaDataStr.append(lineStr);
		}
		metaDataStr.append(">");
		return metaDataStr;
	}

	public void addMetaData(String key, AbstractMetaData metaData){
		metaDataHM.put(key,metaData); 
	}

	
	private void initAllMeta() throws CannotAddAllMetadatasException{
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


	public void addKeyword() throws CannotAddKeywordException{
		try{
		addMetaData(MetaDataKeyword.ATTR, new MetaDataKeyword(meta, meta.getKeywords()));
		}catch(Exception e){
			throw new CannotAddKeywordException(getFileName());
		}
	} 

	private void addCreationDate() {
		Calendar creationDateInXML = meta.getCreationDate();
		Calendar creationDate = creationDateInXML == null ? Calendar.getInstance() : creationDateInXML;
		String creationDateStr = MetaDataCreationDate.CalendarToFormattedString(creationDate);
		addMetaData(MetaDataCreationDate.ATTR, new MetaDataCreationDate(meta, creationDateStr));
	}

	private void addDocStats() throws CannotAddStatisticsException {
		try{
			addMetaData(MetaDataStats.ATTR, new MetaDataStats(meta, MetaDataStats.getStatistics(meta)));
		}catch(Exception e){
			throw new CannotAddStatisticsException(getFileName());
		}
	}
	
	public void addPictures() throws CannotAddPictureException{
		try{
			ArrayList<Picture> picturesDirectoryPath = tempDirHandler.getPicture();
			addMetaData(MetaDataOdfPictures.ATTR, new MetaDataOdfPictures(picturesDirectoryPath));
		}catch(NoPictureException e){
			addMetaData(MetaDataOdfPictures.ATTR, new MetaDataOdfPictures(new ArrayList<Picture>()));
		}catch(Exception e){
			System.out.println(e.getMessage());
			throw new CannotAddPictureException(getFileName());
		}
	}
	
	public void addThumbnail() throws CannotAddThumbnailException{
		try{
			Path thumbnailPath = getTempDirHandler().getThumbnailPath();
			addMetaData(Thumbnail.ATTR, new Thumbnail(thumbnailPath));
		}catch(Exception e){
			throw new CannotAddThumbnailException(getFileName());
		}
	}


	public void addHyperlink() throws CannotAddHyperLinkException{
		try{
			ArrayList<Hyperlink> hyperlinkList = tempDirHandler.getHyperlink();
			addMetaData(MetaDataHyperlink.ATTR,new MetaDataHyperlink(hyperlinkList));
		}catch(Exception e){
			throw new CannotAddHyperLinkException(getFileName());
		}
	}

	public void saveChange() throws Exception {
		odf.save(getPath().toString());
	}

	public JSONObject toJonObject() throws Exception{
		JSONObject odfFileJson = super.toJonObject();
		JSONArray metadataArray = new JSONArray();
		Collection<String> keys = metaDataHM.keySet();
		for(String key: keys){
			metadataArray.put(metaDataHM.get(key).toJson());
		}
		odfFileJson.put(METADATAS, metadataArray);
		return odfFileJson;
	}

}










