package hotareadetector.data;

/**
 * File related commit data, parsed from the commit log.
 * One instance represent s one file within a commit.
 */
public class CommitedFileData {
	private OperationType operationType;
	private String fileName;
	private String fromFileName = null;
	
	public OperationType getOperationType() {
		return operationType;
	}
	
	public void setOperationType(OperationType operationType) {
		this.operationType = operationType;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getFromFileName() {
		return fromFileName;
	}
	
	public void setFromFileName(String fromFileName) {
		this.fromFileName = fromFileName;
	}

}
