package hotareadetector.data;

import java.util.Date;

public class VersionControlHistoryMetrics {
	private Integer numberOfModifications = null;
	private Integer churnValue = null;
	
	private Integer numberOfContributors = null;
	private Integer numberOfContributorsToleranceOne = null;
	private Integer numberOfContributorsToleranceTwo = null;
	private Double focusWeightedContributors = null;

	private Date creatingDate = null;
	private Date lastModificationDate = null;
	private Date modificationDatesAverage = null;
	
	public Integer getNumberOfModifications() {
		return numberOfModifications;
	}
	
	public void setNumberOfModifications(Integer numberOfModifications) {
		this.numberOfModifications = numberOfModifications;
	}
	
	public Integer getChurnValue() {
		return churnValue;
	}
	
	public void setChurnValue(Integer churnValue) {
		this.churnValue = churnValue;
	}
	
	public Integer getNumberOfContributors() {
		return numberOfContributors;
	}
	
	public void setNumberOfContributors(Integer numberOfContributors) {
		this.numberOfContributors = numberOfContributors;
	}
	
	public Integer getNumberOfContributorsToleranceOne() {
		return numberOfContributorsToleranceOne;
	}
	
	public void setNumberOfContributorsToleranceOne(Integer numberOfContributorsToleranceOne) {
		this.numberOfContributorsToleranceOne = numberOfContributorsToleranceOne;
	}
	
	public Integer getNumberOfContributorsToleranceTwo() {
		return numberOfContributorsToleranceTwo;
	}
	
	public void setNumberOfContributorsToleranceTwo(Integer numberOfContributorsToleranceTwo) {
		this.numberOfContributorsToleranceTwo = numberOfContributorsToleranceTwo;
	}
	
	public Double getFocusWeightedContributors() {
		return focusWeightedContributors;
	}
	
	public void setFocusWeightedContributors(Double focusWeightedContributors) {
		this.focusWeightedContributors = focusWeightedContributors;
	}
	
	public Date getCreatingDate() {
		return creatingDate;
	}
	
	public void setCreatingDate(Date creatingDate) {
		this.creatingDate = creatingDate;
	}
	
	public Date getLastModificationDate() {
		return lastModificationDate;
	}
	
	public void setLastModificationDate(Date lastModificationDate) {
		this.lastModificationDate = lastModificationDate;
	}
	
	public Date getModificationDatesAverage() {
		return modificationDatesAverage;
	}
	
	public void setModificationDatesAverage(Date modificationDatesAverage) {
		this.modificationDatesAverage = modificationDatesAverage;
	}
	
	@Override
	public String toString() {
		return "VersionControlHistoryMetrics [numberOfModifications=" + numberOfModifications + ", churnValue="
				+ churnValue + ", numberOfContributors=" + numberOfContributors + ", numberOfContributorsToleranceOne="
				+ numberOfContributorsToleranceOne + ", numberOfContributorsToleranceTwo="
				+ numberOfContributorsToleranceTwo + ", focusWeightedContributors=" + focusWeightedContributors
				+ ", creatingDate=" + creatingDate + ", lastModificationDate=" + lastModificationDate
				+ ", modificationDatesAverage=" + modificationDatesAverage + "]";
	}

}
