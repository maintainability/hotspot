package hotareadetector.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import hotareadetector.mock.CommitFileCellGenerator;
import hotareadetector.mock.CommitFileMatrixGenerator;

import org.junit.Test;

/**
 * Commit file matrix unit test.
 */
public class CommitFileMatrixTest {
	
	/**
	 * Test if a commit-file cell is properly added to the matrix.  
	 */
	@Test
	public void testAddCommitFileData() {
		CommitFileCell commitFileCell = CommitFileCellGenerator.generateOneCommitFileCell();
		CommitFileMatrix commitFileMatrix = new CommitFileMatrix(true);
		
		commitFileMatrix.addCommitFileData(commitFileCell);

		assertEquals(1, commitFileMatrix.files.size());
		assertEquals(1, commitFileMatrix.files.get("Main.java").size());
		assertEquals("Main.java", commitFileMatrix.files.get("Main.java").get(0).getFileName());
		assertEquals(5, commitFileMatrix.files.get("Main.java").get(0).getRevision());
		assertEquals(null, commitFileMatrix.files.get("Game.java"));
		
		assertEquals(1, commitFileMatrix.commits.size());
		assertEquals(1, commitFileMatrix.commits.get(Integer.valueOf(5)).size());
		assertEquals("Main.java", commitFileMatrix.commits.get(Integer.valueOf(5)).get(0).getFileName());
		assertEquals(5, commitFileMatrix.commits.get(Integer.valueOf(5)).get(0).getRevision());
		assertEquals(null, commitFileMatrix.commits.get(Integer.valueOf(6)));
		
		// checks if the two cells are physically the same
		assertTrue(commitFileMatrix.files.get("Main.java").get(0) == commitFileMatrix.commits.get(Integer.valueOf(5)).get(0));
	}
	
	/**
	 * Test if a collection of commit-file cells (3 files in 1 commit) is properly added to the matrix.  
	 */
	@Test
	public void testAddCommitedFileDataCollection() {
		CommitFileMatrix commitFileMatrix = new CommitFileMatrix(true);
		Collection<CommitFileCell> commitFileData = CommitFileCellGenerator.generateOneCommitThreeFileCells();
		commitFileMatrix.addCommitedFileDataCollection(commitFileData );
		
		assertEquals(3, commitFileMatrix.files.size());
		assertEquals(1, commitFileMatrix.files.get("Main.java").size());
		assertEquals("Main.java", commitFileMatrix.files.get("Main.java").get(0).getFileName());
		assertEquals(2, commitFileMatrix.files.get("Main.java").get(0).getRevision());
		
		assertEquals(1, commitFileMatrix.files.get("Game.java").size());
		assertEquals("Game.java", commitFileMatrix.files.get("Game.java").get(0).getFileName());
		assertEquals(2, commitFileMatrix.files.get("Game.java").get(0).getRevision());
		
		assertEquals(1, commitFileMatrix.files.get("Data.java").size());
		assertEquals("Data.java", commitFileMatrix.files.get("Data.java").get(0).getFileName());
		assertEquals(2, commitFileMatrix.files.get("Data.java").get(0).getRevision());
		
		assertEquals(null, commitFileMatrix.files.get("Thread.java"));
		
		assertEquals(1, commitFileMatrix.commits.size());
		assertEquals(3, commitFileMatrix.commits.get(Integer.valueOf(2)).size());
		assertEquals("Data.java", commitFileMatrix.commits.get(Integer.valueOf(2)).get(0).getFileName());
		assertEquals("Game.java", commitFileMatrix.commits.get(Integer.valueOf(2)).get(1).getFileName());
		assertEquals("Main.java", commitFileMatrix.commits.get(Integer.valueOf(2)).get(2).getFileName());
		assertEquals(2, commitFileMatrix.commits.get(Integer.valueOf(2)).get(0).getRevision());
		assertEquals(2, commitFileMatrix.commits.get(Integer.valueOf(2)).get(1).getRevision());
		assertEquals(2, commitFileMatrix.commits.get(Integer.valueOf(2)).get(2).getRevision());
		
		assertEquals(null, commitFileMatrix.commits.get(Integer.valueOf(5)));
	}

