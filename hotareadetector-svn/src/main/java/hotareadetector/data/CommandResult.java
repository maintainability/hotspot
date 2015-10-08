package hotareadetector.data;

import java.util.List;

/**
 * Structure for storing result of an svn command execution.
 */
public class CommandResult {
	private int exitValue;
	private List<String> outputStrList;
	private List<String> errorStrList;
	
	public CommandResult(int exitValue, List<String> outputStrList, List<String> errorStrList) {
		this.exitValue = exitValue;
		this.outputStrList = outputStrList;
		this.errorStrList = errorStrList;
	}
	
	public int getExitValue() {
		return exitValue;
	}
	
	public void setExitValue(int exitValue) {
		this.exitValue = exitValue;
	}
	
	public List<String> getOutputStrList() {
		return outputStrList;
	}
	
	public void setOutputStrList(List<String> outputStrList) {
		this.outputStrList = outputStrList;
	}
	
	public List<String> getErrorStrList() {
		return errorStrList;
	}
	
	public void setErrorStrList(List<String> errorStrList) {
		this.errorStrList = errorStrList;
	}
}
