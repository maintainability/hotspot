package hotareadetector.util;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Map.Entry;

import hotareadetector.data.HotAreaDetectorContext;
import hotareadetector.data.VersionControlHistoryMetrics;

public class MetricsSaver {

	/**
	 * Saves metrics to file.
	 */
	public static void saveMetrics(HotAreaDetectorContext context, Map<String, VersionControlHistoryMetrics> versionControlHistoryMetricsPerFile) throws FileNotFoundException, UnsupportedEncodingException {
		if (context.getMetricsPrefix() == null) {
			return;
		}
		context.getAnalysisType();
		String fileNamePrefix = context.getDirName() + "/" + context.getMetricsPrefix();

		boolean isChurnNeeded = AnalysisNecessityChecker.isChurnNeeded(context.getAnalysisType());
		boolean isFocusNeeded = AnalysisNecessityChecker.isFocusNeeded(context.getAnalysisType());
		
		PrintWriter writerModifications = new PrintWriter(fileNamePrefix + "-modifications.csv", "UTF-8");
		PrintWriter writerOwnership = new PrintWriter(fileNamePrefix + "-ownership.csv", "UTF-8");
		PrintWriter writerOwnership1 = new PrintWriter(fileNamePrefix + "-ownership1.csv", "UTF-8");
		PrintWriter writerOwnership2 = new PrintWriter(fileNamePrefix + "-ownership2.csv", "UTF-8");
		PrintWriter writerDateAdded = new PrintWriter(fileNamePrefix + "-dateadded.csv", "UTF-8");
		PrintWriter writerDateAverage = new PrintWriter(fileNamePrefix + "-dateaverage.csv", "UTF-8");
		PrintWriter writerDateModified = new PrintWriter(fileNamePrefix + "-datemodified.csv", "UTF-8");
		PrintWriter writerChurn = null;
		if (isChurnNeeded) {
			writerChurn = new PrintWriter(fileNamePrefix + "-churn.csv", "UTF-8");
		}
		PrintWriter writerFocus = null;
		if (isFocusNeeded) {
			writerFocus = new PrintWriter(fileNamePrefix + "-focus.csv", "UTF-8");
		}
		
		writerModifications.println("Name;Modifications");
		writerOwnership.println("Name;Ownership");
		writerOwnership1.println("Name;Ownership1");
		writerOwnership2.println("Name;Ownership2");
		writerDateAdded.println("Name;DateAdded");
		writerDateAverage.println("Name;DateAverage");
		writerDateModified.println("Name;DateModified");
		if (isChurnNeeded) {
			writerChurn.println("Name;Churn");
		}
		if (isFocusNeeded) {
			writerFocus.println("Name;Focus");
		}

		for (Entry<String, VersionControlHistoryMetrics> versionControlHistoryMetricsEntry : versionControlHistoryMetricsPerFile.entrySet()) {
			writerModifications.println(versionControlHistoryMetricsEntry.getKey() + ";" + versionControlHistoryMetricsEntry.getValue().getNumberOfModifications());
			writerOwnership.println(versionControlHistoryMetricsEntry.getKey() + ";" + versionControlHistoryMetricsEntry.getValue().getNumberOfContributors());
			writerOwnership1.println(versionControlHistoryMetricsEntry.getKey() + ";" + versionControlHistoryMetricsEntry.getValue().getNumberOfContributorsToleranceOne());
			writerOwnership2.println(versionControlHistoryMetricsEntry.getKey() + ";" + versionControlHistoryMetricsEntry.getValue().getNumberOfContributorsToleranceTwo());
			writerDateAdded.println(versionControlHistoryMetricsEntry.getKey() + ";" + versionControlHistoryMetricsEntry.getValue().getCreatingDate());
			writerDateAverage.println(versionControlHistoryMetricsEntry.getKey() + ";" + versionControlHistoryMetricsEntry.getValue().getModificationDatesAverage());
			writerDateModified.println(versionControlHistoryMetricsEntry.getKey() + ";" + versionControlHistoryMetricsEntry.getValue().getLastModificationDate());
			if (isChurnNeeded) {
				writerChurn.println(versionControlHistoryMetricsEntry.getKey() + ";" + versionControlHistoryMetricsEntry.getValue().getChurnValue());
			}
			if (isFocusNeeded) {
				writerFocus.println(versionControlHistoryMetricsEntry.getKey() + ";" + versionControlHistoryMetricsEntry.getValue().getFocusWeightedContributors());
			}
		}
		
		writerModifications.close();
		writerOwnership.close();
		writerOwnership1.close();
		writerOwnership2.close();
		writerDateAdded.close();
		writerDateAverage.close();
		writerDateModified.close();
		if (isChurnNeeded) {
			writerChurn.close();
		}
		if (isFocusNeeded) {
			writerFocus.close();
		}
	}
}
