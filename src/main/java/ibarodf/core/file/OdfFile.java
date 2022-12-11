package ibarodf.core.file;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Calendar;

import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.dom.OdfMetaDom;
import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;
import org.odftoolkit.odfdom.incubator.meta.OdfMetaDocumentStatistic;

import ibarodf.core.meta.MetaDataTitle;
import ibarodf.core.meta.NoPictureException;
import ibarodf.core.meta.Thumbnail;
import ibarodf.core.meta.MetaDataCreator;
import ibarodf.core.meta.MetaDataHyperlink;
import ibarodf.core.meta.MetaDataInitialCreator;
import ibarodf.core.meta.MetaDataKeyword;
import ibarodf.core.meta.MetaDataOdfPictures;
import ibarodf.core.meta.MetaDataRegularFile;
import ibarodf.core.meta.MetaDataSubject;
import ibarodf.core.meta.Hyperlink;
import ibarodf.core.meta.MetaDataComment;
import ibarodf.core.meta.MetaDataCreationDate;
import ibarodf.core.meta.MetaDataStats;


public class OdfFile extends RegularFile {
	private OdfDocument odf; 
	private OdfOfficeMeta meta; 
	private final TempDirHandler tempDirHandler; 


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
	
	@Override
	public void loadMetaData() throws Exception{
		odf = OdfDocument.loadDocument(getPath().toString());
		OdfMetaDom metaDom = odf.getMetaDom();
		meta = new OdfOfficeMeta(metaDom);
		super.loadMetaData();
		initAllMeta();
	}
	
	private void initAllMeta() throws Exception{
		addMetaData(MetaDataTitle.ATTR, new MetaDataTitle(meta));
		addMetaData(MetaDataCreator.ATTR, new MetaDataCreator(meta));
		addMetaData(MetaDataInitialCreator.ATTR, new MetaDataInitialCreator(meta));
		addMetaData(MetaDataSubject.ATTR, new MetaDataSubject(meta));		
		addMetaData(MetaDataComment.ATTR, new MetaDataComment(meta));
		addMetaData(MetaDataKeyword.ATTR, new MetaDataKeyword(meta));
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
		OdfMetaDocumentStatistic stats = meta.getDocumentStatistic();
		String statsStr = MetaDataStats.objDocumentStatisticToStr(stats);

		addMetaData(MetaDataStats.ATTR, new MetaDataCreationDate(meta, statsStr));
	}
	
	public void addPictures(){
		try{
			Path picturesDirectoryPath = tempDirHandler.getPicturesDirectory();
			addMetaData(MetaDataOdfPictures.ATTR, new MetaDataOdfPictures(picturesDirectoryPath));
		}catch(NoPictureException e){
			addMetaData(MetaDataOdfPictures.ATTR, new MetaDataRegularFile(MetaDataOdfPictures.ATTR, "No picture."));
		}catch(Exception e){
			System.out.println(e.getMessage());
			System.out.println("Something went wrong with the addition of the pictures...");
		}
	}
	
	public void addThumbnail(){
		try{
			Path thumbnailPath = getTempDirHandler().getThumbnailPath();
			addMetaData(Thumbnail.ATTR, new Thumbnail(thumbnailPath));
		}catch(NoPictureException e){
			addMetaData(Thumbnail.ATTR, new MetaDataRegularFile(MetaDataOdfPictures.ATTR, "No thumbnail."));
		}catch(Exception e){
			System.out.println(e.getMessage());
			System.out.println("Something went wrong with the addition of the thumbnail...");
		}
	}


	public void addHyperlink(){
		try{
			ArrayList<Hyperlink> hyperlinkList = tempDirHandler.getHyperlink();
			int size = hyperlinkList.size();
			if(size == 0){
				addMetaData(MetaDataHyperlink.ATTR,new MetaDataHyperlink("No Hyperlink."));
			}else{
				StringBuilder value = new StringBuilder("[");
				for(Hyperlink hyperlink : hyperlinkList){
					value.append(hyperlink.toString());
				}
				value.append("]");
				addMetaData(MetaDataHyperlink.ATTR,new MetaDataHyperlink(value.toString()));
			}
		}catch(Exception e){
			System.err.println(e.getMessage());
			System.err.println("Something went wrong with the addition of the hyperlinks");
		}

	}

	public void saveChange() throws Exception {
		odf.save(getPath().toString());
	}
}










