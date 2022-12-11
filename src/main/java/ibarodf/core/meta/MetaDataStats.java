package ibarodf.core.meta;

import java.lang.Integer;
import java.text.ParseException;
import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;
import org.odftoolkit.odfdom.incubator.meta.OdfMetaDocumentStatistic;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class MetaDataStats extends MetaDataXML {
	public final static String ATTR = "Statistic";
	public MetaDataStats(OdfOfficeMeta meta, String value) {
		super(meta, ATTR, value);
	}

	public void setValue(String value) throws ParseException, ReadOnlyMetaException {
		throw new ReadOnlyMetaException(ATTR);
	}

	public static String objDocumentStatisticToStr(OdfMetaDocumentStatistic stats) {
		final StringBuilder strBuilder = new StringBuilder();
		final LinkedHashMap<String, Integer> hwStats = new LinkedHashMap<>();
		loadStatsIntoHM(hwStats, stats);

		strBuilder.append("[");
		for (Map.Entry<String, Integer> entry: hwStats.entrySet()) {
			String lineStr = String.format("%s: %s;", entry.getKey(), entry.getValue());
			strBuilder.append(lineStr);
		}
		strBuilder.append("]");

		return strBuilder.toString();
	}

	private static void loadStatsIntoHM(HashMap<String, Integer> hwStats, OdfMetaDocumentStatistic stats) {
		hwStats.put("cellCount", stats.getCellCount());
		hwStats.put("characterCount", stats.getCharacterCount());
		hwStats.put("drawnCount", stats.getDrawCount());
		hwStats.put("frameCount", stats.getFrameCount());
		hwStats.put("imageCount", stats.getImageCount());
		hwStats.put("nonWhitespaceCharacterCount", stats.getNonWhitespaceCharacterCount());
		hwStats.put("objectCount", stats.getObjectCount());
		hwStats.put("oleObjectCount", stats.getOleObjectCount());
		hwStats.put("pageCount", stats.getPageCount());
		hwStats.put("paragraphCount", stats.getParagraphCount());
		hwStats.put("rowCount", stats.getRowCount());
		hwStats.put("sentenceCount", stats.getSentenceCount());
		hwStats.put("syllableCount", stats.getSyllableCount());
		hwStats.put("tableCount", stats.getTableCount());
		hwStats.put("wordCount", stats.getWordCount());
	}
}
