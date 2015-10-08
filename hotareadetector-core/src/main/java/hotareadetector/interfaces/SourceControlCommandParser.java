package hotareadetector.interfaces;

import hotareadetector.data.CommitData;
import hotareadetector.data.FileDiffInformation;
import hotareadetector.data.SourceControlResultData;

import java.io.IOException;
import java.util.List;

/**
 * Functions of parsing the result of source control queries.
 */
public interface SourceControlCommandParser {
	/**
	 * Parsing the source control log command.
	 */
	List<CommitData> parseSourceControlLog(SourceControlResultData log) throws IOException;
	
	/**
	 * Parsing the source control diff command.
	 */
	List<FileDiffInformation> parseSourceControlDiff(SourceControlResultData diff) throws IOException;
}
