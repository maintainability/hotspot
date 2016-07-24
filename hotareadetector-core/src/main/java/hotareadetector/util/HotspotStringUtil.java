package hotareadetector.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class HotspotStringUtil {
	
	/**
	 * Creates a sorted list of string from set of strings.
	 * Useful when file names are keys in a map and a well determined and independent traverse is necessary.
	 */
	public static List<String> createSortedListFromSet(Set<String> stringSet) {
		List<String> stringList = new ArrayList<String>();
		stringList.addAll(stringSet);
		Collections.sort(stringList);
		return stringList;
	}

}
