package hotareadetector.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

import hotareadetector.data.CommitDataExtended;
import hotareadetector.data.ContributorFile;
import hotareadetector.data.ContributorFocusStructure;

/**
 * Utility functions for contributor focus calculation.
 */
public class ContributorFocusUtil {
	
	/**
	 * Calculates the focus weighted ownership of all files. Returns the calculated data in the following format:
	 * 
	 * /com/mycompany/myapp/Main.java -> 20/3
	 * /com/mycompany/myapp/game/Game.java -> 4
	 * /com/mycompany/myapp/util/Conversions.java -> 4
	 * /com/mycompany/myapp/util/Calculations.java -> 7
	 * /com/mycompany/myapp/util/Asserts.java -> 5
	 */
	public static Map<String, Double> calculateFocusWeightedOwnership(Map<String, List<CommitDataExtended>> fileCommitMap) {
		System.out.println("Starting building structures for developer focus calculations.");
		ContributorFocusStructure contributorFocusStructure = buildContributorFocusStructure(fileCommitMap);
		System.out.println("Finished building structures, starting calculation of focus weighted ownership.");
		Map<String, Double> result = calculateFocusWeightedOwnershipOfFiles(contributorFocusStructure);
		System.out.println("Finished with focus weighted ownership calculation.");
		return result;
	}
	
	/**
	 * Creates the following structures:
	 * 
	 * Map<String, TreeMap<Date, ArrayList<String>>> contributorsModifications
	 * Contains all the contributors with all their modifications (date and file name).
	 * 
	 * Map<ContributorFile, Date> fileLastModifiedByContributor
	 * Contains the last modification date by contributor and file.
	 * 
	 * For an example, see the related unit test.
	 */
	protected static ContributorFocusStructure buildContributorFocusStructure(Map<String, List<CommitDataExtended>> fileCommitMap) {
		Map<String, TreeMap<Date, ArrayList<String>>> contributorsModifications = new HashMap<String, TreeMap<Date, ArrayList<String>>>();
		Map<ContributorFile, Date> fileLastModifiedByContributor = new HashMap<ContributorFile, Date>();
		Map<String, Set<String>> contributorsPerFile = new HashMap<String, Set<String>>(); 
		
		for (Entry<String, List<CommitDataExtended>> fileCommitMapEntry : fileCommitMap.entrySet()) {
			String fileName = fileCommitMapEntry.getKey();
			List<CommitDataExtended> commitDataExtendedList = fileCommitMapEntry.getValue();
			for (CommitDataExtended commitDataExtended : commitDataExtendedList) {
				String contributor = commitDataExtended.getCommitData().getContributor();
				Date date = commitDataExtended.getCommitData().getDate();
				
				ContributorFile contributorFileStructure = new ContributorFile(contributor, fileName);
				Date lastModification = fileLastModifiedByContributor.get(contributorFileStructure);
				if (lastModification == null || date.after(lastModification)) {
					fileLastModifiedByContributor.put(contributorFileStructure, date);
				}
				
				TreeMap<Date, ArrayList<String>> contributorModifications = contributorsModifications.get(contributor);
				if (contributorModifications == null) {
					contributorModifications = new TreeMap<Date, ArrayList<String>>();
					contributorsModifications.put(contributor, contributorModifications);
				}
				ArrayList<String> modifiedFiles = contributorModifications.get(date);
				if (modifiedFiles == null) {
					modifiedFiles = new ArrayList<String>();
					contributorModifications.put(date, modifiedFiles);
				}
				modifiedFiles.add(fileName);
				
				Set<String> contributors = contributorsPerFile.get(fileName);
				if (contributors == null) {
					contributors = new HashSet<String>();
					contributorsPerFile.put(fileName, contributors);
				}
				contributors.add(contributor);
			}
		}
		return new ContributorFocusStructure(contributorsModifications, fileLastModifiedByContributor, contributorsPerFile);
	}

	/**
	 * Calculates focus weighted ownership values for every file.
	 */
	protected static Map<String, Double> calculateFocusWeightedOwnershipOfFiles(ContributorFocusStructure contributorFocusStructure) {
		Map<String, Double> focusWeightedOwnerships = new HashMap<String, Double>();
		Set<String> fileNames = contributorFocusStructure.getContributorsPerFile().keySet();
		int index = 0;
		for (String fileName : fileNames) {
			System.out.println("(" + ++index + "/" + fileNames.size() + ")" + fileName);
			Double focusWeightedOwnership = calculateFocusWeightedOwnershipOfFile(contributorFocusStructure, fileName);
			focusWeightedOwnerships.put(fileName, focusWeightedOwnership);
		}
		return focusWeightedOwnerships;
	}
	
	/**
	 * Calculates focus weighed ownership of a certain file.
	 */
	protected static Double calculateFocusWeightedOwnershipOfFile(ContributorFocusStructure contributorFocusStructure, String fileName) {
		Set<String> contributorsSet = contributorFocusStructure.getContributorsPerFile().get(fileName);
		double focusWeightedOwnership = 0.0;
		for (String contributorName : contributorsSet) {
			Set<String> modifiedFilesPerContributor = determineModifiedFilesPerContributor(contributorFocusStructure, contributorName, fileName);
			Set<String> modifiedFilesPerContributorSet = new HashSet<String>();
			modifiedFilesPerContributorSet.addAll(modifiedFilesPerContributor);
			List<String> modifiedFilesPerContributorNoDoubles = new ArrayList<String>();
			modifiedFilesPerContributorNoDoubles.addAll(modifiedFilesPerContributorSet);
			double actualFocusValue = calculateFocusValue(modifiedFilesPerContributorNoDoubles);
			focusWeightedOwnership += actualFocusValue;
		}
		return focusWeightedOwnership;
	}
	
	
	/**
	 * Returns all files the contributor contributed until the last modification of the file specified.
	 */
	protected static Set<String> determineModifiedFilesPerContributor(ContributorFocusStructure contributorFocusStructure, String contributor, String fileName) {
		Date lastModificationDate = contributorFocusStructure.getFileLastModifiedByContributor().get(new ContributorFile(contributor, fileName));
		Set<String> result = new TreeSet<String>();
		TreeMap<Date, ArrayList<String>> contributorModifications = contributorFocusStructure.getContributorsModifications().get(contributor);
		for (Date modificationDate : contributorModifications.keySet()) {
			if (!modificationDate.after(lastModificationDate)) {
				result.addAll(contributorModifications.get(modificationDate));
			}
		}
		return result;
	}
	
	
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
