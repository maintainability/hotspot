package hotareadetector.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import hotareadetector.data.CommitData;
import hotareadetector.data.CommitFileCell;
import hotareadetector.data.CommitFileMatrix;
import hotareadetector.data.CommitedFileData;
import hotareadetector.data.FileDiffInformation;
import hotareadetector.data.HotAreaDetectorContext;
import hotareadetector.data.OperationType;
import hotareadetector.interfaces.SourceControlCommandExecutor;
import hotareadetector.interfaces.SourceControlCommandParser;
import hotareadetector.mock.CommitDataGenerator;
import hotareadetector.mock.CommitFileMatrixGenerator;
import hotareadetector.mock.SourceControlCommandExecutorMock;
import hotareadetector.svn.SvnCommandParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

/**
 * Unit tests for source control logic check.
 */
public class SourceControlLogicTest {

	/**
	 * Considering a complete series of 18 commits, including directory and file additions, modifications
	 * deletions, rename, including modification son several places or affecting several files at the same time,
	 * perform the deep analysis (i.e. with file diffs, to calculate churn values) and check the calculated results.
	 */
	@Test
	public void testReadCommitDataWithDeepAnalysis() throws IOException {
		testReadCommitData(true);
	}

	/**
	 * Similar to the previous one, but without deep analysis in this case.
	 */
	@Test
	public void testReadCommitDataNoDeepAnalysis() throws IOException {
		testReadCommitData(false);
	}

