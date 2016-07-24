package hotareadetector.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ContributorFocusStructure {
	private Map<String, TreeMap<Date, ArrayList<String>>> contributorsModifications;
	private Map<ContributorFile, Date> fileLastModifiedByContributor;
	private Map<String, Set<String>> contributorsPerFile;
	
	public ContributorFocusStructure(Map<String, TreeMap<Date, ArrayList<String>>> contributorsModifications,
			Map<ContributorFile, Date> fileLastModifiedByContributor,
			Map<String, Set<String>> contributorsPerFile) {
		super();
		this.contributorsModifications = contributorsModifications;
		this.fileLastModifiedByContributor = fileLastModifiedByContributor;
		this.contributorsPerFile = contributorsPerFile;
	}
	
	public Map<String, TreeMap<Date, ArrayList<String>>> getContributorsModifications() {
		return contributorsModifications;
	}
	
	public Map<ContributorFile, Date> getFileLastModifiedByContributor() {
		return fileLastModifiedByContributor;
	}
	
	public Map<String, Set<String>> getContributorsPerFile() {
		return contributorsPerFile;
	}
}
