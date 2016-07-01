package hotareadetector.data;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Data necessary for contributor focus calculation.
 */
public class ContributorFocusInformation {
	/**
	 * Contains all the contributors with all their modifications (date and file name), like this:
	 * 
	 * "steve" ->
	 *   2016.02.12. 12:43:15 ->
	 *     /com/mycompany/myapp/Main.java
	 *     /com/mycompany/myapp/game/Game.java
	 *     /com/mycompany/myapp/util/Conversions.java
	 *   
	 *   2016.03.08. 10:29:34 ->
	 *     /com/mycompany/myapp/Main.java
	 *     /com/mycompany/myapp/util/Calculations.java
	 *   
	 *   
	 *   2016.03.09. 17:22:08 ->
	 *     /com/mycompany/myapp/util/Calculations.java
	 *     /com/mycompany/myapp/util/Asserts.java
	 * 
	 * "bob" ->
	 *   2016.03.08. 16:18:54 ->
	 *     /com/mycompany/myapp/Main.java
	 *     /com/mycompany/myapp/util/Calculations.java
	 *     
	 *  The internal map (date -> list of modified files) is sorted map.
	 */
	Map<String, TreeMap<Date, ArrayList<String>>> contributorsModifications = new HashMap<String, TreeMap<Date, ArrayList<String>>>();
	
	/**
	 * Contains the last modification date by contributor and file, like this:
	 * 
	 * (steve, /com/mycompany/myapp/game/Game.java)         -> 2016.02.12. 12:43:15
	 * (steve, /com/mycompany/myapp/util/Conversions.java)  -> 2016.02.12. 12:43:15
	 * (steve, /com/mycompany/myapp/Main.java)              -> 2016.03.08. 10:29:34
	 * (bob,   /com/mycompany/myapp/Main.java)              -> 2016.03.08. 16:18:54
	 * (bob,   /com/mycompany/myapp/util/Calculations.java) -> 2016.03.08. 16:18:54
	 * (steve, /com/mycompany/myapp/util/Calculations.java) -> 2016.03.09. 17:22:08
	 * (steve, /com/mycompany/myapp/util/Asserts.java)      -> 2016.03.09. 17:22:08
	 */
	Map<ContributorFile, Date> fileLastModifiedByContributor = new HashMap<ContributorFile, Date>();
	
	/**
	 * Puts the modification information to data structures contributorsModifications and fileLastModifiedByContributor.
	 */
	public void addModification(String contributor, Date date, String fileName) {
		TreeMap<Date, ArrayList<String>> contributorModifications = null;
		if (contributorsModifications.containsKey(contributor)) {
			contributorModifications = contributorsModifications.get(contributor);
		} else {
			contributorModifications = new TreeMap<Date, ArrayList<String>>();
		}
		ArrayList<String> modifications = null;
		if (contributorModifications.containsKey(date)) {
			modifications = contributorModifications.get(date);
		} else {
			modifications = new ArrayList<String>();
		}
		modifications.add(fileName);
		contributorModifications.put(date, modifications);
		contributorsModifications.put(contributor, contributorModifications);
		
		fileLastModifiedByContributor.put(new ContributorFile(contributor, fileName), date);
	}
	
	public void addModifications(String contributor, Date date, List<CommitedFileData> commitFileDataList) {
		for (CommitedFileData commitFileData : commitFileDataList) {
			addModification(contributor, date, commitFileData.getFileName());
		}
	}
	
	/**
	 * Applies the effect of a rename in the data structures. E.g. consider the following case:
	 * - Commit 1: bob updated /com/mycompany/myapp/Main.java
	 * - Commit 2: steve renamed file /com/mycompany/myapp/Main.java to /com/mycompany/myapp/main/Main.java
	 * Then file name change in the history of bob's files should be adopted, as follows:
	 * 
	 * 1. contributorsModifications
	 * 
	 * Original value:
	 * 
	 * "bob" ->
	 *   date of first commit ->
	 *     /com/mycompany/myapp/Main.java
	 * 
	 * Modified value:
	 * 
	 * "bob" ->
	 *   date of first commit ->
	 *     /com/mycompany/myapp/main/Main.java
	 * 
	 * 2. fileLastModifiedByContributor
	 * 
	 * Original value:
	 * 
	 * (bob, /com/mycompany/myapp/Main.java) -> date of first commit
	 * 
	 * Modified value:
	 * 
	 * (bob, /com/mycompany/myapp/main/Main.java) -> date of first commit
	 */
	public void applyRename(String from, String to) {
		for (Entry<String, TreeMap<Date, ArrayList<String>>> contributorModifications : contributorsModifications.entrySet()) {
			for (Entry<Date, ArrayList<String>> modificationsAt : contributorModifications.getValue().entrySet()) {
				ArrayList<String> modifiedFiles = modificationsAt.getValue();
				for (int i = 0; i < modifiedFiles.size(); i++) {
					modifiedFiles.set(i, determineRenamedValue(from, to, modifiedFiles.get(i)));
				}
			}
		}
		
		Set<ContributorFile> keySet = new HashSet<ContributorFile>();
		keySet.addAll(fileLastModifiedByContributor.keySet());
		for (ContributorFile contributorFile : keySet) {
			String renamedValue = determineRenamedValue(from, to, contributorFile.getFileName());
			if (!renamedValue.equals(contributorFile.getFileName())) {
				ContributorFile newContributorFile = new ContributorFile(contributorFile.getContributor(), renamedValue);
				Date date = fileLastModifiedByContributor.get(contributorFile);
				fileLastModifiedByContributor.remove(contributorFile);
				fileLastModifiedByContributor.put(newContributorFile, date);
			}
		}
	}
	
	/**
	 * Determines the new value of the rename. Examples:
	 * 
	 * 1.
	 * From:      /com/mycompany/myapp/Main.java
	 * To:        /com/mycompany/myapp/main/Main.java
	 * File name: /com/mycompany/myapp/Main.java
	 * Result:    /com/mycompany/myapp/main/Main.java
	 * 
	 * 2. 
	 * From:      /com/mycompany
	 * To:        /org/mycompany
	 * File name: /com/mycompany/myapp/Main.java
	 * Result:    /org/mycompany/myapp/Main.java
	 * 
	 * 3.
	 * From:      /com/mycompany/myapp/Main.java
	 * To:        /com/mycompany/myapp/main/Main.java
	 * File name: /com/mycompany/myapp/Util.java
	 * Result:    /com/mycompany/myapp/Util.java
	 */
	protected String determineRenamedValue(String from, String to, String fileName) {
		if (fileName.startsWith(from)) {
			return to + fileName.substring(from.length());
		}
		return fileName;
	}
	
	/**
	 * Returns all files the contributor contributed until the last modification of the file specified.
	 */
	public Set<String> getModifiedFilesPerContributor(String contributor, String fileName) {
		Date lastModificationDate = fileLastModifiedByContributor.get(new ContributorFile(contributor, fileName));
		Set<String> result = new TreeSet<String>();
		// the value of lastModificationDate is null if rename happened meanwhile, therefore null check is necessary here
		if (lastModificationDate != null) {
			TreeMap<Date, ArrayList<String>> contributorModifications = contributorsModifications.get(contributor);
			for (Date modificationDate : contributorModifications.keySet()) {
				if (!modificationDate.after(lastModificationDate)) {
					result.addAll(contributorModifications.get(modificationDate));
				}
			}
		}
		return result;
	}
}
