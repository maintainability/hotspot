package hotareadetector.logic;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
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
		
		assertEquals(0.3, hotNumber, tolerance);
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
			hotAreaCalculator.combinedValues.add(2 * i);
		}
		hotAreaCalculator.ownershipValues.addAll(valueList);
		hotAreaCalculator.ownershipValuesToleranceOne.addAll(valueList);
		hotAreaCalculator.ownershipValuesToleranceTwo.addAll(valueList);
		hotAreaCalculator.numberOfModifications.addAll(valueList);
		if (deepAnalysis) {
			hotAreaCalculator.churnValues.addAll(valueList);
			hotAreaCalculator.churnValuesFiner.addAll(valueList);
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
		
		assertEquals(11, hotAreaCalculator.combinedValues.size());
		assertEquals(2, (int)hotAreaCalculator.combinedValues.get(0));
		assertEquals(2, (int)hotAreaCalculator.combinedValues.get(1));
		assertEquals(2, (int)hotAreaCalculator.combinedValues.get(2));
		assertEquals(2, (int)hotAreaCalculator.combinedValues.get(3));
		assertEquals(2, (int)hotAreaCalculator.combinedValues.get(4));
		assertEquals(2, (int)hotAreaCalculator.combinedValues.get(5));
		assertEquals(2, (int)hotAreaCalculator.combinedValues.get(6));
		assertEquals(3, (int)hotAreaCalculator.combinedValues.get(7));
		assertEquals(3, (int)hotAreaCalculator.combinedValues.get(8));
		assertEquals(4, (int)hotAreaCalculator.combinedValues.get(9));
		assertEquals(11, (int)hotAreaCalculator.combinedValues.get(10));
		
		if (deepAnalysis) {
			assertEquals(11, hotAreaCalculator.churnValues.size());
			assertEquals(0, (int)hotAreaCalculator.churnValues.get(0));
			assertEquals(0, (int)hotAreaCalculator.churnValues.get(1));
			assertEquals(0, (int)hotAreaCalculator.churnValues.get(2));
			assertEquals(0, (int)hotAreaCalculator.churnValues.get(3));
			assertEquals(0, (int)hotAreaCalculator.churnValues.get(4));
			assertEquals(1, (int)hotAreaCalculator.churnValues.get(5));
			assertEquals(1, (int)hotAreaCalculator.churnValues.get(6));
			assertEquals(4, (int)hotAreaCalculator.churnValues.get(7));
			assertEquals(39, (int)hotAreaCalculator.churnValues.get(8));
			assertEquals(77, (int)hotAreaCalculator.churnValues.get(9));
			assertEquals(262, (int)hotAreaCalculator.churnValues.get(10));
			
			assertEquals(11, hotAreaCalculator.churnValuesFiner.size());
			assertEquals(0, (int)hotAreaCalculator.churnValuesFiner.get(0));
			assertEquals(0, (int)hotAreaCalculator.churnValuesFiner.get(1));
			assertEquals(0, (int)hotAreaCalculator.churnValuesFiner.get(2));
			assertEquals(0, (int)hotAreaCalculator.churnValuesFiner.get(3));
			assertEquals(0, (int)hotAreaCalculator.churnValuesFiner.get(4));
			assertEquals(1, (int)hotAreaCalculator.churnValuesFiner.get(5));
			assertEquals(1, (int)hotAreaCalculator.churnValuesFiner.get(6));
			assertEquals(2, (int)hotAreaCalculator.churnValuesFiner.get(7));
			assertEquals(17, (int)hotAreaCalculator.churnValuesFiner.get(8));
			assertEquals(27, (int)hotAreaCalculator.churnValuesFiner.get(9));
			assertEquals(190, (int)hotAreaCalculator.churnValuesFiner.get(10));
		} else {
			assertEquals(0, hotAreaCalculator.churnValues.size());
			assertEquals(0, hotAreaCalculator.churnValuesFiner.size());
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
