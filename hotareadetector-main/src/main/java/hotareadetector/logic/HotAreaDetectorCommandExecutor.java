package hotareadetector.logic;

import hotareadetector.data.CommitDataExtended;
import hotareadetector.data.CommitDataPerFile;
import hotareadetector.data.Credentials;
import hotareadetector.data.HotAreaDetectorContext;
import hotareadetector.data.SourceControlInformation;
import hotareadetector.data.VersionControlHistoryMetrics;
import hotareadetector.interfaces.SourceControlCommandExecutor;
import hotareadetector.interfaces.SourceControlCommandParser;
import hotareadetector.svn.SvnCommandExecutor;
import hotareadetector.svn.SvnCommandParser;
import hotareadetector.util.FilteringUtil;
import hotareadetector.util.ResultSaver;
import hotareadetector.util.VersionControlHistoryMetricsCalculator;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Class for hot area command execution.
 */
public class HotAreaDetectorCommandExecutor {
	/**
	 * Executes hot area detection command, based on the context. Returns a map of the following format:
	 * - metric 1 ->
	 *   file 1 -> hot number 1_1
	 *   file 2 -> hot number 1_2
	 * - metric 2 ->
	 *   file 1 -> hot number 2_1
	 *   file 2 -> hot number 2_2
	 */
	public Map<String, Map<String, Double>> executeHotAreaDetection(HotAreaDetectorContext context) throws IOException {
		CommitDataPerFile commitDataPerFile = readCommitData(context);
		Map<String, List<CommitDataExtended>> fileCommitMapFiltered = filterCommitData(context, commitDataPerFile);
		Map<String, VersionControlHistoryMetrics> versionControlHistoryMetricsPerFile = (new VersionControlHistoryMetricsCalculator()).calculateVersionControlHistoryMetrics(fileCommitMapFiltered);
		ResultSaver.saveMetrics(context, versionControlHistoryMetricsPerFile);
		Map<String, Map<String, Double>> hotNumbers = (new HotAreaCalculator(versionControlHistoryMetricsPerFile)).calculateHotNumbers();
		return hotNumbers;
	}

	protected CommitDataPerFile readCommitData(HotAreaDetectorContext context) throws IOException {
		SourceControlCommandExecutor executor = new SvnCommandExecutor();
		SourceControlCommandParser parser = new SvnCommandParser();
		SourceControlInformation sourceControl = new SourceControlInformation();
		sourceControl.setUrl(context.getSourceControlUrl());
		Credentials credentials = new Credentials(context.getSourceControlUserName(), context.getSourceControlPassword());
		executor.setAccess(context, sourceControl, credentials);
		SourceControlLogic sourceControlLogic = new SourceControlLogic();
		CommitDataPerFile commitDataPerFile = sourceControlLogic.readCommitData(executor, parser, context);
		return commitDataPerFile;
	}
	
	protected Map<String, List<CommitDataExtended>> filterCommitData(HotAreaDetectorContext context, CommitDataPerFile commitDataPerFile) {
		FilteringUtil filteringUtil = new FilteringUtil(context.getIncludePrefixes(), context.getExcludePrefixes(), context.getExtensions());
		Map<String, List<CommitDataExtended>> fileCommitMapFiltered = filteringUtil.filterFileCommitMap(commitDataPerFile.getFileCommitMap());
		return fileCommitMapFiltered;
	}
	

}