	/**
	 * Add 3 revisions with some files, and then check if the latest is returned properly.
	 */
	@Test
	public void testGetFileDataLatest() {
		CommitFileMatrix commitFileMatrix = CommitFileMatrixGenerator.generateCommitFileMatrixThreeRevisions();
		
		assertEquals(3, commitFileMatrix.getLatestRevision());
		
		CommitFileCell mainData = commitFileMatrix.getFileDataLatest("Main.java");
		CommitFileCell gameData = commitFileMatrix.getFileDataLatest("Game.java");
		CommitFileCell dataData = commitFileMatrix.getFileDataLatest("Data.java");
		CommitFileCell testData = commitFileMatrix.getFileDataLatest("Test.java");
		CommitFileCell threadData = commitFileMatrix.getFileDataLatest("Thread.java");
		
		assertEquals(4, commitFileMatrix.files.size());
		
		assertEquals(2, commitFileMatrix.files.get("Main.java").size());
		assertEquals("Main.java", commitFileMatrix.files.get("Main.java").get(0).getFileName());
		assertEquals(3, commitFileMatrix.files.get("Main.java").get(0).getRevision());
		assertEquals(10, commitFileMatrix.files.get("Main.java").get(0).getChurnValueCoarse());
		assertEquals("Main.java", commitFileMatrix.files.get("Main.java").get(1).getFileName());
		assertEquals(1, commitFileMatrix.files.get("Main.java").get(1).getRevision());
		assertEquals(5, commitFileMatrix.files.get("Main.java").get(1).getChurnValueCoarse());
		
		assertEquals(2, commitFileMatrix.files.get("Game.java").size());
		assertEquals("Game.java", commitFileMatrix.files.get("Game.java").get(0).getFileName());
		assertEquals(2, commitFileMatrix.files.get("Game.java").get(0).getRevision());
		assertEquals(25, commitFileMatrix.files.get("Game.java").get(0).getChurnValueCoarse());
		assertEquals("Game.java", commitFileMatrix.files.get("Game.java").get(1).getFileName());
		assertEquals(1, commitFileMatrix.files.get("Game.java").get(1).getRevision());
		assertEquals(10, commitFileMatrix.files.get("Game.java").get(1).getChurnValueCoarse());
		
		assertEquals(1, commitFileMatrix.files.get("Data.java").size());
		assertEquals("Data.java", commitFileMatrix.files.get("Data.java").get(0).getFileName());
		assertEquals(2, commitFileMatrix.files.get("Data.java").get(0).getRevision());
		assertEquals(20, commitFileMatrix.files.get("Data.java").get(0).getChurnValueCoarse());
		
		assertEquals(null, commitFileMatrix.files.get("Thread.java"));
		
		assertEquals(3, commitFileMatrix.commits.size());
		
		assertEquals(3, commitFileMatrix.commits.get(Integer.valueOf(1)).size());
		assertEquals("Test.java", commitFileMatrix.commits.get(Integer.valueOf(1)).get(0).getFileName());
		assertEquals("Game.java", commitFileMatrix.commits.get(Integer.valueOf(1)).get(1).getFileName());
		assertEquals("Main.java", commitFileMatrix.commits.get(Integer.valueOf(1)).get(2).getFileName());
		assertEquals(1, commitFileMatrix.commits.get(Integer.valueOf(1)).get(0).getRevision());
		assertEquals(1, commitFileMatrix.commits.get(Integer.valueOf(1)).get(1).getRevision());
		assertEquals(1, commitFileMatrix.commits.get(Integer.valueOf(1)).get(2).getRevision());
		assertEquals(1, commitFileMatrix.commits.get(Integer.valueOf(1)).get(0).getChurnValueCoarse());
		assertEquals(10, commitFileMatrix.commits.get(Integer.valueOf(1)).get(1).getChurnValueCoarse());
		assertEquals(5, commitFileMatrix.commits.get(Integer.valueOf(1)).get(2).getChurnValueCoarse());

		assertEquals(3, commitFileMatrix.commits.get(Integer.valueOf(2)).size());
		assertEquals("Test.java", commitFileMatrix.commits.get(Integer.valueOf(2)).get(0).getFileName());
		assertEquals("Data.java", commitFileMatrix.commits.get(Integer.valueOf(2)).get(1).getFileName());
		assertEquals("Game.java", commitFileMatrix.commits.get(Integer.valueOf(2)).get(2).getFileName());
		assertEquals(2, commitFileMatrix.commits.get(Integer.valueOf(2)).get(0).getRevision());
		assertEquals(2, commitFileMatrix.commits.get(Integer.valueOf(2)).get(1).getRevision());
		assertEquals(2, commitFileMatrix.commits.get(Integer.valueOf(2)).get(2).getRevision());
		assertEquals(2, commitFileMatrix.commits.get(Integer.valueOf(2)).get(0).getChurnValueCoarse());
		assertEquals(20, commitFileMatrix.commits.get(Integer.valueOf(2)).get(1).getChurnValueCoarse());
		assertEquals(25, commitFileMatrix.commits.get(Integer.valueOf(2)).get(2).getChurnValueCoarse());

		assertEquals(null, commitFileMatrix.commits.get(Integer.valueOf(5)));
		
		assertEquals(10, mainData.getChurnValueCoarse());
		assertEquals(25, gameData.getChurnValueCoarse());
		assertEquals(20, dataData.getChurnValueCoarse());
		assertEquals(2, testData.getChurnValueCoarse());
		assertEquals(null, threadData);
	}
	
