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
import ibarodf.core.meta.MetaDataOdtPictures;
import ibarodf.core.meta.MetaDataRegularFile;
import ibarodf.core.meta.MetaDataSubject;
import ibarodf.core.meta.MetaDataComment;
import ibarodf.core.meta.MetaDataCreationDate;


public class OdfFile extends AbstractRegularFile {
	private OdfDocument odf; 
	private OdfOfficeMeta meta; 
	private TempDirHandler tempDirHandler;
	private static int numberOfOdfFile = 0; 

	public OdfFile(final Path path) throws Exception {
		super(path);
		try{
		tempDirHandler = new TempDirHandler(path);
		loadMetaData();
		}catch(Exception e){
			throw new Exception("Something went wrong with the decompression of "+ path.getFileName()+".\nIt migth not be a REAL odt file.");
		}
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
		Calendar calendar = meta.getCreationDate();
		String calendarStr = MetaDataCreationDate.CalendarToFormattedString(calendar);
		addMetaData(MetaDataTitle.ATTR, new MetaDataTitle(meta, meta.getTitle()));
		addMetaData(MetaDataCreator.ATTR, new MetaDataCreator(meta, meta.getCreator()));
		addMetaData(MetaDataInitialCreator.ATTR, new MetaDataInitialCreator(meta, meta.getInitialCreator()));
		addMetaData(MetaDataSubject.ATTR, new MetaDataSubject(meta, meta.getSubject()));		
		addMetaData(MetaDataComment.ATTR, new MetaDataComment(meta, meta.getDescription()));
		addMetaData(MetaDataCreationDate.ATTR, new MetaDataCreationDate(meta, calendarStr));
		addMetaData(Thumbnail.ATTR, new Thumbnail(tempDirHandler.getThumbnailPath()));
		addPictures();
	}

	public void addPictures(){
		try{
			Path picturesDirectoryPath = tempDirHandler.getPicturesDirectory();
			addMetaData(MetaDataOdtPictures.ATTR, new MetaDataOdtPictures(picturesDirectoryPath));
		}catch(NoPictureException e){
			addMetaData(MetaDataOdtPictures.ATTR, new MetaDataRegularFile(MetaDataOdtPictures.ATTR, "No picture."));
		}catch(Exception e){
			System.out.println("Something went wrong with the addition of the pictures...");
		}
	}

	public void addThumbnail(){
		try{
			Path thumbnailPath = tempDirHandler.getThumbnailPath();
			addMetaData(Thumbnail.ATTR, new Thumbnail(thumbnailPath));
		}catch(NoPictureException e){
			addMetaData(MetaDataOdtPictures.ATTR, new MetaDataRegularFile(MetaDataOdtPictures.ATTR, "No thumbnail."));
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

	public static int getNumberOfOdfFile(){
		return numberOfOdfFile; 
	}
}