	private void testReadCommitData(boolean deepAnalysis) throws IOException {
		SourceControlLogic sourceControlLogic = new SourceControlLogic();
		SourceControlCommandExecutor executor = new SourceControlCommandExecutorMock();		
		SourceControlCommandParser parser = new SvnCommandParser();
		
		HotAreaDetectorContext context = new HotAreaDetectorContext();
		context.setDeepAnalysis(deepAnalysis);
		CommitFileMatrix commitFileMatrix = sourceControlLogic.readCommitData(executor, parser, context);
		
		Set<String> fileNames = commitFileMatrix.getFileNames();
		
		if (deepAnalysis) {
			assertEquals(17, ((SourceControlCommandExecutorMock)executor).getNumberOfDiffInvocations());
			// 17 because there is an error at call BASE:r1. Anyway, it would be 18.
		} else {
			assertEquals(0, ((SourceControlCommandExecutorMock)executor).getNumberOfDiffInvocations());
		}
		assertEquals(14, fileNames.size());
		
		Set<String> fileNamesExpected = new HashSet<String>();
		fileNamesExpected.add("/trunk/myproject/reallyalongtext.txt");
		fileNamesExpected.add("/trunk/myproject/longtext.txt");
		fileNamesExpected.add("/trunk/myfolder/filename with spaces.txt");
		fileNamesExpected.add("/trunk/myproject/myrenamedtext.txt");
		fileNamesExpected.add("/trunk/myfolder");
		fileNamesExpected.add("/trunk/myfolder/myfile.txt");
		fileNamesExpected.add("/trunk/myproject/addnewfile.txt");
		fileNamesExpected.add("/trunk/myproject/myaddedfile.txt");
		fileNamesExpected.add("/trunk/myproject/mysecontext.txt");
		fileNamesExpected.add("/trunk/myproject/mytext.txt");
		fileNamesExpected.add("/trunk/myproject");
		fileNamesExpected.add("/branches");
		fileNamesExpected.add("/tags");
		fileNamesExpected.add("/trunk");
		assertTrue(fileNames.containsAll(fileNamesExpected));
		assertFalse(fileNames.contains("notexisting.file"));
		
		for (String fileName : fileNames) {
			CommitFileCell fileData = commitFileMatrix.getFileDataLatest(fileName);
			switch (fileName) {
			case "/trunk/myproject/reallyalongtext.txt":
				assertEquals(18, fileData.getRevision());
				assertEquals(OperationType.M, fileData.getLatestOperation());
				assertEquals(1, fileData.getNumberOfContributors());
				assertEquals(1, fileData.getNumberOfContributorsToleranceOne());
				assertEquals(1, fileData.getNumberOfContributorsToleranceTwo());
				assertEquals(3, fileData.getNumberOfModifications());
				if (deepAnalysis) {
					assertEquals(262, fileData.getChurnValueCoarse());
					assertEquals(190, fileData.getChurnValueFine());
				} else {
					assertEquals(0, fileData.getChurnValueCoarse());
					assertEquals(0, fileData.getChurnValueFine());
				}
				break;
				
			case "/trunk/myproject/longtext.txt":
				assertEquals(15, fileData.getRevision());
				assertEquals(OperationType.M, fileData.getLatestOperation());
				assertEquals(1, fileData.getNumberOfContributors());
				assertEquals(1, fileData.getNumberOfContributorsToleranceOne());
				assertEquals(0, fileData.getNumberOfContributorsToleranceTwo());
				assertEquals(2, fileData.getNumberOfModifications());
				if (deepAnalysis) {
					assertEquals(39, fileData.getChurnValueCoarse());
					assertEquals(17, fileData.getChurnValueFine());
				} else {
					assertEquals(0, fileData.getChurnValueCoarse());
					assertEquals(0, fileData.getChurnValueFine());
				}
				break;
				
			case "/trunk/myfolder/filename with spaces.txt":
				assertEquals(13, fileData.getRevision());
				assertEquals(OperationType.A, fileData.getLatestOperation());
				assertEquals(1, fileData.getNumberOfContributors());
				assertEquals(0, fileData.getNumberOfContributorsToleranceOne());
				assertEquals(0, fileData.getNumberOfContributorsToleranceTwo());
				assertEquals(1, fileData.getNumberOfModifications());
				if (deepAnalysis) {
					assertEquals(1, fileData.getChurnValueCoarse());
					assertEquals(1, fileData.getChurnValueFine());
				} else {
					assertEquals(0, fileData.getChurnValueCoarse());
					assertEquals(0, fileData.getChurnValueFine());
				}
				break;
				
			case "/trunk/myproject/myrenamedtext.txt":
				assertEquals(11, fileData.getRevision());
				assertEquals(OperationType.R, fileData.getLatestOperation());
				assertEquals(2, fileData.getNumberOfContributors());
				assertEquals(1, fileData.getNumberOfContributorsToleranceOne());
				assertEquals(1, fileData.getNumberOfContributorsToleranceTwo());
				assertEquals(4, fileData.getNumberOfModifications());
				if (deepAnalysis) {
					assertEquals(12, fileData.getChurnValueCoarse());
					assertEquals(6, fileData.getChurnValueFine());
				} else {
					assertEquals(0, fileData.getChurnValueCoarse());
					assertEquals(0, fileData.getChurnValueFine());
				}
				break;
				
			case "/trunk/myfolder":
				assertEquals(10, fileData.getRevision());
				assertEquals(OperationType.A, fileData.getLatestOperation());
				assertEquals(1, fileData.getNumberOfContributors());
				assertEquals(0, fileData.getNumberOfContributorsToleranceOne());
				assertEquals(0, fileData.getNumberOfContributorsToleranceTwo());
				assertEquals(1, fileData.getNumberOfModifications());
				if (deepAnalysis) {
					assertEquals(0, fileData.getChurnValueCoarse());
					assertEquals(0, fileData.getChurnValueFine());
				} else {
					assertEquals(0, fileData.getChurnValueCoarse());
					assertEquals(0, fileData.getChurnValueFine());
				}
				break;
				
			case "/trunk/myfolder/myfile.txt":
				assertEquals(12, fileData.getRevision());
				assertEquals(OperationType.M, fileData.getLatestOperation());
				assertEquals(1, fileData.getNumberOfContributors());
				assertEquals(1, fileData.getNumberOfContributorsToleranceOne());
				assertEquals(0, fileData.getNumberOfContributorsToleranceTwo());
				assertEquals(2, fileData.getNumberOfModifications());
				if (deepAnalysis) {
					assertEquals(4, fileData.getChurnValueCoarse());
					assertEquals(2, fileData.getChurnValueFine());
				} else {
					assertEquals(0, fileData.getChurnValueCoarse());
					assertEquals(0, fileData.getChurnValueFine());
				}
				break;
				
			case "/trunk/myproject/addnewfile.txt":
				assertEquals(9, fileData.getRevision());
				assertEquals(OperationType.A, fileData.getLatestOperation());
				assertEquals(1, fileData.getNumberOfContributors());
				assertEquals(0, fileData.getNumberOfContributorsToleranceOne());
				assertEquals(0, fileData.getNumberOfContributorsToleranceTwo());
				assertEquals(1, fileData.getNumberOfModifications());
				if (deepAnalysis) {
					assertEquals(1, fileData.getChurnValueCoarse());
					assertEquals(1, fileData.getChurnValueFine());
				} else {
					assertEquals(0, fileData.getChurnValueCoarse());
					assertEquals(0, fileData.getChurnValueFine());
				}
				break;
				
			case "/trunk/myproject/myaddedfile.txt":
				assertEquals(9, fileData.getRevision());
				assertEquals(OperationType.D, fileData.getLatestOperation());
				assertEquals(1, fileData.getNumberOfContributors());
				assertEquals(1, fileData.getNumberOfContributorsToleranceOne());
				assertEquals(0, fileData.getNumberOfContributorsToleranceTwo());
				assertEquals(2, fileData.getNumberOfModifications());
				if (deepAnalysis) {
					assertEquals(2, fileData.getChurnValueCoarse());
					assertEquals(2, fileData.getChurnValueFine());
				} else {
					assertEquals(0, fileData.getChurnValueCoarse());
					assertEquals(0, fileData.getChurnValueFine());
				}
				break;
				
			case "/trunk/myproject/mysecontext.txt":
				assertEquals(18, fileData.getRevision());
				assertEquals(OperationType.M, fileData.getLatestOperation());
				assertEquals(2, fileData.getNumberOfContributors());
				assertEquals(1, fileData.getNumberOfContributorsToleranceOne());
				assertEquals(1, fileData.getNumberOfContributorsToleranceTwo());
				assertEquals(9, fileData.getNumberOfModifications());
				if (deepAnalysis) {
					assertEquals(77, fileData.getChurnValueCoarse());
					assertEquals(27, fileData.getChurnValueFine());
				} else {
					assertEquals(0, fileData.getChurnValueCoarse());
					assertEquals(0, fileData.getChurnValueFine());
				}
				break;
				
			case "/trunk/myproject/mytext.txt":
				assertEquals(11, fileData.getRevision());
				assertEquals(OperationType.D, fileData.getLatestOperation());
				assertEquals(2, fileData.getNumberOfContributors());
				assertEquals(1, fileData.getNumberOfContributorsToleranceOne());
				assertEquals(1, fileData.getNumberOfContributorsToleranceTwo());
				assertEquals(4, fileData.getNumberOfModifications());
				if (deepAnalysis) {
					assertEquals(12, fileData.getChurnValueCoarse());
					assertEquals(6, fileData.getChurnValueFine());
				} else {
					assertEquals(0, fileData.getChurnValueCoarse());
					assertEquals(0, fileData.getChurnValueFine());
				}
				break;
				
			case "/trunk/myproject":
				assertEquals(2, fileData.getRevision());
				assertEquals(OperationType.A, fileData.getLatestOperation());
				assertEquals(1, fileData.getNumberOfContributors());
				assertEquals(0, fileData.getNumberOfContributorsToleranceOne());
				assertEquals(0, fileData.getNumberOfContributorsToleranceTwo());
				assertEquals(1, fileData.getNumberOfModifications());
				if (deepAnalysis) {
					assertEquals(0, fileData.getChurnValueCoarse());
					assertEquals(0, fileData.getChurnValueFine());
				} else {
					assertEquals(0, fileData.getChurnValueCoarse());
					assertEquals(0, fileData.getChurnValueFine());
				}
				break;
				
			case "/branches":
				assertEquals(1, fileData.getRevision());
				assertEquals(OperationType.A, fileData.getLatestOperation());
				assertEquals(1, fileData.getNumberOfContributors());
				assertEquals(0, fileData.getNumberOfContributorsToleranceOne());
				assertEquals(0, fileData.getNumberOfContributorsToleranceTwo());
				assertEquals(1, fileData.getNumberOfModifications());
				if (deepAnalysis) {
					assertEquals(0, fileData.getChurnValueCoarse());
					assertEquals(0, fileData.getChurnValueFine());
				} else {
					assertEquals(0, fileData.getChurnValueCoarse());
					assertEquals(0, fileData.getChurnValueFine());
				}
				break;
				
			case "/tags":
				assertEquals(1, fileData.getRevision());
				assertEquals(OperationType.A, fileData.getLatestOperation());
				assertEquals(1, fileData.getNumberOfContributors());
				assertEquals(0, fileData.getNumberOfContributorsToleranceOne());
				assertEquals(0, fileData.getNumberOfContributorsToleranceTwo());
				assertEquals(1, fileData.getNumberOfModifications());
				if (deepAnalysis) {
					assertEquals(0, fileData.getChurnValueCoarse());
					assertEquals(0, fileData.getChurnValueFine());
				} else {
					assertEquals(0, fileData.getChurnValueCoarse());
					assertEquals(0, fileData.getChurnValueFine());
				}
				break;
				
			case "/trunk":
				assertEquals(1, fileData.getRevision());
				assertEquals(OperationType.A, fileData.getLatestOperation());
				assertEquals(1, fileData.getNumberOfContributors());
				assertEquals(0, fileData.getNumberOfContributorsToleranceOne());
				assertEquals(0, fileData.getNumberOfContributorsToleranceTwo());
				assertEquals(1, fileData.getNumberOfModifications());
				if (deepAnalysis) {
					assertEquals(0, fileData.getChurnValueCoarse());
					assertEquals(0, fileData.getChurnValueFine());
				} else {
					assertEquals(0, fileData.getChurnValueCoarse());
					assertEquals(0, fileData.getChurnValueFine());
				}
				break;
			}
		}
	}

