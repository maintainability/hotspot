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
			switch (committedFile.getOperationType()) {
			case A:
				if (committedFile.getFromFileName() == null) {
					performAdd(commitData, fileName, churnData.containsKey(fileName) ? churnData.get(fileName) : null);
				} else {
					performRename(commitData, committedFile.getFromFileName(), fileName, true);
				}
				break;
				
			case M:
				performModify(commitData, fileName, churnData.containsKey(fileName) ? churnData.get(fileName) : null);
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
