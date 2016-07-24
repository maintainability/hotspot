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
 * Mock file for source control command executor, returning predefined results.
 */
public class SourceControlCommandExecutorMock implements SourceControlCommandExecutor {
	
	private int numberOfDiffInvocations = 0;

	@Override
	public void setAccess(
			HotAreaDetectorContext context,
			SourceControlInformation sourceControl, 
			Credentials credentials) {
	}

	@Override
	public SourceControlResultData executeSourceControlLogCommand() throws IOException {
		SourceControlResultData testSvnLogResult = mock(SourceControlResultData.class);
		when(testSvnLogResult.getResultBufferedReader()).thenReturn(new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("svnlog.txt"))));
		return testSvnLogResult;
	}

	@Override
	public SourceControlResultData executeSourceControlDiffCommand(
			String revision1,
			String revision2) throws IOException {
		numberOfDiffInvocations++;
		String fileName = "svndiff_" + revision1 + "_" + revision2 + ".txt";
		SourceControlResultData testSvnDiffResult = mock(SourceControlResultData.class);
		when(testSvnDiffResult.getResultBufferedReader()).thenReturn(new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(fileName))));
		return testSvnDiffResult;
	}

	public int getNumberOfDiffInvocations() {
		return numberOfDiffInvocations;
	}

}
