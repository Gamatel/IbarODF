package ibarodf.core.file;

import java.nio.file.Path;
import java.util.Calendar;
import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.dom.OdfMetaDom;
import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;

import ibarodf.core.meta.MetaDataTitle;
import ibarodf.core.meta.MetaDataCreator;
import ibarodf.core.meta.MetaDataInitialCreator;
import ibarodf.core.meta.MetaDataSubject;
import ibarodf.core.meta.MetaDataDescription;
import ibarodf.core.meta.MetaDataHandler;
import ibarodf.core.meta.MetaDataCreationDate;


public class OdfFile extends AbstractRegularFile {
	private OdfDocument odf; 
	private OdfOfficeMeta meta; 

	public OdfFile(final Path path) throws Exception {
		super(path);
		loadMetaData();
		MetaDataHandler handler = new MetaDataHandler(path);
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
		addMetaData(MetaDataDescription.ATTR, new MetaDataDescription(meta, meta.getDescription()));
		addMetaData(MetaDataCreationDate.ATTR, new MetaDataCreationDate(meta, calendarStr));
	}

	public void saveChange() throws Exception {
		odf.save(getPath().toString());
	}

	public void saveChangeInOtherFile(final String path) throws Exception {
		odf.save(path);
	}
}










