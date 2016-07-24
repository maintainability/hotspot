package hotareadetector.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents information parsed from source control diff command.
 * An instance is related to diff information of one file, one commit.
 */
public class FileDiffInformation {
	/**
	 * The name of the file (with full path).
	 */
	private String fileName = "";
	
	/**
	 * These are the "@@ -a,b +c,d @@" entries.
	 */
	private List<String> atAtDiffs = new ArrayList<String>();
	
	/**
	 * Number of + signs, as first character.
	 */
	private int numberOfAdds = 0;
	
	/**
	 * Number of - signs, as first character.
	 */
	private int numberOfRemoves = 0;
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public List<String> getAtAtDiffs() {
		return atAtDiffs;
	}

	public void setAtAtDiffs(List<String> atAtDiffs) {
		this.atAtDiffs = atAtDiffs;
	}
	
	public void addAtAtDiff(String atAtDiff) {
		atAtDiffs.add(atAtDiff);
	}

	public int getNumberOfAdds() {
		return numberOfAdds;
	}

	public void setNumberOfAdds(int numberOfAdds) {
		this.numberOfAdds = numberOfAdds;
	}
	
	public void increaseNumberOfAdds() {
		this.numberOfAdds++;
	}

	public int getNumberOfRemoves() {
		return numberOfRemoves;
	}

	public void setNumberOfRemoves(int numberOfRemoves) {
		this.numberOfRemoves = numberOfRemoves;
	}

	public void increaseNumberOfRemoves() {
		this.numberOfRemoves++;
	}

	@Override
	public String toString() {
		return "FileDiffInformation [fileName=" + fileName + ", atAtDiffs=" + atAtDiffs + ", numberOfAdds=" + numberOfAdds + ", numberOfRemoves=" + numberOfRemoves + "]";
	}

}