	/**
	 * Check if the file information related to a commit is correctly calculated.
	 */
	@Test
	public void testGetAllFilesOfRevision() {
		CommitFileMatrix commitFileMatrix = CommitFileMatrixGenerator.generateCommitFileMatrixThreeRevisions();
		
		List<CommitFileCell> filesOfFirstRevision = commitFileMatrix.getAllFilesOfRevision(1);
		assertEquals(3, filesOfFirstRevision.size());
		int elements = 0;
		for (int i = 0; i < 3; i++) {
			CommitFileCell commitFileCell = filesOfFirstRevision.get(i);
			switch (commitFileCell.getFileName()) {
			case "Main.java":
			case "Game.java":
			case "Test.java":
				elements++;
				assertEquals(1, commitFileCell.getRevision());
				break;
				
			default:
				assertTrue(false);
			}
		}
		assertEquals(3, elements);
		
		List<CommitFileCell> filesOfSecondRevision = commitFileMatrix.getAllFilesOfRevision(2);
		assertEquals(4, filesOfSecondRevision.size());
		elements = 0;
		for (int i = 0; i < 4; i++) {
			CommitFileCell commitFileCell = filesOfSecondRevision.get(i);
			switch (commitFileCell.getFileName()) {
			case "Main.java":
				elements++;
				assertEquals(1, commitFileCell.getRevision());
				break;
				
			case "Game.java":
			case "Data.java":
			case "Test.java":
				elements++;
				assertEquals(2, commitFileCell.getRevision());
				break;
				
			default:
				assertTrue(false);
			}
		}
		assertEquals(4, elements);
		
		List<CommitFileCell> filesOfThirdRevision = commitFileMatrix.getAllFilesOfRevision(3);
		assertEquals(3, filesOfThirdRevision.size());
		elements = 0;
		for (int i = 0; i < 3; i++) {
			CommitFileCell commitFileCell = filesOfThirdRevision.get(i);
			switch (commitFileCell.getFileName()) {
			case "Main.java":
				elements++;
				assertEquals(3, commitFileCell.getRevision());
				break;
				
			case "Game.java":
			case "Data.java":
				elements++;
				assertEquals(2, commitFileCell.getRevision());
				break;
				
			case "Test.java":
			default:
				assertTrue(false);
			}
		}
		assertEquals(3, elements);
		
	}
	
