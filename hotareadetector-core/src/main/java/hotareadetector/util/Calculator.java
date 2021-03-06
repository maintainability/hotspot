package hotareadetector.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Calculator functions.
 */
public class Calculator {
	
	/**
	 * Calculates the distribution position of an element within a list.
	 * E.g. if there are 5 numbers: 1.0, 2.0, 3.0, 4.0, 5.0, then the distribution value of 1 is 0.0, of 2 is 0.2, of 3 is 0.4, of 4 is 0.6, of 5 is 0.8 and of 6 is 1.0.
	 * 
	 * It is assumed that the allValues are sorted ascending. The allValues should contain the valueToCheck.
	 */
	public static Double calculateDistributionValue(List<Double> allValues, double valueToCheck) {
		if (allValues == null || allValues.isEmpty()) {
			return null;
		}
		int numberOfLowerElements = 0;
		for (int i = 0; i < allValues.size(); i++) {
			if (allValues.get(i) < valueToCheck) {
				numberOfLowerElements++;
			}
		}
		double result = 1.0 * numberOfLowerElements / allValues.size();
		return  result;
	}
	
	/**
	 * Distribution position calculation for integer elements.
	 */
	public static Double calculateDistributionValue(List<Integer> allValues, int valueToCheck) {
		ArrayList<Double> allValuesDouble = new ArrayList<Double>();
		if (allValues != null) {
			for (Integer value : allValues) {
				allValuesDouble.add(value * 1.0);
			}
		}
		return calculateDistributionValue(allValuesDouble, valueToCheck * 1.0);
	}

	/**
	 * Calculates the distribution position of a date within a date of lists, using the same logic as above.
	 */
	public static Double calculateDistributionValue(List<Date> allValues, Date valueToCheck) {
		if (allValues == null || allValues.isEmpty()) {
			return null;
		}
		int numberOfLowerElements = 0;
		for (int i = 0; i < allValues.size(); i++) {
			if (allValues.get(i).before(valueToCheck)) {
				numberOfLowerElements++;
			}
		}
		double result = 1.0 * numberOfLowerElements / allValues.size();
		return  result;
	}
	
	/**
	 * Calculates the average of the given array.
	 * Returns null of it is empty or all elements are null.
	 * E.g. in case of 0.3, 0.4 and 0.8 the result is 0.5.
	 */
	public static Double calculateAverage(Double[] distributionValues) {
		if (distributionValues == null || distributionValues.length == 0) {
			return null;
		}
		double sum = 0.0;
		int numberOfElements = 0;
		for (Double distributionValue : distributionValues) {
			if (distributionValue != null) {
				sum += distributionValue;
				numberOfElements++;
			}
		}
		if (numberOfElements == 0) {
			return null;
		}
		return sum / numberOfElements;
	}

	/**
	 * Calculate the average of the dates.
	 * It indeed calculates the average of the longs representing the times, and creates a Date object again.
	 */
	public static Date calculateAverage(List<Date> dates) {
		if (dates == null || dates.isEmpty()) {
			return null;
		}
		long sum = 0;
		int numberOfElements = 0;
		for (Date date : dates) {
			if (date != null) {
				sum += date.getTime();
				numberOfElements++;
			}
		}
		if (numberOfElements == 0) {
			return null;
		}
		return new Date(sum / numberOfElements);
	}
}
