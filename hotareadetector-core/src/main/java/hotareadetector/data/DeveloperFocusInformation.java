package hotareadetector.data;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

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
	 * 
	 * "bob" ->
	 *   2016.03.08. 16:18:54 ->
	 *     /com/mycompany/myapp/Main.java
	 *     /com/mycompany/myapp/util/Calculations.java
	 *     
	 *  The internal map (date -> list of modified files) is sorted map.
	 */
	Map<String, TreeMap<Date, ArrayList<String>>> developersModifications = new HashMap<String, TreeMap<Date, ArrayList<String>>>();
	
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
	}
	
	public List<String> getModifiedFilesPerDeveloperAfter(String developer, Date date) {
		List<String> result = new ArrayList<String>();
		TreeMap<Date, ArrayList<String>> developerModifications = developersModifications.get(developer);
		if (developerModifications != null) {
			SortedMap<Date, ArrayList<String>> modificationsFromDate = developerModifications.tailMap(date);
			if (modificationsFromDate != null) {
				for (ArrayList<String> modificationFromDate : modificationsFromDate.values()) {
					result.addAll(modificationFromDate);
				}
			}
		}
		return result;
	}	
}
