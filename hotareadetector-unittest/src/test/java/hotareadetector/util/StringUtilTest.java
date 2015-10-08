package hotareadetector.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Unit tests for StringUtil.
 *
 */
public class StringUtilTest {
	
	/**
	 * Positive check: the extension of Main.java is one of extensions java and xml.
	 */
	@Test
	public void testContainsExtensionContains() {
		List<String> extensions = new ArrayList<String>();
		extensions.add("java");
		extensions.add("xml");
		
		boolean result = StringUtil.containsExtension("Main.java", extensions);
		
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
		
		boolean result = StringUtil.containsExtension("Main.java", extensions);
		
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
		
		boolean result = StringUtil.containsExtension("no_extension", extensions);
		
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
		
		boolean result = StringUtil.containsExtension("no_extension", extensions);
		
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
		
		boolean result = StringUtil.containsExtension("strange.", extensions);
		
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
		
		boolean result = StringUtil.containsExtension("no_extension", extensions);
		
		assertFalse(result);
	}

	/**
	 * Null check.
	 */
	@Test
	public void testContainsExtensionNullCheck() {
		boolean result = StringUtil.containsExtension("any.ext", null);
		
		assertTrue(result);
	}

	/**
	 * Empty check.
	 */
	@Test
	public void testContainsExtensionEmptyCheck() {
		List<String> extensions = new ArrayList<String>();
		
		boolean result = StringUtil.containsExtension("any.ext", extensions);
		
		assertFalse(result);
	}

}
