package hotareadetector.util;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

/**
 * Unit tests for DateUtil.
 *
 */
public class DateUtilTest {
	
	/**
	 * Testing date conversion.
	 */
	@Test
	public void testConvertToDate() {
		Date result = DateUtil.convertToDate("2015-04-22 13:48:32");
		assertEquals(1429703312000L, result.getTime());
	}
	
	/**
	 * Testing date conversion, negative case.
	 */
	@Test
	public void testConvertToDateNegative() {
		Date result = DateUtil.convertToDate("invalid date format");
		assertEquals(null, result);
	}
}
