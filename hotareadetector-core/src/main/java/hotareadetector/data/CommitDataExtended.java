package hotareadetector.data;

/**
 * Commit data extended with churn information.
 */
public class CommitDataExtended {
	private CommitData commitData;
	private Integer churn;
	
	public CommitDataExtended() {
		this(null, null);
	}
	
	public CommitDataExtended(CommitData commitData, Integer churn) {
		this.commitData = commitData;
		this.churn = churn;
	}
	
	public CommitData getCommitData() {
		return commitData;
	}
	
	public void setCommitData(CommitData commitData) {
		this.commitData = commitData;
	}
	
	public Integer getChurn() {
		return churn;
	}
	
	public void setChurn(Integer churn) {
		this.churn = churn;
	}

	@Override
	public String toString() {
		return "CommitDataExtended [commitData=" + commitData + ", churn=" + churn + "]";
	}
}
