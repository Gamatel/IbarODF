package ibarodf.core.metadata;

import java.lang.Integer;
import java.text.ParseException;
import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;

import ibarodf.core.metadata.exception.NoStatisticsException;
import ibarodf.core.metadata.exception.ReadOnlyMetaException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.odftoolkit.odfdom.incubator.meta.OdfMetaDocumentStatistic;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class MetadataStats extends AbstractMetadataXML {
	public final static String ATTR = "Statistics";

	// Json key
	public final static String STATISTICS = ATTR;
	public final static String CELL_COUNT = "cell Count";
	public final static String CHARACTER_COUNT = "character Count";
	public final static String DRAWN_COUNT = "drawn Count";
	public final static String FRAME_COUNT = "frame Count";
	public final static String IMAGE_COUNT = "image Count";
	public final static String NON_WHITE_SPACE_CHARACTER_COUNT = "Non White Space Character Count";
	public final static String OBJECT_COUNT = "Object Count";
	public final static String OLE_OBJECT_COUNT = "Ole Object Count";
	public final static String PAGE_COUNT = "Page Count";
	public final static String PARAGRAPH_COUNT = "Paragraph Count";
	public final static String ROW_COUNT = "Row Count";
	public final static String SENTENCE_COUNT = "Sentence Count";
	public final static String SYLLABLE_COUNT = "Syllable Count";
	public final static String TABLE_COUNT = "Table Count";
	public final static String WORD_COUNT = "Word Count";

	public MetadataStats(OdfOfficeMeta meta, HashMap<String, Integer> value) {
		super(meta, ATTR, value);
	}

	@Override
	public void setValue(String value) throws ParseException, ReadOnlyMetaException {
		throw new ReadOnlyMetaException(ATTR);
	}

	/**
	 * It takes an OdfOfficeMeta object and returns a HashMap of the document
	 * statistics
	 * 
	 * @param meta The OdfOfficeMeta object that you want to get the statistics
	 *             from.
	 * @return A HashMap of the statistics of the document.
	 */
	public static HashMap<String, Integer> getStatistics(OdfOfficeMeta meta) throws NoStatisticsException {
		OdfMetaDocumentStatistic stats = meta.getDocumentStatistic();
		HashMap<String, Integer> hwStats = new HashMap<String, Integer>();
		hwStats.put(CELL_COUNT, stats.getCellCount());
		hwStats.put(CHARACTER_COUNT, stats.getCharacterCount());
		hwStats.put(DRAWN_COUNT, stats.getDrawCount());
		hwStats.put(FRAME_COUNT, stats.getFrameCount());
		hwStats.put(IMAGE_COUNT, stats.getImageCount());
		hwStats.put(NON_WHITE_SPACE_CHARACTER_COUNT, stats.getNonWhitespaceCharacterCount());
		hwStats.put(OBJECT_COUNT, stats.getObjectCount());
		hwStats.put(OLE_OBJECT_COUNT, stats.getOleObjectCount());
		hwStats.put(PAGE_COUNT, stats.getPageCount());
		hwStats.put(PARAGRAPH_COUNT, stats.getParagraphCount());
		hwStats.put(ROW_COUNT, stats.getRowCount());
		hwStats.put(SENTENCE_COUNT, stats.getSentenceCount());
		hwStats.put(SYLLABLE_COUNT, stats.getSyllableCount());
		hwStats.put(TABLE_COUNT, stats.getTableCount());
		hwStats.put(WORD_COUNT, stats.getWordCount());
		if (hwStats.isEmpty()) {
			throw new NoStatisticsException();
		}
		return hwStats;
	}

	@Override
	public JSONObject toJson() {
		HashMap<String, Integer> statisticHashMap = (HashMap<String, Integer>) getValue();
		Collection<String> statisticKey = statisticHashMap.keySet();
		Iterator<String> statisticsIt = statisticKey.iterator();
		String currentKey;
		JSONObject statToAdd;
		JSONArray statisticJsonArray = new JSONArray();
		while (statisticsIt.hasNext()) {
			currentKey = statisticsIt.next();
			statToAdd = (new JSONObject()).put(currentKey, statisticHashMap.get(currentKey));
			if (!statToAdd.isNull(currentKey)) {
				statisticJsonArray.put(statToAdd);
			}
		}
		return (new JSONObject()).put(STATISTICS, statisticJsonArray);
	}
}
