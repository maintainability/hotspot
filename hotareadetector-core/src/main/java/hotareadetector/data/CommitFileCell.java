package hotareadetector.data;

import hotareadetector.logic.SourceControlLogic;
import hotareadetector.util.Calculator;
import hotareadetector.util.DeveloperFocusUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * One entry within the commit-file matrix. These are calculated values.
 */
public class CommitFileCell {
	// once it has been set to finished, it cannot be modified anymore
	private boolean finished = false;
	
	private int revision = 0;
	
	private OperationType latestOperation;
	
	private String fileName = "";
	
	private List<ContributorDate> contributors = new ArrayList<ContributorDate>();
	
	private int numberOfContributors = -1;
	private int numberOfContributorsToleranceOne = -1;
	private int numberOfContributorsToleranceTwo = -1;
	
	private double focusWeightedContributors = -1.0;

	private int numberOfModifications = 0;
	
	// calculated from @@ ... @@ values	
	private int churnValueCoarse = 0;
	
	// calculated from + and - signs
	private int churnValueFine = 0;
	
	private List<Date> modificationDates = new ArrayList<Date>();
	private Date dateAdded = null;
	private Date dateLastModified = null;
	private Date dateAverage = null;

	/**
	 * Calculates the number of different contributors with 0, 1 and 2 tolerances and the focus weighted contributors,
	 * updates the modification dates and sets the object finished.
	 */
	public void setFinished(DeveloperFocusInformation developerFocusInformation) {
		Date actualCommitDate = modificationDates.get(modificationDates.size() - 1);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(actualCommitDate);
		calendar.add(Calendar.MONTH, -3);
		Date threeMonthsBefore = calendar.getTime();
		Set<String> contributorsSet = new HashSet<String>();
		List<String> contributorsToleranceOne = new ArrayList<String>();
		List<String> contributedFilesLast90Days = new ArrayList<String>();
		for (ContributorDate contributor : contributors) {
			contributorsSet.add(contributor.getContributor());
			contributorsToleranceOne.add(contributor.getContributor());
			if (developerFocusInformation != null && contributor.getDate().after(threeMonthsBefore)) {
				contributedFilesLast90Days.addAll(developerFocusInformation.getModifiedFilesPerDeveloperAfter(contributor.getContributor(), threeMonthsBefore));
			}
		}
		focusWeightedContributors = DeveloperFocusUtil.calculateFocusValue(contributedFilesLast90Days);
		
		numberOfContributors = contributorsSet.size();		
		for (String contributor : contributorsSet) {
			contributorsToleranceOne.remove(contributor);
		}
		Set<String> contributorsToleranceOneSet = new HashSet<String>();
		contributorsToleranceOneSet.addAll(contributorsToleranceOne);
		numberOfContributorsToleranceOne = contributorsToleranceOneSet.size();
		
		List<String> contributorsToleranceTwo = new ArrayList<String>();
		contributorsToleranceTwo.addAll(contributorsToleranceOne);
		for (String contributor : contributorsToleranceOneSet) {
			contributorsToleranceTwo.remove(contributor);
		}
		Set<String> contributorsToleranceTwoSet = new HashSet<String>();
		contributorsToleranceTwoSet.addAll(contributorsToleranceTwo);
		numberOfContributorsToleranceTwo = contributorsToleranceTwoSet.size();
		
		int modificationDatesSize = modificationDates.size();
		if (modificationDatesSize > 0) {
			dateAdded = modificationDates.get(0);
			dateLastModified = modificationDates.get(modificationDatesSize - 1);
			// The last but first date is considered if available.
			// Reason: in the most cases the last action before release is a directory rename of the complete directory.
			// Therefore the real last modification will be the same for all files.
			// Therefore the last but first date is more relevant.
			if (modificationDatesSize > 1) {
				dateLastModified = modificationDates.get(modificationDatesSize - 2);
			}
			dateAverage = Calculator.calculateAverage(modificationDates);
		}
		
		finished = true;
	}

