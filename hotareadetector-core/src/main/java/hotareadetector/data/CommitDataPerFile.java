package hotareadetector.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import hotareadetector.util.RenameUtil;

/**
 * Structure containing all commit information, necessary for hotspot calculation.
 */
public class CommitDataPerFile {
	/**
	 * This is a file name -> commit data structure
	 */
	private Map<String, List<CommitDataExtended>> fileCommitMap = new HashMap<String, List<CommitDataExtended>>();
	
	public Map<String, List<CommitDataExtended>> getFileCommitMap() {
		return fileCommitMap;
	}
	
	public void addCommitData(CommitData commitData, Map<String, Integer> churnData) {
		Collections.sort(commitData.getCommitedFiles());
		for (CommitedFileData committedFile : commitData.getCommitedFiles()) {
			String fileName = committedFile.getFileName();
			Integer churnValue = findChurnValue(churnData, fileName);
			switch (committedFile.getOperationType()) {
			case A:
				if (committedFile.getFromFileName() == null) {
					performAdd(commitData, fileName, churnValue);
				} else {
					performRename(commitData, committedFile.getFromFileName(), fileName, true);
				}
				break;
				
			case M:
				performModify(commitData, fileName, churnValue);
				break;
				
			case D:
				performDelete(fileName, commitData.getCommitedFiles());
				break;
				
			case R:
				performRename(commitData, committedFile.getFromFileName(), fileName, false);
				break;
			}
		}
	}
	
	/**
	 * The logic implemented in this function is necessary due to the following reason.
	 * 
	 * In the svn log the file names are typically longer than in the results of the svn diff. Here are some examples:
	 * 
	 * In svn log: /ant/core/trunk/src/main/org/apache/tools/ant/Main.java
	 * The same in svn diff: core/trunk/src/main/org/apache/tools/ant/Main.java
	 * 
	 * In svn log: /ant/sandbox/antlibs/dotnet/branches/Ant_1.6.2_compatible/docs/msbuild.html
	 * Same in the svn diff: /sandbox/antlibs/dotnet/branches/Ant_1.6.2_compatible/docs/msbuild.html
	 * 
	 * In svn log: /jEdit/trunk/org/gjt/sp/jedit/View.java
	 * The same in svn diff: trunk/org/gjt/sp/jedit/View.java
	 * 
	 * In svn log: /logging/log4j/trunk/src/java/org/apache/log4j/Appender.java
	 * The same in svn diff: trunk/src/java/org/apache/log4j/Appender.java
	 * 
	 * In svn log: /xerces/c/trunk/src/util/BitSet.cpp
	 * The same in svn diff: c/trunk/src/util/BitSet.cpp
	 * 
	 * In these cases some of the prefixes is missing from svn diff.
	 * If the file name is in format /prefix1/prefix2/path/File.ext, then the following lookups are tried, in this order:
	 * 1. /prefix1/prefix2/path/File.ext
	 * 2. prefix1/prefix2/path/File.ext
	 * 3. /prefix2/path/File.ext
	 * 4. prefix2/path/File.ext
	 * 5. /path/File.ext
	 * 6. path/File.ext
	 */
	protected Integer findChurnValue(Map<String, Integer> churnData, String fileName) {
		// 1. match
		Integer churnValue = churnData.get(fileName);
		if (churnValue == null) {
			int firstIndex = fileName.indexOf('/');
			if (firstIndex >= 0 && fileName.length() > firstIndex) {
				// 2. match
				churnValue = churnData.get(fileName.substring(firstIndex + 1));
				if (churnValue == null) {
					int secondIndex = fileName.indexOf('/', firstIndex + 1);
					if (secondIndex >= 0 && fileName.length() > secondIndex + 1) {
						// 3. match
						churnValue = churnData.get(fileName.substring(secondIndex));
						if (churnValue == null) {
							// 4. match
							churnValue = churnData.get(fileName.substring(secondIndex + 1));
							if (churnValue == null) {
								int thirdIndex = fileName.indexOf('/', secondIndex + 1);
								if (thirdIndex >= 0 && fileName.length() > thirdIndex + 1) {
									// 5. match
									churnValue = churnData.get(fileName.substring(thirdIndex));
									if (churnValue == null) {
										// 6. match
										churnValue = churnData.get(fileName.substring(thirdIndex + 1));
									}
								}
							}
						}
					}
				}
			}
		}
		return churnValue;
	}
	

