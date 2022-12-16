package ibarodf.core.meta;

import java.lang.Integer;
import java.text.ParseException;
import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;

import ibarodf.core.meta.exception.NoStatisticsException;
import ibarodf.core.meta.exception.ReadOnlyMetaException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.odftoolkit.odfdom.incubator.meta.OdfMetaDocumentStatistic;
import java.util.Collection;
import java.util.HashMap;

public class MetadataStats extends MetadataXML {
	public final static String ATTR = "Statistic";

	// Json key
	public final static String STATISTICS = "Statistics";
	public final static String CELLCOUNT = "cellCount";
	public final static String CHARACTERCOUNT = "characterCount";
	public final static String DRAWNCOUNT = "drawnCount";
	public final static String FRAMECOUNT = "frameCount";
	public final static String IMAGECOUNT = "imageCount";
	public final static String NONWHITESPACECHARACTERCOUNT = "nonWhitespaceCharacterCount";
	public final static String OBJECTCOUNT = "objectCount";
	public final static String OLEOBJECTCOUNT = "oleObjectCount";
	public final static String PAGECOUNT = "pageCount";
	public final static String PARAGRAPHCOUNT = "paragraphCount";
	public final static String ROWCOUNT = "rowCount";
	public final static String SENTENCECOUNT = "sentenceCount";
	public final static String SYLLABLECOUNT = "syllableCount";
	public final static String TABLECOUNT = "tableCount";
	public final static String WORDCOUNT = "wordCount";

	public MetadataStats(OdfOfficeMeta meta, HashMap<String, Integer> value) {
		super(meta, ATTR, value);
	}

	@Override
	public void setValue(String value) throws ParseException, ReadOnlyMetaException {
		throw new ReadOnlyMetaException(ATTR);
	}

	/**
	 * It takes an OdfOfficeMeta object and returns a HashMap of the document statistics
	 * 
	 * @param meta The OdfOfficeMeta object that you want to get the statistics from.
	 * @return A HashMap of the statistics of the document.
	 */
	public static HashMap<String, Integer> getStatistics(OdfOfficeMeta meta) throws NoStatisticsException {
		OdfMetaDocumentStatistic stats = meta.getDocumentStatistic();
		HashMap<String, Integer> hwStats = new HashMap<String, Integer>();
		hwStats.put(CELLCOUNT, stats.getCellCount());
		hwStats.put(CHARACTERCOUNT, stats.getCharacterCount());
		hwStats.put(DRAWNCOUNT, stats.getDrawCount());
		hwStats.put(FRAMECOUNT, stats.getFrameCount());
		hwStats.put(IMAGECOUNT, stats.getImageCount());
		hwStats.put(NONWHITESPACECHARACTERCOUNT, stats.getNonWhitespaceCharacterCount());
		hwStats.put(OBJECTCOUNT, stats.getObjectCount());
		hwStats.put(OLEOBJECTCOUNT, stats.getOleObjectCount());
		hwStats.put(PAGECOUNT, stats.getPageCount());
		hwStats.put(PARAGRAPHCOUNT, stats.getParagraphCount());
		hwStats.put(ROWCOUNT, stats.getRowCount());
		hwStats.put(SENTENCECOUNT, stats.getSentenceCount());
		hwStats.put(SYLLABLECOUNT, stats.getSyllableCount());
		hwStats.put(TABLECOUNT, stats.getTableCount());
		hwStats.put(WORDCOUNT, stats.getWordCount());
		if (hwStats.isEmpty()) {
			throw new NoStatisticsException();
		}
		return hwStats;
	}

	@Override
	public JSONObject toJson() {
		HashMap<String, Integer> statisticHashMap = (HashMap<String, Integer>) getValue();
		Collection<String> statisticKey = statisticHashMap.keySet();
		JSONArray statisticJsonArray = new JSONArray();
		for (String key : statisticKey) {
			statisticJsonArray.put((new JSONObject()).put(key, statisticHashMap.get(key)));
		}
		return (new JSONObject()).put(STATISTICS, statisticJsonArray);
	}
}
