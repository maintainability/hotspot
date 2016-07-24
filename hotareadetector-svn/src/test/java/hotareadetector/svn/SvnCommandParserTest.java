package hotareadetector.svn;

import static org.junit.Assert.assertEquals;

import hotareadetector.data.CommitData;
import hotareadetector.data.FileDiffInformation;
import hotareadetector.data.OperationType;
import hotareadetector.data.SourceControlResultData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.List;

import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 * Unit tests for SVN implementation of parsers.
 */
public class SvnCommandParserTest {
	
	/**
	 * Unit test for checking SVN log command.
	 */
	@Test
	public void testParseSourceControlLog() throws IOException {
		SourceControlResultData testSvnLogResult = mock(SourceControlResultData.class);
		when(testSvnLogResult.getResultBufferedReader()).thenReturn(new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("svnlog.txt"))));
		
		SvnCommandParser svnCommandParser = new SvnCommandParser();
		
		List<CommitData> result = svnCommandParser.parseSourceControlLog(testSvnLogResult);
		
		assertEquals(18, result.size());
		
		assertEquals(1, result.get(0).getRevisionNumber());
		assertEquals("VisualSVN Server", result.get(0).getContributor());
		assertEquals("Initial structure.\n", result.get(0).getComment());
		assertEquals(OperationType.A, result.get(0).getCommitedFiles().get(0).getOperationType());
		assertEquals("/branches", result.get(0).getCommitedFiles().get(0).getFileName());
		assertEquals(null, result.get(0).getCommitedFiles().get(0).getFromFileName());
		assertEquals(OperationType.A, result.get(0).getCommitedFiles().get(1).getOperationType());
		assertEquals("/tags", result.get(0).getCommitedFiles().get(1).getFileName());
		assertEquals(null, result.get(0).getCommitedFiles().get(1).getFromFileName());
		assertEquals(OperationType.A, result.get(0).getCommitedFiles().get(2).getOperationType());
		assertEquals("/trunk", result.get(0).getCommitedFiles().get(2).getFileName());
		assertEquals(null, result.get(0).getCommitedFiles().get(2).getFromFileName());
		
		assertEquals(2, result.get(1).getRevisionNumber());
		assertEquals("VisualSVN Server", result.get(1).getContributor());
		assertEquals("Created folder 'myproject'.\n", result.get(1).getComment());
		assertEquals(OperationType.A, result.get(1).getCommitedFiles().get(0).getOperationType());
		assertEquals("/trunk/myproject", result.get(1).getCommitedFiles().get(0).getFileName());
		assertEquals(null, result.get(1).getCommitedFiles().get(0).getFromFileName());
		
		assertEquals(3, result.get(2).getRevisionNumber());
		assertEquals("user1", result.get(2).getContributor());
		assertEquals("My first commit.\n", result.get(2).getComment());
		assertEquals(OperationType.A, result.get(2).getCommitedFiles().get(0).getOperationType());
		assertEquals("/trunk/myproject/mytext.txt", result.get(2).getCommitedFiles().get(0).getFileName());
		assertEquals(null, result.get(2).getCommitedFiles().get(0).getFromFileName());
		
		assertEquals(4, result.get(3).getRevisionNumber());
		assertEquals("user1", result.get(3).getContributor());
		assertEquals("Secon text file.\n", result.get(3).getComment());
		assertEquals(OperationType.A, result.get(3).getCommitedFiles().get(0).getOperationType());
		assertEquals("/trunk/myproject/mysecontext.txt", result.get(3).getCommitedFiles().get(0).getFileName());
		assertEquals(null, result.get(3).getCommitedFiles().get(0).getFromFileName());
		
		assertEquals(5, result.get(4).getRevisionNumber());
		assertEquals("user1", result.get(4).getContributor());
		assertEquals("My First modification.\n", result.get(4).getComment());
		assertEquals(OperationType.M, result.get(4).getCommitedFiles().get(0).getOperationType());
		assertEquals("/trunk/myproject/mysecontext.txt", result.get(4).getCommitedFiles().get(0).getFileName());
		assertEquals(null, result.get(4).getCommitedFiles().get(0).getFromFileName());
		
		assertEquals(6, result.get(5).getRevisionNumber());
		assertEquals("user1", result.get(5).getContributor());
		assertEquals("Within line modification.\n", result.get(5).getComment());
		assertEquals(OperationType.M, result.get(5).getCommitedFiles().get(0).getOperationType());
		assertEquals("/trunk/myproject/mysecontext.txt", result.get(5).getCommitedFiles().get(0).getFileName());
		assertEquals(null, result.get(5).getCommitedFiles().get(0).getFromFileName());
		
		assertEquals(7, result.get(6).getRevisionNumber());
		assertEquals("user2", result.get(6).getContributor());
		assertEquals("Add a line by user2.\n", result.get(6).getComment());
		assertEquals(OperationType.M, result.get(6).getCommitedFiles().get(0).getOperationType());
		assertEquals("/trunk/myproject/mysecontext.txt", result.get(6).getCommitedFiles().get(0).getFileName());
		assertEquals(null, result.get(6).getCommitedFiles().get(0).getFromFileName());
		
		assertEquals(8, result.get(7).getRevisionNumber());
		assertEquals("user1", result.get(7).getContributor());
		assertEquals("Complex modification.\n", result.get(7).getComment());
		assertEquals(OperationType.A, result.get(7).getCommitedFiles().get(0).getOperationType());
		assertEquals("/trunk/myproject/myaddedfile.txt", result.get(7).getCommitedFiles().get(0).getFileName());
		assertEquals(null, result.get(7).getCommitedFiles().get(0).getFromFileName());
		assertEquals(OperationType.M, result.get(7).getCommitedFiles().get(1).getOperationType());
		assertEquals("/trunk/myproject/mysecontext.txt", result.get(7).getCommitedFiles().get(1).getFileName());
		assertEquals(null, result.get(7).getCommitedFiles().get(1).getFromFileName());
		assertEquals(OperationType.M, result.get(7).getCommitedFiles().get(2).getOperationType());
		assertEquals("/trunk/myproject/mytext.txt", result.get(7).getCommitedFiles().get(2).getFileName());
		assertEquals(null, result.get(7).getCommitedFiles().get(2).getFromFileName());
		
		assertEquals(9, result.get(8).getRevisionNumber());
		assertEquals("user1", result.get(8).getContributor());
		assertEquals("Very complex update.\n", result.get(8).getComment());
		assertEquals(OperationType.A, result.get(8).getCommitedFiles().get(0).getOperationType());
		assertEquals("/trunk/myproject/addnewfile.txt", result.get(8).getCommitedFiles().get(0).getFileName());
		assertEquals(null, result.get(8).getCommitedFiles().get(0).getFromFileName());
		assertEquals(OperationType.D, result.get(8).getCommitedFiles().get(1).getOperationType());
		assertEquals("/trunk/myproject/myaddedfile.txt", result.get(8).getCommitedFiles().get(1).getFileName());
		assertEquals(null, result.get(8).getCommitedFiles().get(1).getFromFileName());
		assertEquals(OperationType.M, result.get(8).getCommitedFiles().get(2).getOperationType());
		assertEquals("/trunk/myproject/mysecontext.txt", result.get(8).getCommitedFiles().get(2).getFileName());
		assertEquals(null, result.get(8).getCommitedFiles().get(2).getFromFileName());
		assertEquals(OperationType.M, result.get(8).getCommitedFiles().get(3).getOperationType());
		assertEquals("/trunk/myproject/mytext.txt", result.get(8).getCommitedFiles().get(3).getFileName());
		assertEquals(null, result.get(8).getCommitedFiles().get(3).getFromFileName());
		
		assertEquals(10, result.get(9).getRevisionNumber());
		assertEquals("user1", result.get(9).getContributor());
		assertEquals("New folder.\n", result.get(9).getComment());
		assertEquals(OperationType.A, result.get(9).getCommitedFiles().get(0).getOperationType());
		assertEquals("/trunk/myfolder", result.get(9).getCommitedFiles().get(0).getFileName());
		assertEquals(null, result.get(9).getCommitedFiles().get(0).getFromFileName());
		assertEquals(OperationType.A, result.get(9).getCommitedFiles().get(1).getOperationType());
		assertEquals("/trunk/myfolder/myfile.txt", result.get(9).getCommitedFiles().get(1).getFileName());
		assertEquals(null, result.get(9).getCommitedFiles().get(1).getFromFileName());
		
		assertEquals(11, result.get(10).getRevisionNumber());
		assertEquals("user2", result.get(10).getContributor());
		assertEquals("Rename.\n", result.get(10).getComment());
		assertEquals(OperationType.A, result.get(10).getCommitedFiles().get(0).getOperationType());
		assertEquals("/trunk/myproject/myrenamedtext.txt", result.get(10).getCommitedFiles().get(0).getFileName());
		assertEquals("/trunk/myproject/mytext.txt", result.get(10).getCommitedFiles().get(0).getFromFileName());
		assertEquals(OperationType.D, result.get(10).getCommitedFiles().get(1).getOperationType());
		assertEquals("/trunk/myproject/mytext.txt", result.get(10).getCommitedFiles().get(1).getFileName());
		assertEquals(null, result.get(10).getCommitedFiles().get(1).getFromFileName());
		
		assertEquals(12, result.get(11).getRevisionNumber());
		assertEquals("user1", result.get(11).getContributor());
		assertEquals("This commit has multiple lines of comment.\nSecond line.\nThird line.\n\n", result.get(11).getComment());
		assertEquals(OperationType.M, result.get(11).getCommitedFiles().get(0).getOperationType());
		assertEquals("/trunk/myfolder/myfile.txt", result.get(11).getCommitedFiles().get(0).getFileName());
		assertEquals(null, result.get(11).getCommitedFiles().get(0).getFromFileName());
		
		assertEquals(13, result.get(12).getRevisionNumber());
		assertEquals("user1", result.get(12).getContributor());
		assertEquals("Filename with spaces.\n", result.get(12).getComment());
		assertEquals(OperationType.A, result.get(12).getCommitedFiles().get(0).getOperationType());
		assertEquals("/trunk/myfolder/filename with spaces.txt", result.get(12).getCommitedFiles().get(0).getFileName());
		assertEquals(null, result.get(12).getCommitedFiles().get(0).getFromFileName());
	}

	/**
	 * Unit test for checking SVN diff command.
	 */
	@Test
	public void testParseSourceControlDiff() throws IOException {
		SourceControlResultData testSvnDiffResult = mock(SourceControlResultData.class);
		when(testSvnDiffResult.getResultBufferedReader()).thenReturn(new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("svndiff_r17_r18.txt"))));
		SvnCommandParser svnCommandParser = new SvnCommandParser();
		
		List<FileDiffInformation> result = svnCommandParser.parseSourceControlDiff(testSvnDiffResult);
		
		assertEquals(2, result.size());
		
		assertEquals("/trunk/myproject/mysecontext.txt", result.get(0).getFileName());
		assertEquals(2, result.get(0).getNumberOfAdds());
		assertEquals(2, result.get(0).getNumberOfRemoves());
		assertEquals(1, result.get(0).getAtAtDiffs().size());
		assertEquals("@@ -1,9 +1,9 @@", result.get(0).getAtAtDiffs().get(0));
		
		assertEquals("/trunk/myproject/reallyalongtext.txt", result.get(1).getFileName());
		assertEquals(2, result.get(1).getNumberOfAdds());
		assertEquals(2, result.get(1).getNumberOfRemoves());
		assertEquals(3, result.get(1).getAtAtDiffs().size());
		assertEquals("@@ -16,7 +16,7 @@", result.get(1).getAtAtDiffs().get(0));
		assertEquals("@@ -59,6 +59,7 @@", result.get(1).getAtAtDiffs().get(1));
		assertEquals("@@ -140,7 +141,6 @@", result.get(1).getAtAtDiffs().get(2));
	}
}

