package hotareadetector.svn;

import hotareadetector.data.CommitData;
import hotareadetector.data.CommitedFileData;
import hotareadetector.data.FileDiffInformation;
import hotareadetector.data.OperationType;
import hotareadetector.data.SourceControlResultData;
import hotareadetector.interfaces.SourceControlCommandParser;
import hotareadetector.util.DateUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * SVN implementation of the source control parser.
 */
public class SvnCommandParser implements SourceControlCommandParser{
	enum SvnLogPhase {
		NEW_COMMIT,
		MAIN_INFO,
		CHANGED_PATH,
		AFFECTED_FILES,
		COMMENT
	}
	
	private static String newCommitSpearator = "------------------------------------------------------------------------";
	private static String changedPaths = "Changed paths:";
	
	@Override
	public List<CommitData> parseSourceControlLog(SourceControlResultData svnLogResult) throws IOException {
		List<CommitData> sourceControlData = new LinkedList<CommitData>();
		SvnLogPhase nextPhase = SvnLogPhase.NEW_COMMIT;
		StringBuilder comment = new StringBuilder("");
		int commentLengthRemaining = 0;
		CommitData commitData = null;
		String svnLogLine = null;
		BufferedReader svnLogResultBufferedReader = svnLogResult.getResultBufferedReader();
		while ((svnLogLine = svnLogResultBufferedReader.readLine()) != null) {
			switch (nextPhase) {
			case NEW_COMMIT:
				if (newCommitSpearator.equals(svnLogLine)) {
					nextPhase = SvnLogPhase.MAIN_INFO;
					commitData = new CommitData();
				} else {
					System.out.println("Commit separator expected, but the following line found: " + svnLogLine);
				}
				break;
				
			case MAIN_INFO:
				String[] splitted = svnLogLine.split("\\|");
				if (splitted.length != 4) {
					System.out.println("Wrong format of main information line (it should consist of 4 parts): " + svnLogLine);
					break;
				}
				commitData = new CommitData();
				sourceControlData.add(0, commitData);
				commitData.setRevisionNumber(Integer.parseInt(splitted[0].trim().substring(1)));
				commitData.setContributor(splitted[1].trim());
				commitData.setDate(DateUtil.convertToDate(splitted[2]));
				String commentLengthStr = splitted[3].split(" ")[1].trim();
				commentLengthRemaining = Integer.parseInt(commentLengthStr);
				nextPhase = SvnLogPhase.CHANGED_PATH;
				break;
				
			case CHANGED_PATH:
				if (changedPaths.equals(svnLogLine)) {
					nextPhase = SvnLogPhase.AFFECTED_FILES;
				} else {
					System.out.println("'" + changedPaths + "' was expected, but the following line found: " + svnLogLine);
				}
				break;
				
			case AFFECTED_FILES:
				if ("".equals(svnLogLine)) {
					nextPhase = SvnLogPhase.COMMENT;
				} else {
					String trimmed = svnLogLine.trim();
					CommitedFileData commitedFile = new CommitedFileData();
					commitedFile.setOperationType(OperationType.valueOf(trimmed.substring(0, 1)));
					String fileNamePart = trimmed.substring(2);
					if (fileNamePart.matches(".* \\(from .*:.*\\)")) {
						splitted = trimmed.split(" \\(from ");
						commitedFile.setFileName(splitted[0].trim().substring(2));
						commitedFile.setFromFileName(splitted[1].split(":")[0]);
					} else {
						commitedFile.setFileName(trimmed.substring(2));
					}
					commitData.addCommitedFile(commitedFile);
				}
				break;
				
			case COMMENT:
				if (commentLengthRemaining > 0) {
					comment.append(svnLogLine).append("\n");
					commentLengthRemaining--;
				} 
				if (commentLengthRemaining == 0) {
					commitData.setComment(comment.toString());
					comment = new StringBuilder("");
					nextPhase = SvnLogPhase.NEW_COMMIT;
				}
				break;
			}
		}
		Collections.sort(sourceControlData);
		return sourceControlData;
	}

	enum SvnDiffPhase {
		NEW_DIFF,
		INDEX,
		MODIFICATIONS
	}
	
	
	@Override
	public List<FileDiffInformation> parseSourceControlDiff(SourceControlResultData svnDiffResult) throws IOException {
		final String indexStr = "Index: ";
		List<FileDiffInformation> result = new ArrayList<FileDiffInformation>();
		SvnDiffPhase nextPhase = SvnDiffPhase.NEW_DIFF;
		FileDiffInformation actualFileData = null;
		String svnDiffLine = null;
		BufferedReader svnDiffResultBufferedReader = svnDiffResult.getResultBufferedReader();
		while ((svnDiffLine = svnDiffResultBufferedReader.readLine()) != null) {
			switch (nextPhase) {
			case NEW_DIFF:
			case MODIFICATIONS:
				char firstCharacter = ' ';
				if (!"".equals(svnDiffLine)) {
					firstCharacter = svnDiffLine.charAt(0);
				}
				switch (firstCharacter) {
				case 'I':
					actualFileData = new FileDiffInformation();
					// In the log the file path starts with '/', and in case of diff there is no starting '/'.
					// The '/' is added here for consistency.
					actualFileData.setFileName("/" + svnDiffLine.substring(indexStr.length()));
					result.add(actualFileData);
					nextPhase = SvnDiffPhase.INDEX;
					break;
					
				case '+':
					actualFileData.increaseNumberOfAdds();
					break;
					
				case '-':
					actualFileData.increaseNumberOfRemoves();
					break;
					
				case '@':
					actualFileData.addAtAtDiff(svnDiffLine);
					break;
				}
				break;
				
			case INDEX:
				if (svnDiffLine.startsWith("+++ ")) {
					nextPhase = SvnDiffPhase.MODIFICATIONS;
				}
				break;
			}
			
		}
		return result;
	}

}