	/**
	 * Test directory rename.
	 */
	@Test
	public void testPerformRename() {
		CommitFileMatrix commitFileMatrix = CommitFileMatrixGenerator.generateCommitFileMatrixForDirectoryRename();
		
		List<CommitFileCell> renameResult = commitFileMatrix.performRename("/dir", "/renameddir", "user2", new Date(), 2, null);
		
		Collections.sort(renameResult, new Comparator<CommitFileCell>() {
			@Override
			public int compare(CommitFileCell commitFileCell1, CommitFileCell commitFileCell2) {
				return commitFileCell1.getFileName().compareTo(commitFileCell2.getFileName());
			}});
		
		assertEquals(3, renameResult.size());
		
		assertEquals("/renameddir", renameResult.get(0).getFileName());
		assertEquals("/renameddir/file1.txt", renameResult.get(1).getFileName());
		assertEquals("/renameddir/file2.txt", renameResult.get(2).getFileName());
		assertEquals(2, renameResult.get(0).getNumberOfContributors());
		assertEquals(2, renameResult.get(1).getNumberOfContributors());
		assertEquals(2, renameResult.get(2).getNumberOfContributors());
	}
	
	/**
	 * Check the correctness of filterData function.
	 */
	@Test
	public void testFilterData() {
		CommitFileMatrix commitFileMatrix = CommitFileMatrixGenerator.generateCommitFileMatrixThreeRevisions();
		List<String> excludePrefixes = new ArrayList<String>();
		excludePrefixes.add("Main");
		
		commitFileMatrix.filterData(null, excludePrefixes , null);
		
		assertEquals(2, commitFileMatrix.commits.size());
		assertEquals(2, commitFileMatrix.commits.get(1).size());
		assertFalse("Main".equals(commitFileMatrix.commits.get(1).get(0)));
		assertFalse("Main".equals(commitFileMatrix.commits.get(1).get(1)));
		assertEquals(3, commitFileMatrix.commits.get(2).size());
		assertFalse("Main".equals(commitFileMatrix.commits.get(2).get(0)));
		
		assertEquals(3, commitFileMatrix.files.size());
		assertEquals(null, commitFileMatrix.files.get("Main.java"));
	}
	
	/**
	 * Unit test for checking if the file should retain function works properly.
	 */
	@Test
	public void testFileShouldRetain() {
		List<String> includePrefixes = new ArrayList<String>();
		includePrefixes.add("/trunk");
		includePrefixes.add("/branches");
		List<String> excludePrefixes = new ArrayList<String>();
		excludePrefixes.add("/trunk/test");
		excludePrefixes.add("/branches/test");
		List<String> extensions = new ArrayList<String>();
		extensions.add("java");
		extensions.add("xml");
		
		assertTrue(CommitFileMatrix.fileShouldRetain("/trunk/MyFile.java", includePrefixes, excludePrefixes, extensions));
		assertFalse(CommitFileMatrix.fileShouldRetain("/trunk/myfile.txt", includePrefixes, excludePrefixes, extensions));
		assertTrue(CommitFileMatrix.fileShouldRetain("/trunk/myfile.txt", includePrefixes, excludePrefixes, null));
		assertFalse(CommitFileMatrix.fileShouldRetain("/tags/MyFile.java", includePrefixes, excludePrefixes, extensions));
		assertTrue(CommitFileMatrix.fileShouldRetain("/tags/MyFile.java", null, excludePrefixes, extensions));
		assertFalse(CommitFileMatrix.fileShouldRetain("/trunk/test/MyFile.java", includePrefixes, excludePrefixes, extensions));
		assertTrue(CommitFileMatrix.fileShouldRetain("/trunk/test/MyFile.java", includePrefixes, null, extensions));
	}
	
}
