package ibarodf.core.file;

import java.nio.file.Path;
import java.util.Calendar;

import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.dom.OdfMetaDom;
import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;

import ibarodf.core.meta.MetaDataTitle;
import ibarodf.core.meta.NoPictureException;
import ibarodf.core.meta.Thumbnail;
import ibarodf.core.meta.MetaDataCreator;
import ibarodf.core.meta.MetaDataInitialCreator;
import ibarodf.core.meta.MetaDataOdfPictures;
import ibarodf.core.meta.MetaDataRegularFile;
import ibarodf.core.meta.MetaDataSubject;
import ibarodf.core.meta.MetaDataComment;
import ibarodf.core.meta.MetaDataCreationDate;


public class OdfFile extends RegularFile {
	private OdfDocument odf; 
	private OdfOfficeMeta meta; 
	private TempDirHandler tempDirHandler;

	public OdfFile(final Path path) throws Exception {
		super(path);
		try{
		tempDirHandler = new TempDirHandler(path);
		loadMetaData();
		}catch(Exception e){
			throw new Exception("Something went wrong with the decompression of "+ path.getFileName()+".\nIt migth not be a REAL odt file.");
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
		addMetaData(MetaDataTitle.ATTR, new MetaDataTitle(meta, meta.getTitle()));
		addMetaData(MetaDataCreator.ATTR, new MetaDataCreator(meta, meta.getCreator()));
		addMetaData(MetaDataInitialCreator.ATTR, new MetaDataInitialCreator(meta, meta.getInitialCreator()));
		addMetaData(MetaDataSubject.ATTR, new MetaDataSubject(meta, meta.getSubject()));		
		addMetaData(MetaDataComment.ATTR, new MetaDataComment(meta, meta.getDescription()));
		addMetaData(Thumbnail.ATTR, new Thumbnail(tempDirHandler.getThumbnailPath()));
		addPictures();
		addCreationDate();
	}

	private void addCreationDate() {
		Calendar creationDateInXML = meta.getCreationDate();
		Calendar creationDate = creationDateInXML.equals(null) ? Calendar.getInstance() : creationDateInXML;
		String creationDateStr = MetaDataCreationDate.CalendarToFormattedString(creationDate);

		addMetaData(MetaDataCreationDate.ATTR, new MetaDataCreationDate(meta, creationDateStr));
	}
	
	public void addPictures(){
		try{
			Path picturesDirectoryPath = tempDirHandler.getPicturesDirectory();
			addMetaData(MetaDataOdfPictures.ATTR, new MetaDataOdfPictures(picturesDirectoryPath));
		}catch(NoPictureException e){
			addMetaData(MetaDataOdfPictures.ATTR, new MetaDataRegularFile(MetaDataOdfPictures.ATTR, "No picture."));
		}catch(Exception e){
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
			System.out.println("Something went wrong with the addition of the thumbnail...");
		}

	}

	public void saveChange() throws Exception {
		odf.save(getPath().toString());
	}

	public void saveChangeInOtherFile(final String path) throws Exception {
		odf.save(path);
	}



}










