package hotareadetector.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hotareadetector.data.CommitDataExtended;

public class FocusDistanceCalculatorCache {
	FocusDistanceCalculator focusDistanceCalculator;
	
	public FocusDistanceCalculatorCache(FocusDistanceCalculator focusDistanceCalculator) {
		this.focusDistanceCalculator = focusDistanceCalculator;
	}
	
	/**
	 * Ordinary number of each file; used in caching mechanism.
	 */
	Map<String, Integer> fileNameOrder = new HashMap<String, Integer>();
	
	/**
	 * Two dimensional array for caching already calculated distances.
	 * E.g. consider the following orders (fileNameOrder):
	 * - /com/mycompany/myapp/utils/MyUtil.java -> 3
	 * - /com/mycompany/myapp/game/MyGame.java -> 6
	 * In this case if the distance between these files are calculated, then the following value will be inserted:
	 * distanceCache[3][6] = 2
	 * distanceCache[6][3] = 2
	 */
	int[][] distanceCache;
	
	/**
	 * Initialize the cache.
	 */
	protected void initializeCache(Map<String, List<CommitDataExtended>> fileCommitMap) {
		List<String> fileNameList = HotspotStringUtil.createSortedListFromSet(fileCommitMap.keySet());
		int index = 0;
		for (String fileName : fileNameList) {
			fileNameOrder.put(fileName, index++);
		}
		distanceCache = new int[index][index];
		for (int i = 0; i < index; i++) {
			for (int j = 0; j < index; j++) {
				distanceCache[i][j] = -1;
			}
		}
	}
	
	/**
	 * First check if the value has already been calculated. If not, then calculate and save. if yes, just retrieve from the cache.
	 */
	protected int calculateDistanceCaching(String fileName1, String fileName2) {
		Integer orderOfFile1 = fileNameOrder.get(fileName1);
		Integer orderOfFile2 = fileNameOrder.get(fileName2);
		if (orderOfFile1 == null || orderOfFile2 == null) {
			System.out.println("Order of file " + orderOfFile1 + " or file " + orderOfFile2 + " not found. This message should appear in case of unit testing only. Proceeding with real calculation.");
			return focusDistanceCalculator.calculateDistance(fileName1, fileName2);
		}
		int cachedDistance = distanceCache[orderOfFile1][orderOfFile2];
		if (cachedDistance >= 0) {
			return cachedDistance;
		}
		int calculatedDistance = focusDistanceCalculator.calculateDistance(fileName1, fileName2);
		distanceCache[orderOfFile1][orderOfFile2] = calculatedDistance;
		distanceCache[orderOfFile2][orderOfFile1] = calculatedDistance;
		return calculatedDistance;
	}
		

}
