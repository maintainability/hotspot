package hotareadetector.interfaces;

import hotareadetector.data.Credentials;
import hotareadetector.data.HotAreaDetectorContext;
import hotareadetector.data.SourceControlInformation;
import hotareadetector.data.SourceControlResultData;

import java.io.IOException;

/**
 * Functions for executing source control functions.
 */
public interface SourceControlCommandExecutor {
	/**
	 * Set up accessing the source control.
	 */
	void setAccess(HotAreaDetectorContext context, SourceControlInformation sourceControl, Credentials credentials);
	
	/**
	 * Execute source control specific log command.
	 */
	SourceControlResultData executeSourceControlLogCommand() throws IOException;
	
	/**
	 * Execute source control specific diff command.
	 */
	SourceControlResultData executeSourceControlDiffCommand(String revision1, String revision2) throws IOException;
}
