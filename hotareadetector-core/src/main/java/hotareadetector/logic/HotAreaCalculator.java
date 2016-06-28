package hotareadetector.logic;

import hotareadetector.data.AnalysisType;
import hotareadetector.data.CommitFileCell;
import hotareadetector.data.CommitFileMatrix;
import hotareadetector.data.HotNumber;
import hotareadetector.util.Calculator;

import java.io.IOException;
import java.io.PrintWriter;
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
	List<Double> focusWeightedContributors = new ArrayList<Double>();
	
	List<Integer> numberOfModifications = new ArrayList<Integer>();
	List<Integer> churnValuesCoarse = new ArrayList<Integer>();
	List<Integer> churnValuesFine = new ArrayList<Integer>();
	
	List<Date> addedDates = new ArrayList<Date>();
	List<Date> lastModifiedDates = new ArrayList<Date>();
	List<Date> averageDates = new ArrayList<Date>();
	
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
			focusWeightedContributors.add(fileData.getFocusWeightedContributors());
			numberOfModifications.add(fileData.getNumberOfModifications());
			if (commitFileMatrix.isDeepAnalysis()) {
				churnValuesCoarse.add(fileData.getChurnValueCoarse());
				churnValuesFine.add(fileData.getChurnValueFine());
			}
			addedDates.add(fileData.getDateAdded());
			lastModifiedDates.add(fileData.getDateLastModified());
			averageDates.add(fileData.getDateAverage());
		}
		Collections.sort(ownershipValues);
		Collections.sort(ownershipValuesToleranceOne);
		Collections.sort(ownershipValuesToleranceTwo);
		Collections.sort(focusWeightedContributors);
		Collections.sort(numberOfModifications);
		Collections.sort(churnValuesCoarse);
		Collections.sort(churnValuesFine);
		Collections.sort(addedDates);
		Collections.sort(lastModifiedDates);
		Collections.sort(averageDates);
		
		if (!ownershipValues.isEmpty()) {
			int maxOwnershipValuePlusOne = ownershipValues.get(ownershipValues.size() - 1) + 1;
			for (CommitFileCell fileData : fileDataListOfRevisionTemp) {
				combinedValues.add(fileData.getNumberOfContributors() + maxOwnershipValuePlusOne * fileData.getNumberOfModifications());
			}
			Collections.sort(combinedValues);
		}
	}
	
	/**
	 * Saves metrics to file.
	 */
	public void saveMetrics(String fileNamePrefix) throws IOException {
		PrintWriter writerChurn = new PrintWriter(fileNamePrefix + "-churn.csv", "UTF-8");
		PrintWriter writerChurnCoarse = new PrintWriter(fileNamePrefix + "-churncoarse.csv", "UTF-8");
		PrintWriter writerModifications = new PrintWriter(fileNamePrefix + "-modifications.csv", "UTF-8");
		PrintWriter writerOwnership = new PrintWriter(fileNamePrefix + "-ownership.csv", "UTF-8");
		PrintWriter writerOwnership1 = new PrintWriter(fileNamePrefix + "-ownership1.csv", "UTF-8");
		PrintWriter writerOwnership2 = new PrintWriter(fileNamePrefix + "-ownership2.csv", "UTF-8");
		PrintWriter writerFocus = new PrintWriter(fileNamePrefix + "-focus.csv", "UTF-8");
		PrintWriter writerDateAdded = new PrintWriter(fileNamePrefix + "-dateadded.csv", "UTF-8");
		PrintWriter writerDateAverage = new PrintWriter(fileNamePrefix + "-dateaverage.csv", "UTF-8");
		PrintWriter writerDateModified = new PrintWriter(fileNamePrefix + "-datemodified.csv", "UTF-8");
		
		
		writerChurn.println("Name;Churn");
		writerChurnCoarse.println("Name;Churncoarse");
		writerModifications.println("Name;Modifications");
		writerOwnership.println("Name;Ownership");
		writerOwnership1.println("Name;Ownership1");
		writerOwnership2.println("Name;Ownership2");
		writerFocus.println("Name;Focus");
		writerDateAdded.println("Name;DateAdded");
		writerDateAverage.println("Name;DateAverage");
		writerDateModified.println("Name;DateModified");

		for (CommitFileCell fileData : fileDataListOfRevision) {
			writerChurn.println(fileData.getFileName() + ";" + fileData.getChurnValueFine());
			writerChurnCoarse.println(fileData.getFileName() + ";" + fileData.getChurnValueCoarse());
			writerModifications.println(fileData.getFileName() + ";" + fileData.getNumberOfModifications());
			writerOwnership.println(fileData.getFileName() + ";" + fileData.getNumberOfContributors());
			writerOwnership1.println(fileData.getFileName() + ";" + fileData.getNumberOfContributorsToleranceOne());
			writerOwnership2.println(fileData.getFileName() + ";" + fileData.getNumberOfContributorsToleranceTwo());
			writerFocus.println(fileData.getFileName() + ";" + fileData.getFocusWeightedContributors());
			writerDateAdded.println(fileData.getFileName() + ";" + fileData.getDateAdded());
			writerDateAverage.println(fileData.getFileName() + ";" + fileData.getDateAverage());
			writerDateModified.println(fileData.getFileName() + ";" + fileData.getDateLastModified());
		}
		
		writerChurn.close();
		writerChurnCoarse.close();
		writerModifications.close();
		writerOwnership.close();
		writerOwnership1.close();
		writerOwnership2.close();
		writerFocus.close();
		writerDateAdded.close();
		writerDateAverage.close();
		writerDateModified.close();
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
			result = Calculator.calculateDistributionValue(churnValuesFine, commitFileCell.getChurnValueFine());
			break;
			
		case MODIFICATION:
			result = Calculator.calculateDistributionValue(numberOfModifications, commitFileCell.getNumberOfModifications());
			break;
			
		case OWNERSHIP:
			result = Calculator.calculateDistributionValue(ownershipValues, commitFileCell.getNumberOfContributors());
			break;
			
		case OWNERSHIP_TOLERANCE1:
			result = Calculator.calculateDistributionValue(ownershipValuesToleranceOne, commitFileCell.getNumberOfContributorsToleranceOne());
			break;
			
		case OWNERSHIP_TOLERANCE2:
			result = Calculator.calculateDistributionValue(ownershipValuesToleranceTwo, commitFileCell.getNumberOfContributorsToleranceTwo());
			break;
			
		case OWNERSHIP_FOCUS:
			result = Calculator.calculateDistributionValue(focusWeightedContributors, commitFileCell.getFocusWeightedContributors());
			break;
			
		case DATEADDED:
			result = Calculator.calculateDistributionValue(addedDates, commitFileCell.getDateAdded());
			break;
			
		case DATEMODIFIED:
			result = Calculator.calculateDistributionValue(lastModifiedDates, commitFileCell.getDateLastModified());
			break;
			
		case DATEAVERAGE:
			result = Calculator.calculateDistributionValue(averageDates, commitFileCell.getDateAverage());
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
			churnDistributionValues[1] = Calculator.calculateDistributionValue(churnValuesCoarse, commitFileCell.getChurnValueCoarse());
			churnDistributionValues[2] = Calculator.calculateDistributionValue(churnValuesFine, commitFileCell.getChurnValueFine());
			aggregatedDistributionValues[1] = Calculator.calculateAverage(churnDistributionValues);
			
			result = Calculator.calculateAverage(aggregatedDistributionValues);
			break;
			
		case NONE:
			break;
		}
		
		return result;
	}

}
