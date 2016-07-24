package hotareadetector.data;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CommitDataTest {
	private CommitData commitData1;
	private CommitData commitData2a;
	private CommitData commitData2b;
	private CommitData commitData3;
	
	@Before
	public void setUp() {
		commitData1 = new CommitData();
		commitData1.setRevisionNumber(1);
		commitData1.setComment("Comment 1");;
		
		commitData2a = new CommitData();
		commitData2a.setRevisionNumber(2);
		commitData2a.setComment("Comment 2A");;
		
		commitData2b = new CommitData();
		commitData2b.setRevisionNumber(2);
		commitData2b.setComment("Comment 2B");;
		
		commitData3 = new CommitData();
		commitData3.setRevisionNumber(3);
		commitData3.setComment("Comment 3");;
	}
	
	@Test
	public void testCompareTo() {
		assertTrue(commitData1.compareTo(commitData1) == 0);
		assertTrue(commitData1.compareTo(commitData2a) < 0);
		assertTrue(commitData1.compareTo(commitData2b) < 0);
		assertTrue(commitData1.compareTo(commitData3) < 0);
		
		assertTrue(commitData2a.compareTo(commitData1) > 0);
		assertTrue(commitData2a.compareTo(commitData2a) == 0);
		assertTrue(commitData2a.compareTo(commitData2b) == 0);
		assertTrue(commitData2a.compareTo(commitData3) < 0);
		
		assertTrue(commitData2b.compareTo(commitData1) > 0);
		assertTrue(commitData2b.compareTo(commitData2a) == 0);
		assertTrue(commitData2b.compareTo(commitData2b) == 0);
		assertTrue(commitData2b.compareTo(commitData3) < 0);
		
		assertTrue(commitData3.compareTo(commitData1) > 0);
		assertTrue(commitData3.compareTo(commitData2a) > 0);
		assertTrue(commitData3.compareTo(commitData2b) > 0);
		assertTrue(commitData3.compareTo(commitData3) == 0);
	}

}
