package hotareadetector.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Testing populating commit data per file.
 */
public class CommitDataPerFileTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();	
	
	@Test
	public void testCreateOneElementList() {
		CommitDataExtended commitDataExtended = new CommitDataExtended();
		
		List<CommitDataExtended> oneElementList = CommitDataPerFile.createOneElementList(commitDataExtended);
		
		assertEquals(1, oneElementList.size());
		assertEquals(commitDataExtended, oneElementList.get(0));
	}
	
	private static String fileNameBasicCases = "/com/mycompany/myapp/Main.java";
	private static String fileNameAddFromCase = "/com/mycompany/myotherapp/Main.java";
	private static String fileNameForDirectoryRename = "/com/mycompany/myapplication/File.java";
	private static String renamedFileNameBasicCases = "/com/mycompany/myapp/ProgramEntry.java";
	private static Integer churnFirstScenarioFirstCommit = 3;
	private static Integer churnFirstScenarioSecondCommit = 4;
	
	/**
     * 1. commit:
     * - add /com/mycompany/myapp/Main.java, churn 3
	 */
	@Test
	public void testPerformAdd() {
		CommitDataPerFile commitDataPerFile = new CommitDataPerFile();
		executePerformAdd(commitDataPerFile);
		
		assertEquals(1, commitDataPerFile.getFileCommitMap().keySet().size());
		assertTrue(commitDataPerFile.getFileCommitMap().containsKey(fileNameBasicCases));
		assertEquals(1, commitDataPerFile.getFileCommitMap().get(fileNameBasicCases).size());
		assertEquals(churnFirstScenarioFirstCommit, commitDataPerFile.getFileCommitMap().get(fileNameBasicCases).get(0).getChurn());
		assertEquals(1, commitDataPerFile.getFileCommitMap().get(fileNameBasicCases).get(0).getCommitData().getCommitedFiles().size());
		assertEquals(fileNameBasicCases, commitDataPerFile.getFileCommitMap().get(fileNameBasicCases).get(0).getCommitData().getCommitedFiles().get(0).getFileName());
		assertEquals(OperationType.A, commitDataPerFile.getFileCommitMap().get(fileNameBasicCases).get(0).getCommitData().getCommitedFiles().get(0).getOperationType());
	}
	
	/**
	 * Add /com/mycompany/myapp/Main.java in the first and in the second commit as well. Check the expected error.
	 */
	@Test
	public void testPerformAddAlreadyAdded() {
		thrown.expect(RuntimeException.class);
		thrown.expectMessage("Error: file " + fileNameBasicCases + " already added.");
		
		CommitDataPerFile commitDataPerFile = new CommitDataPerFile();
		executePerformAdd(commitDataPerFile);
		executePerformAdd(commitDataPerFile);
	}
	
	/**
	 * 1. commit:
	 * - add /com/mycompany/myapp/Main.java
	 * 
	 * 2. commit
	 * - add /com/mycompany/myotherapp/Main.java (from /com/mycompany/myapp/Main.java)
	 * 
	 * Expected result:
	 * - history of /com/mycompany/myapp/Main.java is 1
	 * - history of /com/mycompany/myotherapp/Main.java is 2
	 */
	@Test
	public void testPerformAddFromOther() {
		CommitDataPerFile commitDataPerFile = new CommitDataPerFile();
		executePerformAdd(commitDataPerFile);
		executePerformAddFrom(commitDataPerFile);
		
		assertEquals(2, commitDataPerFile.getFileCommitMap().size());
		assertEquals(1, commitDataPerFile.getFileCommitMap().get(fileNameBasicCases).size());
		assertEquals(1, commitDataPerFile.getFileCommitMap().get(fileNameAddFromCase).size());
	}
	
	/**
     * 1. commit:
     * - add /com/mycompany/myapp/Main.java, churn 3
     * 
     * 2. commit:
     * - modify /com/mycompany/myapp/Main.java, churn 4
	 */
	@Test
	public void testPerformModify() {
		CommitDataPerFile commitDataPerFile = new CommitDataPerFile();
		executePerformAdd(commitDataPerFile);
		executePerformModify(commitDataPerFile);
		
		assertEquals(1, commitDataPerFile.getFileCommitMap().keySet().size());
		assertTrue(commitDataPerFile.getFileCommitMap().containsKey(fileNameBasicCases));
		assertEquals(2, commitDataPerFile.getFileCommitMap().get(fileNameBasicCases).size());
		
		assertEquals(churnFirstScenarioFirstCommit, commitDataPerFile.getFileCommitMap().get(fileNameBasicCases).get(0).getChurn());
		assertEquals(1, commitDataPerFile.getFileCommitMap().get(fileNameBasicCases).get(0).getCommitData().getCommitedFiles().size());
		assertEquals(fileNameBasicCases, commitDataPerFile.getFileCommitMap().get(fileNameBasicCases).get(0).getCommitData().getCommitedFiles().get(0).getFileName());
		assertEquals(OperationType.A, commitDataPerFile.getFileCommitMap().get(fileNameBasicCases).get(0).getCommitData().getCommitedFiles().get(0).getOperationType());
		
		assertEquals(churnFirstScenarioSecondCommit, commitDataPerFile.getFileCommitMap().get(fileNameBasicCases).get(1).getChurn());
		assertEquals(1, commitDataPerFile.getFileCommitMap().get(fileNameBasicCases).get(1).getCommitData().getCommitedFiles().size());
		assertEquals(fileNameBasicCases, commitDataPerFile.getFileCommitMap().get(fileNameBasicCases).get(1).getCommitData().getCommitedFiles().get(0).getFileName());
		assertEquals(OperationType.M, commitDataPerFile.getFileCommitMap().get(fileNameBasicCases).get(1).getCommitData().getCommitedFiles().get(0).getOperationType());
	}
	
	/**
	 * Add /com/mycompany/myapp/Main.java and then modify /com/mycompany/myapp/ProgramEntry.java.
	 * Exception is expected.
	 * 
	 * This test fails; see the explanation in the comment of file CommitDataPerFile.java, function performModify.
	 */
	@Ignore
	@Test
	public void testPerformModifyHistoryNotFound() {
		thrown.expect(RuntimeException.class);
		thrown.expectMessage("Error: the history of file " + renamedFileNameBasicCases + " is empty at modify.");
		
		CommitDataPerFile commitDataPerFile = new CommitDataPerFile();
		executePerformAdd(commitDataPerFile);
		executePerformModifyRenamed(commitDataPerFile);
	}
	
	/**
     * 1. commit:
     * - add /com/mycompany/myapp/Main.java, churn 3
     * 
     * 2. commit:
     * - delete /com/mycompany/myapp/Main.java
	 */
	@Test
	public void testPerformDelete() {
		CommitDataPerFile commitDataPerFile = new CommitDataPerFile();
		executePerformAdd(commitDataPerFile);
		executePerformDelete(commitDataPerFile);
		
		assertEquals(0, commitDataPerFile.getFileCommitMap().keySet().size());
	}
	
	/**
	 * 1. commit:
	 * - add /mydir
	 * - add /mydir/MyFile1.txt
	 * - add /mydir/MyFile2.txt
	 * - add /mydirectory
	 * - add /mydirectory/OtherFile.txt
	 * - add /mydir.html
	 * 
	 * 2. commit
	 * - delete /mydir
	 * 
	 * Expected result: /mydir, /mydir/MyFile1.txt, and /mydir/MyFile2.txt are deleted, the rest remains.
	 */
	@Test
	public void testPerformDeleteDirectory() {
		// preparation
		CommitDataPerFile commitDataPerFile = new CommitDataPerFile();
		
		CommitData commitData1 = new CommitData();
		CommitedFileData commitedFile1_1 = new CommitedFileData();
		commitedFile1_1.setFileName("/mydir");
		commitedFile1_1.setOperationType(OperationType.A);
		commitData1.addCommitedFile(commitedFile1_1);
		CommitedFileData commitedFile1_2 = new CommitedFileData();
		commitedFile1_2.setFileName("/mydir/MyFile1.txt");
		commitedFile1_2.setOperationType(OperationType.A);
		commitData1.addCommitedFile(commitedFile1_2);
		CommitedFileData commitedFile1_3 = new CommitedFileData();
		commitedFile1_3.setFileName("/mydir/MyFile2.txt");
		commitedFile1_3.setOperationType(OperationType.A);
		commitData1.addCommitedFile(commitedFile1_3);
		CommitedFileData commitedFile1_4 = new CommitedFileData();
		commitedFile1_4.setFileName("/mydirectory");
		commitedFile1_4.setOperationType(OperationType.A);
		commitData1.addCommitedFile(commitedFile1_4);
		CommitedFileData commitedFile1_5 = new CommitedFileData();
		commitedFile1_5.setFileName("/mydirectory/OtherFile.txt");
		commitedFile1_5.setOperationType(OperationType.A);
		commitData1.addCommitedFile(commitedFile1_5);
		CommitedFileData commitedFile1_6 = new CommitedFileData();
		commitedFile1_6.setFileName("/mydir.html");
		commitedFile1_6.setOperationType(OperationType.A);
		commitData1.addCommitedFile(commitedFile1_6);
		
		Map<String, Integer> churnData = new HashMap<String, Integer>();
		
		commitDataPerFile.addCommitData(commitData1, churnData);
		
		// test
		commitDataPerFile.performDelete("/mydir", null);
		
		
		// checks
		assertEquals(3, commitDataPerFile.getFileCommitMap().size());
		assertFalse(commitDataPerFile.getFileCommitMap().containsKey("/mydir"));
		assertFalse(commitDataPerFile.getFileCommitMap().containsKey("/mydir/MyFile1.txt"));
		assertFalse(commitDataPerFile.getFileCommitMap().containsKey("/mydir/MyFile2.txt"));
		assertTrue(commitDataPerFile.getFileCommitMap().containsKey("/mydirectory"));
		assertTrue(commitDataPerFile.getFileCommitMap().containsKey("/mydirectory/OtherFile.txt"));
		assertTrue(commitDataPerFile.getFileCommitMap().containsKey("/mydir.html"));
	}
	
	
	/**
	 * Performs delete on a non existing file.
	 * Ignored because of problem in Ant svn log.
	 */
	@Ignore
	@Test
	public void testPerformDeleteNotFound() {
		thrown.expect(RuntimeException.class);
		thrown.expectMessage("Error: the history of file " + "/myprefix" + fileNameBasicCases + " not found at delete.");
		
		CommitDataPerFile commitDataPerFile = new CommitDataPerFile();
		executePerformAdd(commitDataPerFile);
		commitDataPerFile.performDelete("/myprefix" + fileNameBasicCases, Arrays.asList(new CommitedFileData(OperationType.D, "/myprefix" + fileNameBasicCases, null)));
	}
	
	/**
	 * Performs delete on a non-existing file /myprefix/com/mycompany/myapp/Main.java, but act as /myprefix/com/mycompany/myapp/Main.java
	 * has just been renamed to /com/mycompany/myapp/Main.java. Therefore in this case we just check no exception is thrown.
	 */
	@Test
	public void testPerformDeleteWithRename() {
		CommitDataPerFile commitDataPerFile = new CommitDataPerFile();
		
		commitDataPerFile.performDelete("/myprefix" + fileNameBasicCases, Arrays.asList(
				new CommitedFileData(OperationType.R, fileNameBasicCases, "/myprefix" + fileNameBasicCases),
				new CommitedFileData(OperationType.D, "/myprefix" + fileNameBasicCases, null)				
			));
		
		assertEquals(0, commitDataPerFile.getFileCommitMap().keySet().size());
	}
	
	
	/**
     * 1. commit:
     * - add /com/mycompany/myapp/Main.java, churn 3
     * 
     * 2. commit:
     * - rename /com/mycompany/myapp/Main.java to /com/mycompany/myapp/ProgramEntry.java
	 */
	@Test
	public void testPerformRenameFile() {
		CommitDataPerFile commitDataPerFile = new CommitDataPerFile();
		executePerformAdd(commitDataPerFile);
		executePerformRenameFile(commitDataPerFile);
		
		assertEquals(1, commitDataPerFile.getFileCommitMap().keySet().size());
		assertTrue(commitDataPerFile.getFileCommitMap().containsKey(renamedFileNameBasicCases));
		assertEquals(2, commitDataPerFile.getFileCommitMap().get(renamedFileNameBasicCases).size());
		
		assertEquals(churnFirstScenarioFirstCommit, commitDataPerFile.getFileCommitMap().get(renamedFileNameBasicCases).get(0).getChurn());
		assertEquals(1, commitDataPerFile.getFileCommitMap().get(renamedFileNameBasicCases).get(0).getCommitData().getCommitedFiles().size());
		assertEquals(fileNameBasicCases, commitDataPerFile.getFileCommitMap().get(renamedFileNameBasicCases).get(0).getCommitData().getCommitedFiles().get(0).getFileName());
		assertEquals(OperationType.A, commitDataPerFile.getFileCommitMap().get(renamedFileNameBasicCases).get(0).getCommitData().getCommitedFiles().get(0).getOperationType());
		
		assertEquals(new Integer(0), commitDataPerFile.getFileCommitMap().get(renamedFileNameBasicCases).get(1).getChurn());
		assertEquals(1, commitDataPerFile.getFileCommitMap().get(renamedFileNameBasicCases).get(1).getCommitData().getCommitedFiles().size());
		assertEquals(renamedFileNameBasicCases, commitDataPerFile.getFileCommitMap().get(renamedFileNameBasicCases).get(1).getCommitData().getCommitedFiles().get(0).getFileName());
		assertEquals(fileNameBasicCases, commitDataPerFile.getFileCommitMap().get(renamedFileNameBasicCases).get(1).getCommitData().getCommitedFiles().get(0).getFromFileName());
		assertEquals(OperationType.R, commitDataPerFile.getFileCommitMap().get(renamedFileNameBasicCases).get(1).getCommitData().getCommitedFiles().get(0).getOperationType());
	}
	
	/**
     * 1. commit:
     * - add /com/mycompany/myapp/Main.java, churn 3
     * 
     * 2. commit:
     * - rename /com/mycompany/myapp to /org/mycompany/myapp
	 */
	@Test
	public void testPerformRenameDirectory() {
		CommitDataPerFile commitDataPerFile = new CommitDataPerFile();
		executePerformAdd(commitDataPerFile);
		executePerformRenameDirectory(commitDataPerFile);
		
		String renamedFileNameDirectoryRenameScenario = "/org/mycompany/myapp/Main.java";
		
		assertEquals(1, commitDataPerFile.getFileCommitMap().keySet().size());
		assertTrue(commitDataPerFile.getFileCommitMap().containsKey(renamedFileNameDirectoryRenameScenario));
		assertEquals(2, commitDataPerFile.getFileCommitMap().get(renamedFileNameDirectoryRenameScenario).size());
		
		assertEquals(churnFirstScenarioFirstCommit, commitDataPerFile.getFileCommitMap().get(renamedFileNameDirectoryRenameScenario).get(0).getChurn());
		assertEquals(1, commitDataPerFile.getFileCommitMap().get(renamedFileNameDirectoryRenameScenario).get(0).getCommitData().getCommitedFiles().size());
		
		assertEquals(new Integer(0), commitDataPerFile.getFileCommitMap().get(renamedFileNameDirectoryRenameScenario).get(1).getChurn());
		assertEquals(1, commitDataPerFile.getFileCommitMap().get(renamedFileNameDirectoryRenameScenario).get(1).getCommitData().getCommitedFiles().size());
	}

	/**
     * 1. commit:
     * - add /com/mycompany/myapp/Main.java, churn 3
     * - add /com/mycompany/myapplication/File.java, churn 5
     * 
     * 2. commit:
     * - rename /com/mycompany/my to /org/mycompany/my
     * 
     * The rename is applicable on /com/mycompany/myapp/Main.java only.
	 */
	@Test
	public void testPerformFalseRenameDirectory() {
		CommitDataPerFile commitDataPerFile = new CommitDataPerFile();
		executePerformAddTwoFiles(commitDataPerFile);
		executePerformRenameDirectory(commitDataPerFile);
		
		String renamedFileNameDirectoryRenameScenario = "/org/mycompany/myapp/Main.java";
		
		assertEquals(2, commitDataPerFile.getFileCommitMap().keySet().size());
		
		assertTrue(commitDataPerFile.getFileCommitMap().containsKey(renamedFileNameDirectoryRenameScenario));
		assertFalse(commitDataPerFile.getFileCommitMap().containsKey(fileNameBasicCases));
		assertTrue(commitDataPerFile.getFileCommitMap().containsKey(fileNameForDirectoryRename));
		
		assertEquals(2, commitDataPerFile.getFileCommitMap().get(renamedFileNameDirectoryRenameScenario).size());
		assertEquals(1, commitDataPerFile.getFileCommitMap().get(fileNameForDirectoryRename).size());
	}
	
	private void executePerformAdd(CommitDataPerFile commitDataPerFile) {
		CommitData commitData = new CommitData();
		CommitedFileData commitedFile = new CommitedFileData();
		commitedFile.setFileName(fileNameBasicCases);
		commitedFile.setOperationType(OperationType.A);
		commitData.addCommitedFile(commitedFile);
		commitDataPerFile.performAdd(commitData, fileNameBasicCases, churnFirstScenarioFirstCommit);
	}
	
	private void executePerformAddFrom(CommitDataPerFile commitDataPerFile) {
		CommitData commitData = new CommitData();
		CommitedFileData commitedFile = new CommitedFileData();
		commitedFile.setFileName(fileNameAddFromCase);
		commitedFile.setFromFileName(fileNameBasicCases);
		commitedFile.setOperationType(OperationType.A);
		commitData.addCommitedFile(commitedFile);
		commitDataPerFile.performAdd(commitData, fileNameAddFromCase, 0);
	}
	
	private void executePerformAddTwoFiles(CommitDataPerFile commitDataPerFile) {
		CommitedFileData commitedFile1 = new CommitedFileData();
		commitedFile1.setFileName(fileNameBasicCases);
		commitedFile1.setOperationType(OperationType.A);
		
		CommitedFileData commitedFile2 = new CommitedFileData();
		commitedFile2.setFileName(fileNameForDirectoryRename);
		commitedFile2.setOperationType(OperationType.A);
		
		CommitData commitData = new CommitData();
		commitData.addCommitedFile(commitedFile1);
		commitData.addCommitedFile(commitedFile2);
		
		commitDataPerFile.performAdd(commitData, fileNameBasicCases, churnFirstScenarioFirstCommit);
		commitDataPerFile.performAdd(commitData, fileNameForDirectoryRename, churnFirstScenarioFirstCommit + 2);
	}
	
	private void executePerformModify(CommitDataPerFile commitDataPerFile) {
		CommitData commitData = new CommitData();
		CommitedFileData commitedFile = new CommitedFileData();
		commitedFile.setFileName(fileNameBasicCases);
		commitedFile.setOperationType(OperationType.M);
		commitData.addCommitedFile(commitedFile);
		commitDataPerFile.performModify(commitData, fileNameBasicCases, churnFirstScenarioSecondCommit);
	}
	
	private void executePerformModifyRenamed(CommitDataPerFile commitDataPerFile) {
		CommitData commitData = new CommitData();
		CommitedFileData commitedFile = new CommitedFileData();
		commitedFile.setFileName(renamedFileNameBasicCases);
		commitedFile.setOperationType(OperationType.M);
		commitData.addCommitedFile(commitedFile);
		commitDataPerFile.performModify(commitData, renamedFileNameBasicCases, churnFirstScenarioSecondCommit + 3);
	}
	
	private void executePerformDelete(CommitDataPerFile commitDataPerFile) {
		commitDataPerFile.performDelete(fileNameBasicCases, Arrays.asList(new CommitedFileData(OperationType.D, fileNameBasicCases, null)));
	}
	
	private void executePerformRenameFile(CommitDataPerFile commitDataPerFile) {
		CommitData commitData = new CommitData();
		CommitedFileData commitedFile = new CommitedFileData();
		commitedFile.setFileName(renamedFileNameBasicCases);
		commitedFile.setFromFileName(fileNameBasicCases);
		commitedFile.setOperationType(OperationType.R);
		commitData.addCommitedFile(commitedFile);
		commitDataPerFile.performRename(commitData, fileNameBasicCases, renamedFileNameBasicCases, false);
	}
	
	private void executePerformRenameDirectory(CommitDataPerFile commitDataPerFile) {
		CommitData commitData = new CommitData();
		CommitedFileData commitedFile = new CommitedFileData();
		commitedFile.setFileName("/org/mycompany/myapp");
		commitedFile.setFromFileName("/com/mycompany/myapp");
		commitedFile.setOperationType(OperationType.R);
		commitData.addCommitedFile(commitedFile);
		commitDataPerFile.performRename(commitData, "/com/mycompany/myapp", "/org/mycompany/myapp", false);
	}
	
	
	/**
	 * More complex scenario, containing most of the possible cases.
	 * 
	 * 1.
	 * - add /com/mycompany/myapp/Main.java
	 * - add /com/mycompany/myapp/Util.java
	 * 
	 * 2.
	 * - add /com/mycompany/myapp/Game.java
	 * - modify /com/mycompany/myapp/Main.java
	 * 
	 * 3.
	 * - rename /com/mycompany/myapp/Util.java -> /com/mycompany/myapp/Utilities.java
	 * - delete /com/mycompany/myapp/Util.java
	 * 
	 * 4.
	 * - delete /com/mycompany/myapp/Main.java
	 * 
	 * 5.
	 * - rename /com/mycompany/myapp -> /org/mycompany/myapp
	 * 
	 * 6.
	 * - add /org/mycompany/myapp/EntryPoint.java
	 * 
	 * Expected final result (affected commits):
	 * - /org/mycompany/myapp/Utilities.java (1, 3, 5)
	 * - /org/mycompany/myapp/Game.java (2, 5)
	 * - /org/mycompany/myapp/EntryPoint.java (6)
	 */	
	@Test
	public void testCommitDataPerFile() {
		CommitDataPerFile commitDataPerFile = new CommitDataPerFile();
		
		// first commit
		List<CommitedFileData> commitedFiles_1 = new ArrayList<CommitedFileData>();
		commitedFiles_1.add(new CommitedFileData(OperationType.A, "/com/mycompany/myapp/Main.java", null));
		commitedFiles_1.add(new CommitedFileData(OperationType.A, "/com/mycompany/myapp/Util.java", null));		
		Map<String, Integer> churnData_1 = new HashMap<String, Integer>();
		churnData_1.put("/com/mycompany/myapp/Main.java", 5);
		churnData_1.put("/com/mycompany/myapp/Util.java", 6);
		commitDataPerFile.addCommitData(new CommitData(1, "sully", new Date(1469021111566L), "First commit.", commitedFiles_1), churnData_1);
		
		// second commit
		List<CommitedFileData> commitedFiles_2 = new ArrayList<CommitedFileData>();
		commitedFiles_2.add(new CommitedFileData(OperationType.A, "/com/mycompany/myapp/Game.java", null));
		commitedFiles_2.add(new CommitedFileData(OperationType.M, "/com/mycompany/myapp/Main.java", null));
		Map<String, Integer> churnData_2 = new HashMap<String, Integer>();
		churnData_2.put("/com/mycompany/myapp/Game.java", 7);
		churnData_2.put("/com/mycompany/myapp/Main.java", 8);
		commitDataPerFile.addCommitData(new CommitData(2, "mike", new Date(1469021211566L), "Second commit.", commitedFiles_2), churnData_2);
		
		// third commit
		List<CommitedFileData> commitedFiles_3 = new ArrayList<CommitedFileData>();
		commitedFiles_3.add(new CommitedFileData(OperationType.R, "/com/mycompany/myapp/Utilities.java", "/com/mycompany/myapp/Util.java"));
		commitedFiles_3.add(new CommitedFileData(OperationType.D, "/com/mycompany/myapp/Util.java", null));
		Map<String, Integer> churnData_3 = new HashMap<String, Integer>();
		commitDataPerFile.addCommitData(new CommitData(3, "mike", new Date(1469021311566L), "Third commit.", commitedFiles_3), churnData_3);
		
		// fourth commit
		List<CommitedFileData> commitedFiles_4 = new ArrayList<CommitedFileData>();
		commitedFiles_4.add(new CommitedFileData(OperationType.D, "/com/mycompany/myapp/Main.java", null));
		Map<String, Integer> churnData_4 = new HashMap<String, Integer>();
		commitDataPerFile.addCommitData(new CommitData(4, "mike", new Date(1469021411566L), "Fourth commit.", commitedFiles_4), churnData_4);
		
		// fifth commit
		List<CommitedFileData> commitedFiles_5 = new ArrayList<CommitedFileData>();
		commitedFiles_5.add(new CommitedFileData(OperationType.R, "/org/mycompany/myapp", "/com/mycompany/myapp"));
		Map<String, Integer> churnData_5 = new HashMap<String, Integer>();
		commitDataPerFile.addCommitData(new CommitData(5, "mike", new Date(1469021511566L), "Fifth commit.", commitedFiles_5), churnData_5);
		
		// sixth commit
		List<CommitedFileData> commitedFiles_6 = new ArrayList<CommitedFileData>();
		commitedFiles_6.add(new CommitedFileData(OperationType.A, "/org/mycompany/myapp/EntryPoint.java", null));
		Map<String, Integer> churnData_6 = new HashMap<String, Integer>();
		churnData_6.put("/org/mycompany/myapp/EntryPoint.java", 9);
		commitDataPerFile.addCommitData(new CommitData(6, "mike", new Date(1469021611566L), "Sixth commit.", commitedFiles_6), churnData_6);
		
		assertEquals(3, commitDataPerFile.getFileCommitMap().size());
		assertEquals(3, commitDataPerFile.getFileCommitMap().get("/org/mycompany/myapp/Utilities.java").size());
		assertEquals(1, commitDataPerFile.getFileCommitMap().get("/org/mycompany/myapp/Utilities.java").get(0).getCommitData().getRevisionNumber());
		assertEquals(new Integer(6), commitDataPerFile.getFileCommitMap().get("/org/mycompany/myapp/Utilities.java").get(0).getChurn());
		assertEquals(3, commitDataPerFile.getFileCommitMap().get("/org/mycompany/myapp/Utilities.java").get(1).getCommitData().getRevisionNumber());
		assertEquals(5, commitDataPerFile.getFileCommitMap().get("/org/mycompany/myapp/Utilities.java").get(2).getCommitData().getRevisionNumber());
		assertEquals(2, commitDataPerFile.getFileCommitMap().get("/org/mycompany/myapp/Game.java").size());
		assertEquals(2, commitDataPerFile.getFileCommitMap().get("/org/mycompany/myapp/Game.java").get(0).getCommitData().getRevisionNumber());
		assertEquals(5, commitDataPerFile.getFileCommitMap().get("/org/mycompany/myapp/Game.java").get(1).getCommitData().getRevisionNumber());
		assertEquals(1, commitDataPerFile.getFileCommitMap().get("/org/mycompany/myapp/EntryPoint.java").size());
		assertEquals(6, commitDataPerFile.getFileCommitMap().get("/org/mycompany/myapp/EntryPoint.java").get(0).getCommitData().getRevisionNumber());
	}
}