	/**
	 * Check if the number of invocation is proper if the revision is limited.
	 */
	@Test
	public void testReadCommitDataSpecificRevision() throws IOException {
		SourceControlLogic sourceControlLogic = new SourceControlLogic();
		SourceControlCommandExecutor executor = new SourceControlCommandExecutorMock();		
		SourceControlCommandParser parser = new SvnCommandParser();
		
		HotAreaDetectorContext context = new HotAreaDetectorContext();
		context.setDeepAnalysis(true);
		context.setRevision(10);
		sourceControlLogic.readCommitData(executor, parser, context);
		
		// 9 because there is an error at call BASE:r1. Anyway, it would be 10.
		assertEquals(9, ((SourceControlCommandExecutorMock)executor).getNumberOfDiffInvocations());
	}
	
	/**
	 * Test data:
	 * 
	 * Commit 1:
	 * - developer: mike
	 * - sources:
	 * -- Main.java added, 5 lines
	 * -- Game.java added, 10 lines
	 * -- Test.java added, 1 line
	 * 
	 * Commit 2:
	 * - developers: sully
	 * - sources
	 * -- Game.java modified, churn value 25, finer 15
	 * -- Data.java added, 20 lines
	 * -- Test.java deleted, 1 line
	 * 
	 * Commit 3:
	 * - developers: mike
	 * - sources
	 * -- Main.java modified, churn value 10, finer 10
	 * 
	 * Commit 4 (current):
	 * - developer: mike
	 * - sources:
	 * -- /directory; new directory added, without any diff counterpart)
	 * -- Main.java modified, 7 lines added and 2 removed
	 * -- Thread.java added, 30 lines
	 * -- Data.java deleted (was 20 lines long)
	 * -- Game.java added 2 lines and renamed to MyGame.java
	 * --- MyGame.java added (16 lines)
	 * --- Game.java deleted (was 14 lines long)
	 * 
	 * Expectations (modifications, churn, churn finer, developers):
	 * - /directory (1, 0, 0, {mike})
	 * - Main.java (3, 10 + 53 = 63, 10 + 7 + 2 = 19, {mike, mike, mike})
	 * - Thread.java (1, 30, 30, {mike})
	 * - Data.java (2, 40, {sully, mike})
	 * - MyGame.java (3, 25 + 16 = 41, 15 + 16 = 31, {mike, sully, mike})
	 * - Game.java (3, 25 + 14 = 39, 15 + 14 = 29, {mike, sully})
	 */
	@Test
	public void testCalculateCumulativeData() {
		CommitFileMatrix commitFileMatrix = CommitFileMatrixGenerator.generateCommitFileMatrixThreeRevisions();
		
		CommitData commitData = new CommitData();
		commitData.setRevisionNumber(4);
		commitData.setDeveloper("mike");
		commitData.setDate(new Date());
		commitData.setComment("Third commit.");
		
		CommitedFileData commitedDirectory = new CommitedFileData();
		commitedDirectory.setFileName("/directory");
		commitedDirectory.setOperationType(OperationType.A);
		commitData.addCommitedFile(commitedDirectory);
		
		CommitedFileData commitedFile1 = new CommitedFileData();
		commitedFile1.setFileName("Main.java");
		commitedFile1.setOperationType(OperationType.M);		
		commitData.addCommitedFile(commitedFile1);
		
		CommitedFileData commitedFile2 = new CommitedFileData();
		commitedFile2.setFileName("Thread.java");
		commitedFile2.setOperationType(OperationType.A);
		commitData.addCommitedFile(commitedFile2);
		
		CommitedFileData commitedFile3 = new CommitedFileData();
		commitedFile3.setFileName("Data.java");
		commitedFile3.setOperationType(OperationType.D);
		commitData.addCommitedFile(commitedFile3);
		
		CommitedFileData commitedFile4 = new CommitedFileData();
		commitedFile4.setFileName("MyGame.java");
		commitedFile4.setFromFileName("Game.java");
		commitedFile4.setOperationType(OperationType.R);
		commitData.addCommitedFile(commitedFile4);
		
		CommitedFileData commitedFile5 = new CommitedFileData();
		commitedFile5.setFileName("Game.java");
		commitedFile5.setOperationType(OperationType.D);
		commitData.addCommitedFile(commitedFile5);
		
		List<FileDiffInformation> fileDiffInformation = new ArrayList<FileDiffInformation>();
		
		FileDiffInformation fileDiffInformation1 = new FileDiffInformation();
		fileDiffInformation1.setFileName("Main.java");
		fileDiffInformation1.setNumberOfAdds(7);
		fileDiffInformation1.setNumberOfRemoves(2);
		List<String> atAtDiffs1 = new ArrayList<String>();
		atAtDiffs1.add("@@ -16,17 +16,20 @@");
		atAtDiffs1.add("@@ -40,7 +47,9 @@");
		fileDiffInformation1.setAtAtDiffs(atAtDiffs1);
		fileDiffInformation.add(fileDiffInformation1);
		
		FileDiffInformation fileDiffInformation2 = new FileDiffInformation();
		fileDiffInformation2.setFileName("Thread.java");
		fileDiffInformation2.setNumberOfAdds(30);
		fileDiffInformation2.setNumberOfRemoves(0);
		List<String> atAtDiffs2 = new ArrayList<String>();
		atAtDiffs2.add("@@ -0,0 +30 @@");
		fileDiffInformation2.setAtAtDiffs(atAtDiffs2);
		fileDiffInformation.add(fileDiffInformation2);
		
		FileDiffInformation fileDiffInformation3 = new FileDiffInformation();
		fileDiffInformation3.setFileName("Data.java");
		fileDiffInformation3.setNumberOfAdds(0);
		fileDiffInformation3.setNumberOfRemoves(20);
		List<String> atAtDiffs3 = new ArrayList<String>();
		atAtDiffs3.add("@@ -1,20 +0,0 @@");
		fileDiffInformation3.setAtAtDiffs(atAtDiffs3);
		fileDiffInformation.add(fileDiffInformation3);
		
		FileDiffInformation fileDiffInformation4 = new FileDiffInformation();
		fileDiffInformation4.setFileName("MyGame.java");
		fileDiffInformation4.setNumberOfAdds(16);
		fileDiffInformation4.setNumberOfRemoves(0);
		List<String> atAtDiffs4 = new ArrayList<String>();
		atAtDiffs4.add("@@ -0,0 16 @@");
		fileDiffInformation4.setAtAtDiffs(atAtDiffs4);
		fileDiffInformation.add(fileDiffInformation4);
		
		FileDiffInformation fileDiffInformation5 = new FileDiffInformation();
		fileDiffInformation5.setFileName("Game.java");
		fileDiffInformation5.setNumberOfAdds(0);
		fileDiffInformation5.setNumberOfRemoves(14);
		List<String> atAtDiffs5 = new ArrayList<String>();
		atAtDiffs5.add("@@ -1,14 0,0 @@");
		fileDiffInformation5.setAtAtDiffs(atAtDiffs5);
		fileDiffInformation.add(fileDiffInformation5);
		
		SourceControlLogic sourceControlLogic = new SourceControlLogic();
		List<CommitFileCell> commitFileCells = sourceControlLogic.calculateCumulativeData(commitFileMatrix, commitData, fileDiffInformation);
		
		assertEquals(6, commitFileCells.size());
		
		assertEquals("/directory", commitFileCells.get(0).getFileName());
		assertEquals(4, commitFileCells.get(0).getRevision());
		assertEquals(OperationType.A, commitFileCells.get(0).getLatestOperation());
		assertEquals(1, commitFileCells.get(0).getNumberOfContributors());
		assertEquals(0, commitFileCells.get(0).getNumberOfContributorsToleranceOne());
		assertEquals(0, commitFileCells.get(0).getNumberOfContributorsToleranceTwo());
		assertTrue(commitFileCells.get(0).getContributors().contains("mike"));
		assertFalse(commitFileCells.get(0).getContributors().contains("sully"));
		assertEquals(1, commitFileCells.get(0).getNumberOfModifications());
		assertEquals(0, commitFileCells.get(0).getChurnValueCoarse());
		assertEquals(0, commitFileCells.get(0).getChurnValueFine());
		
		assertEquals("Main.java", commitFileCells.get(1).getFileName());
		assertEquals(4, commitFileCells.get(1).getRevision());
		assertEquals(OperationType.M, commitFileCells.get(1).getLatestOperation());
		assertEquals(1, commitFileCells.get(1).getNumberOfContributors());
		assertEquals(1, commitFileCells.get(1).getNumberOfContributorsToleranceOne());
		assertEquals(1, commitFileCells.get(1).getNumberOfContributorsToleranceTwo());
		assertTrue(commitFileCells.get(1).getContributors().contains("mike"));
		assertFalse(commitFileCells.get(1).getContributors().contains("sully"));
		assertEquals(3, commitFileCells.get(1).getNumberOfModifications());
		assertEquals(63, commitFileCells.get(1).getChurnValueCoarse());
		assertEquals(19, commitFileCells.get(1).getChurnValueFine());
		
		assertEquals("Thread.java", commitFileCells.get(2).getFileName());
		assertEquals(4, commitFileCells.get(2).getRevision());
		assertEquals(OperationType.A, commitFileCells.get(2).getLatestOperation());
		assertEquals(1, commitFileCells.get(2).getNumberOfContributors());
		assertEquals(0, commitFileCells.get(2).getNumberOfContributorsToleranceOne());
		assertEquals(0, commitFileCells.get(2).getNumberOfContributorsToleranceTwo());
		assertTrue(commitFileCells.get(2).getContributors().contains("mike"));
		assertFalse(commitFileCells.get(2).getContributors().contains("sully"));
		assertEquals(1, commitFileCells.get(2).getNumberOfModifications());
		assertEquals(30, commitFileCells.get(2).getChurnValueCoarse());
		assertEquals(30, commitFileCells.get(2).getChurnValueFine());

		assertEquals("Data.java", commitFileCells.get(3).getFileName());
		assertEquals(4, commitFileCells.get(3).getRevision());
		assertEquals(OperationType.D, commitFileCells.get(3).getLatestOperation());
		assertEquals(2, commitFileCells.get(3).getNumberOfContributors());
		assertEquals(0, commitFileCells.get(3).getNumberOfContributorsToleranceOne());
		assertEquals(0, commitFileCells.get(3).getNumberOfContributorsToleranceTwo());
		assertTrue(commitFileCells.get(3).getContributors().contains("mike"));
		assertTrue(commitFileCells.get(3).getContributors().contains("sully"));
		assertEquals(2, commitFileCells.get(3).getNumberOfModifications());
		assertEquals(40, commitFileCells.get(3).getChurnValueCoarse());
		assertEquals(40, commitFileCells.get(3).getChurnValueFine());
		
		assertEquals("MyGame.java", commitFileCells.get(4).getFileName());
		assertEquals(4, commitFileCells.get(4).getRevision());
		assertEquals(OperationType.R, commitFileCells.get(4).getLatestOperation());
		assertEquals(2, commitFileCells.get(4).getNumberOfContributors());
		assertEquals(1, commitFileCells.get(4).getNumberOfContributorsToleranceOne());
		assertEquals(0, commitFileCells.get(4).getNumberOfContributorsToleranceTwo());
		assertTrue(commitFileCells.get(4).getContributors().contains("mike"));
		assertTrue(commitFileCells.get(4).getContributors().contains("sully"));
		assertEquals(3, commitFileCells.get(4).getNumberOfModifications());
		assertEquals(41, commitFileCells.get(4).getChurnValueCoarse());
		assertEquals(31, commitFileCells.get(4).getChurnValueFine());
		
		assertEquals("Game.java", commitFileCells.get(5).getFileName());
		assertEquals(4, commitFileCells.get(5).getRevision());
		assertEquals(OperationType.D, commitFileCells.get(5).getLatestOperation());
		assertEquals(2, commitFileCells.get(5).getNumberOfContributors());
		assertEquals(1, commitFileCells.get(5).getNumberOfContributorsToleranceOne());
		assertEquals(0, commitFileCells.get(5).getNumberOfContributorsToleranceTwo());
		assertTrue(commitFileCells.get(5).getContributors().contains("mike"));
		assertTrue(commitFileCells.get(5).getContributors().contains("sully"));
		assertEquals(3, commitFileCells.get(5).getNumberOfModifications());
		assertEquals(39, commitFileCells.get(5).getChurnValueCoarse());
		assertEquals(29, commitFileCells.get(5).getChurnValueFine());
	}
	
