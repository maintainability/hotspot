package hotareadetector.data;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ContributorFileTest {
	@Test
	public void testCompare() {
		ContributorFile contributorFile1 = new ContributorFile("jack", "Main.java");
		ContributorFile contributorFile2 = new ContributorFile("jack", "Main.java");
		ContributorFile contributorFile3 = new ContributorFile("joe", "Main.java");
		ContributorFile contributorFile4 = new ContributorFile("joe", "Util.java");
		assertTrue(contributorFile1.equals(contributorFile1));
		assertTrue(contributorFile1.equals(contributorFile2));
		assertFalse(contributorFile1.equals(contributorFile3));
		assertFalse(contributorFile1.equals(contributorFile4));
		assertTrue(contributorFile2.equals(contributorFile1));
		assertTrue(contributorFile2.equals(contributorFile2));
		assertFalse(contributorFile2.equals(contributorFile3));
		assertFalse(contributorFile2.equals(contributorFile4));
		assertFalse(contributorFile3.equals(contributorFile1));
		assertFalse(contributorFile3.equals(contributorFile2));
		assertTrue(contributorFile3.equals(contributorFile3));
		assertFalse(contributorFile3.equals(contributorFile4));
		assertFalse(contributorFile4.equals(contributorFile1));
		assertFalse(contributorFile4.equals(contributorFile2));
		assertFalse(contributorFile4.equals(contributorFile3));
		assertTrue(contributorFile4.equals(contributorFile4));
	}
}
