package util;

import static org.apache.commons.lang.time.DateUtils.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtils {

	private static final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
	private static final SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSSSS");

	/**
	 * Format as dd.MM.yyyy
	 */
	public static synchronized String format(Date date) {
		return formatter.format(date);
	}

	/**
	 * Format as yyyy-MM-dd hh:mm:ss.SSSSS
	 */
	public static synchronized String timestampFormat(Date date) {
		return dateTimeFormatter.format(date);
	}

	public static Date getDate(int day, int month, int year) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day);
		return cal.getTime();
	}

	/**
	 * Parse the string in format dd.MM.yyyy to a {@link Date}
	 */
	public static synchronized Date parse(String date) {
		try {
			return formatter.parse(date);
		} catch (ParseException e) {
			throw new RuntimeException();
		}
	}

	public static synchronized String oracleFormat(Date date) {
		return "to_date('" + formatter.format(date) + "','dd.mm.yyyy')";
	}
}
