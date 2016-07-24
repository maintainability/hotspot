package hotareadetector.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Main data of a commit, parsed from the commit log.
 * One instance is related to one commit.
 */
public class CommitData implements Comparable<CommitData> {
	private int revisionNumber;
	private String contributor;
	private Date date = new Date();
	private String comment;
	private List<CommitedFileData> commitedFiles = new ArrayList<CommitedFileData>();
	
	public CommitData() {}
	
	public CommitData(int revisionNumber, String contributor, Date date, String comment, List<CommitedFileData> commitedFiles) {
		this.revisionNumber = revisionNumber;
		this.contributor = contributor;
		this.date = date;
		this.comment = comment;
		this.commitedFiles = commitedFiles;
	}

	public int getRevisionNumber() {
		return revisionNumber;
	}
	
	public void setRevisionNumber(int revisionNumber) {
		this.revisionNumber = revisionNumber;
	}
	
	public String getContributor() {
		return contributor;
	}
	
	public void setContributor(String contributor) {
		this.contributor = contributor;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}

	public List<CommitedFileData> getCommitedFiles() {
		return commitedFiles;
	}

	public void setCommitedFiles(List<CommitedFileData> commitedFiles) {
		this.commitedFiles = commitedFiles;
	}

	public void addCommitedFile(CommitedFileData commitedFile) {
		this.commitedFiles.add(commitedFile);
	}

	@Override
	public int compareTo(CommitData commitData) {
		return Integer.valueOf(revisionNumber).compareTo(Integer.valueOf(commitData.getRevisionNumber()));
	}
	
	@Override
	public String toString() {
		return "CommitData [revisionNumber=" + revisionNumber + ", contributor=" + contributor + ", date=" + date
				+ ", comment=" + comment + ", commitedFiles=" + commitedFiles + "]";
	}
}
