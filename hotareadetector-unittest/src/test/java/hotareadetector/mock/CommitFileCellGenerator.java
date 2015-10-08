package hotareadetector.mock;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import hotareadetector.data.CommitFileCell;
import hotareadetector.data.OperationType;

/**
 * Helper class for generating commit-file cells.
 */
public class CommitFileCellGenerator {
	
	/**
	 * Creates one commit-file cell.
	 */
	public static CommitFileCell generateOneCommitFileCell() {
		CommitFileCell commitFileCell = new CommitFileCell();
		commitFileCell.setFileName("Main.java");
		commitFileCell.setRevision(5);
		
		return commitFileCell;
	}

	/**
	 * Creates commit data with 3 files.
	 */
	public static List<CommitFileCell> generateOneCommitThreeFileCells() {
		CommitFileCell commitFileCell1 = new CommitFileCell();
		commitFileCell1.setFileName("Main.java");
		commitFileCell1.setRevision(2);
		
		CommitFileCell commitFileCell2 = new CommitFileCell();
		commitFileCell2.setFileName("Game.java");
		commitFileCell2.setRevision(2);
		
		CommitFileCell commitFileCell3 = new CommitFileCell();
		commitFileCell3.setFileName("Data.java");
		commitFileCell3.setRevision(2);
		
		List<CommitFileCell> commitFileData = new ArrayList<CommitFileCell>();
		commitFileData.add(commitFileCell1);
		commitFileData.add(commitFileCell2);
		commitFileData.add(commitFileCell3);
		
		return commitFileData;
	}
	
	/**
	 * Creates a commit with 2 files. To be used with generateSecondCommitTwoFileCells().
	 */
	public static List<CommitFileCell> generateFirstCommitThreeFileCells() {
		CommitFileCell commitFileCell11 = new CommitFileCell();
		commitFileCell11.setFileName("Main.java");
		commitFileCell11.setRevision(1);
		commitFileCell11.setNumberOfModifications(1);
		commitFileCell11.setChurnValue(5);
		commitFileCell11.setChurnValueFiner(5);
		commitFileCell11.addContributor("mike");
		commitFileCell11.setLatestOperation(OperationType.A);
		
		CommitFileCell commitFileCell12 = new CommitFileCell();
		commitFileCell12.setFileName("Game.java");
		commitFileCell12.setRevision(1);
		commitFileCell12.setNumberOfModifications(1);
		commitFileCell12.setChurnValue(10);
		commitFileCell12.setChurnValueFiner(10);
		commitFileCell12.addContributor("mike");
		commitFileCell12.setLatestOperation(OperationType.A);
		
		CommitFileCell commitFileCell13 = new CommitFileCell();
		commitFileCell13.setFileName("Test.java");
		commitFileCell13.setRevision(1);
		commitFileCell13.setNumberOfModifications(1);
		commitFileCell13.setChurnValue(1);
		commitFileCell13.setChurnValueFiner(1);
		commitFileCell13.addContributor("mike");
		commitFileCell13.setLatestOperation(OperationType.A);
		
		List<CommitFileCell> firstCommit = new ArrayList<CommitFileCell>();
		firstCommit.add(commitFileCell11);
		firstCommit.add(commitFileCell12);
		firstCommit.add(commitFileCell13);
		
		return firstCommit;
	}
	
	/**
	 * Creates a commit with 2 files. To be used with generateFirstCommitTwoFileCells().
	 */
	public static List<CommitFileCell> generateSecondCommitThreeFileCells() {
		CommitFileCell commitFileCell21 = new CommitFileCell();
		commitFileCell21.setFileName("Game.java");
		commitFileCell21.setRevision(2);
		commitFileCell21.setNumberOfModifications(2);
		commitFileCell21.setChurnValue(25);
		commitFileCell21.setChurnValueFiner(15);
		commitFileCell21.addContributor("mike");
		commitFileCell21.addContributor("sully");
		commitFileCell21.setLatestOperation(OperationType.M);
		
		CommitFileCell commitFileCell22 = new CommitFileCell();
		commitFileCell22.setFileName("Data.java");
		commitFileCell22.setRevision(2);
		commitFileCell22.setNumberOfModifications(1);
		commitFileCell22.setChurnValue(20);
		commitFileCell22.setChurnValueFiner(20);
		commitFileCell22.addContributor("sully");
		commitFileCell22.setLatestOperation(OperationType.M);
		
		CommitFileCell commitFileCell23 = new CommitFileCell();
		commitFileCell23.setFileName("Test.java");
		commitFileCell23.setRevision(2);
		commitFileCell23.setNumberOfModifications(2);
		commitFileCell23.setChurnValue(2);
		commitFileCell23.setChurnValueFiner(2);
		commitFileCell23.addContributor("sully");
		commitFileCell23.setLatestOperation(OperationType.D);
		
		List<CommitFileCell> secondCommit = new ArrayList<CommitFileCell>();
		secondCommit.add(commitFileCell21);
		secondCommit.add(commitFileCell22);
		secondCommit.add(commitFileCell23);
		
		return secondCommit;
	}
	