	public int getRevision() {
		return revision;
	}
	
	public void setRevision(int revision) {
		assert(!finished);
		this.revision = revision;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		assert(!finished);
		this.fileName = fileName;
	}
	
	public int getNumberOfModifications() {
		return numberOfModifications;
	}
	
	public void setNumberOfModifications(int numberOfModifications) {
		assert(!finished);
		this.numberOfModifications = numberOfModifications;
	}
	
	public int getChurnValueCoarse() {
		return churnValueCoarse;
	}
	
	public void setChurnValueCoarse(int churnValueCoarse) {
		assert(!finished);
		this.churnValueCoarse = churnValueCoarse;
	}
	
	public int getChurnValueFine() {
		return churnValueFine;
	}
	
	public void setChurnValueFine(int churnValueFine) {
		assert(!finished);
		this.churnValueFine = churnValueFine;
	}
	
	public List<ContributorDate> getContributors() {
		List<ContributorDate> contributorsLocal = new ArrayList<ContributorDate>();
		contributorsLocal.addAll(contributors);
		return contributorsLocal;
	}

	public void addContributor(ContributorDate contributor) {
		assert(!finished);
		contributors.add(contributor);
	}

	public void addContributors(List<ContributorDate> contributorsParam) {
		assert(!finished);
		contributors.addAll(contributorsParam);
	}

	public int getNumberOfContributors() {
		assert(finished);
		return numberOfContributors;
	}
	
	public int getNumberOfContributorsToleranceOne() {
		assert(finished);
		return numberOfContributorsToleranceOne;
	}

	public int getNumberOfContributorsToleranceTwo() {
		assert(finished);
		return numberOfContributorsToleranceTwo;
	}

	public OperationType getLatestOperation() {
		return latestOperation;
	}

	public void setLatestOperation(OperationType latestOperation) {
		assert(!finished);
		this.latestOperation = latestOperation;
	}
	
	public void addModificationDate(Date modificationDate) {
		assert(!finished);
		modificationDates.add(modificationDate);
	}
	
	public void addModificationDates(List<Date> modificationDatesParam) {
		assert(!finished);
		modificationDates.addAll(modificationDatesParam);
	}
	
	public Date getDateAdded() {
		return dateAdded;
	}
	
	public Date getDateLastModified() {
		return dateLastModified;
	}
	
	public Date getDateAverage() {
		return dateAverage;
	}
	
	public List<Date> getModificationDates() {
		List<Date> modificationDatesLocal = new ArrayList<Date>();
		modificationDatesLocal.addAll(modificationDates);
		return modificationDatesLocal;
	}

	/**
	 * Creates a clone about the current commit file cell, with modifications necessary for rename.
	 */
	public CommitFileCell cloneRenamed(String newFileName, String contributor, Date date, int newRevision, FileDiffInformation relatedFileDiff, DeveloperFocusInformation developerFocusInformation) {
		CommitFileCell clone = new CommitFileCell();
		clone.churnValueCoarse = churnValueCoarse;
		clone.churnValueFine = churnValueFine;
		if (relatedFileDiff != null) {
			clone.churnValueCoarse += SourceControlLogic.extractChurnValue(relatedFileDiff.getAtAtDiffs());
			clone.churnValueFine += relatedFileDiff.getNumberOfAdds() + relatedFileDiff.getNumberOfRemoves();
		}
		clone.contributors = new ArrayList<ContributorDate>();
		clone.contributors.addAll(contributors);
		clone.contributors.add(new ContributorDate(contributor, date));
		clone.fileName = newFileName;
		clone.finished = false;
		clone.latestOperation = OperationType.R;
		clone.numberOfModifications = numberOfModifications + 1;
		clone.modificationDates = new ArrayList<Date>();
		clone.modificationDates.addAll(modificationDates);
		clone.modificationDates.add(date);
		clone.revision = newRevision;
		clone.setFinished(developerFocusInformation);
		return clone;
	}

}
