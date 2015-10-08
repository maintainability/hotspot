package hotareadetector.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Date utility.
 */
public class DateUtil {
	/**
	 * Converts the date to String format, e.g. 2015-04-28 09:42:23.
	 */
	public static Date convertToDate(String dateStr) {
		Date result = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			result = formatter.parse(dateStr);
		} catch (ParseException e) {
			System.out.println("Invalid date format: " + dateStr);
		}
		return result;
	}

}
