package hotareadetector.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import hotareadetector.data.AnalysisType;

public class AnalysisNecessityCheckerTest {
	@Test
	public void testIsChurnNeeded() {
		assertFalse(AnalysisNecessityChecker.isChurnNeeded(AnalysisType.CHEAP));
		assertTrue(AnalysisNecessityChecker.isChurnNeeded(AnalysisType.CHEAP_AND_CHURN));
		assertFalse(AnalysisNecessityChecker.isChurnNeeded(AnalysisType.CHEAP_AND_FOCUS));
		assertFalse(AnalysisNecessityChecker.isChurnNeeded(AnalysisType.COMBINED));
		assertTrue(AnalysisNecessityChecker.isChurnNeeded(AnalysisType.FULL));
	}

	@Test
	public void testIsFocusNeeded() {
		assertFalse(AnalysisNecessityChecker.isFocusNeeded(AnalysisType.CHEAP));
		assertFalse(AnalysisNecessityChecker.isFocusNeeded(AnalysisType.CHEAP_AND_CHURN));
		assertTrue(AnalysisNecessityChecker.isFocusNeeded(AnalysisType.CHEAP_AND_FOCUS));
		assertTrue(AnalysisNecessityChecker.isFocusNeeded(AnalysisType.COMBINED));
		assertTrue(AnalysisNecessityChecker.isFocusNeeded(AnalysisType.FULL));
	}
}
