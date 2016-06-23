package hotareadetector.logic;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hotareadetector.data.CommitFileCell;
import hotareadetector.data.CommitFileMatrix;
import hotareadetector.data.HotNumber;
import hotareadetector.mock.CommitFileCellGenerator;
import hotareadetector.mock.CommitFileMatrixGenerator;
import static hotareadetector.data.AnalysisType.*;

/**
 * Unit tests for hot area calculator.
 */
public class HotAreaCalculatorTest {
	private static final double tolerance = 0.0001;
	
	/**
	 * Test the hot number calculator with all information.
	 */
	@Test
	public void testCalculateHotNumberCommitFileCellDeepAnalysis() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		CommitFileCell commitFileCell =  CommitFileCellGenerator.createCommitFileCellWithPredefinedValues();
		HotAreaCalculator hotAreaCalculator = createHotAreaCalculator(true);
		
		Double hotNumber = hotAreaCalculator.calculateHotNumberCommitFileCell(commitFileCell, FULL);
		
		assertEquals(0.3, hotNumber, tolerance);
	}
	
	/**
	 * Test the hot number calculator, churn only.
	 */
	@Test
	public void testCalculateHotNumberCommitFileCellChurn() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		CommitFileCell commitFileCell =  CommitFileCellGenerator.createCommitFileCellWithPredefinedValues();
		HotAreaCalculator hotAreaCalculator = createHotAreaCalculator(true);
		
		Double hotNumber = hotAreaCalculator.calculateHotNumberCommitFileCell(commitFileCell, CHURN);
		
		assertEquals(0.5, hotNumber, tolerance);
	}
	
	/**
	 * Test the hot number calculator, ownership only.
	 */
	@Test
	public void testCalculateHotNumberCommitFileCellOwnership() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		CommitFileCell commitFileCell =  CommitFileCellGenerator.createCommitFileCellWithPredefinedValues();
		HotAreaCalculator hotAreaCalculator = createHotAreaCalculator(false);
		
		Double hotNumber = hotAreaCalculator.calculateHotNumberCommitFileCell(commitFileCell, OWNERSHIP);
		
		assertEquals(0.2, hotNumber, tolerance);
	}
	
	/**
	 * Test the hot number calculator, modifications only.
	 */
	@Test
	public void testCalculateHotNumberCommitFileCellModifications() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		CommitFileCell commitFileCell =  CommitFileCellGenerator.createCommitFileCellWithPredefinedValues();
		HotAreaCalculator hotAreaCalculator = createHotAreaCalculator(false);
		
		Double hotNumber = hotAreaCalculator.calculateHotNumberCommitFileCell(commitFileCell, MODIFICATION);
		
		assertEquals(0.4, hotNumber, tolerance);
	}
	
	/**
	 * Test the hot number calculator, combined ownership and modifications.
	 */
	@Test
	public void testCalculateHotNumberCommitFileCellCombined() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		CommitFileCell commitFileCell =  CommitFileCellGenerator.createCommitFileCellWithPredefinedValues();
		HotAreaCalculator hotAreaCalculator = createHotAreaCalculator(false);
		
		Double hotNumber = hotAreaCalculator.calculateHotNumberCommitFileCell(commitFileCell, COMBINED);
		
		assertEquals(0.4, hotNumber, tolerance);
	}
	
	/**
	 * Test the hot number calculator without diff information.
	 */
	@Test
	public void testCalculateHotNumberCommitFileCellNoDeepAnalysis() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		CommitFileCell commitFileCell =  CommitFileCellGenerator.createCommitFileCellWithPredefinedValues();
		HotAreaCalculator hotAreaCalculator = createHotAreaCalculator(false);
		
		Double hotNumber = hotAreaCalculator.calculateHotNumberCommitFileCell(commitFileCell, FULL);
		
		assertEquals(0.25, hotNumber, tolerance);
	}
	
	/**
	 * Creates a hot area calculator instance, filled in with hot area values.
	 */
	private HotAreaCalculator createHotAreaCalculator(boolean deepAnalysis) {
		CommitFileMatrix commitFileMatrix = new CommitFileMatrix(deepAnalysis);
		HotAreaCalculator hotAreaCalculator = new HotAreaCalculator(commitFileMatrix);
		List<Integer> valueList = new ArrayList<Integer>();
		for (int i = 1; i <= 10; i++) {
			valueList.add(i);
			hotAreaCalculator.combinedValues.add(12 * i); // max ownership = 10 => (10 + 1) * i + i = 12 * i 
		}
		hotAreaCalculator.ownershipValues.addAll(valueList);
		hotAreaCalculator.ownershipValuesToleranceOne.addAll(valueList);
		hotAreaCalculator.ownershipValuesToleranceTwo.addAll(valueList);
		hotAreaCalculator.numberOfModifications.addAll(valueList);
		if (deepAnalysis) {
			hotAreaCalculator.churnValuesCoarse.addAll(valueList);
			hotAreaCalculator.churnValuesFine.addAll(valueList);
		}
		return hotAreaCalculator;
	}

	/**
	 * Perform hot area calculation test on a real SVN query data file, including diffs.
	 */
	@Test
	public void testHotAreaCalculatorLatestRevisionDeepAnalysis() throws IOException {
		testHotAreaCalculatorLatestRevision(true);
	}
	
	/**
	 * Perform hot area calculation test on a real SVN query data file, excluding diffs.
	 */
	@Test
	public void testHotAreaCalculatorLatestRevisionNoDeepAnalysis() throws IOException {
		testHotAreaCalculatorLatestRevision(false);
	}
	
	/**
	 * The hot area calculation test implementation with checks.
	 */
	private void testHotAreaCalculatorLatestRevision(boolean deepAnalysis) throws IOException {
		CommitFileMatrix commitFileMatrix = CommitFileMatrixGenerator.generateCommitFileMatrixRealExecution(deepAnalysis);
		HotAreaCalculator hotAreaCalculator = new HotAreaCalculator(commitFileMatrix);
		
		List<HotNumber> hotNumbers = hotAreaCalculator.calculateHotNumbers(FULL);
		
		assertEquals(11, hotAreaCalculator.ownershipValues.size());
		assertEquals(1, (int)hotAreaCalculator.ownershipValues.get(0));
		assertEquals(1, (int)hotAreaCalculator.ownershipValues.get(1));
		assertEquals(1, (int)hotAreaCalculator.ownershipValues.get(2));
		assertEquals(1, (int)hotAreaCalculator.ownershipValues.get(3));
		assertEquals(1, (int)hotAreaCalculator.ownershipValues.get(4));
		assertEquals(1, (int)hotAreaCalculator.ownershipValues.get(5));
		assertEquals(1, (int)hotAreaCalculator.ownershipValues.get(6));
		assertEquals(1, (int)hotAreaCalculator.ownershipValues.get(7));
		assertEquals(1, (int)hotAreaCalculator.ownershipValues.get(8));
		assertEquals(1, (int)hotAreaCalculator.ownershipValues.get(9));
		assertEquals(2, (int)hotAreaCalculator.ownershipValues.get(10));
		
		assertEquals(11, hotAreaCalculator.ownershipValuesToleranceOne.size());
		assertEquals(0, (int)hotAreaCalculator.ownershipValuesToleranceOne.get(0));
		assertEquals(0, (int)hotAreaCalculator.ownershipValuesToleranceOne.get(1));
		assertEquals(0, (int)hotAreaCalculator.ownershipValuesToleranceOne.get(2));
		assertEquals(0, (int)hotAreaCalculator.ownershipValuesToleranceOne.get(3));
		assertEquals(0, (int)hotAreaCalculator.ownershipValuesToleranceOne.get(4));
		assertEquals(0, (int)hotAreaCalculator.ownershipValuesToleranceOne.get(5));
		assertEquals(0, (int)hotAreaCalculator.ownershipValuesToleranceOne.get(6));
		assertEquals(1, (int)hotAreaCalculator.ownershipValuesToleranceOne.get(7));
		assertEquals(1, (int)hotAreaCalculator.ownershipValuesToleranceOne.get(8));
		assertEquals(1, (int)hotAreaCalculator.ownershipValuesToleranceOne.get(9));
		assertEquals(1, (int)hotAreaCalculator.ownershipValuesToleranceOne.get(10));
		
		assertEquals(11, hotAreaCalculator.ownershipValuesToleranceTwo.size());
		assertEquals(0, (int)hotAreaCalculator.ownershipValuesToleranceTwo.get(0));
		assertEquals(0, (int)hotAreaCalculator.ownershipValuesToleranceTwo.get(1));
		assertEquals(0, (int)hotAreaCalculator.ownershipValuesToleranceTwo.get(2));
		assertEquals(0, (int)hotAreaCalculator.ownershipValuesToleranceTwo.get(3));
		assertEquals(0, (int)hotAreaCalculator.ownershipValuesToleranceTwo.get(4));
		assertEquals(0, (int)hotAreaCalculator.ownershipValuesToleranceTwo.get(5));
		assertEquals(0, (int)hotAreaCalculator.ownershipValuesToleranceTwo.get(6));
		assertEquals(0, (int)hotAreaCalculator.ownershipValuesToleranceTwo.get(7));
		assertEquals(0, (int)hotAreaCalculator.ownershipValuesToleranceTwo.get(8));
		assertEquals(1, (int)hotAreaCalculator.ownershipValuesToleranceTwo.get(9));
		assertEquals(1, (int)hotAreaCalculator.ownershipValuesToleranceTwo.get(10));
		
		assertEquals(11, hotAreaCalculator.numberOfModifications.size());
		assertEquals(1, (int)hotAreaCalculator.numberOfModifications.get(0));
		assertEquals(1, (int)hotAreaCalculator.numberOfModifications.get(1));
		assertEquals(1, (int)hotAreaCalculator.numberOfModifications.get(2));
		assertEquals(1, (int)hotAreaCalculator.numberOfModifications.get(3));
		assertEquals(1, (int)hotAreaCalculator.numberOfModifications.get(4));
		assertEquals(1, (int)hotAreaCalculator.numberOfModifications.get(5));
		assertEquals(1, (int)hotAreaCalculator.numberOfModifications.get(6));
		assertEquals(2, (int)hotAreaCalculator.numberOfModifications.get(7));
		assertEquals(2, (int)hotAreaCalculator.numberOfModifications.get(8));
		assertEquals(3, (int)hotAreaCalculator.numberOfModifications.get(9));
		assertEquals(9, (int)hotAreaCalculator.numberOfModifications.get(10));
		
		assertEquals(11, hotAreaCalculator.addedDates.size());
		assertEquals(new Date(1427386433000l), hotAreaCalculator.addedDates.get(0));
		assertEquals(new Date(1427386433000l), hotAreaCalculator.addedDates.get(1));
		assertEquals(new Date(1427386433000l), hotAreaCalculator.addedDates.get(2));
		assertEquals(new Date(1427386535000l), hotAreaCalculator.addedDates.get(3));
		assertEquals(new Date(1427387105000l), hotAreaCalculator.addedDates.get(4));
		assertEquals(new Date(1427387721000l), hotAreaCalculator.addedDates.get(5));
		assertEquals(new Date(1427387776000l), hotAreaCalculator.addedDates.get(6));
		assertEquals(new Date(1427387776000l), hotAreaCalculator.addedDates.get(7));
		assertEquals(new Date(1427959571000l), hotAreaCalculator.addedDates.get(8));
		assertEquals(new Date(1428507519000l), hotAreaCalculator.addedDates.get(9));
		assertEquals(new Date(1428507701000l), hotAreaCalculator.addedDates.get(10));
		
		assertEquals(11, hotAreaCalculator.lastModifiedDates.size());
		assertEquals(new Date(1427386433000l), hotAreaCalculator.lastModifiedDates.get(0));
		assertEquals(new Date(1427386433000l), hotAreaCalculator.lastModifiedDates.get(1));
		assertEquals(new Date(1427386433000l), hotAreaCalculator.lastModifiedDates.get(2));
		assertEquals(new Date(1427386535000l), hotAreaCalculator.lastModifiedDates.get(3));
		assertEquals(new Date(1427387721000l), hotAreaCalculator.lastModifiedDates.get(4));
		assertEquals(new Date(1427387776000l), hotAreaCalculator.lastModifiedDates.get(5));
		assertEquals(new Date(1427387776000l), hotAreaCalculator.lastModifiedDates.get(6));
		assertEquals(new Date(1427959571000l), hotAreaCalculator.lastModifiedDates.get(7));
		assertEquals(new Date(1428507519000l), hotAreaCalculator.lastModifiedDates.get(8));
		assertEquals(new Date(1428507583000l), hotAreaCalculator.lastModifiedDates.get(9));
		assertEquals(new Date(1428507778000l), hotAreaCalculator.lastModifiedDates.get(10));
		
		assertEquals(11, hotAreaCalculator.averageDates.size());
		assertEquals(new Date(1427386433000l), hotAreaCalculator.averageDates.get(0));
		assertEquals(new Date(1427386433000l), hotAreaCalculator.averageDates.get(1));
		assertEquals(new Date(1427386433000l), hotAreaCalculator.averageDates.get(2));
		assertEquals(new Date(1427386535000l), hotAreaCalculator.averageDates.get(3));
		assertEquals(new Date(1427387721000l), hotAreaCalculator.averageDates.get(4));
		assertEquals(new Date(1427387776000l), hotAreaCalculator.averageDates.get(5));
		assertEquals(new Date(1427645512000l), hotAreaCalculator.averageDates.get(6));
		assertEquals(new Date(1427808796111l), hotAreaCalculator.averageDates.get(7));
		assertEquals(new Date(1427959571000l), hotAreaCalculator.averageDates.get(8));
		assertEquals(new Date(1428507551000l), hotAreaCalculator.averageDates.get(9));
		assertEquals(new Date(1428651804333l), hotAreaCalculator.averageDates.get(10));
		
		assertEquals(11, hotAreaCalculator.combinedValues.size());
		assertEquals(4, (int)hotAreaCalculator.combinedValues.get(0));
		assertEquals(4, (int)hotAreaCalculator.combinedValues.get(1));
		assertEquals(4, (int)hotAreaCalculator.combinedValues.get(2));
		assertEquals(4, (int)hotAreaCalculator.combinedValues.get(3));
		assertEquals(4, (int)hotAreaCalculator.combinedValues.get(4));
		assertEquals(4, (int)hotAreaCalculator.combinedValues.get(5));
		assertEquals(4, (int)hotAreaCalculator.combinedValues.get(6));
		assertEquals(7, (int)hotAreaCalculator.combinedValues.get(7));
		assertEquals(7, (int)hotAreaCalculator.combinedValues.get(8));
		assertEquals(10, (int)hotAreaCalculator.combinedValues.get(9));
		assertEquals(29, (int)hotAreaCalculator.combinedValues.get(10));
		
		if (deepAnalysis) {
			assertEquals(11, hotAreaCalculator.churnValuesCoarse.size());
			assertEquals(0, (int)hotAreaCalculator.churnValuesCoarse.get(0));
			assertEquals(0, (int)hotAreaCalculator.churnValuesCoarse.get(1));
			assertEquals(0, (int)hotAreaCalculator.churnValuesCoarse.get(2));
			assertEquals(0, (int)hotAreaCalculator.churnValuesCoarse.get(3));
			assertEquals(0, (int)hotAreaCalculator.churnValuesCoarse.get(4));
			assertEquals(1, (int)hotAreaCalculator.churnValuesCoarse.get(5));
			assertEquals(1, (int)hotAreaCalculator.churnValuesCoarse.get(6));
			assertEquals(4, (int)hotAreaCalculator.churnValuesCoarse.get(7));
			assertEquals(39, (int)hotAreaCalculator.churnValuesCoarse.get(8));
			assertEquals(77, (int)hotAreaCalculator.churnValuesCoarse.get(9));
			assertEquals(262, (int)hotAreaCalculator.churnValuesCoarse.get(10));
			
			assertEquals(11, hotAreaCalculator.churnValuesFine.size());
			assertEquals(0, (int)hotAreaCalculator.churnValuesFine.get(0));
			assertEquals(0, (int)hotAreaCalculator.churnValuesFine.get(1));
			assertEquals(0, (int)hotAreaCalculator.churnValuesFine.get(2));
			assertEquals(0, (int)hotAreaCalculator.churnValuesFine.get(3));
			assertEquals(0, (int)hotAreaCalculator.churnValuesFine.get(4));
			assertEquals(1, (int)hotAreaCalculator.churnValuesFine.get(5));
			assertEquals(1, (int)hotAreaCalculator.churnValuesFine.get(6));
			assertEquals(2, (int)hotAreaCalculator.churnValuesFine.get(7));
			assertEquals(17, (int)hotAreaCalculator.churnValuesFine.get(8));
			assertEquals(27, (int)hotAreaCalculator.churnValuesFine.get(9));
			assertEquals(190, (int)hotAreaCalculator.churnValuesFine.get(10));
		} else {
			assertEquals(0, hotAreaCalculator.churnValuesCoarse.size());
			assertEquals(0, hotAreaCalculator.churnValuesFine.size());
		}
		
		Map<String, Double> hotNumbersMap = createHotNumbersMap(hotNumbers);
		assertEquals(11, hotNumbersMap.size());
		
		if (deepAnalysis) {
			assertEquals(0.68182, hotNumbersMap.get("/trunk/myproject/reallyalongtext.txt"), tolerance);
			assertEquals(0.45455, hotNumbersMap.get("/trunk/myproject/longtext.txt"), tolerance);
			assertEquals(0.15152, hotNumbersMap.get("/trunk/myfolder/filename with spaces.txt"), tolerance);
			assertEquals(0.0, hotNumbersMap.get("/trunk/myfolder"), tolerance);
			assertEquals(0.42424, hotNumbersMap.get("/trunk/myfolder/myfile.txt"), tolerance);
			assertEquals(0.15152, hotNumbersMap.get("/trunk/myproject/addnewfile.txt"), tolerance);
			assertEquals(0.81818, hotNumbersMap.get("/trunk/myproject/mysecontext.txt"), tolerance);
			assertEquals(0.0, hotNumbersMap.get("/trunk/myproject"), tolerance);
			assertEquals(0.0, hotNumbersMap.get("/branches"), tolerance);
			assertEquals(0.0, hotNumbersMap.get("/tags"), tolerance);
			assertEquals(0.0, hotNumbersMap.get("/trunk"), tolerance);
		} else {
			assertEquals(0.65152, hotNumbersMap.get("/trunk/myproject/reallyalongtext.txt"), tolerance);
			assertEquals(0.42424, hotNumbersMap.get("/trunk/myproject/longtext.txt"), tolerance);
			assertEquals(0.0, hotNumbersMap.get("/trunk/myfolder/filename with spaces.txt"), tolerance);
			assertEquals(0.0, hotNumbersMap.get("/trunk/myfolder"), tolerance);
			assertEquals(0.42424, hotNumbersMap.get("/trunk/myfolder/myfile.txt"), tolerance);
			assertEquals(0.0, hotNumbersMap.get("/trunk/myproject/addnewfile.txt"), tolerance);
			assertEquals(0.84848, hotNumbersMap.get("/trunk/myproject/mysecontext.txt"), tolerance);
			assertEquals(0.0, hotNumbersMap.get("/trunk/myproject"), tolerance);
			assertEquals(0.0, hotNumbersMap.get("/branches"), tolerance);
			assertEquals(0.0, hotNumbersMap.get("/tags"), tolerance);
			assertEquals(0.0, hotNumbersMap.get("/trunk"), tolerance);
		}
	}
	
	/**
	 * Check hot numbers of dates added.
	 */
	@Test
	public void testDateAdded() throws IOException {
		CommitFileMatrix commitFileMatrix = CommitFileMatrixGenerator.generateCommitFileMatrixRealExecution(false);
		HotAreaCalculator hotAreaCalculator = new HotAreaCalculator(commitFileMatrix);
		
		List<HotNumber> hotNumbers = hotAreaCalculator.calculateHotNumbers(DATEADDED);
		
		Map<String, Double> hotNumbersMap = createHotNumbersMap(hotNumbers);
		assertEquals(11, hotNumbersMap.size());
		assertEquals(0.90909, hotNumbersMap.get("/trunk/myproject/reallyalongtext.txt"), tolerance);
		assertEquals(0.81818, hotNumbersMap.get("/trunk/myproject/longtext.txt"), tolerance);
		assertEquals(0.72727, hotNumbersMap.get("/trunk/myfolder/filename with spaces.txt"), tolerance);
		assertEquals(0.54545, hotNumbersMap.get("/trunk/myfolder"), tolerance);
		assertEquals(0.54545, hotNumbersMap.get("/trunk/myfolder/myfile.txt"), tolerance);
		assertEquals(0.45454, hotNumbersMap.get("/trunk/myproject/addnewfile.txt"), tolerance);
		assertEquals(0.36363, hotNumbersMap.get("/trunk/myproject/mysecontext.txt"), tolerance);
		assertEquals(0.27272, hotNumbersMap.get("/trunk/myproject"), tolerance);
		assertEquals(0.0, hotNumbersMap.get("/branches"), tolerance);
		assertEquals(0.0, hotNumbersMap.get("/tags"), tolerance);
		assertEquals(0.0, hotNumbersMap.get("/trunk"), tolerance);
	}

	/**
	 * Check hot numbers of dates last modified.
	 */
	@Test
	public void testDateLastModified() throws IOException {
		CommitFileMatrix commitFileMatrix = CommitFileMatrixGenerator.generateCommitFileMatrixRealExecution(false);
		HotAreaCalculator hotAreaCalculator = new HotAreaCalculator(commitFileMatrix);
		
		List<HotNumber> hotNumbers = hotAreaCalculator.calculateHotNumbers(DATEMODIFIED);
		Map<String, Double> hotNumbersMap = createHotNumbersMap(hotNumbers);
		assertEquals(11, hotNumbersMap.size());
		assertEquals(0.90909, hotNumbersMap.get("/trunk/myproject/reallyalongtext.txt"), tolerance);
		assertEquals(0.72727, hotNumbersMap.get("/trunk/myproject/longtext.txt"), tolerance);
		assertEquals(0.63636, hotNumbersMap.get("/trunk/myfolder/filename with spaces.txt"), tolerance);
		assertEquals(0.45454, hotNumbersMap.get("/trunk/myfolder"), tolerance);
		assertEquals(0.45454, hotNumbersMap.get("/trunk/myfolder/myfile.txt"), tolerance);
		assertEquals(0.36363, hotNumbersMap.get("/trunk/myproject/addnewfile.txt"), tolerance);
		assertEquals(0.81818, hotNumbersMap.get("/trunk/myproject/mysecontext.txt"), tolerance);
		assertEquals(0.27272, hotNumbersMap.get("/trunk/myproject"), tolerance);
		assertEquals(0.0, hotNumbersMap.get("/branches"), tolerance);
		assertEquals(0.0, hotNumbersMap.get("/tags"), tolerance);
		assertEquals(0.0, hotNumbersMap.get("/trunk"), tolerance);
	}

	/**
	 * Check hot numbers of average dates.
	 */
	@Test
	public void testDateAverage() throws IOException {
		CommitFileMatrix commitFileMatrix = CommitFileMatrixGenerator.generateCommitFileMatrixRealExecution(false);
		HotAreaCalculator hotAreaCalculator = new HotAreaCalculator(commitFileMatrix);
		
		List<HotNumber> hotNumbers = hotAreaCalculator.calculateHotNumbers(DATEAVERAGE);
		Map<String, Double> hotNumbersMap = createHotNumbersMap(hotNumbers);
		assertEquals(11, hotNumbersMap.size());
		assertEquals(0.90909, hotNumbersMap.get("/trunk/myproject/reallyalongtext.txt"), tolerance);
		assertEquals(0.81818, hotNumbersMap.get("/trunk/myproject/longtext.txt"), tolerance);
		assertEquals(0.72727, hotNumbersMap.get("/trunk/myfolder/filename with spaces.txt"), tolerance);
		assertEquals(0.45454, hotNumbersMap.get("/trunk/myfolder"), tolerance);
		assertEquals(0.54545, hotNumbersMap.get("/trunk/myfolder/myfile.txt"), tolerance);
		assertEquals(0.36363, hotNumbersMap.get("/trunk/myproject/addnewfile.txt"), tolerance);
		assertEquals(0.63636, hotNumbersMap.get("/trunk/myproject/mysecontext.txt"), tolerance);
		assertEquals(0.27272, hotNumbersMap.get("/trunk/myproject"), tolerance);
		assertEquals(0.0, hotNumbersMap.get("/branches"), tolerance);
		assertEquals(0.0, hotNumbersMap.get("/tags"), tolerance);
		assertEquals(0.0, hotNumbersMap.get("/trunk"), tolerance);
	}

	/**
	 * Perform hot area calculation test on a real SVN query data file on an intermediate revision, including diffs.
	 */
	@Test
	public void testHotAreaCalculatorConcreteRevisionDeepAnalysis() throws IOException {
		testHotAreaCalculatorConcreteRevision(true);
	}
	
	/**
	 * Perform hot area calculation test on a real SVN query data file on an intermediate revision, excluding diffs.
	 */
	@Test
	public void testHotAreaCalculatorConcreteRevisionNoDeepAnalysis() throws IOException {
		testHotAreaCalculatorConcreteRevision(false);
	}
	
	/**
	 * Performs developer focus test on the following example:
	 * 
	 * Commit 1:
	 * - Developer: mike
	 * - Date: 2016.02.06. 09:50
	 * - Committed files:
	 * -- A /com/mycompany/myapp/main/Main.java
	 * -- A /com/mycompany/myapp/util/Converter.java
	 * -- A /com/mycompany/myapp/data/Shape.java
	 * 
	 * Commit 2:
	 * - Developer: sully
	 * - Date: 2016.02.06. 12:42
	 * -- A /com/mycompany/myapp/data/concrete/Circle.java
	 * -- A /com/mycompany/myapp/util/Calculator.java
	 * -- M /com/mycompany/myapp/main/Main.java
	 * 
	 * Commit 3:
	 * - Developer: mike
	 * - Date: 2016.02.06. 18:14
	 * - Committed files:
	 * -- M /com/mycompany/myapp/data/concrete/Circle.java
	 * 
	 * Commit 4:
	 * - Developer: mike
	 * - Date: 2016.06.08. 12:49
	 * - Committed files:
	 * -- M /com/mycompany/myapp/util/Converter.java
	 * -- M /com/mycompany/myapp/data/concrete/Circle.java
	 * 
	 * Commit 5:
	 * - Developer: sully
	 * - Date: 2016.06.08. 15:22
	 * - Committed files:
	 * -- M /com/mycompany/myapp/data/Shape.java
	 * -- M /com/mycompany/myapp/data/concrete/Circle.java
	 * 
	 * Commit 6:
	 * - Developer: mike
	 * - Date: 2016.06.08. 16:09
	 * - Committed files:
	 * -- A /com/mycompany/myapp/util/Helper.java
	 * 
	 * 
	 * There are the following focus values:
	 * 
	 * 1. /com/mycompany/myapp/main/Main.java
	 * Modified by mike and sully.
	 * Last modified in the second commit.
	 * 
	 * A. mike
	 * Files to be considered to calculate the focus information:
	 * - /com/mycompany/myapp/main/Main.java
	 * - /com/mycompany/myapp/util/Converter.java
	 * - /com/mycompany/myapp/data/Shape.java
	 * Focus information: (3 / (3 over 2)) * (2 + 2 + 2) = (3 / 3) * 6 = 6.0
	 * 
	 * B. sully
	 * Files to be considered to calculate the focus information:
	 * - /com/mycompany/myapp/data/concrete/Circle.java
	 * - /com/mycompany/myapp/util/Calculator.java
	 * - /com/mycompany/myapp/main/Main.java
	 * Focus information: (3 / (3 over 2)) * (3 + 3 + 2) = (3 / 3) * 8 = 8.00
	 * 
	 * Resulting focus weighted contributors: 6.00 + 8.00 = 14.00
	 * 
	 * 
	 * 2. /com/mycompany/myapp/util/Converter.java
	 * Modified by mike.
	 * Last modified in the fourth commit.
	 * 
	 * A. mike
	 * Files to be considered to calculate the focus information:
	 * - /com/mycompany/myapp/main/Main.java
	 * - /com/mycompany/myapp/util/Converter.java
	 * - /com/mycompany/myapp/data/Shape.java
	 * - /com/mycompany/myapp/data/concrete/Circle.java
	 * Focus information: (4 / (4 over 2)) * (2 + 2 + 3 + 2 + 3 + 1) = (4 / 6) * 13 = 8.67
	 * 
	 * Resulting focus weighted contributors: 8.67
	 * 
	 * 
	 * 3. /com/mycompany/myapp/data/Shape.java
	 * Modified by mike and sully.
	 * Last modified in the fifth commit.
	 * 
	 * A. mike
	 * Files to be considered to calculate the focus information: same as /com/mycompany/myapp/util/Converter.java
	 * Focus information: 8.67
	 * 
	 * B. sully
	 * Last modified in the fifth commit. Files to be considered to calculate the focus information:
	 * - /com/mycompany/myapp/data/concrete/Circle.java
	 * - /com/mycompany/myapp/util/Calculator.java
	 * - /com/mycompany/myapp/main/Main.java
	 * - /com/mycompany/myapp/data/Shape.java
	 * Focus information: (4 / (4 over 2)) * (3 + 3 + 1 + 2 + 2 + 2) = (4 / 6) * 13 = 8.67
	 * 
	 * Resulting focus weighted contributors: 8.67 + 8.67 = 17.33
	 * 
	 * 
	 * 4. /com/mycompany/myapp/data/concrete/Circle.java
	 * Modified by mike and sully.
	 * Last modified in the fifth commit.
	 * 
	 * A. mike
	 * The focus information is same as in case of /com/mycompany/myapp/util/Converter.java: 8.67.
	 * 
	 * B. sully
	 * The focus information is the same as in case of /com/mycompany/myapp/data/Shape.java: 8.67.
	 * 
	 * Resulting focus weighted contributors: 8.67 + 8.67 = 17.33
	 * 
	 * 
	 * 5. /com/mycompany/myapp/util/Calculator.java
	 * Modified by sully.
	 * Last modified in the second commit.
	 * 
	 * A. sully
	 * Therefore the focus information is the same as in the case of /com/mycompany/myapp/main/Main.java: 8.00.
	 * 
	 * Resulting focus weighted contributors: 8.00
	 * 
	 * 
	 * 6. /com/mycompany/myapp/util/Helper.java
	 * Modified by mike.
	 * Last modified in the sixth commit.
	 * 
	 * A. mike
	 * Files to be considered to calculate the focus information:
	 * - /com/mycompany/myapp/main/Main.java
	 * - /com/mycompany/myapp/util/Converter.java
	 * - /com/mycompany/myapp/data/Shape.java
	 * - /com/mycompany/myapp/data/concrete/Circle.java
	 * - /com/mycompany/myapp/util/Helper.java
	 * Focus information: (5 / (5 over 2)) * (2 + 2 + 3 + 2 + 2 + 3 + 0 + 1 + 2 + 3) = (5 / 10) * 20 = 10.00
	 * 
	 * Resulting focus weighted contributors: 10.00

	 * 
	 * As final result we receive the following values:
	 * - /com/mycompany/myapp/util/Calculator.java       (8.00): 0.000
	 * - /com/mycompany/myapp/util/Converter.java        (8.67): 0.167
	 * - /com/mycompany/myapp/util/Helper.java          (10.00): 0.333
	 * - /com/mycompany/myapp/main/Main.java            (14.00): 0.500
	 * - /com/mycompany/myapp/data/Shape.java           (17.33): 0.667
	 * - /com/mycompany/myapp/data/concrete/Circle.java (17.33): 0.667
	 * 
	 * Remark: this example was originally prepared for the last 3 months logic, this is why there are jumps in the log.
	 */
	@Test
	public void testHotAreaCalculatorDeveloperFocus() throws IOException {
		CommitFileMatrix commitFileMatrix = CommitFileMatrixGenerator.generateCommitFileMatrixDeveloperFocus();
		HotAreaCalculator hotAreaCalculator = new HotAreaCalculator(commitFileMatrix, 6);
		
		List<HotNumber> hotNumbers = hotAreaCalculator.calculateHotNumbers(OWNERSHIP_FOCUS);
		
		Map<String, Double> hotNumbersMap = createHotNumbersMap(hotNumbers);
		
		assertEquals(6, hotNumbers.size());
		assertEquals(0.0 / 6, hotNumbersMap.get("/com/mycompany/myapp/util/Calculator.java"), tolerance);
		assertEquals(1.0 / 6, hotNumbersMap.get("/com/mycompany/myapp/util/Converter.java"), tolerance);
		assertEquals(2.0 / 6, hotNumbersMap.get("/com/mycompany/myapp/util/Helper.java"), tolerance);
		assertEquals(3.0 / 6, hotNumbersMap.get("/com/mycompany/myapp/main/Main.java"), tolerance);
		assertEquals(4.0 / 6, hotNumbersMap.get("/com/mycompany/myapp/data/Shape.java"), tolerance);
		assertEquals(4.0 / 6, hotNumbersMap.get("/com/mycompany/myapp/data/concrete/Circle.java"), tolerance);
	}
	
	/**
	 * The hot area calculation of a specific revision test implementation with checks.
	 */
	private void testHotAreaCalculatorConcreteRevision(boolean deepAnalysis) throws IOException {
		CommitFileMatrix commitFileMatrix = CommitFileMatrixGenerator.generateCommitFileMatrixRealExecution(deepAnalysis);
		HotAreaCalculator hotAreaCalculator = new HotAreaCalculator(commitFileMatrix, 5);
		
		List<HotNumber> hotNumbers = hotAreaCalculator.calculateHotNumbers(FULL);
		
		Map<String, Double> hotNumbersMap = createHotNumbersMap(hotNumbers);
		
		assertEquals(6, hotAreaCalculator.ownershipValues.size());
		if (deepAnalysis) {
			assertEquals(0.0, hotNumbersMap.get("/branches"), tolerance);
			assertEquals(0.0, hotNumbersMap.get("/tags"), tolerance);
			assertEquals(0.0, hotNumbersMap.get("/trunk"), tolerance);
			assertEquals(0.0, hotNumbersMap.get("/trunk/myproject"), tolerance);
			assertEquals(0.22222, hotNumbersMap.get("/trunk/myproject/mytext.txt"), tolerance);
			assertEquals(0.55556, hotNumbersMap.get("/trunk/myproject/mysecontext.txt"), tolerance);
		} else {
			assertEquals(0.0, hotNumbersMap.get("/branches"), tolerance);
			assertEquals(0.0, hotNumbersMap.get("/tags"), tolerance);
			assertEquals(0.0, hotNumbersMap.get("/trunk"), tolerance);
			assertEquals(0.0, hotNumbersMap.get("/trunk/myproject"), tolerance);
			assertEquals(0.0, hotNumbersMap.get("/trunk/myproject/mytext.txt"), tolerance);
			assertEquals(0.55556, hotNumbersMap.get("/trunk/myproject/mysecontext.txt"), tolerance);
		}
	}
	
	/**
	 * Creates a fileName -> hotNumber map from hot numbers.
	 */
	private static Map<String, Double> createHotNumbersMap(List<HotNumber> hotNumbers) {
		Map<String, Double> hotNumbersMap = new HashMap<String, Double>();
		for (HotNumber hotNumber : hotNumbers) {
			hotNumbersMap.put(hotNumber.getFileName(), hotNumber.getHotNumber());
		}
		return hotNumbersMap;
	}
	
	
}
