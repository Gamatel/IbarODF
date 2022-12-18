package ibarodf.core.meta;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;
import java.text.SimpleDateFormat; 
import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;

import ibarodf.core.meta.exception.ReadOnlyMetaException;

import java.text.ParseException;

public class MetadataCreationDate extends MetadataXML {
	public final static String ATTR = "Creation Date";
	public final static String CREATION_DATE = ATTR;

	private final static String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";
	public final static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT);

	public MetadataCreationDate(final OdfOfficeMeta meta, final String value) {
		super(meta, ATTR, value);
	}

	@Override
	public void setValue(final String value) throws ParseException, ReadOnlyMetaException {
		GregorianCalendar creationDate = parseStringToCalendar(value);
		getMeta().setCreationDate(creationDate);
		super.setValue(value);
	}

	/**
	 * It takes a string, parses it to a date, then takes the date and creates a new GregorianCalendar
	 * object with the date's values
	 * 
	 * @param dateString "2016-01-01T00:00:00.000Z"
	 * @return A GregorianCalendar object.
	 * @deprecated
	 */
	private static GregorianCalendar parseStringToCalendar(final String dateString) throws ParseException {
		Date creationDate = DATE_FORMATTER.parse(dateString);
		final int day = creationDate.getDate();
		final int mouth = creationDate.getMonth();
		final int years = creationDate.getYear() + 1900;

		final int sec = creationDate.getSeconds();
		final int min = creationDate.getMinutes();
		final int hrs = creationDate.getHours();

		return new GregorianCalendar(years, mouth, day, hrs, min, sec);
	}

	/**
	 * It takes a Calendar object and returns a String in the format "dd/mm/yyyy hh:mm:ss"
	 * 
	 * @param date The date to be formatted.
	 * @return A string with the date and time in the format dd/mm/yyyy hh:mm:ss
	 */
	public static String CalendarToFormattedString(final Calendar date) {
		final int day = date.get(GregorianCalendar.DAY_OF_MONTH);
		final int mouth = date.get(GregorianCalendar.MONTH) + 1; // get(GregorianCalendar.MONTH) give value from 0 to 11 and not 1 to 12
		final int years = date.get(GregorianCalendar.YEAR); 

		final int sec = date.get(GregorianCalendar.SECOND);
		final int min = date.get(GregorianCalendar.MINUTE);
		final int hrs = date.get(GregorianCalendar.HOUR_OF_DAY); 
		
		return String.format("%02d/%02d/%04d %02d:%02d:%02d", day, mouth, years, hrs, min, sec);
	}
}
