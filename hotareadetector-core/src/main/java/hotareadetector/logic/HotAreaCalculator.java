package hotareadetector.logic;

import hotareadetector.data.AnalysisType;
import hotareadetector.data.CommitFileCell;
import hotareadetector.data.CommitFileMatrix;
import hotareadetector.data.HotNumber;
import hotareadetector.util.Calculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Calculation of hot areas in a given revision.
 */
public class HotAreaCalculator {
	private final CommitFileMatrix commitFileMatrix;
	private final List<CommitFileCell> fileDataListOfRevision;
	// package private attributes because they are used at unit testing
	List<Integer> ownershipValues = new ArrayList<Integer>();
	List<Integer> ownershipValuesToleranceOne = new ArrayList<Integer>();
	List<Integer> ownershipValuesToleranceTwo = new ArrayList<Integer>();
	
	List<Integer> numberOfModifications = new ArrayList<Integer>();
	List<Integer> churnValues = new ArrayList<Integer>();
	List<Integer> churnValuesFiner = new ArrayList<Integer>();
	
	List<Date> addedDates = new ArrayList<Date>();
	List<Date> lastModifiedDates = new ArrayList<Date>();
	
	List<Integer> combinedValues = new ArrayList<Integer>();
	
	final int revision;

	/**
	 * Saves commit file matrix, retrieves all files of the revision given, and performs the initialization.
	 */
	public HotAreaCalculator(CommitFileMatrix commitFileMatrix, int revision) {
		this.commitFileMatrix = commitFileMatrix;
		this.revision = revision;
		fileDataListOfRevision = new ArrayList<CommitFileCell>();
		initialize();
	}

	/**
	 * HotAreaCalculator with the latest revision.
	 */
	public HotAreaCalculator(CommitFileMatrix commitFileMatrix) {
		this(commitFileMatrix, commitFileMatrix.getLatestRevision());
	}

	/**
	 * Populates the ownership and churn values with all the data found in the revision in question, and then sorts it.
	 */
	private void initialize() {
		List<CommitFileCell> fileDataListOfRevisionTemp = commitFileMatrix.getAllFilesOfRevision(revision);
		for (CommitFileCell fileData : fileDataListOfRevisionTemp) {
			fileDataListOfRevision.add(fileData);
			ownershipValues.add(fileData.getNumberOfContributors());
			ownershipValuesToleranceOne.add(fileData.getNumberOfContributorsToleranceOne());
			ownershipValuesToleranceTwo.add(fileData.getNumberOfContributorsToleranceTwo());
			numberOfModifications.add(fileData.getNumberOfModifications());
			if (commitFileMatrix.isDeepAnalysis()) {
				churnValues.add(fileData.getChurnValue());
				churnValuesFiner.add(fileData.getChurnValueFiner());
			}
			addedDates.add(fileData.getDateAdded());
			lastModifiedDates.add(fileData.getDateLastModified());
		}
		Collections.sort(ownershipValues);
		Collections.sort(ownershipValuesToleranceOne);
		Collections.sort(ownershipValuesToleranceTwo);
		Collections.sort(numberOfModifications);
		Collections.sort(churnValues);
		Collections.sort(churnValuesFiner);
		Collections.sort(addedDates);
		Collections.sort(lastModifiedDates);
		
		if (!ownershipValues.isEmpty()) {
			int maxOwnershipValuePlusOne = ownershipValues.get(ownershipValues.size() - 1) + 1;
			for (CommitFileCell fileData : fileDataListOfRevisionTemp) {
				combinedValues.add(fileData.getNumberOfContributors() + maxOwnershipValuePlusOne * fileData.getNumberOfModifications());
			}
			Collections.sort(combinedValues);
		}
	}

	/**
	 * Calculates hot numbers of each occurring file, with specific extensions only.
	 */
	public List<HotNumber> calculateHotNumbers(AnalysisType analysisType) {
		List<HotNumber> hotNumbers = new ArrayList<HotNumber>();
		for (CommitFileCell fileData : fileDataListOfRevision) {
			hotNumbers.add(new HotNumber(fileData.getFileName(), calculateHotNumberCommitFileCell(fileData, analysisType)));
		}
		Collections.sort(hotNumbers, Collections.reverseOrder());
		return hotNumbers;
	}

	/**
	 * Calculates the distribution position of a commit file cell, considering various ownership and churn values.
	 * The relative positions are aggregated.
	 */
	protected Double calculateHotNumberCommitFileCell(CommitFileCell commitFileCell, AnalysisType analysisType) {
		Double result = null;
		
		switch (analysisType) {
		case CHURN:
			result = Calculator.calculateDistributionValue(churnValuesFiner, commitFileCell.getChurnValueFiner());
			break;
			
		case MODIFICATION:
			result = Calculator.calculateDistributionValue(numberOfModifications, commitFileCell.getNumberOfModifications());
			break;
			
		case OWNERSHIP:
			result = Calculator.calculateDistributionValue(ownershipValues, commitFileCell.getNumberOfContributors());
			break;
			
		case DATEADDED:
			result = Calculator.calculateDistributionValue(addedDates, commitFileCell.getDateAdded());
			break;
			
		case DATEMODIFIED:
			result = Calculator.calculateDistributionValue(lastModifiedDates, commitFileCell.getDateLastModified());
			break;
			
		case COMBINED:
			if (!ownershipValues.isEmpty()) {
				int maxOwnershipValuePlusOne = ownershipValues.get(ownershipValues.size() - 1) + 1;
				result = Calculator.calculateDistributionValue(combinedValues, commitFileCell.getNumberOfContributors() + maxOwnershipValuePlusOne * commitFileCell.getNumberOfModifications());
			}
			break;
			
		case FULL:
			Double[] aggregatedDistributionValues = new Double[2];
			
			Double[] ownershipDistributionValues = new Double[3];
			ownershipDistributionValues[0] = Calculator.calculateDistributionValue(ownershipValues, commitFileCell.getNumberOfContributors());
			ownershipDistributionValues[1] = Calculator.calculateDistributionValue(ownershipValuesToleranceOne, commitFileCell.getNumberOfContributorsToleranceOne());
			ownershipDistributionValues[2] = Calculator.calculateDistributionValue(ownershipValuesToleranceTwo, commitFileCell.getNumberOfContributorsToleranceTwo());
			aggregatedDistributionValues[0] = Calculator.calculateAverage(ownershipDistributionValues);
			
			Double[] churnDistributionValues = new Double[3];
			churnDistributionValues[0] = Calculator.calculateDistributionValue(numberOfModifications, commitFileCell.getNumberOfModifications());
			churnDistributionValues[1] = Calculator.calculateDistributionValue(churnValues, commitFileCell.getChurnValue());
			churnDistributionValues[2] = Calculator.calculateDistributionValue(churnValuesFiner, commitFileCell.getChurnValueFiner());
			aggregatedDistributionValues[1] = Calculator.calculateAverage(churnDistributionValues);
			
			result = Calculator.calculateAverage(aggregatedDistributionValues);
			break;
		}
		
		return result;
	}

}
