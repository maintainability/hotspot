package hotareadetector.data;

public enum AnalysisType {
	COMBINED,
	FULL,
	CHEAP, // modifications, ownerships without focus, date related
	CHEAP_AND_FOCUS, // cheap + focus
	CHEAP_AND_CHURN // cheap + churn
}
