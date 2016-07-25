package hotareadetector.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import hotareadetector.data.VersionControlHistoryMetrics;
import hotareadetector.util.Calculator;

/**
 * Calculation of hot areas in a given revision.
 */
public class HotAreaCalculator {
	private Map<String, VersionControlHistoryMetrics> versionControlHistoryMetricsPerFile;

	List<Integer> numberOfModifications = new ArrayList<Integer>();
	List<Integer> churnValues = new ArrayList<Integer>();
	
	List<Integer> ownershipValues = new ArrayList<Integer>();
	List<Integer> ownershipValuesToleranceOne = new ArrayList<Integer>();
	List<Integer> ownershipValuesToleranceTwo = new ArrayList<Integer>();
	List<Double> focusWeightedOwnership = new ArrayList<Double>();
	
	List<Date> creatingDates = new ArrayList<Date>();
	List<Date> lastModificationDates = new ArrayList<Date>();
	List<Date> modificationDatesAverage = new ArrayList<Date>();
	
	List<Double> combinedValues = new ArrayList<Double>();
	
	public HotAreaCalculator(Map<String, VersionControlHistoryMetrics> versionControlHistoryMetricsPerFile) {
		this.versionControlHistoryMetricsPerFile = versionControlHistoryMetricsPerFile;
	}

	public Map<String, Map<String, Double>> calculateHotNumbers() {
		initializeStructures();
		Map<String, Map<String, Double>> hotNumbers = performHotNumberCalculation();
		return hotNumbers;
	}

	private void initializeStructures() {
		for (Entry<String, VersionControlHistoryMetrics> versionControlHistoryMetricsPerFileEntry : versionControlHistoryMetricsPerFile.entrySet()) {
			VersionControlHistoryMetrics versionControlHistoryMetrics = versionControlHistoryMetricsPerFileEntry.getValue();
			numberOfModifications.add(versionControlHistoryMetrics.getNumberOfModifications());
			if (versionControlHistoryMetrics.getChurnValue() != null) {
				churnValues.add(versionControlHistoryMetrics.getChurnValue());
			}
			ownershipValues.add(versionControlHistoryMetrics.getNumberOfContributors());
			ownershipValuesToleranceOne.add(versionControlHistoryMetrics.getNumberOfContributorsToleranceOne());
			ownershipValuesToleranceTwo.add(versionControlHistoryMetrics.getNumberOfContributorsToleranceTwo());
			focusWeightedOwnership.add(versionControlHistoryMetrics.getFocusWeightedContributors());
			creatingDates.add(versionControlHistoryMetrics.getCreatingDate());
			lastModificationDates.add(versionControlHistoryMetrics.getLastModificationDate());
			modificationDatesAverage.add(versionControlHistoryMetrics.getModificationDatesAverage());
			combinedValues.add(calculateCombinedMetric(versionControlHistoryMetrics));
		}
		Collections.sort(numberOfModifications);
		if (!churnValues.contains(null)) {
			Collections.sort(churnValues);
		}
		Collections.sort(ownershipValues);
		Collections.sort(ownershipValuesToleranceOne);
		Collections.sort(ownershipValuesToleranceTwo);
		if (!focusWeightedOwnership.contains(null)) {
			Collections.sort(focusWeightedOwnership);
		}
		Collections.sort(creatingDates);
		Collections.sort(lastModificationDates);
		Collections.sort(modificationDatesAverage);
		if (!combinedValues.contains(null)) {
			Collections.sort(combinedValues);
		}
	}
	
