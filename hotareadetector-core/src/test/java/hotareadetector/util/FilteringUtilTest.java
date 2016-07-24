package hotareadetector.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import hotareadetector.data.CommitDataExtended;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FilteringUtilTest {
	/**
	 * Check filtering commit maps based on include, exclude prefixes and keeping extensions.
	 */
	@Test
	public void testFilterFileCommitMap() {
		Map<String, List<CommitDataExtended>> fileCommitMap = new HashMap<String, List<CommitDataExtended>>();
		fileCommitMap.put("/trunk/src/Main.java", Arrays.asList(new CommitDataExtended(null, 1)));
		fileCommitMap.put("/trunk/src/Main.scala", Arrays.asList(new CommitDataExtended(null, 2)));
		fileCommitMap.put("/trunk/src/Main.xml", Arrays.asList(new CommitDataExtended(null, 3)));
		fileCommitMap.put("/trunk/test/Main.java", Arrays.asList(new CommitDataExtended(null, 4)));
		fileCommitMap.put("/trunk/test/Main.scala", Arrays.asList(new CommitDataExtended(null, 5)));
		fileCommitMap.put("/trunk/test/Main.xml", Arrays.asList(new CommitDataExtended(null, 6)));
		fileCommitMap.put("/branches/src/Main.java", Arrays.asList(new CommitDataExtended(null, 7)));
		fileCommitMap.put("/branches/src/Main.scala", Arrays.asList(new CommitDataExtended(null, 8)));
		fileCommitMap.put("/branches/src/Main.xml", Arrays.asList(new CommitDataExtended(null, 9)));
		fileCommitMap.put("/branches/test/Main.java", Arrays.asList(new CommitDataExtended(null, 10)));
		fileCommitMap.put("/branches/test/Main.scala", Arrays.asList(new CommitDataExtended(null, 11)));
		fileCommitMap.put("/branches/test/Main.xml", Arrays.asList(new CommitDataExtended(null, 12)));
		fileCommitMap.put("/tags/src/Main.java", Arrays.asList(new CommitDataExtended(null, 13)));
		fileCommitMap.put("/tags/src/Main.scala", Arrays.asList(new CommitDataExtended(null, 14)));
		fileCommitMap.put("/tags/src/Main.xml", Arrays.asList(new CommitDataExtended(null, 15)));
		fileCommitMap.put("/tags/test/Main.java", Arrays.asList(new CommitDataExtended(null, 16)));
		fileCommitMap.put("/tags/test/Main.scala", Arrays.asList(new CommitDataExtended(null, 17)));
		fileCommitMap.put("/tags/test/Main.xml", Arrays.asList(new CommitDataExtended(null, 18)));
		
		List<String> includePrefixes = new ArrayList<String>();
		includePrefixes.add("/trunk/");
		includePrefixes.add("/branches/");
		
		List<String> excludePrefixes = new ArrayList<String>();
		excludePrefixes.add("/trunk/test/");
		excludePrefixes.add("/branches/test/");
		
		List<String> extensions = new ArrayList<String>();
		extensions.add("java");
		extensions.add("scala");

		FilteringUtil filteringUtil = new FilteringUtil(includePrefixes, excludePrefixes, extensions);
		Map<String, List<CommitDataExtended>> result = filteringUtil.filterFileCommitMap(fileCommitMap);
		
		assertEquals(4, result.size());
		assertTrue(result.containsKey("/trunk/src/Main.java"));
		assertEquals(new Integer(1), result.get("/trunk/src/Main.java").get(0).getChurn());
		assertTrue(result.containsKey("/trunk/src/Main.scala"));
		assertEquals(new Integer(2), result.get("/trunk/src/Main.scala").get(0).getChurn());
		assertTrue(result.containsKey("/branches/src/Main.java"));
		assertEquals(new Integer(7), result.get("/branches/src/Main.java").get(0).getChurn());
		assertTrue(result.containsKey("/branches/src/Main.scala"));
		assertEquals(new Integer(8), result.get("/branches/src/Main.scala").get(0).getChurn());
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
		
		FilteringUtil filteringUtil1 = new FilteringUtil(includePrefixes, excludePrefixes, extensions);
		FilteringUtil filteringUtil2 = new FilteringUtil(null, excludePrefixes, extensions);
		FilteringUtil filteringUtil3 = new FilteringUtil(includePrefixes, null, extensions);
		FilteringUtil filteringUtil4 = new FilteringUtil(includePrefixes, excludePrefixes, null);
		assertTrue(filteringUtil1.fileShouldRetain("/trunk/MyFile.java"));
		assertFalse(filteringUtil1.fileShouldRetain("/trunk/myfile.txt"));
		assertTrue(filteringUtil4.fileShouldRetain("/trunk/myfile.txt"));
		assertFalse(filteringUtil1.fileShouldRetain("/tags/MyFile.java"));
		assertTrue(filteringUtil2.fileShouldRetain("/tags/MyFile.java"));
		assertFalse(filteringUtil1.fileShouldRetain("/trunk/test/MyFile.java"));
		assertTrue(filteringUtil3.fileShouldRetain("/trunk/test/MyFile.java"));
	}
	
	/**
	 * Positive check of contains extension: the extension of Main.java is one of extensions java and xml.
	 */
	@Test
	public void testContainsExtensionContains() {
		List<String> extensions = new ArrayList<String>();
		extensions.add("java");
		extensions.add("xml");
		
		boolean result = (new FilteringUtil(null, null, extensions)).containsExtension("Main.java");
		
		assertTrue(result);
	}

	/**
	 * Negative check: the extension of Main.java is none of extensions cpp and h.
	 */
	@Test
	public void testContainsExtensionDoesNotContain() {
		List<String> extensions = new ArrayList<String>();
		extensions.add("cpp");
		extensions.add("h");
		
		boolean result = (new FilteringUtil(null, null, extensions)).containsExtension("Main.java");
		
		assertFalse(result);
	}

	/**
	 * No extension: positive case.
	 */
	@Test
	public void testContainsExtensionNoExtPositive() {
		List<String> extensions = new ArrayList<String>();
		extensions.add("java");
		extensions.add("xml");
		extensions.add("");
		
		boolean result = (new FilteringUtil(null, null, extensions)).containsExtension("no_extension");
		
		assertTrue(result);
	}

	/**
	 * No extension: negative case.
	 */
	@Test
	public void testContainsExtensionNoExtNegative() {
		List<String> extensions = new ArrayList<String>();
		extensions.add("java");
		extensions.add("xml");
		
		boolean result = (new FilteringUtil(null, null, extensions)).containsExtension("no_extension");
		
		assertFalse(result);
	}

	/**
	 * Empty extension: positive case.
	 */
	@Test
	public void testContainsExtensionEmptyPositive() {
		List<String> extensions = new ArrayList<String>();
		extensions.add("java");
		extensions.add("xml");
		extensions.add("");
		
		boolean result = (new FilteringUtil(null, null, extensions)).containsExtension("strange.");
		
		assertTrue(result);
	}

	/**
	 * Empty extension: negative case.
	 */
	@Test
	public void testContainsExtensionEmptyNegative() {
		List<String> extensions = new ArrayList<String>();
		extensions.add("java");
		extensions.add("xml");
		
		boolean result = (new FilteringUtil(null, null, extensions)).containsExtension("no_extension");
		
		assertFalse(result);
	}

	/**
	 * Null check.
	 */
	@Test
	public void testContainsExtensionNullCheck() {
		boolean result = (new FilteringUtil(null, null, null)).containsExtension("any.ext");
		
		assertTrue(result);
	}

	/**
	 * Empty check.
	 */
	@Test
	public void testContainsExtensionEmptyCheck() {
		List<String> extensions = new ArrayList<String>();
		
		boolean result = (new FilteringUtil(null, null, extensions)).containsExtension("any.ext");
		
		assertFalse(result);
	}


}
