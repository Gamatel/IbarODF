package ibarodf.core.file;

import java.util.Map;
import java.util.HashMap;
import java.util.Calendar;
import java.text.ParseException;
import java.lang.StringBuilder;
import java.lang.IllegalArgumentException;
import ibarodf.core.meta.MetaDataAbstract;
import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.dom.OdfMetaDom;
import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;

import ibarodf.core.meta.MetaDataTitle;
import ibarodf.core.meta.MetaDataCreator;
import ibarodf.core.meta.MetaDataInitialCreator;
import ibarodf.core.meta.MetaDataSubject;
import ibarodf.core.meta.MetaDataDescription;
import ibarodf.core.meta.MetaDataCreationDate;


public class OdfFile extends RegularFile {
	private OdfDocument odf; 
	private OdfOfficeMeta meta; 

	public OdfFile(final String path) throws Exception {
		super(path);
		loadMetaData();
	}	

	@Override
	public void loadMetaData() throws Exception{
		odf = OdfDocument.loadDocument(getPath());
		OdfMetaDom metaDom = odf.getMetaDom();
		meta = new OdfOfficeMeta(metaDom);

		initAllMeta();
	}

	public void setMetaData(final String attribut, final String value) throws ParseException {
		HashMap<String, MetaDataAbstract> metaDataHM = getMetaData();
		metaDataHM.get(attribut).setValue(value);
	}

	public String getMetaData(final String attribut) {
		HashMap<String, MetaDataAbstract> metaDataHM = getMetaData();
		if(!metaDataHM.containsKey(attribut)) 
			throw new IllegalArgumentException(String.format("Attribut %s doesnn't exist", attribut));

		return metaDataHM.get(attribut).getValue();
	}

	private void initAllMeta() {
		HashMap<String, MetaDataAbstract> metaDataHM = getMetaData();
		Calendar calendar = meta.getCreationDate();
		String calendarStr = MetaDataCreationDate.CalendarToFormattedString(calendar);

		metaDataHM.put(MetaDataTitle.attr, new MetaDataTitle(meta, meta.getTitle()));
		metaDataHM.put(MetaDataCreator.attr, new MetaDataCreator(meta, meta.getCreator()));
		metaDataHM.put(MetaDataInitialCreator.attr, new MetaDataInitialCreator(meta, meta.getInitialCreator()));
		metaDataHM.put(MetaDataSubject.attr, new MetaDataSubject(meta, meta.getSubject()));
		metaDataHM.put(MetaDataDescription.attr, new MetaDataDescription(meta, meta.getDescription()));
		metaDataHM.put(MetaDataCreationDate.attr, new MetaDataCreationDate(meta, calendarStr));
	}

	@Override
	public String displayMetaData() {
		StringBuilder metaDataStr = new StringBuilder();
		
		for (Map.Entry<String, MetaDataAbstract> entry: getMetaData().entrySet()) {
			String lineStr = String.format("%s: %s,\n", entry.getKey(), entry.getValue());
			metaDataStr.append(lineStr);
		}

		return metaDataStr.toString();
	}

	public void saveChange() throws Exception {
		odf.save(getPath());
	}

	public void saveChangeInOtherFile(final String path) throws Exception {
		odf.save(path);
	}
}










