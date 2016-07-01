package hotareadetector.data;

import hotareadetector.util.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * A commit x file "matrix", each "cell" contains data about the file in question, at the commit in question.
 * Indeed, it is not a real matrix, as such a matrix would be huge; the matrix functions a simulated.
 */
public class CommitFileMatrix {
	/**
	 * Keys: revision numbers.
	 * Values: list of commit file data in that revision.
	 * 
	 * E.g.:
	 * 1: Main.java, Data.java
	 * 2: Data.java, Game.java
	 * 3: Game.java
	 * Each containing data about the actual file and revision.
	 */
	Map<Integer, List<CommitFileCell>> commits = new HashMap<Integer, List<CommitFileCell>>();
	
	/**
	 * Keys: file names.
	 * Values: list of commit file data of that file. The first one is always the actual one.
	 * 
	 * E.g.:
	 * "Main.java": 1
	 * "Data.java": 2, 1
	 * "Game.java": 3, 2
	 * Each containing data about the actual file and revision.
	 */
	Map<String, List<CommitFileCell>> files = new HashMap<String, List<CommitFileCell>>();
	
	/**
	 * Deep analysis indicates that the diff data are also used.
	 */
	private final boolean deepAnalysis;

	/**
	 * Revision number of the latest revision.
	 */
	private int latestRevision = 0;

	/**
	 * Constructor indicating if a deep analysis (i.e. using diff source control command) is necessary.
	 * 
	 * @param deepAnalysis If set to true, then diff commands are executed as well.
	 */
	public CommitFileMatrix(boolean deepAnalysis) {
		this.deepAnalysis = deepAnalysis;
	}

	/**
	 * Add a commit-file structure to the matrix.
	 */
	public void addCommitFileData(CommitFileCell commitFileData) {
		Integer revision = commitFileData.getRevision();
		if (revision > latestRevision) {
			latestRevision = revision;
		}
		
		List<CommitFileCell> revisionData = commits.get(revision);
		if (revisionData == null) {
			revisionData = new LinkedList<CommitFileCell>();
			commits.put(revision, revisionData);
		}
		revisionData.add(0, commitFileData);
		
		String fileName = commitFileData.getFileName();
		List<CommitFileCell> fileData = files.get(fileName);
		if (fileData == null) {
			fileData = new LinkedList<CommitFileCell>();
			files.put(fileName, fileData);
		}
		fileData.add(0, commitFileData);
	}
	
	/**
	 * Adds all elements of a collection to the matrix.
	 */
	public void addCommitedFileDataCollection(Collection<CommitFileCell> commitFileDataCollection) {
		for (CommitFileCell commitFileData : commitFileDataCollection) {
			addCommitFileData(commitFileData);
		}
	}

	/**
	 * Retrieves the latest information about a file.
	 */
	public CommitFileCell getFileDataLatest(String fileName) {
		List<CommitFileCell> filesList = files.get(fileName);
		if (filesList != null && !filesList.isEmpty()) {
			return files.get(fileName).get(0);
		}
		return null;
	}

	/**
	 * Returns all the files of latest revision.
	 */
	public List<CommitFileCell> getAllFilesOfLatestRevision() {
		return getAllFilesOfRevision(getLatestRevision());
	}

	/**
	 * Returns all the files of a revision. Files already deleted or added later are not returned.
	 */
	public List<CommitFileCell> getAllFilesOfRevision(int revision) {
		List<CommitFileCell> result = new ArrayList<CommitFileCell>();
		for (Entry<String, List<CommitFileCell>> entry : files.entrySet()) {
			List<CommitFileCell> fileCommits = entry.getValue();
			CommitFileCell fileCommitActual = null;
			for (CommitFileCell fileCommit : fileCommits) {
				int actualRevision = fileCommit.getRevision();
				if (actualRevision <= revision) {
					fileCommitActual = fileCommit;
					break;
				}
			}
			if (fileCommitActual != null && fileCommitActual.getLatestOperation() != null && ((!fileCommitActual.getLatestOperation().equals(OperationType.D) && !fileCommitActual.getLatestOperation().equals(OperationType.R)) || fileCommitActual.getRevision() == revision)) {
				result.add(fileCommitActual);
			}
		}
		return result;
	}
	
