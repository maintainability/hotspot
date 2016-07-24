package hotareadetector.util;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import hotareadetector.data.HotAreaDetectorContext;
import hotareadetector.data.VersionControlHistoryMetrics;

public class ResultSaver {

	/**
	 * Saves metrics to file.
	 */
	public static void saveMetrics(HotAreaDetectorContext context, Map<String, VersionControlHistoryMetrics> versionControlHistoryMetricsPerFile) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter metricsWriter = new PrintWriter(createPath(context, "metrics.csv"), "UTF-8");
		metricsWriter.println("Filename;Modifications;Churn;Ownership;Ownership1;Ownership2;Focus;DateAdded;DateAverage;DateModified");
		List<String> fileNamesList = HotspotStringUtil.createSortedListFromSet(versionControlHistoryMetricsPerFile.keySet());
		for (String fileName : fileNamesList) {
			metricsWriter.print(fileName);
			metricsWriter.print(";" + versionControlHistoryMetricsPerFile.get(fileName).getNumberOfModifications());
			metricsWriter.print(";" + versionControlHistoryMetricsPerFile.get(fileName).getChurnValue());
			metricsWriter.print(";" + versionControlHistoryMetricsPerFile.get(fileName).getNumberOfContributors());
			metricsWriter.print(";" + versionControlHistoryMetricsPerFile.get(fileName).getNumberOfContributorsToleranceOne());
			metricsWriter.print(";" + versionControlHistoryMetricsPerFile.get(fileName).getNumberOfContributorsToleranceTwo());
			metricsWriter.print(";" + versionControlHistoryMetricsPerFile.get(fileName).getFocusWeightedContributors());
			metricsWriter.print(";" + versionControlHistoryMetricsPerFile.get(fileName).getCreatingDate());
			metricsWriter.print(";" + versionControlHistoryMetricsPerFile.get(fileName).getLastModificationDate());
			metricsWriter.print(";" + versionControlHistoryMetricsPerFile.get(fileName).getModificationDatesAverage());
			metricsWriter.println();
		}
		
		metricsWriter.close();
	}
	
	/**
	 * Displays the result of hot area detection.
	 */
	public static void writeHotNumbersIntoFile(HotAreaDetectorContext context, Map<String, Map<String, Double>> hotNumbers)  throws FileNotFoundException, UnsupportedEncodingException {
		String result = formatResult(hotNumbers);
		System.out.print(result);
		PrintWriter metricsWriter = new PrintWriter(createPath(context, "hotspot.csv"), "UTF-8");
		metricsWriter.print(result);
		metricsWriter.close();
	}
	
	protected static String formatResult(Map<String, Map<String, Double>> hotNumbers) {
		NumberFormat formatter = new DecimalFormat("#0.00000"); 
		System.out.println("Hot numbers:\n");
		Set<String> hotNumberTypesSet = hotNumbers.keySet();
		List<String> hotNumberTypesList = new ArrayList<String>();
		Set<String> fileNameSet = null;
		List<String> fileNameList = new ArrayList<String>();
		hotNumberTypesList.addAll(hotNumberTypesSet);
		Collections.sort(hotNumberTypesList);
		StringBuffer result = new StringBuffer();
		result.append("Filename");
		for (String hotNumberType : hotNumberTypesList) {
			result.append(";" + hotNumberType);
			if (fileNameSet == null) {
				fileNameSet = hotNumbers.get(hotNumberType).keySet();
			}
		}
		result.append("\n");
		fileNameList.addAll(fileNameSet);
		Collections.sort(fileNameList);
		
		for (String fileName : fileNameList) {
			result.append(fileName);
			for (String hotNumberType : hotNumberTypesList) {
				Double hotNumber = hotNumbers.get(hotNumberType).get(fileName);
				result.append(";" + formatter.format(hotNumber));
			}
			result.append("\n");
		}
		return result.toString();
	}
	
	protected static String createPath(HotAreaDetectorContext context, String tail) {
		String filePath = context.getDirName() + "/" + tail;
		if (context.getPrefix() != null) {
			filePath = context.getDirName() + "/" + context.getPrefix() + "-" + tail;
		}
		return filePath;
	}
	
}
