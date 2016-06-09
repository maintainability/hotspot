package hotareadetector.logic;

import hotareadetector.data.CommitData;
import hotareadetector.data.CommitFileCell;
import hotareadetector.data.CommitFileMatrix;
import hotareadetector.data.CommitedFileData;
import hotareadetector.data.ContributorDate;
import hotareadetector.data.DeveloperFocusInformation;
import hotareadetector.data.FileDiffInformation;
import hotareadetector.data.HotAreaDetectorContext;
import hotareadetector.data.OperationType;
import hotareadetector.data.SourceControlResultData;
import hotareadetector.interfaces.SourceControlCommandExecutor;
import hotareadetector.interfaces.SourceControlCommandParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * General algorithm of source control queries.
 */
public class SourceControlLogic {
	public CommitFileMatrix readCommitData(
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
		List<CommitData> commitDataListFiltered = filterCommitDataList(commitDataList, context.getRevision());
		CommitFileMatrix result = new CommitFileMatrix(context.isDeepAnalysis());
		CommitData previous = null;
		CommitData actual = null;
		List<FileDiffInformation> emptyFiles = new ArrayList<FileDiffInformation>();
		DeveloperFocusInformation developerFocusInformation = new DeveloperFocusInformation();
		for (CommitData commitData : commitDataListFiltered) {
			System.out.println("Elaborating revision r" + commitData.getRevisionNumber() + "...");
			
			previous = actual;
			actual = commitData;
			
			List<FileDiffInformation> commitedFiles = emptyFiles;
			// I can't execute the diff command for the BASE and the first revision due to the following error:
			// svn: E195002: PREV, BASE, or COMMITTED revision keywords are invalid for URL
			if (context.isDeepAnalysis() && previous != null && (context.getRevision() == null ? true : actual.getRevisionNumber() <= context.getRevision())) {
				System.out.println("Executing diff command execution between revisions r" + previous.getRevisionNumber() + " and r" + actual.getRevisionNumber() + "...");
				SourceControlResultData sourceControlDiffResultData = executor.executeSourceControlDiffCommand("r" + previous.getRevisionNumber(), "r" + actual.getRevisionNumber());
				if (sourceControlDiffResultData == null) {
					System.out.println("Problem executing diff command. Exiting.");
					return null;
				}
				if (!context.isSaveLogsOnly()) {
					System.out.println("Diff command executed. Parsing...");
					commitedFiles = parser.parseSourceControlDiff(sourceControlDiffResultData);
					System.out.println("Diff parsed.");
				}
			}
			
			if (!context.isSaveLogsOnly()) {
				developerFocusInformation.addModifications(commitData.getDeveloper(), commitData.getDate(), commitData.getCommitedFiles());
				List<CommitFileCell> cumulativeCommitFiles = calculateCumulativeData(result, commitData, commitedFiles, developerFocusInformation);
				
				result.addCommitedFileDataCollection(cumulativeCommitFiles);
				System.out.println("Revision r" + commitData.getRevisionNumber() + " elaborated.");
			}
		}
		System.out.println("Elaboration of all revisions finished.");
		if (!context.isSaveLogsOnly()) {
			result.filterData(context.getIncludePrefixes(), context.getExcludePrefixes(), context.getExtensions());
			return result;
		}
		return null;
	}
	
	protected List<CommitFileCell> calculateCumulativeData(CommitFileMatrix commitFileMatrix, CommitData commitData, List<FileDiffInformation> commitedFiles, DeveloperFocusInformation developerFocusInformation) {
		List<CommitFileCell> result = new ArrayList<CommitFileCell>();
		CommitFileCell emptyFileData = new CommitFileCell();
		for (CommitedFileData commitedFile : commitData.getCommitedFiles()) {
			FileDiffInformation relatedFileDiff = getRelatedFileDiff(commitedFiles, commitedFile);
			CommitFileCell fileDataLatest = null;
			switch (commitedFile.getOperationType()) {
			case A:
				fileDataLatest = emptyFileData;
				break;
				
			case R:
				List<CommitFileCell> renameResult = commitFileMatrix.performRename(commitedFile.getFromFileName(), commitedFile.getFileName(), commitData.getDeveloper(), commitData.getDate(), commitData.getRevisionNumber(), relatedFileDiff, developerFocusInformation);
				result.addAll(renameResult);
				break;
				
			case D:
			case M:
				fileDataLatest = commitFileMatrix.getFileDataLatest(commitedFile.getFileName());
				break;
			}
			
			if (commitedFile.getOperationType() != OperationType.R) {
				if (fileDataLatest == null) {
					System.out.println("The history information of file " + commitedFile.getFileName() + " is not found. Considering the history empty.");
					fileDataLatest = emptyFileData;
				}
				CommitFileCell actualCommitFile = new CommitFileCell();
				result.add(actualCommitFile);
				actualCommitFile.setFileName(commitedFile.getFileName());
				actualCommitFile.setRevision(commitData.getRevisionNumber());
				actualCommitFile.setLatestOperation(commitedFile.getOperationType());
				actualCommitFile.setNumberOfModifications(fileDataLatest.getNumberOfModifications() + 1);
				// null check is necessary because in case of directory changes there is no diff
				if (relatedFileDiff != null) {
					actualCommitFile.setChurnValueCoarse(fileDataLatest.getChurnValueCoarse() + extractChurnValue(relatedFileDiff.getAtAtDiffs()));
					actualCommitFile.setChurnValueFine(fileDataLatest.getChurnValueFine() + relatedFileDiff.getNumberOfAdds() + relatedFileDiff.getNumberOfRemoves());
				}
				actualCommitFile.addContributors(fileDataLatest.getContributors());
				actualCommitFile.addContributor(new ContributorDate(commitData.getDeveloper(), commitData.getDate()));
				actualCommitFile.addModificationDates(fileDataLatest.getModificationDates());
				actualCommitFile.addModificationDate(commitData.getDate());
				actualCommitFile.setFinished(developerFocusInformation);
			}
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
	
	public static int extractChurnValue(List<String> atAtDiffs) {
		int result = 0;
		for (String atAtDiff : atAtDiffs) {
			String[] atAtDiffSplitted = atAtDiff.split(" ");
			String atAtDiffSplittedRemove = atAtDiffSplitted[1].trim();
			if (atAtDiffSplittedRemove.contains(",")) {
				atAtDiffSplittedRemove = atAtDiffSplitted[1].split(",")[1];
			}
			// Remark: the remove part is sometimes just one number,
			// and in some cases it means the number of removed lines,
			// and in other cases just the line number.
			// Therefore this calculation is not absolutely perfect.
			String atAtDiffSplittedAdd = atAtDiffSplitted[2].trim();
			if (atAtDiffSplittedAdd.contains(",")) {
				atAtDiffSplittedAdd = atAtDiffSplitted[2].split(",")[1];
			}
			result += Math.abs(Integer.parseInt(atAtDiffSplittedRemove));
			result += Integer.parseInt(atAtDiffSplittedAdd);
		}
		return result;
	}

	/**
	 * Performs the filtering mechanism based on revision.
 	 */
	protected static List<CommitData> filterCommitDataList(
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
