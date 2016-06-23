package hotareadetector.mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import hotareadetector.data.Credentials;
import hotareadetector.data.HotAreaDetectorContext;
import hotareadetector.data.SourceControlInformation;
import hotareadetector.data.SourceControlResultData;
import hotareadetector.interfaces.SourceControlCommandExecutor;

/**
 * Mock read svn log for developer focus test.
 *
 */
public class SourceControlCommandExecutorDeveloperFocusMock implements SourceControlCommandExecutor {
	@Override
	public void setAccess(
			HotAreaDetectorContext context,
			SourceControlInformation sourceControl, 
			Credentials credentials) {
	}

	@Override
	public SourceControlResultData executeSourceControlLogCommand() throws IOException {
		SourceControlResultData testSvnLogResult = mock(SourceControlResultData.class);
		when(testSvnLogResult.getResultBufferedReader()).thenReturn(new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("svnlog_developerfocus.txt"))));
		return testSvnLogResult;
	}

	@Override
	public SourceControlResultData executeSourceControlDiffCommand(
			String revision1,
			String revision2) throws IOException {
		// this should never be called
		return null;
	}
}
