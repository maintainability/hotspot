package hotareadetector.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import hotareadetector.data.CommitDataExtended;

public class FilteringUtil {
	private List<String> includePrefixes;
	private List<String> excludePrefixes;
	private List<String> extensions;
	
	public FilteringUtil(
			List<String> includePrefixes,
			List<String> excludePrefixes,
			List<String> extensions) {
		this.includePrefixes = includePrefixes;
		this.excludePrefixes = excludePrefixes;
		this.extensions = extensions;
	}

	
	/**
	 * Filters the files in the structure according to filter criteria: 
	 * prefixes (e.g. /trunk) and extensions (e.g. .java) to consider, and prefixes to exclude (e.g. /trunk/test).
	 */
	public Map<String, List<CommitDataExtended>> filterFileCommitMap(Map<String, List<CommitDataExtended>> fileCommitMap) {
		Map<String, List<CommitDataExtended>> filteredFileCommitMap = new HashMap<String, List<CommitDataExtended>>();
		for (Entry<String, List<CommitDataExtended>> fileCommitEntry : fileCommitMap.entrySet()) {
			if (fileShouldRetain(fileCommitEntry.getKey())) {
				filteredFileCommitMap.put(fileCommitEntry.getKey(), fileCommitEntry.getValue());
			}
		}
		return filteredFileCommitMap;
	}
	
	/**
	 * Checks if a file should be retained based on extension, include and exclude policies.
	 */
	public boolean fileShouldRetain(String fileName) {
		if (extensions != null) {
			if (!containsExtension(fileName)) {
				return false;
			}
		}
		if (includePrefixes != null) {
			boolean shouldExclude = true;
			for (String includePrefix : includePrefixes) {
				if (fileName.startsWith(includePrefix)) {
					shouldExclude = false;
					break;
				}
			}
			if (shouldExclude) {
				return false;
			}
		}
		if (excludePrefixes != null) {
			boolean shouldExclude = false;
			for (String excludePrefix : excludePrefixes) {
				if (fileName.startsWith(excludePrefix)) {
					shouldExclude = true;
					break;
				}
			}
			if (shouldExclude) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if the extension of the file name is contained by the set of extensions.
	 * If the extensions is null, then it contains per definition.
	 */
	public boolean containsExtension(String fileName) {
		if (extensions == null) {
			return true;
		}
		int lastDot = fileName.lastIndexOf(".");
		if (lastDot >= 0 && fileName.length() > lastDot + 1) {
			String extension = fileName.substring(lastDot + 1);
			for (String actualExtension : extensions) {
				if (extension.equals(actualExtension)) {
					return true;
				}
			}
		} else {
			for (String actualExtension : extensions) {
				if ("".equals(actualExtension)) {
					return true;
				}
			}
		}
		return false;
	}
}
