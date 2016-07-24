package hotareadetector.util;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.util.List;
import java.util.Map;
import hotareadetector.data.CommitDataExtended;
import hotareadetector.data.VersionControlHistoryMetrics;
import hotareadetector.mock.CommitFileMapGenerator;

public class VersionControlHistoryMetricsCalculatorTest {
	@Test
	public void testSetModificationIntensityLikeMetrics() {
		Map<String, List<CommitDataExtended>> fileCommitMap = CommitFileMapGenerator.createFileCommitMap(true);
		
		Map<String, VersionControlHistoryMetrics> result = (new VersionControlHistoryMetricsCalculator()).calculateVersionControlHistoryMetrics(fileCommitMap, true);
		
		assertEquals(5, result.size());
		
		assertEquals(3, result.get(CommitFileMapGenerator.file1).getNumberOfModifications().intValue());
		assertEquals(6, result.get(CommitFileMapGenerator.file1).getChurnValue().intValue());
		assertEquals(2, result.get(CommitFileMapGenerator.file1).getNumberOfContributors().intValue());
		assertEquals(1, result.get(CommitFileMapGenerator.file1).getNumberOfContributorsToleranceOne().intValue());
		assertEquals(0, result.get(CommitFileMapGenerator.file1).getNumberOfContributorsToleranceTwo().intValue());
		assertEquals(CommitFileMapGenerator.date1, result.get(CommitFileMapGenerator.file1).getCreatingDate());
		assertEquals(CommitFileMapGenerator.date2, result.get(CommitFileMapGenerator.file1).getLastModificationDate());
		assertEquals(1456704634333L, result.get(CommitFileMapGenerator.file1).getModificationDatesAverage().getTime());
		assertEquals(new Double(20.0/3), result.get(CommitFileMapGenerator.file1).getFocusWeightedContributors());
		
		assertEquals(1, result.get(CommitFileMapGenerator.file2).getNumberOfModifications().intValue());
		assertEquals(4, result.get(CommitFileMapGenerator.file2).getChurnValue().intValue());
		assertEquals(1, result.get(CommitFileMapGenerator.file2).getNumberOfContributors().intValue());
		assertEquals(0, result.get(CommitFileMapGenerator.file2).getNumberOfContributorsToleranceOne().intValue());
		assertEquals(0, result.get(CommitFileMapGenerator.file2).getNumberOfContributorsToleranceTwo().intValue());
		assertEquals(CommitFileMapGenerator.date1, result.get(CommitFileMapGenerator.file2).getCreatingDate());
		assertEquals(CommitFileMapGenerator.date1, result.get(CommitFileMapGenerator.file2).getLastModificationDate());
		assertEquals(CommitFileMapGenerator.date1, result.get(CommitFileMapGenerator.file2).getModificationDatesAverage());
		assertEquals(new Double(4.0), result.get(CommitFileMapGenerator.file2).getFocusWeightedContributors());
		
		assertEquals(1, result.get(CommitFileMapGenerator.file3).getNumberOfModifications().intValue());
		assertEquals(5, result.get(CommitFileMapGenerator.file3).getChurnValue().intValue());
		assertEquals(1, result.get(CommitFileMapGenerator.file3).getNumberOfContributors().intValue());
		assertEquals(0, result.get(CommitFileMapGenerator.file3).getNumberOfContributorsToleranceOne().intValue());
		assertEquals(0, result.get(CommitFileMapGenerator.file3).getNumberOfContributorsToleranceTwo().intValue());
		assertEquals(CommitFileMapGenerator.date1, result.get(CommitFileMapGenerator.file3).getCreatingDate());
		assertEquals(CommitFileMapGenerator.date1, result.get(CommitFileMapGenerator.file3).getLastModificationDate());
		assertEquals(CommitFileMapGenerator.date1, result.get(CommitFileMapGenerator.file3).getModificationDatesAverage());
		assertEquals(new Double(4.0), result.get(CommitFileMapGenerator.file3).getFocusWeightedContributors());
		
		assertEquals(3, result.get(CommitFileMapGenerator.file4).getNumberOfModifications().intValue());
		assertEquals(21, result.get(CommitFileMapGenerator.file4).getChurnValue().intValue());
		assertEquals(2, result.get(CommitFileMapGenerator.file4).getNumberOfContributors().intValue());
		assertEquals(1, result.get(CommitFileMapGenerator.file4).getNumberOfContributorsToleranceOne().intValue());
		assertEquals(0, result.get(CommitFileMapGenerator.file4).getNumberOfContributorsToleranceTwo().intValue());
		assertEquals(CommitFileMapGenerator.date2, result.get(CommitFileMapGenerator.file4).getCreatingDate());
		assertEquals(CommitFileMapGenerator.date3, result.get(CommitFileMapGenerator.file4).getLastModificationDate());
		assertEquals(1457473412000L, result.get(CommitFileMapGenerator.file4).getModificationDatesAverage().getTime());
		assertEquals(new Double(7.0), result.get(CommitFileMapGenerator.file4).getFocusWeightedContributors());
		
		assertEquals(1, result.get(CommitFileMapGenerator.file5).getNumberOfModifications().intValue());
		assertEquals(9, result.get(CommitFileMapGenerator.file5).getChurnValue().intValue());
		assertEquals(1, result.get(CommitFileMapGenerator.file5).getNumberOfContributors().intValue());
		assertEquals(0, result.get(CommitFileMapGenerator.file5).getNumberOfContributorsToleranceOne().intValue());
		assertEquals(0, result.get(CommitFileMapGenerator.file5).getNumberOfContributorsToleranceTwo().intValue());
		assertEquals(CommitFileMapGenerator.date4, result.get(CommitFileMapGenerator.file5).getCreatingDate());
		assertEquals(CommitFileMapGenerator.date4, result.get(CommitFileMapGenerator.file5).getLastModificationDate());
		assertEquals(CommitFileMapGenerator.date4, result.get(CommitFileMapGenerator.file5).getModificationDatesAverage());
		assertEquals(new Double(5.0), result.get(CommitFileMapGenerator.file5).getFocusWeightedContributors());
	}