	/**
	 * Test the extraction of @@ modifications.
	 */
	@Test
	public void testExtractChurnValueModify() {
		List<String> atAtDiffs = new ArrayList<String>();
		atAtDiffs.add("@@ -16,7 +16,7 @@");
		atAtDiffs.add("@@ -59,6 +59,7 @@");
		atAtDiffs.add("@@ -140,7 +141,6 @@");
		
		int result = SourceControlLogic.extractChurnValue(atAtDiffs);
		
		assertEquals(40, result);
	}
	
	/**
	 * Test the extraction of @@ file addition. It has a slightly different format than modification.
	 */
	@Test
	public void testExtractChurnValueAdd() {
		List<String> atAtDiffs = new ArrayList<String>();
		atAtDiffs.add("@@ -0,0 +25 @@");
		
		int result = SourceControlLogic.extractChurnValue(atAtDiffs);
		
		assertEquals(25, result);
	}
	
	/**
	 * Test the extraction of @@ file deletion.
	 */
	@Test
	public void testExtractChurnValueDelete() {
		List<String> atAtDiffs = new ArrayList<String>();
		atAtDiffs.add("@@ -1,3 +0,0 @@");
		
		int result = SourceControlLogic.extractChurnValue(atAtDiffs);
		
		assertEquals(3, result);
	}

	/**
	 * Test revision filter.
	 */
	@Test
	public void testFilterCommitDataListRevision() {
		List<CommitData> commitDataList = CommitDataGenerator.generateCommitDataList();
		
		List<String> extensions = new ArrayList<String>();
		extensions.add("java");
		extensions.add("txt");
		List<CommitData> result = SourceControlLogic.filterCommitDataList(commitDataList, 3);
		
		assertEquals(3, result.size());
		
		assertEquals(1, result.get(0).getRevisionNumber());
		assertEquals(9, result.get(0).getCommitedFiles().size());
		
		assertEquals(2, result.get(1).getRevisionNumber());
		assertEquals(1, result.get(1).getCommitedFiles().size());
		assertEquals("/trunk/myproject/src/mypackage/MyFile.xml", result.get(1).getCommitedFiles().get(0).getFileName());
		
		assertEquals(3, result.get(2).getRevisionNumber());
		assertEquals(1, result.get(2).getCommitedFiles().size());
		assertEquals("/trunk/myproject/test/mypackage/MyFileTest.java", result.get(2).getCommitedFiles().get(0).getFileName());
		
		
	}
}
