package hotareadetector.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class CommitedFileDataTest {
	private static final CommitedFileData commitFileData1 = new CommitedFileData(OperationType.D, "file1", null);
	private static final CommitedFileData commitFileData2 = new CommitedFileData(OperationType.D, "file2", null);
	private static final CommitedFileData commitFileData3 = new CommitedFileData(OperationType.A, "file3", null);
	private static final CommitedFileData commitFileData4 = new CommitedFileData(OperationType.M, "file4", null);
	private static final CommitedFileData commitFileData5 = new CommitedFileData(OperationType.R, "file5", "from");
	
	@Test
	public void testCompareTo() {
		assertTrue(commitFileData1.compareTo(commitFileData1) == 0);
		assertTrue(commitFileData1.compareTo(commitFileData2) == 0);
		assertTrue(commitFileData1.compareTo(commitFileData3) > 0);
		assertTrue(commitFileData1.compareTo(commitFileData4) > 0);
		assertTrue(commitFileData1.compareTo(commitFileData5) > 0);
		
		assertTrue(commitFileData2.compareTo(commitFileData1) == 0);
		assertTrue(commitFileData3.compareTo(commitFileData1) < 0);
		assertTrue(commitFileData4.compareTo(commitFileData1) < 0);
		assertTrue(commitFileData5.compareTo(commitFileData1) < 0);
	}
	
	@Test
	public void testSort() {
		List<CommitedFileData> commitedFileDataList = new ArrayList<CommitedFileData>();
		commitedFileDataList.add(commitFileData1);
		commitedFileDataList.add(commitFileData2);
		commitedFileDataList.add(commitFileData3);
		commitedFileDataList.add(commitFileData4);
		commitedFileDataList.add(commitFileData5);
		
		Collections.sort(commitedFileDataList);
		
		assertEquals(5, commitedFileDataList.size());
		assertEquals("file3", commitedFileDataList.get(0).getFileName());
		assertEquals("file4", commitedFileDataList.get(1).getFileName());
		assertEquals("file5", commitedFileDataList.get(2).getFileName());
		assertEquals("file1", commitedFileDataList.get(3).getFileName());
		assertEquals("file2", commitedFileDataList.get(4).getFileName());
		
	}

}