	protected void performAdd(CommitData commitData, String fileName, Integer churnValue) {
		if (fileCommitMap.keySet().contains(fileName)) {
			throw new RuntimeException("Error: file " + fileName + " already added.");
		}
		fileCommitMap.put(fileName, createOneElementList(new CommitDataExtended(commitData, churnValue)));
	}
	
	protected void performModify(CommitData commitData, String fileName, Integer churnValue) {
		List<CommitDataExtended> actualCommitDataExtendedList = fileCommitMap.get(fileName);
		if (actualCommitDataExtendedList == null) {
			System.out.println("    Error: the history of file " + fileName + " is empty at modify. Creating an empty list.");
			actualCommitDataExtendedList = new ArrayList<CommitDataExtended>();
			// At this point an exception should be thrown, as follows:
			//throw new RuntimeException("Error: the history of file " + fileName + " is empty at modify.");
			// However, there is the following case at Ant:
			// - /ant/core/trunk/build.xml already existed before commit r268587
			// - In commit r268587 /ant/core/branches/ANT_13_BRANCH was added from /ant/core/trunk, therefore /ant/core/branches/ANT_13_BRANCH/build.xml was created (and /ant/core/trunk/build.xml retained)
			// - Modified in commits r268633, r268639, r268667, and r268669
			// - In commit r268673 it was renamed to /ant/core/tags/ANT_13_B2/build.xml (operation R, therefore removing the original)
			// - However, in commit r268675 it was modified again (when theoretically not-existing).
			// The last point is erroneous I think.
		}
		actualCommitDataExtendedList.add(new CommitDataExtended(commitData, churnValue));
	}
	
	protected void performDelete(String fileName, List<CommitedFileData> committedFiles) {
		boolean somethingDeleted = false;
		// First case: the file name is found as is.
		if (fileCommitMap.containsKey(fileName)) {
			fileCommitMap.remove(fileName);
			somethingDeleted = true;
		}
		// Second case: the file name is a directory. Both the first and the second can find something in case of deleting a non-empty directrory.
		Set<String> fileNamesSet = fileCommitMap.keySet();
		List<String> fileNamesList = new ArrayList<String>();
		fileNamesList.addAll(fileNamesSet);
		for (String actualFileName : fileNamesList) {
			if (actualFileName.startsWith(fileName + "/")) {
				fileCommitMap.remove(actualFileName);
				somethingDeleted = true;
			}
		}
		// If the key fileName to be deleted is missing, the reason could be that a rename has already been happened in the same commit.
		// In the next few lines this is checked, and exception is thrown if not found.
		if (!somethingDeleted) {
			boolean foundRename = false;
			for (CommitedFileData committedFile : committedFiles) {
				if (committedFile.getFromFileName() != null && (committedFile.getFromFileName().equals(fileName) || fileName.startsWith(committedFile.getFromFileName() + "/"))) {
					foundRename = true;
					break;
				}
			}
			if (!foundRename) {
				System.out.println("    Error: the history of file " + fileName + " not found at delete.");
				// Question why it occurs.
				// throw new RuntimeException("Error: the history of file " + fileName + " not found at delete.");
			}
		}
	}
	
	protected void performRename(CommitData commitData, String fromName, String toName, boolean retain) {
		Set<String> fileNames = new HashSet<String>();
		fileNames.addAll(fileCommitMap.keySet());
		for (String fileName : fileNames) {
			String renamedFileName = RenameUtil.determineRenamedValue(fromName, toName, fileName);
			if (!fileName.equals(renamedFileName)) {
				List<CommitDataExtended> commitDataListNew = new ArrayList<CommitDataExtended>();
				commitDataListNew.addAll(fileCommitMap.get(fileName));
				commitDataListNew.add(new CommitDataExtended(commitData, 0));
				if (!retain) {
					fileCommitMap.remove(fileName);
				}
				fileCommitMap.put(renamedFileName, commitDataListNew);
			}
		}
	}
	
	protected static List<CommitDataExtended> createOneElementList(CommitDataExtended commitDataExtended) {
		List<CommitDataExtended> commitDataExtendedList = new ArrayList<CommitDataExtended>();
		commitDataExtendedList.add(commitDataExtended);
		return commitDataExtendedList;
	}
	
}
