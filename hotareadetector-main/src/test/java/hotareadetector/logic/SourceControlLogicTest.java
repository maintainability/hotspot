package hotareadetector.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import hotareadetector.data.AnalysisType;
import hotareadetector.data.CommitData;
import hotareadetector.data.CommitDataExtended;
import hotareadetector.data.CommitDataPerFile;
import hotareadetector.data.HotAreaDetectorContext;
import hotareadetector.interfaces.SourceControlCommandExecutor;
import hotareadetector.interfaces.SourceControlCommandParser;
import hotareadetector.mock.CommitDataGenerator;
import hotareadetector.mock.SourceControlCommandExecutorMock;
import hotareadetector.svn.SvnCommandParser;

import java.io.IOException;
import java.util.ArrayList;
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
		if (deepAnalysis) {
			context.setAnalysisType(AnalysisType.FULL);
		} else {
			context.setAnalysisType(AnalysisType.CHEAP);
		}
		CommitDataPerFile commitDataPerFile = sourceControlLogic.readCommitData(executor, parser, context);
		
		Set<String> fileNames = commitDataPerFile.getFileCommitMap().keySet();
		
		if (deepAnalysis) {
			assertEquals(17, ((SourceControlCommandExecutorMock)executor).getNumberOfDiffInvocations());
			// 17 because there is an error at call BASE:r1. Anyway, it would be 18.
		} else {
			assertEquals(0, ((SourceControlCommandExecutorMock)executor).getNumberOfDiffInvocations());
		}
		assertEquals(12, fileNames.size());
		
		Set<String> fileNamesExpected = new HashSet<String>();
		fileNamesExpected.add("/trunk/myproject/reallyalongtext.txt");
		fileNamesExpected.add("/trunk/myproject/longtext.txt");
		fileNamesExpected.add("/trunk/myfolder/filename with spaces.txt");
		fileNamesExpected.add("/trunk/myproject/myrenamedtext.txt");
		fileNamesExpected.add("/trunk/myfolder");
		fileNamesExpected.add("/trunk/myfolder/myfile.txt");
		fileNamesExpected.add("/trunk/myproject/addnewfile.txt");
		fileNamesExpected.add("/trunk/myproject/mysecontext.txt");
		fileNamesExpected.add("/trunk/myproject");
		fileNamesExpected.add("/branches");
		fileNamesExpected.add("/tags");
		fileNamesExpected.add("/trunk");
		assertTrue(fileNames.containsAll(fileNamesExpected));
		assertFalse(fileNames.contains("notexisting.file"));
		
		for (String fileName : fileNames) {
			List<CommitDataExtended> commitDataExtendedList = commitDataPerFile.getFileCommitMap().get(fileName);
			CommitDataExtended lastCommitDataExtended = commitDataExtendedList.get(commitDataExtendedList.size()-1);
			switch (fileName) {
			case "/trunk/myproject/reallyalongtext.txt":
				assertEquals(18, lastCommitDataExtended.getCommitData().getRevisionNumber());
				break;
				
			case "/trunk/myproject/longtext.txt":
				assertEquals(15, lastCommitDataExtended.getCommitData().getRevisionNumber());
				break;
				
			case "/trunk/myfolder/filename with spaces.txt":
				assertEquals(13, lastCommitDataExtended.getCommitData().getRevisionNumber());
				break;
				
			case "/trunk/myproject/myrenamedtext.txt":
				assertEquals(11, lastCommitDataExtended.getCommitData().getRevisionNumber());
				break;
				
			case "/trunk/myfolder":
				assertEquals(10, lastCommitDataExtended.getCommitData().getRevisionNumber());
				break;
				
			case "/trunk/myfolder/myfile.txt":
				assertEquals(12, lastCommitDataExtended.getCommitData().getRevisionNumber());
				break;
				
			case "/trunk/myproject/addnewfile.txt":
				assertEquals(9, lastCommitDataExtended.getCommitData().getRevisionNumber());
				break;
				
			case "/trunk/myproject/mysecontext.txt":
				assertEquals(18, lastCommitDataExtended.getCommitData().getRevisionNumber());
				break;
				
			case "/trunk/myproject":
				assertEquals(2, lastCommitDataExtended.getCommitData().getRevisionNumber());
				break;
				
			case "/branches":
				assertEquals(1, lastCommitDataExtended.getCommitData().getRevisionNumber());
				break;
				
			case "/tags":
				assertEquals(1, lastCommitDataExtended.getCommitData().getRevisionNumber());
				break;
				
			case "/trunk":
				assertEquals(1, lastCommitDataExtended.getCommitData().getRevisionNumber());
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
		context.setRevision(10);
		sourceControlLogic.readCommitData(executor, parser, context);
		
		// 9 because there is an error at call BASE:r1. Anyway, it would be 10.
		assertEquals(9, ((SourceControlCommandExecutorMock)executor).getNumberOfDiffInvocations());
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
		List<CommitData> result = SourceControlLogic.filterCommitsByRevision(commitDataList, 3);
		
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
