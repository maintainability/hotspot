package hotareadetector.logic;

import hotareadetector.data.CommitData;
import hotareadetector.data.CommitDataPerFile;
import hotareadetector.data.CommitedFileData;
import hotareadetector.data.FileDiffInformation;
import hotareadetector.data.HotAreaDetectorContext;
import hotareadetector.data.SourceControlResultData;
import hotareadetector.interfaces.SourceControlCommandExecutor;
import hotareadetector.interfaces.SourceControlCommandParser;
import hotareadetector.util.AnalysisNecessityChecker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * General algorithm of source control queries.
 */
public class SourceControlLogic {
	public CommitDataPerFile readCommitData(
			SourceControlCommandExecutor executor, 
			SourceControlCommandParser parser,
			HotAreaDetectorContext context) throws IOException {
		System.out.println("Executing source control log command...");
		SourceControlResultData sourceControlLogResultData = executor.executeSourceControlLogCommand();
		if (sourceControlLogResultData == null) {
			System.out.println("Problem with executing source control log command.");
			return null;
		}
		System.out.println("Source control log command executed. Parsing log result...");
		List<CommitData> commitDataList = parser.parseSourceControlLog(sourceControlLogResultData);
		System.out.println("Log result parsed.");
		List<CommitData> commitDataListFiltered = filterCommitsByRevision(commitDataList, context.getRevision());
		CommitDataPerFile commitDataPerFile = new CommitDataPerFile();
		CommitData previous = null;
		CommitData actual = null;
		boolean churnNeeded = AnalysisNecessityChecker.isChurnNeeded(context.getAnalysisType());
		List<FileDiffInformation> emptyFiles = new ArrayList<FileDiffInformation>();
		for (CommitData commitData : commitDataListFiltered) {
			System.out.println("Elaborating revision r" + commitData.getRevisionNumber() + "...");
			
			previous = actual;
			actual = commitData;
			
			List<FileDiffInformation> fileDiffInformationList = emptyFiles;
			// I can't execute the diff command for the BASE and the first revision due to the following error:
			// svn: E195002: PREV, BASE, or COMMITTED revision keywords are invalid for URL
			if (churnNeeded && previous != null && (context.getRevision() == null ? true : actual.getRevisionNumber() <= context.getRevision())) {
				System.out.println("Executing diff command execution between revisions r" + previous.getRevisionNumber() + " and r" + actual.getRevisionNumber() + "...");
				SourceControlResultData sourceControlDiffResultData = executor.executeSourceControlDiffCommand("r" + previous.getRevisionNumber(), "r" + actual.getRevisionNumber());
				if (sourceControlDiffResultData == null) {
					System.out.println("Problem executing diff command. Exiting.");
					return null;
				}
				System.out.println("Diff command executed. Parsing...");
				fileDiffInformationList = parser.parseSourceControlDiff(sourceControlDiffResultData);
				System.out.println("Diff parsed.");
			}
			
			commitDataPerFile.addCommitData(commitData, createChurnData(fileDiffInformationList));
			System.out.println("Revision r" + commitData.getRevisionNumber() + " elaborated.");
		}
		System.out.println("Elaboration of all revisions finished.");
		return commitDataPerFile;
	}
	
	protected Map<String, Integer> createChurnData(List<FileDiffInformation> fileDiffInformationList) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		for (FileDiffInformation fileDiffInformation : fileDiffInformationList) {
			result.put(fileDiffInformation.getFileName(), fileDiffInformation.getNumberOfAdds() + fileDiffInformation.getNumberOfRemoves());
		}
		return result;
	}

	protected FileDiffInformation getRelatedFileDiff(List<FileDiffInformation> commitedFiles, CommitedFileData commitedFileData) {
		String fileName = commitedFileData.getFileName();
		for (FileDiffInformation commitedFile : commitedFiles) {
			if (fileName.endsWith(commitedFile.getFileName())) {
				return commitedFile;
			}
		}
		return null;
	}
	
	/**
	 * Performs the filtering mechanism based on revision.
 	 */
	protected static List<CommitData> filterCommitsByRevision(
			List<CommitData> commitDataList, 
			Integer revision) {
		// revision filtering part
		List<CommitData> commitDataListRevision = new ArrayList<CommitData>();
		if (revision == null) {
			commitDataListRevision.addAll(commitDataList);
		} else {
			for (CommitData commitData : commitDataList) {
				if (commitData.getRevisionNumber() <= revision) {
					commitDataListRevision.add(commitData);
				}
			}
		}
		return commitDataListRevision;
	}

}
