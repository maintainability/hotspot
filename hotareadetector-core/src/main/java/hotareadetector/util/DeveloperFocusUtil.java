package hotareadetector.util;

import java.util.List;

/**
 * Utility functions for developer focus calculation.
 */
public class DeveloperFocusUtil {
	
	/**
	 * Calculates the focus information of the fully qualified file names as follows.
	 * First is calculates the pairwise package name based distances, and calculates their sum.
	 * Then it multiplies with the following value: number of files multiplied by the number of files above two.
	 * This is identical with the following: sum of pairwise distance * 2 / (number of files - 1)
	 */
	public static double calculateFocusValue(final List<String> fileNames) {
		if (fileNames == null || fileNames.size() < 2) {
			return 0.0;
		} else {
			int fileNamesSize = fileNames.size();
			int totalDistance = 0;
			for (int i = 0; i < fileNamesSize - 1; i++) {
				for (int j = i + 1; j < fileNamesSize; j++) {
					totalDistance += calculateDistance(fileNames.get(i), fileNames.get(j));
				}
			}
			double result = totalDistance * 2.0 / (fileNamesSize - 1);
			return result;
		}
	}
	
	/**
	 * Calculates the distance between two fully qualified file names.
	 * If they belong to the same package, then the distance is 0.
	 * Otherwise, the distance is the number of minimal number of steps to be taken to reach from one package name the other.
	 * E.g. distances:
	 * - com/mycompany/myapp/Main.java and com/mycompany/myapp/myutil/MyUtil.java: 1
	 * - com/mycompany/myapp/mydata/Data.java and com/mycompany/myapp/myutil/MyUtil.java: 2
	 * - com/mycompany/myapp/mydata/Data.java and com/mycompany/myapp/myutil/myfuction/MyFunction.java: 3
	 */
	public static int calculateDistance(String fileName1, String fileName2) {
		String[] fileName1Parts = fileName1.split("/");
		String[] fileName2Parts = fileName2.split("/");
		int minPackageLenght = Math.min(fileName1Parts.length, fileName2Parts.length) - 1;
		int commonPackageLength = 0;
		if (minPackageLenght > 0) {
			for (int i = 0; i < minPackageLenght; i++) {
				if (fileName1Parts[i].equals(fileName2Parts[i])) {
					commonPackageLength++;
				}
			}
		}
		int result = fileName1Parts.length + fileName2Parts.length - 2 * (commonPackageLength + 1);
		return result;
	}

}
