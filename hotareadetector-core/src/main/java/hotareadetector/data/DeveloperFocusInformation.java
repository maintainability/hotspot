package hotareadetector.data;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Data necessary for developer focus calculation.
 */
public class DeveloperFocusInformation {
	/**
	 * Contains all the developers with all their modifications (date and file name), like this:
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
	Map<String, TreeMap<Date, ArrayList<String>>> developersModifications = new HashMap<String, TreeMap<Date, ArrayList<String>>>();
	
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
	Map<ContributorFile, Date> fileLastModifiedByDeveloper = new HashMap<ContributorFile, Date>();
	
	/**
	 * Puts the modification information to data structures developersModifications and fileLastModifiedByDeveloper.
	 */
	public void addModification(String developer, Date date, String fileName) {
		TreeMap<Date, ArrayList<String>> developerModifications = null;
		if (developersModifications.containsKey(developer)) {
			developerModifications = developersModifications.get(developer);
		} else {
			developerModifications = new TreeMap<Date, ArrayList<String>>();
		}
		ArrayList<String> modifications = null;
		if (developerModifications.containsKey(date)) {
			modifications = developerModifications.get(date);
		} else {
			modifications = new ArrayList<String>();
		}
		modifications.add(fileName);
		developerModifications.put(date, modifications);
		developersModifications.put(developer, developerModifications);
		
		fileLastModifiedByDeveloper.put(new ContributorFile(developer, fileName), date);
	}
	
	public void addModifications(String developer, Date date, List<CommitedFileData> commitFileDataList) {
		for (CommitedFileData commitFileData : commitFileDataList) {
			addModification(developer, date, commitFileData.getFileName());
		}
	}
	
	/**
	 * Returns all files the developer contributed until the last modification of the file specified.
	 */
	public Set<String> getModifiedFilesPerDeveloper(String contributor, String fileName) {
		Date lastModificationDate = fileLastModifiedByDeveloper.get(new ContributorFile(contributor, fileName));
		Set<String> result = new TreeSet<String>();
		// the value of lastModificationDate is null if rename happened meanwhile, therefore null check is necessary here
		if (lastModificationDate != null) {
			TreeMap<Date, ArrayList<String>> developerModifications = developersModifications.get(contributor);
			for (Date modificationDate : developerModifications.keySet()) {
				if (!modificationDate.after(lastModificationDate)) {
					result.addAll(developerModifications.get(modificationDate));
				}
			}
		}
		return result;
	}
}
