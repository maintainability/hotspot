package hotareadetector.data;

/**
 * File related commit data, parsed from the commit log.
 * One instance represents one file within a commit.
 */
public class CommitedFileData implements Comparable<CommitedFileData> {
	private OperationType operationType;
	private String fileName;
	private String fromFileName = null;
	
	public CommitedFileData() {}
	
	public CommitedFileData(OperationType operationType, String fileName, String fromFileName) {
		this.operationType = operationType;
		this.fileName = fileName;
		this.fromFileName = fromFileName;
	}

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

	@Override
	public String toString() {
		return "CommitedFileData [operationType=" + operationType + ", fileName=" + fileName + ", fromFileName=" + fromFileName + "]";
	}

	/**
	 * Operations with delete should come last to avoid the problem with the following case:
	 * - delete /directory/
	 * - rename /directory/subdir/ to /otherdirectory/subdir/
	 */
	@Override
	public int compareTo(CommitedFileData otherCommitFileData) {
		assert(otherCommitFileData != null);
		assert(operationType != null);
		assert(otherCommitFileData.getOperationType() != null);
		if (operationType == OperationType.D) {
			if (otherCommitFileData.getOperationType() == OperationType.D) {
				return 0;
			} else {
				return 1;
			}
		} else {
			if (otherCommitFileData.getOperationType() == OperationType.D) {
				return -1;
			} else {
				return 0;
			}
		}
	}
}
