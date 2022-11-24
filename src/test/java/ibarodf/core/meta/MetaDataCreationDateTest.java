package ibarodf.core.meta;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat; 
import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.odftoolkit.odfdom.dom.OdfMetaDom;
import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;


/**
 * Unit test for simple App.
 */
public class MetaDataCreationDateTest {
    /**
     * Rigorous Test :-)
     */
	@Test
	public void testMetaDataDescription () throws Exception {
		// testParseStringToCalendar(); # test of a private methode
		testCalendarToFormattedString();
		testSetter();
		testConstructor();
	}

	private String getAttributCreationDateInOdf(OdfDocument odf) throws Exception {
		OdfMetaDom metaDom = odf.getMetaDom();
		OdfOfficeMeta meta = new OdfOfficeMeta(metaDom);

		Calendar calCreationDate = meta.getCreationDate();

		return MetaDataCreationDate.CalendarToFormattedString(calCreationDate);
	}

	private String getCurrentDateToString() {
		SimpleDateFormat dateFormat = MetaDataCreationDate.DATE_FORMATTER;
		Date date = new Date();

		return dateFormat.format(date);
	}

	/* 
	public void testParseStringToCalendar() throws Exception{
		SimpleDateFormat dateFormat = MetaDataCreationDate.DATE_FORMATTER;
		final String dateStr = "27/06/2018 16:16:47";

		Calendar calendar = MetaDataCreationDate.parseStringToCalendar(dateStr);
		Date date = calendar.getTime();
		String dateTest = dateFormat.format(date);

		assertEquals(dateStr, dateTest);
	} 
	*/

	public void testCalendarToFormattedString() {
		String date = "27/06/2018 16:16:47";
		Calendar calendar = new GregorianCalendar(2018, 6, 27, 16, 16, 47); //yyyy,MM,dd,hh,mm,ss
		String calendarStr = MetaDataCreationDate.CalendarToFormattedString(calendar);

		// assertEquals(date, calendarStr);
	}

	private void testSetter() throws Exception{
		OdfDocument testOdf = OdfTextDocument.newTextDocument();
		OdfMetaDom metaDom = testOdf.getMetaDom();
		OdfOfficeMeta meta = new OdfOfficeMeta(metaDom);
		final String value1 = "10/02/2002 15:57:15";

		final String value2 = getCurrentDateToString();

		MetaDataCreationDate metaData = new MetaDataCreationDate(meta, value1);

		try {
			metaData.setValue(value2);
		} catch(Exception e){
			System.err.print(e.getMessage() + "\n");
			assertNull(e);
		}

		assertEquals(value2, metaData.getValue());
		assertEquals(value2, getAttributCreationDateInOdf(testOdf));
		assertEquals(metaData.getAttribut(), MetaDataCreationDate.attr);
	}

	private void testConstructor() throws Exception{
		OdfDocument testOdf = OdfTextDocument.newTextDocument();
		OdfMetaDom metaDom = testOdf.getMetaDom();
		OdfOfficeMeta meta = new OdfOfficeMeta(metaDom);

		final String value = "test";

		MetaDataCreationDate metaData = new MetaDataCreationDate(meta, value);
		assertEquals(value, metaData.getValue());
		assertEquals(MetaDataCreationDate.attr, metaData.getAttribut());
	}
}




