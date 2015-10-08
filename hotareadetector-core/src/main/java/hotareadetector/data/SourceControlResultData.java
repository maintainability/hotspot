package hotareadetector.data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Result of a source control command execution.
 */
public class SourceControlResultData {
	private String executedCommand;
	private int exitValue;
	private String resultFileName;
	private List<String> errorStrList = null;
	
	public SourceControlResultData(
			String executedCommand, 
			int exitValue, 
			String resultFileName, 
			List<String> errorStrList) {
		this.executedCommand = executedCommand;
		this.exitValue = exitValue;
		this.resultFileName = resultFileName;
		this.errorStrList = errorStrList;
	}

	public SourceControlResultData(String resultFileName) {
		this("", 0, resultFileName, new ArrayList<String>());
	}

	public String getExecutedCommand() {
		return executedCommand;
	}

	public int getExitValue() {
		return exitValue;
	}

	public String getResultFileName() {
		return resultFileName;
	}
	
	public BufferedReader getResultBufferedReader() {
		try {
			return new BufferedReader(new FileReader(resultFileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<String> getErrorStrList() {
		return errorStrList;
	}
}
