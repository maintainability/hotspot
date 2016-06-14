package hotareadetector.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

/**
 * Unit tests for calculator.
 */
public class CalculatorTest {
	private static final double tolerance = 0.0001;
	
	/**
	 * Positive test case of calculating average of double values.
	 */
	@Test
	public void testCalculateAverage() {
		Double[] distributionValues = new Double[3];
		distributionValues[0] = 0.3;
		distributionValues[1] = 0.4;
		distributionValues[2] = 0.8;
		
		Double average = Calculator.calculateAverage(distributionValues);
		
		assertEquals(0.5, average, tolerance);
	}

	/**
	 * Calculating average of double values, containing null.
	 */
	@Test
	public void testCalculateAverageContainingNull() {
		Double[] distributionValues = new Double[3];
		distributionValues[0] = null;
		distributionValues[1] = 0.4;
		distributionValues[2] = 0.8;
		
		Double average = Calculator.calculateAverage(distributionValues);
		
		assertEquals(0.6, average, tolerance);
	}

	/**
	 * Calculating average of double values, if all the values are null.
	 */
	@Test
	public void testCalculateAverageNullOnly() {
		Double[] distributionValues = new Double[3];
		distributionValues[0] = null;
		distributionValues[1] = null;
		distributionValues[2] = null;
		
		Double average = Calculator.calculateAverage(distributionValues);
		
		assertEquals(null, average);
	}

	/**
	 * Calculating average of double values of empty array.
	 */
	@Test
	public void testCalculateAverageEmpty() {
		Double[] distributionValues = new Double[0];
		
		Double average = Calculator.calculateAverage(distributionValues);
		
		assertEquals(null, average);
	}

	/**
	 * Calculating average of double values, if the array itself is null.
	 */
	@Test
	public void testCalculateAverageNull() {
		Double[] distributionValues = null;
		
		Double average = Calculator.calculateAverage(distributionValues);
		
		assertEquals(null, average);
	}
	
	/**
	 * Calculating distribution value, normal case.
	 */
	@Test
	public void testCalculateDistributionValue() {
		List<Integer> allValues = new ArrayList<Integer>();
		allValues.add(1);
		allValues.add(2);
		allValues.add(3);
		allValues.add(4);
		allValues.add(5);
		int valueToCheck = 4;
		
		Double result = Calculator.calculateDistributionValue(allValues, valueToCheck);
		
		assertEquals(0.6, result, tolerance);
	}

	/**
	 * Calculating distribution value, select the highest element.
	 */
	@Test
	public void testCalculateDistributionValueHighestElement() {
		List<Integer> allValues = new ArrayList<Integer>();
		allValues.add(1);
		allValues.add(2);
		allValues.add(3);
		allValues.add(4);
		allValues.add(5);
		int valueToCheck = 5;
		
		Double result = Calculator.calculateDistributionValue(allValues, valueToCheck);
		
		assertEquals(0.8, result, tolerance);
	}

	/**
	 * Calculating distribution value, select the lowest element.
	 */
	@Test
	public void testCalculateDistributionValueLowestElement() {
		List<Integer> allValues = new ArrayList<Integer>();
		allValues.add(1);
		allValues.add(2);
		allValues.add(3);
		allValues.add(4);
		allValues.add(5);
		int valueToCheck = 1;
		
		Double result = Calculator.calculateDistributionValue(allValues, valueToCheck);
		
		assertEquals(0.0, result, tolerance);
	}

	/**
	 * Calculating distribution value, select the middle element.
	 */
	@Test
	public void testCalculateDistributionValueMiddleElement() {
		List<Integer> allValues = new ArrayList<Integer>();
		allValues.add(1);
		allValues.add(2);
		allValues.add(3);
		allValues.add(4);
		allValues.add(5);
		int valueToCheck = 3;
		
		Double result = Calculator.calculateDistributionValue(allValues, valueToCheck);
		
		assertEquals(0.4, result, tolerance);
	}

	/**
	 * Calculating distribution value, select an element which occurs twice.
	 */
	@Test
	public void testCalculateDistributionValueTwice() {
		List<Integer> allValues = new ArrayList<Integer>();
		allValues.add(1);
		allValues.add(2);
		allValues.add(4);
		allValues.add(4);
		allValues.add(5);
		int valueToCheck = 4;
		
		Double result = Calculator.calculateDistributionValue(allValues, valueToCheck);
		
		assertEquals(0.4, result, tolerance);
	}

	/**
	 * Calculating distribution value, an element occurs twice, select another.
	 */
	@Test
	public void testCalculateDistributionValueTwiceOther() {
		List<Integer> allValues = new ArrayList<Integer>();
		allValues.add(1);
		allValues.add(2);
		allValues.add(4);
		allValues.add(4);
		allValues.add(5);
		int valueToCheck = 2;
		
		Double result = Calculator.calculateDistributionValue(allValues, valueToCheck);
		
		assertEquals(0.2, result, tolerance);
	}