	/**
	 * Creates a commit with one file. To be used with generateFirstCommitTwoFileCells() and generateSecondCommitTwoFileCells().
	 */
	public static List<CommitFileCell> generateThirdCommitOneFileCell() {
		CommitFileCell commitFileCell31 = new CommitFileCell();
		commitFileCell31.setFileName("Main.java");
		commitFileCell31.setRevision(3);
		commitFileCell31.setNumberOfModifications(2);
		commitFileCell31.setChurnValue(10);
		commitFileCell31.setChurnValueFiner(10);
		commitFileCell31.addContributor("mike");
		commitFileCell31.addContributor("mike");
		commitFileCell31.setLatestOperation(OperationType.M);
		
		List<CommitFileCell> thirdCommit = new ArrayList<CommitFileCell>();
		thirdCommit.add(commitFileCell31);
		
		return thirdCommit;
	}

	/**
	 * First commit for directory rename test.
	 */
	public static List<CommitFileCell> generateCommitForDirectoryRename() {
		CommitFileCell commitFileCell1 = new CommitFileCell();
		commitFileCell1.setFileName("/dir");
		commitFileCell1.setRevision(1);
		commitFileCell1.setNumberOfModifications(1);
		commitFileCell1.setChurnValue(0);
		commitFileCell1.setChurnValueFiner(0);
		commitFileCell1.addContributor("user1");
		commitFileCell1.setLatestOperation(OperationType.A);
		
		CommitFileCell commitFileCell2 = new CommitFileCell();
		commitFileCell2.setFileName("/dir/file1.txt");
		commitFileCell2.setRevision(1);
		commitFileCell2.setNumberOfModifications(1);
		commitFileCell2.setChurnValue(5);
		commitFileCell2.setChurnValueFiner(2);
		commitFileCell2.addContributor("user1");
		commitFileCell2.setLatestOperation(OperationType.A);
		
		CommitFileCell commitFileCell3 = new CommitFileCell();
		commitFileCell3.setFileName("/dir/file2.txt");
		commitFileCell3.setRevision(1);
		commitFileCell3.setNumberOfModifications(1);
		commitFileCell3.setChurnValue(10);
		commitFileCell3.setChurnValueFiner(7);
		commitFileCell3.addContributor("user1");
		commitFileCell3.setLatestOperation(OperationType.A);
		
		CommitFileCell commitFileCell4 = new CommitFileCell();
		commitFileCell4.setFileName("/dir2");
		commitFileCell4.setRevision(1);
		commitFileCell4.setNumberOfModifications(1);
		commitFileCell4.setChurnValue(0);
		commitFileCell4.setChurnValueFiner(0);
		commitFileCell4.addContributor("user1");
		commitFileCell4.setLatestOperation(OperationType.A);
		
		CommitFileCell commitFileCell5 = new CommitFileCell();
		commitFileCell5.setFileName("/dir2/file1.txt");
		commitFileCell5.setRevision(1);
		commitFileCell5.setNumberOfModifications(1);
		commitFileCell5.setChurnValue(15);
		commitFileCell5.setChurnValueFiner(12);
		commitFileCell5.addContributor("user1");
		commitFileCell5.setLatestOperation(OperationType.A);
		
		List<CommitFileCell> commitForDirRename = new ArrayList<CommitFileCell>();
		commitForDirRename.add(commitFileCell1);
		commitForDirRename.add(commitFileCell2);
		commitForDirRename.add(commitFileCell3);
		commitForDirRename.add(commitFileCell4);
		commitForDirRename.add(commitFileCell5);
		
		return commitForDirRename;
	}

	/**
	 * Creates a commit file cell with already filled in fields.
	 */
	public static CommitFileCell createCommitFileCellWithPredefinedValues() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		CommitFileCell commitFileCell = new CommitFileCell();
		setPrivateField(commitFileCell, "churnValue", 7);
		setPrivateField(commitFileCell, "churnValueFiner", 6);
		setPrivateField(commitFileCell, "numberOfModifications", 5);
		setPrivateField(commitFileCell, "numberOfContributors", 3);
		setPrivateField(commitFileCell, "numberOfContributorsToleranceOne", 2);
		setPrivateField(commitFileCell, "numberOfContributorsToleranceTwo", 1);
		setPrivateField(commitFileCell, "finished", true);
		return commitFileCell;
	}
	
	/**
	 * Sets a private field of an object to the specified value.
	 */
	private static void setPrivateField(Object object, String field, Object value) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		Field numberOfContributors = object.getClass().getDeclaredField(field);
		numberOfContributors.setAccessible(true);
		numberOfContributors.set(object, value);
	}

}
