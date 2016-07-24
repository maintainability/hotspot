package hotareadetector.util;

import hotareadetector.data.AnalysisType;

public class AnalysisNecessityChecker {
	public static boolean isChurnNeeded(AnalysisType analysisType) {
		return analysisType == AnalysisType.CHEAP_AND_CHURN || analysisType == AnalysisType.FULL;
	}

	public static boolean isFocusNeeded(AnalysisType analysisType) {
		return analysisType == AnalysisType.CHEAP_AND_FOCUS || analysisType == AnalysisType.COMBINED || analysisType == AnalysisType.FULL;
	}
}
