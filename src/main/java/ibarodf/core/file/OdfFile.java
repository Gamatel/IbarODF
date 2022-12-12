package ibarodf.core.file;

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
import ibarodf.core.meta.NoPictureException;
import ibarodf.core.meta.Picture;
import ibarodf.core.meta.Thumbnail;
import ibarodf.core.meta.MetaDataCreator;
import ibarodf.core.meta.MetaDataHyperlink;
import ibarodf.core.meta.MetaDataInitialCreator;
import ibarodf.core.meta.MetaDataKeyword;
import ibarodf.core.meta.MetaDataOdfPictures;
import ibarodf.core.meta.MetaDataSubject;
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


	public OdfFile(Path path) throws Exception{
		super(path);
		tempDirHandler = new TempDirHandler(path);
		try{
			loadMetaData();
		}catch(Exception e){
			throw new Exception("Something went wrong in the addition of the metadatas."); 
		}	
	}	

	TempDirHandler getTempDirHandler(){
		return tempDirHandler;
	}
	
	public void loadMetaData() throws Exception{
		odf = OdfDocument.loadDocument(getPath().toString());
		OdfMetaDom metaDom = odf.getMetaDom();
		meta = new OdfOfficeMeta(metaDom);
		initAllMeta();
	}

	public LinkedHashMap<String, AbstractMetaData> getMetaData() {
		return metaDataHM;	
	}
	

	public void setMetaData(final String attribut, final String value) throws ParseException {
		try {
			metaDataHM.get(attribut).setValue(value);
		} catch (ibarodf.core.meta.ReadOnlyMetaException e) {
			throw new RuntimeException(e);
		}
	}

	public Object getMetaData(final String attribut) throws Exception{
		if(!metaDataHM.containsKey(attribut)) 
			throw new IllegalArgumentException(String.format("Attribut %s doesnn't exist", attribut));
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

	
	private void initAllMeta() throws Exception{
		addMetaData(MetaDataTitle.ATTR, new MetaDataTitle(meta));
		addMetaData(MetaDataCreator.ATTR, new MetaDataCreator(meta));
		addMetaData(MetaDataInitialCreator.ATTR, new MetaDataInitialCreator(meta));
		addMetaData(MetaDataSubject.ATTR, new MetaDataSubject(meta));		
		addMetaData(MetaDataComment.ATTR, new MetaDataComment(meta));
		addMetaData(MetaDataKeyword.ATTR, new MetaDataKeyword(meta, meta.getKeywords()));
		addHyperlink();
		addCreationDate();
		addDocStats();
		addThumbnail();
		addPictures();
	}

	private void addCreationDate() {
		Calendar creationDateInXML = meta.getCreationDate();
		Calendar creationDate = creationDateInXML == null ? Calendar.getInstance() : creationDateInXML;
		String creationDateStr = MetaDataCreationDate.CalendarToFormattedString(creationDate);

		addMetaData(MetaDataCreationDate.ATTR, new MetaDataCreationDate(meta, creationDateStr));
	}

	private void addDocStats() {
		addMetaData(MetaDataStats.ATTR, new MetaDataStats(meta, MetaDataStats.getStatistics(meta)));
	}
	
	public void addPictures(){
		try{
			ArrayList<Picture> picturesDirectoryPath = tempDirHandler.getPicture();
			addMetaData(MetaDataOdfPictures.ATTR, new MetaDataOdfPictures(picturesDirectoryPath));
		}catch(NoPictureException e){
			addMetaData(MetaDataOdfPictures.ATTR, new MetaDataOdfPictures(new ArrayList<Picture>()));
		}catch(Exception e){
			System.out.println(e.getMessage());
			System.out.println("Something went wrong with the addition of the pictures...");
		}
	}
	
	public void addThumbnail(){
		try{
			Path thumbnailPath = getTempDirHandler().getThumbnailPath();
			addMetaData(Thumbnail.ATTR, new Thumbnail(thumbnailPath));
		}catch(Exception e){
			System.out.println(e.getMessage());
			System.out.println("Something went wrong with the addition of the thumbnail...");
		}
	}


	public void addHyperlink(){
		try{
			ArrayList<Hyperlink> hyperlinkList = tempDirHandler.getHyperlink();
			addMetaData(MetaDataHyperlink.ATTR,new MetaDataHyperlink(hyperlinkList));
		}catch(Exception e){
			System.err.println(e.getMessage());
			System.err.println("Something went wrong with the addition of the hyperlinks");
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