	@Test
	public void testSetModificationIntensityLikeMetricsNoFocusNoChurn() {
		Map<String, List<CommitDataExtended>> fileCommitMap = CommitFileMapGenerator.createFileCommitMap(false);
		
		Map<String, VersionControlHistoryMetrics> result = (new VersionControlHistoryMetricsCalculator()).calculateVersionControlHistoryMetrics(fileCommitMap, false);
		
		assertEquals(5, result.size());
		
		assertEquals(3, result.get(CommitFileMapGenerator.file1).getNumberOfModifications().intValue());
		assertEquals(2, result.get(CommitFileMapGenerator.file1).getNumberOfContributors().intValue());
		assertEquals(1, result.get(CommitFileMapGenerator.file1).getNumberOfContributorsToleranceOne().intValue());
		assertEquals(0, result.get(CommitFileMapGenerator.file1).getNumberOfContributorsToleranceTwo().intValue());
		assertEquals(CommitFileMapGenerator.date1, result.get(CommitFileMapGenerator.file1).getCreatingDate());
		assertEquals(CommitFileMapGenerator.date2, result.get(CommitFileMapGenerator.file1).getLastModificationDate());
		assertEquals(1456704634333L, result.get(CommitFileMapGenerator.file1).getModificationDatesAverage().getTime());
		
		assertEquals(null, result.get(CommitFileMapGenerator.file1).getChurnValue());
		assertEquals(null, result.get(CommitFileMapGenerator.file2).getChurnValue());
		assertEquals(null, result.get(CommitFileMapGenerator.file3).getChurnValue());
		assertEquals(null, result.get(CommitFileMapGenerator.file4).getChurnValue());
		assertEquals(null, result.get(CommitFileMapGenerator.file5).getChurnValue());
		
		assertEquals(null, result.get(CommitFileMapGenerator.file1).getFocusWeightedContributors());
		assertEquals(null, result.get(CommitFileMapGenerator.file2).getFocusWeightedContributors());
		assertEquals(null, result.get(CommitFileMapGenerator.file3).getFocusWeightedContributors());
		assertEquals(null, result.get(CommitFileMapGenerator.file4).getFocusWeightedContributors());
		assertEquals(null, result.get(CommitFileMapGenerator.file5).getFocusWeightedContributors());
	}
}