	protected Map<String, Map<String, Double>> performHotNumberCalculation() {
		Map<String, Map<String, Double>> hotNumbers = new HashMap<String, Map<String, Double>>();
		Map<String, Double> hotNumbersModifications = new HashMap<String, Double>();
		Map<String, Double> hotNumbersChurn = new HashMap<String, Double>();
		Map<String, Double> hotNumbersOwnership = new HashMap<String, Double>();
		Map<String, Double> hotNumbersOwnershipToleranceOne = new HashMap<String, Double>();
		Map<String, Double> hotNumbersOwnershipToleranceTwo = new HashMap<String, Double>();
		Map<String, Double> hotNumbersFocusWeightedOwnership = new HashMap<String, Double>();
		Map<String, Double> hotNumbersCreatingDates = new HashMap<String, Double>();
		Map<String, Double> hotNumbersLastModificationDates = new HashMap<String, Double>();
		Map<String, Double> hotNumbersModificationDateAverage = new HashMap<String, Double>();
		Map<String, Double> hotNumbersCombinedValues = new HashMap<String, Double>();
		for (Entry<String, VersionControlHistoryMetrics> versionControlHistoryMetricsPerFileEntry : versionControlHistoryMetricsPerFile.entrySet()) {
			String fileName = versionControlHistoryMetricsPerFileEntry.getKey();
			VersionControlHistoryMetrics versionControlHistoryMetrics = versionControlHistoryMetricsPerFileEntry.getValue();
			
			Double hotNumberModification = Calculator.calculateDistributionValue(numberOfModifications, versionControlHistoryMetrics.getNumberOfModifications());
			hotNumbersModifications.put(fileName, hotNumberModification);
			
			if (versionControlHistoryMetrics.getChurnValue() == null) {
				hotNumbersChurn.put(fileName, 0.0);
			} else {
				Double hotNumberChurn = Calculator.calculateDistributionValue(churnValues, versionControlHistoryMetrics.getChurnValue());
				hotNumbersChurn.put(fileName, hotNumberChurn);
			}
			
			Double hotNumberOwnership = Calculator.calculateDistributionValue(ownershipValues, versionControlHistoryMetrics.getNumberOfContributors());
			hotNumbersOwnership.put(fileName, hotNumberOwnership);
			
			Double hotNumberOwnershipToleranceOne = Calculator.calculateDistributionValue(ownershipValuesToleranceOne, versionControlHistoryMetrics.getNumberOfContributorsToleranceOne());
			hotNumbersOwnershipToleranceOne.put(fileName, hotNumberOwnershipToleranceOne);
			
			Double hotNumberOwnershipToleranceTwo = Calculator.calculateDistributionValue(ownershipValuesToleranceTwo, versionControlHistoryMetrics.getNumberOfContributorsToleranceTwo());
			hotNumbersOwnershipToleranceTwo.put(fileName, hotNumberOwnershipToleranceTwo);
					
			Double hotNumberFocusWeightedOwnership = Calculator.calculateDistributionValue(focusWeightedOwnership, versionControlHistoryMetrics.getFocusWeightedContributors());
			hotNumbersFocusWeightedOwnership.put(fileName, hotNumberFocusWeightedOwnership);
			
			Double hotNumberCreatingDate = Calculator.calculateDistributionValue(creatingDates, versionControlHistoryMetrics.getCreatingDate());
			hotNumbersCreatingDates.put(fileName, hotNumberCreatingDate);
			
			Double hotNumberLastModificationDate = Calculator.calculateDistributionValue(lastModificationDates, versionControlHistoryMetrics.getLastModificationDate());
			hotNumbersLastModificationDates.put(fileName, hotNumberLastModificationDate);
			
			Double hotNumberModificationDateAverage = Calculator.calculateDistributionValue(modificationDatesAverage, versionControlHistoryMetrics.getModificationDatesAverage());
			hotNumbersModificationDateAverage.put(fileName, hotNumberModificationDateAverage);

			Double combinedValue = calculateCombinedMetric(versionControlHistoryMetrics);
			Double hotNumberCombined = Calculator.calculateDistributionValue(combinedValues, combinedValue);
			hotNumbersCombinedValues.put(fileName, hotNumberCombined);
		}
		hotNumbers.put("modifications", hotNumbersModifications);
		hotNumbers.put("churn", hotNumbersChurn);
		hotNumbers.put("ownership", hotNumbersOwnership);
		hotNumbers.put("ownershipToleranceOne", hotNumbersOwnershipToleranceOne);
		hotNumbers.put("ownershipToleranceTwo", hotNumbersOwnershipToleranceTwo);
		hotNumbers.put("focusWeightedOwnership", hotNumbersFocusWeightedOwnership);
		hotNumbers.put("creatingDates", hotNumbersCreatingDates);
		hotNumbers.put("lastModificationDates", hotNumbersLastModificationDates);
		hotNumbers.put("modificationDatesAverage", hotNumbersModificationDateAverage);
		hotNumbers.put("combined", hotNumbersCombinedValues);
		return hotNumbers;
	}
	
	protected static Double calculateCombinedMetric(VersionControlHistoryMetrics versionControlHistoryMetrics) {
		Double result = null;
		if (versionControlHistoryMetrics.getFocusWeightedContributors() != null) {
			result = versionControlHistoryMetrics.getNumberOfModifications() * versionControlHistoryMetrics.getFocusWeightedContributors();
		}
		return result;
	}

}