	/**
	 * Calculating distribution value, all elements are the same, select that element.
	 */
	@Test
	public void testCalculateDistributionValueAllEqual() {
		List<Integer> allValues = new ArrayList<Integer>();
		allValues.add(4);
		allValues.add(4);
		allValues.add(4);
		allValues.add(4);
		allValues.add(4);
		int valueToCheck = 4;
		
		Double result = Calculator.calculateDistributionValue(allValues, valueToCheck);
		
		assertEquals(0.0, result, tolerance);
	}

	/**
	 * Calculating distribution value, select an element which doesn't included, located in the middle.
	 */
	@Test
	public void testCalculateDistributionNotIncludedMiddle() {
		List<Integer> allValues = new ArrayList<Integer>();
		allValues.add(2);
		allValues.add(4);
		allValues.add(6);
		allValues.add(8);
		allValues.add(10);
		int valueToCheck = 7;
		
		Double result = Calculator.calculateDistributionValue(allValues, valueToCheck);
		
		assertEquals(0.6, result, tolerance);
	}

	/**
	 * Calculating distribution value, select an element which doesn't included, lower than all the others.
	 */
	@Test
	public void testCalculateDistributionValueDoubleOutsideLower() {
		List<Integer> allValues = new ArrayList<Integer>();
		allValues.add(1);
		allValues.add(2);
		allValues.add(3);
		allValues.add(4);
		allValues.add(5);
		int valueToCheck = 0;
		
		Double result = Calculator.calculateDistributionValue(allValues, valueToCheck);
		
		assertEquals(0.0, result, tolerance);
	}

	/**
	 * Calculating distribution value, select an element which doesn't included, higher than all the others.
	 */
	@Test
	public void testCalculateDistributionValueDoubleOutsideHigher() {
		List<Integer> allValues = new ArrayList<Integer>();
		allValues.add(1);
		allValues.add(2);
		allValues.add(3);
		allValues.add(4);
		allValues.add(5);
		int valueToCheck = 6;
		
		Double result = Calculator.calculateDistributionValue(allValues, valueToCheck);
		
		assertEquals(1.0, result, tolerance);
	}

	/**
	 * Calculating distribution value, single element check, select that element.
	 */
	@Test
	public void testCalculateDistributionValueDoubleSingleElement() {
		List<Integer> allValues = new ArrayList<Integer>();
		allValues.add(1);
		int valueToCheck = 1;
		
		Double result = Calculator.calculateDistributionValue(allValues, valueToCheck);
		
		assertEquals(0.0, result, tolerance);
	}

	/**
	 * Calculating distribution value, null check.
	 */
	@Test
	public void testCalculateDistributionValueNullCheck() {
		List<Integer> allValues = null;
		int valueToCheck = 2;
		
		Double result = Calculator.calculateDistributionValue(allValues, valueToCheck);
		
		assertEquals(null, result);
	}

	/**
	 * Calculating distribution value, empty check.
	 */
	@Test
	public void testCalculateDistributionValueEmptyCheck() {
		List<Integer> allValues = new ArrayList<Integer>();
		int valueToCheck = 2;
		
		Double result = Calculator.calculateDistributionValue(allValues, valueToCheck);
		
		assertEquals(null, result);
	}
	
	/**
	 * Calculating distribution value for double elements.
	 */
	@Test
	public void testCalculateDistributionValueDouble() {
		List<Double> allValues = new ArrayList<Double>();
		allValues.add(1.0);
		allValues.add(2.0);
		allValues.add(3.0);
		allValues.add(4.0);
		allValues.add(5.0);
		double valueToCheck = 3.0;

		Double result = Calculator.calculateDistributionValue(allValues, valueToCheck);

		assertEquals(0.4, result, tolerance);
	}

	/**
	 * Check distribution value of dates.
	 */
	@Test
	public void testCalculateDistributionValueDate() {
		List<Date> allValues = new ArrayList<Date>();
		long now = new Date().getTime();
		for (long i = 0; i < 10; i++) {
			allValues.add(new Date(now + i));
		}
		Date valueToCheck = new Date(now + 3);
		
		Double result = Calculator.calculateDistributionValue(allValues, valueToCheck);
		
		assertEquals(0.3, result, tolerance);
	}

	/**
	 * Check the average of the dates.
	 */
	@Test
	public void testCalculateAverageDate() {
		List<Date> dates = new ArrayList<Date>();
		Date now = new Date();
		
		dates.add(now);
		dates.add(new Date(now.getTime() - 1000));
		dates.add(null);
		dates.add(new Date(now.getTime() + 4000));
		
		Date result = Calculator.calculateAverage(dates);
		
		assertEquals(new Date(now.getTime() + 1000), result);
	}
}
