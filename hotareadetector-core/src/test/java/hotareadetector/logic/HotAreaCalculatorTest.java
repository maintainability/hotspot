package hotareadetector.logic;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import hotareadetector.data.CommitDataExtended;
import hotareadetector.data.VersionControlHistoryMetrics;
import hotareadetector.mock.CommitFileMapGenerator;
import hotareadetector.util.VersionControlHistoryMetricsCalculator;

public class HotAreaCalculatorTest {
	@Test
	public void testCalculateHotNumbers() {
		Map<String, List<CommitDataExtended>> fileCommitMap = CommitFileMapGenerator.createFileCommitMap(true);
		Map<String, VersionControlHistoryMetrics> versionControlHistoryMetricsPerFile = (new VersionControlHistoryMetricsCalculator()).calculateVersionControlHistoryMetrics(fileCommitMap);
		
		HotAreaCalculator hotAreaCalculator = new HotAreaCalculator(versionControlHistoryMetricsPerFile);
		Map<String, Map<String, Double>> result = hotAreaCalculator.calculateHotNumbers();
		
		assertEquals(5, hotAreaCalculator.numberOfModifications.size());
		assertEquals(1, hotAreaCalculator.numberOfModifications.get(0).intValue());
		assertEquals(1, hotAreaCalculator.numberOfModifications.get(1).intValue());
		assertEquals(1, hotAreaCalculator.numberOfModifications.get(2).intValue());
		assertEquals(3, hotAreaCalculator.numberOfModifications.get(3).intValue());
		assertEquals(3, hotAreaCalculator.numberOfModifications.get(4).intValue());
		
		assertEquals(5, hotAreaCalculator.churnValues.size());
		assertEquals(4, hotAreaCalculator.churnValues.get(0).intValue());
		assertEquals(5, hotAreaCalculator.churnValues.get(1).intValue());
		assertEquals(6, hotAreaCalculator.churnValues.get(2).intValue());
		assertEquals(9, hotAreaCalculator.churnValues.get(3).intValue());
		assertEquals(21, hotAreaCalculator.churnValues.get(4).intValue());
		
		assertEquals(5, hotAreaCalculator.ownershipValues.size());
		assertEquals(1, hotAreaCalculator.ownershipValues.get(0).intValue());
		assertEquals(1, hotAreaCalculator.ownershipValues.get(1).intValue());
		assertEquals(1, hotAreaCalculator.ownershipValues.get(2).intValue());
		assertEquals(2, hotAreaCalculator.ownershipValues.get(3).intValue());
		assertEquals(2, hotAreaCalculator.ownershipValues.get(4).intValue());
		
		assertEquals(5, hotAreaCalculator.ownershipValuesToleranceOne.size());
		assertEquals(0, hotAreaCalculator.ownershipValuesToleranceOne.get(0).intValue());
		assertEquals(0, hotAreaCalculator.ownershipValuesToleranceOne.get(1).intValue());
		assertEquals(0, hotAreaCalculator.ownershipValuesToleranceOne.get(2).intValue());
		assertEquals(1, hotAreaCalculator.ownershipValuesToleranceOne.get(3).intValue());
		assertEquals(1, hotAreaCalculator.ownershipValuesToleranceOne.get(4).intValue());
		
		assertEquals(5, hotAreaCalculator.ownershipValuesToleranceTwo.size());
		assertEquals(0, hotAreaCalculator.ownershipValuesToleranceTwo.get(0).intValue());
		assertEquals(0, hotAreaCalculator.ownershipValuesToleranceTwo.get(1).intValue());
		assertEquals(0, hotAreaCalculator.ownershipValuesToleranceTwo.get(2).intValue());
		assertEquals(0, hotAreaCalculator.ownershipValuesToleranceTwo.get(3).intValue());
		assertEquals(0, hotAreaCalculator.ownershipValuesToleranceTwo.get(4).intValue());
		
		assertEquals(5, hotAreaCalculator.focusWeightedOwnership.size());
		assertEquals(new Double(4.0), hotAreaCalculator.focusWeightedOwnership.get(0));
		assertEquals(new Double(4.0), hotAreaCalculator.focusWeightedOwnership.get(1));
		assertEquals(new Double(5.0), hotAreaCalculator.focusWeightedOwnership.get(2));
		assertEquals(new Double(20.0/3), hotAreaCalculator.focusWeightedOwnership.get(3));
		assertEquals(new Double(7.0), hotAreaCalculator.focusWeightedOwnership.get(4));
		
		assertEquals(5, hotAreaCalculator.creatingDates.size());
		assertEquals(CommitFileMapGenerator.date1, hotAreaCalculator.creatingDates.get(0));
		assertEquals(CommitFileMapGenerator.date1, hotAreaCalculator.creatingDates.get(1));
		assertEquals(CommitFileMapGenerator.date1, hotAreaCalculator.creatingDates.get(2));
		assertEquals(CommitFileMapGenerator.date2, hotAreaCalculator.creatingDates.get(3));
		assertEquals(CommitFileMapGenerator.date4, hotAreaCalculator.creatingDates.get(4));
		
		assertEquals(5, hotAreaCalculator.lastModificationDates.size());
		assertEquals(CommitFileMapGenerator.date1, hotAreaCalculator.lastModificationDates.get(0));
		assertEquals(CommitFileMapGenerator.date1, hotAreaCalculator.lastModificationDates.get(1));
		assertEquals(CommitFileMapGenerator.date2, hotAreaCalculator.lastModificationDates.get(2));
		assertEquals(CommitFileMapGenerator.date3, hotAreaCalculator.lastModificationDates.get(3));
		assertEquals(CommitFileMapGenerator.date4, hotAreaCalculator.lastModificationDates.get(4));
		
		assertEquals(5, hotAreaCalculator.modificationDatesAverage.size());
		assertEquals(CommitFileMapGenerator.date1, hotAreaCalculator.modificationDatesAverage.get(0));
		assertEquals(CommitFileMapGenerator.date1, hotAreaCalculator.modificationDatesAverage.get(1));
		assertEquals(1456704634333L, hotAreaCalculator.modificationDatesAverage.get(2).getTime());
		assertEquals(1457473412000L, hotAreaCalculator.modificationDatesAverage.get(3).getTime());
		assertEquals(CommitFileMapGenerator.date4, hotAreaCalculator.modificationDatesAverage.get(4));
		
		assertEquals(5, hotAreaCalculator.combinedValues.size());
		assertEquals(new Double(4.0), hotAreaCalculator.combinedValues.get(0));
		assertEquals(new Double(4.0), hotAreaCalculator.combinedValues.get(1));
		assertEquals(new Double(5.0), hotAreaCalculator.combinedValues.get(2));
		assertEquals(new Double(20.0), hotAreaCalculator.combinedValues.get(3));
		assertEquals(new Double(21.0), hotAreaCalculator.combinedValues.get(4));
		
		assertEquals(10, result.size());
		
		assertEquals(5, result.get("modifications").size());
		assertEquals(new Double(0.6), result.get("modifications").get(CommitFileMapGenerator.file1));
		assertEquals(new Double(0.0), result.get("modifications").get(CommitFileMapGenerator.file2));
		assertEquals(new Double(0.0), result.get("modifications").get(CommitFileMapGenerator.file3));
		assertEquals(new Double(0.6), result.get("modifications").get(CommitFileMapGenerator.file4));
		assertEquals(new Double(0.0), result.get("modifications").get(CommitFileMapGenerator.file5));
		
		assertEquals(5, result.get("churn").size());
		assertEquals(new Double(0.4), result.get("churn").get(CommitFileMapGenerator.file1));
		assertEquals(new Double(0.0), result.get("churn").get(CommitFileMapGenerator.file2));
		assertEquals(new Double(0.2), result.get("churn").get(CommitFileMapGenerator.file3));
		assertEquals(new Double(0.8), result.get("churn").get(CommitFileMapGenerator.file4));
		assertEquals(new Double(0.6), result.get("churn").get(CommitFileMapGenerator.file5));
		
		assertEquals(5, result.get("ownership").size());
		assertEquals(new Double(0.6), result.get("ownership").get(CommitFileMapGenerator.file1));
		assertEquals(new Double(0.0), result.get("ownership").get(CommitFileMapGenerator.file2));
		assertEquals(new Double(0.0), result.get("ownership").get(CommitFileMapGenerator.file3));
		assertEquals(new Double(0.6), result.get("ownership").get(CommitFileMapGenerator.file4));
		assertEquals(new Double(0.0), result.get("ownership").get(CommitFileMapGenerator.file5));
		
		assertEquals(5, result.get("ownershipToleranceOne").size());
		assertEquals(new Double(0.6), result.get("ownershipToleranceOne").get(CommitFileMapGenerator.file1));
		assertEquals(new Double(0.0), result.get("ownershipToleranceOne").get(CommitFileMapGenerator.file2));
		assertEquals(new Double(0.0), result.get("ownershipToleranceOne").get(CommitFileMapGenerator.file3));
		assertEquals(new Double(0.6), result.get("ownershipToleranceOne").get(CommitFileMapGenerator.file4));
		assertEquals(new Double(0.0), result.get("ownershipToleranceOne").get(CommitFileMapGenerator.file5));
		
		assertEquals(5, result.get("ownershipToleranceTwo").size());
		assertEquals(new Double(0.0), result.get("ownershipToleranceTwo").get(CommitFileMapGenerator.file1));
		assertEquals(new Double(0.0), result.get("ownershipToleranceTwo").get(CommitFileMapGenerator.file2));
		assertEquals(new Double(0.0), result.get("ownershipToleranceTwo").get(CommitFileMapGenerator.file3));
		assertEquals(new Double(0.0), result.get("ownershipToleranceTwo").get(CommitFileMapGenerator.file4));
		assertEquals(new Double(0.0), result.get("ownershipToleranceTwo").get(CommitFileMapGenerator.file5));
		
		assertEquals(5, result.get("focusWeightedOwnership").size());
		assertEquals(new Double(0.6), result.get("focusWeightedOwnership").get(CommitFileMapGenerator.file1));
		assertEquals(new Double(0.0), result.get("focusWeightedOwnership").get(CommitFileMapGenerator.file2));
		assertEquals(new Double(0.0), result.get("focusWeightedOwnership").get(CommitFileMapGenerator.file3));
		assertEquals(new Double(0.8), result.get("focusWeightedOwnership").get(CommitFileMapGenerator.file4));
		assertEquals(new Double(0.4), result.get("focusWeightedOwnership").get(CommitFileMapGenerator.file5));
		
		assertEquals(5, result.get("creatingDates").size());
		assertEquals(new Double(0.0), result.get("creatingDates").get(CommitFileMapGenerator.file1));
		assertEquals(new Double(0.0), result.get("creatingDates").get(CommitFileMapGenerator.file2));
		assertEquals(new Double(0.0), result.get("creatingDates").get(CommitFileMapGenerator.file3));
		assertEquals(new Double(0.6), result.get("creatingDates").get(CommitFileMapGenerator.file4));
		assertEquals(new Double(0.8), result.get("creatingDates").get(CommitFileMapGenerator.file5));
		
		assertEquals(5, result.get("lastModificationDates").size());
		assertEquals(new Double(0.4), result.get("lastModificationDates").get(CommitFileMapGenerator.file1));
		assertEquals(new Double(0.0), result.get("lastModificationDates").get(CommitFileMapGenerator.file2));
		assertEquals(new Double(0.0), result.get("lastModificationDates").get(CommitFileMapGenerator.file3));
		assertEquals(new Double(0.6), result.get("lastModificationDates").get(CommitFileMapGenerator.file4));
		assertEquals(new Double(0.8), result.get("lastModificationDates").get(CommitFileMapGenerator.file5));
		
		assertEquals(5, result.get("modificationDatesAverage").size());
		assertEquals(new Double(0.4), result.get("modificationDatesAverage").get(CommitFileMapGenerator.file1));
		assertEquals(new Double(0.0), result.get("modificationDatesAverage").get(CommitFileMapGenerator.file2));
		assertEquals(new Double(0.0), result.get("modificationDatesAverage").get(CommitFileMapGenerator.file3));
		assertEquals(new Double(0.6), result.get("modificationDatesAverage").get(CommitFileMapGenerator.file4));
		assertEquals(new Double(0.8), result.get("modificationDatesAverage").get(CommitFileMapGenerator.file5));
		
		assertEquals(5, result.get("combined").size());
		assertEquals(new Double(0.6), result.get("combined").get(CommitFileMapGenerator.file1));
		assertEquals(new Double(0.0), result.get("combined").get(CommitFileMapGenerator.file2));
		assertEquals(new Double(0.0), result.get("combined").get(CommitFileMapGenerator.file3));
		assertEquals(new Double(0.8), result.get("combined").get(CommitFileMapGenerator.file4));
		assertEquals(new Double(0.4), result.get("combined").get(CommitFileMapGenerator.file5));
	}
	
	@Test
	public void testCalculateCombinedMetric() {
		VersionControlHistoryMetrics versionControlHistoryMetrics = new VersionControlHistoryMetrics();
		versionControlHistoryMetrics.setNumberOfModifications(5);
		versionControlHistoryMetrics.setNumberOfContributors(2);
		versionControlHistoryMetrics.setFocusWeightedContributors(20.0);
		
		assertEquals(new Double(100.0), HotAreaCalculator.calculateCombinedMetric(versionControlHistoryMetrics));
	}

}
