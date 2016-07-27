package hotareadetector.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import hotareadetector.data.CommitDataExtended;
import hotareadetector.mock.CommitFileMapGenerator;

public class FocusDistanceCalculatorCacheTest {
	@Test
	public void testCachingMechanism() {
		FocusDistanceCalculatorCache focusDistanceCalculatorCache = new FocusDistanceCalculatorCache(new FocusDistanceCalculator());
		Map<String, List<CommitDataExtended>> fileCommitMap = CommitFileMapGenerator.createFileCommitMap();
		
		assertEquals(0, focusDistanceCalculatorCache.fileNameOrder.size());
		
		focusDistanceCalculatorCache.initializeCache(fileCommitMap);
		
		assertEquals(5, focusDistanceCalculatorCache.fileNameOrder.size());
		assertEquals(new Integer(0), focusDistanceCalculatorCache.fileNameOrder.get(CommitFileMapGenerator.file1));
		assertEquals(new Integer(1), focusDistanceCalculatorCache.fileNameOrder.get(CommitFileMapGenerator.file2));
		assertEquals(new Integer(4), focusDistanceCalculatorCache.fileNameOrder.get(CommitFileMapGenerator.file3));
		assertEquals(new Integer(3), focusDistanceCalculatorCache.fileNameOrder.get(CommitFileMapGenerator.file4));
		assertEquals(new Integer(2), focusDistanceCalculatorCache.fileNameOrder.get(CommitFileMapGenerator.file5));
		
		assertEquals(5, focusDistanceCalculatorCache.distanceCache.length);
		assertEquals(5, focusDistanceCalculatorCache.distanceCache[0].length);
		assertEquals(5, focusDistanceCalculatorCache.distanceCache[1].length);
		assertEquals(5, focusDistanceCalculatorCache.distanceCache[2].length);
		assertEquals(5, focusDistanceCalculatorCache.distanceCache[3].length);
		assertEquals(5, focusDistanceCalculatorCache.distanceCache[4].length);
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				assertTrue(focusDistanceCalculatorCache.distanceCache[i][j] < 0);
			}
		}
		
		// now calling calculateDistance
		assertEquals(1, focusDistanceCalculatorCache.calculateDistanceCaching(CommitFileMapGenerator.file1, CommitFileMapGenerator.file2));
		assertEquals(1, focusDistanceCalculatorCache.distanceCache[0][1]);
		assertEquals(1, focusDistanceCalculatorCache.distanceCache[1][0]);
		
		// now not calling calculateDistance
		assertEquals(1, focusDistanceCalculatorCache.calculateDistanceCaching(CommitFileMapGenerator.file1, CommitFileMapGenerator.file2));
	}

	
	@Test
	public void testNumberOfInvocations() {
		FocusDistanceCalculator focusDistanceCalculator = mock(FocusDistanceCalculator.class);
		when(focusDistanceCalculator.calculateDistance(CommitFileMapGenerator.file1, CommitFileMapGenerator.file2)).thenReturn(1);
		
		FocusDistanceCalculatorCache focusDistanceCalculatorCache = new FocusDistanceCalculatorCache(focusDistanceCalculator);
		Map<String, List<CommitDataExtended>> fileCommitMap = CommitFileMapGenerator.createFileCommitMap();		
		focusDistanceCalculatorCache.initializeCache(fileCommitMap);
		
		// verify that not called yet
		verify(focusDistanceCalculator, times(0)).calculateDistance(CommitFileMapGenerator.file1, CommitFileMapGenerator.file2);
		
		// now calling calculateDistance
		assertEquals(1, focusDistanceCalculatorCache.calculateDistanceCaching(CommitFileMapGenerator.file1, CommitFileMapGenerator.file2));
		assertEquals(1, focusDistanceCalculatorCache.distanceCache[0][1]);
		assertEquals(1, focusDistanceCalculatorCache.distanceCache[1][0]);
		
		// verify that already called once
		verify(focusDistanceCalculator, times(1)).calculateDistance(CommitFileMapGenerator.file1, CommitFileMapGenerator.file2);
		
		// now not calling calculateDistance
		assertEquals(1, focusDistanceCalculatorCache.calculateDistanceCaching(CommitFileMapGenerator.file1, CommitFileMapGenerator.file2));
		
		// verify that not called later
		verify(focusDistanceCalculator, times(1)).calculateDistance(CommitFileMapGenerator.file1, CommitFileMapGenerator.file2);
	}

}