	/**
	 * Performs rename. It handles the directory rename as well.
	 */
	public List<CommitFileCell> performRename(String fromName, String toName, String developer, Date date, int revision, FileDiffInformation relatedFileDiff, ContributorFocusInformation developerFocusInformation) {
		List<CommitFileCell> renamedEntries = new ArrayList<CommitFileCell>();
		for (Entry<String, List<CommitFileCell>> fileEntry: files.entrySet()) {
			String fileName = fileEntry.getKey();
			if (fileName.equals(fromName) || fileName.startsWith(fromName + '/')) {
				String tail = "";
				if (fileName.startsWith(fromName + '/')) {
					tail = fileName.substring(fromName.length());
				}
				CommitFileCell latestInfoBeforeRename = fileEntry.getValue().get(0);
				if (latestInfoBeforeRename != null) {
					CommitFileCell actualRenamedEntry = latestInfoBeforeRename.cloneRenamed(toName + tail, developer, date, revision, relatedFileDiff, developerFocusInformation);
					renamedEntries.add(actualRenamedEntry);
				} else {
					System.out.println("The history of a renamed file is not found. Normally this should not occur. Information for investigation:");
					System.out.println("Revision: " + revision);
					System.out.println("Original file name: " + fromName);
					System.out.println("New file name: " + toName);
				}
			}
		}
		addCommitedFileDataCollection(renamedEntries);
		return renamedEntries;
	}
	
	/**
	 * Returns all files in a directory of a specified revision.
	 */
	public List<CommitFileCell> getFilesInDirectory(String dirName, int revision) {
		List<CommitFileCell> filesInDirectory = new ArrayList<CommitFileCell>();
		for (String fileName : files.keySet()) {
			for (CommitFileCell file : files.get(fileName)) {
				if (file.getRevision() <= revision && file.getFileName().startsWith(dirName + '/')) {
					// the actual name of the file is considered, and not the latest; this handles the renames of the renames
					filesInDirectory.add(file);
					break;
				}
			}
		}
		return filesInDirectory;
	}
	
	/**
	 * Filters the files in the matrix according to filter criteria: 
	 * prefixes (e.g. /trunk) and extensions (e.g. .java) to consider, and prefixes to exclude (e.g. /trunk/test).
	 */
	public void filterData(
			List<String> includePrefixes,
			List<String> excludePrefixes,
			List<String> extensions) {
		Map<Integer, List<CommitFileCell>> filteredCommits = new HashMap<Integer, List<CommitFileCell>>();
		for (Entry<Integer, List<CommitFileCell>> commit : commits.entrySet()) {
			List<CommitFileCell> filteredCommitFileCells = new ArrayList<CommitFileCell>();
			for (CommitFileCell actualFile : commit.getValue()) {
				if (fileShouldRetain(actualFile.getFileName(), includePrefixes, excludePrefixes, extensions)) {
					filteredCommitFileCells.add(actualFile);
				}
			}
			if (!filteredCommitFileCells.isEmpty()) {
				filteredCommits.put(commit.getKey(), filteredCommitFileCells);
			}
		}
		commits = filteredCommits;
		
		Map<String, List<CommitFileCell>> filteredFiles = new HashMap<String, List<CommitFileCell>>();
		for (Entry<String, List<CommitFileCell>> file: files.entrySet()) {
			if (fileShouldRetain(file.getKey(), includePrefixes, excludePrefixes, extensions)) {
				filteredFiles.put(file.getKey(), file.getValue());
			}
		}
		files = filteredFiles;
	}
	
	
	/**
	 * Checks if a file should be retained based on extension, include and exclude policies.
	 */
	protected static boolean fileShouldRetain(
			String fileName,
			List<String> includePrefixes,
			List<String> excludePrefixes,
			List<String> extensions) {
		if (extensions != null) {
			if (!StringUtil.containsExtension(fileName, extensions)) {
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
	 * Returns the set of file names in the matrix. It contains all the file names ever occurred, i.e. the deleted ones also.
	 */
	public Set<String> getFileNames() {
		return files.keySet();
	}

	public boolean isDeepAnalysis() {
		return deepAnalysis;
	}
	
	public int getLatestRevision() {
		return latestRevision;
	}

}
