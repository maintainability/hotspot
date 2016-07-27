package hotareadetector.util;

public class FocusDistanceCalculator {
	/**
	 * Calculates the distance between two fully qualified file names.
	 * If they belong to the same package, then the distance is 0.
	 * Otherwise, the distance is the number of minimal number of steps to be taken to reach from one package name the other.
	 * E.g. distances:
	 * - com/mycompany/myapp/Main.java and com/mycompany/myapp/myutil/MyUtil.java: 1
	 * - com/mycompany/myapp/mydata/Data.java and com/mycompany/myapp/myutil/MyUtil.java: 2
	 * - com/mycompany/myapp/mydata/Data.java and com/mycompany/myapp/myutil/myfuction/MyFunction.java: 3
	 */
	protected int calculateDistance(String fileName1, String fileName2) {
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
