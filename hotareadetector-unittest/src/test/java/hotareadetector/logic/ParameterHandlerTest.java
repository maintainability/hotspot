package hotareadetector.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import hotareadetector.data.HotAreaDetectorContext;
import hotareadetector.exception.ParameterException;

import org.junit.Test;

/**
 * Unit tests for parameter handling.
 */
public class ParameterHandlerTest {
	
	/**
	 * Null check of parse parameters.
	 */
	@Test
	public void testParseParametersNullCheck() {
		ParameterException arrivedException = null;
		try {
			ParameterHandler.parseParameters(null);
		} catch (ParameterException e) {
			arrivedException = e;
		}
		assertTrue(arrivedException != null);
		assertEquals("Parameter list must not be empty.", arrivedException.getMessage());
	}
	
	/**
	 * Empty check of parse parameters.
	 */
	@Test
	public void testParseParametersEmptyCheck() {
		ParameterException arrivedException = null;
		String[] args = new String[0];
		try {
			ParameterHandler.parseParameters(args);
		} catch (ParameterException e) {
			arrivedException = e;
		}
		assertTrue(arrivedException != null);
		assertEquals("Parameter list must not be empty.", arrivedException.getMessage());
	}
	
	/**
	 * Wrong format of parse parameters.
	 */
	@Test
	public void testParseParametersWrongFormat() {
		ParameterException arrivedException = null;
		String[] args = new String[1];
		args[0] = "param:value";
		try {
			ParameterHandler.parseParameters(args);
		} catch (ParameterException e) {
			arrivedException = e;
		}
		assertTrue(arrivedException != null);
		assertEquals("Parameter format must be arg=param. The following parameter doesn't comply this: " + args[0], arrivedException.getMessage());
	}
	
	/**
	 * Parse parameters test: invalid parameter.
	 */
	@Test
	public void testParseParametersInvalid() {
		ParameterException arrivedException = null;
		String[] args = new String[1];
		args[0] = "invalid=value";
		try {
			ParameterHandler.parseParameters(args);
		} catch (ParameterException e) {
			arrivedException = e;
		}
		assertTrue(arrivedException != null);
		assertEquals("Invalid parameter: invalid", arrivedException.getMessage());
	}
	
	/**
	 * Parse parameters test: wrong deep analysis.
	 */
	@Test
	public void testParseParametersWrongDeepAnalysis() {
		ParameterException arrivedException = null;
		String[] args = new String[1];
		args[0] = "-deepAnalysis=nonexist";
		try {
			ParameterHandler.parseParameters(args);
		} catch (ParameterException e) {
			arrivedException = e;
		}
		assertTrue(arrivedException != null);
		assertEquals("Parameter 'deepAnalysis' must be either true or false (case insensitive), but was: nonexist.", arrivedException.getMessage());
	}
	
	/**
	 * Parse parameters, minimal approach.
	 */
	@Test
	public void testParseParametersMinimal() {
		ParameterException arrivedException = null;
		HotAreaDetectorContext context = null;
		String[] args = new String[2];
		args[0] = "-client=svn.exe";
		args[1] = "-url=http://localhost/svn";
		try {
			context = ParameterHandler.parseParameters(args);
		} catch (ParameterException e) {
			arrivedException = e;
		}
		assertTrue(arrivedException == null);
		assertEquals(false, context.isDeepAnalysis());
		assertEquals("", context.getDirName());
		assertEquals(null, context.getOutputFileName());
		assertEquals("svn.exe", context.getSourceControlClientExecutor());
		assertEquals("http://localhost/svn", context.getSourceControlUrl());
		assertEquals(null, context.getSourceControlUserName());
		assertEquals(null, context.getSourceControlPassword());
		assertFalse(context.isSaveLogsOnly());
	}
	
	/**
	 * Parse parameters, maximal approach.
	 */
	@Test
	public void testParseParametersMaximal() {
		ParameterException arrivedException = null;
		HotAreaDetectorContext context = null;
		String[] args = new String[5];
		args[0] = "-client=svn.exe";
		args[1] = "-url=http://localhost/svn";
		args[2] = "-userName=myUser";
		args[3] = "-password=myPass";
		args[4] = "-deepAnalysis=false";
		try {
			context = ParameterHandler.parseParameters(args);
		} catch (ParameterException e) {
			arrivedException = e;
		}
		assertTrue(arrivedException == null);
		assertEquals(false, context.isDeepAnalysis());
		assertEquals("", context.getDirName());
		assertEquals(null, context.getOutputFileName());
		assertEquals("svn.exe", context.getSourceControlClientExecutor());
		assertEquals("http://localhost/svn", context.getSourceControlUrl());
		assertEquals("myUser", context.getSourceControlUserName());
		assertEquals("myPass", context.getSourceControlPassword());
	}
	
	/**
	 * Test extensions.
	 */
	@Test
	public void testParseParametersExtension() {
		ParameterException arrivedException = null;
		HotAreaDetectorContext context = null;
		String[] args = new String[3];
		args[0] = "-client=svn.exe";
		args[1] = "-url=http://localhost/svn";
		args[2] = "-extensions=java,xml";
		try {
			context = ParameterHandler.parseParameters(args);
		} catch (ParameterException e) {
			arrivedException = e;
		}
		assertTrue(arrivedException == null);
		assertEquals(2, context.getExtensions().size());
		assertEquals("java", context.getExtensions().get(0));
		assertEquals("xml", context.getExtensions().get(1));
	}
	
	/**
	 * Test includes.
	 */
	@Test
	public void testParseParametersIncludes() {
		ParameterException arrivedException = null;
		HotAreaDetectorContext context = null;
		String[] args = new String[3];
		args[0] = "-client=svn.exe";
		args[1] = "-url=http://localhost/svn";
		args[2] = "-includePrefixes=/trunk/myproject";
		try {
			context = ParameterHandler.parseParameters(args);
		} catch (ParameterException e) {
			arrivedException = e;
		}
		assertTrue(arrivedException == null);
		assertEquals(1, context.getIncludePrefixes().size());
		assertEquals("/trunk/myproject", context.getIncludePrefixes().get(0));
	}
	
	/**
	 * Test excludes.
	 */
	@Test
	public void testParseParametersExcludes() {
		ParameterException arrivedException = null;
		HotAreaDetectorContext context = null;
		String[] args = new String[3];
		args[0] = "-client=svn.exe";
		args[1] = "-url=http://localhost/svn";
		args[2] = "-excludePrefixes=/trunk/myproject/test,/trunk/myproject/bbtest";
		try {
			context = ParameterHandler.parseParameters(args);
		} catch (ParameterException e) {
			arrivedException = e;
		}
		assertTrue(arrivedException == null);
		assertEquals(2, context.getExcludePrefixes().size());
		assertEquals("/trunk/myproject/test", context.getExcludePrefixes().get(0));
		assertEquals("/trunk/myproject/bbtest", context.getExcludePrefixes().get(1));
	}
	
	/**
	 * Test revision.
	 */
	@Test
	public void testParseParametersRevision() {
		ParameterException arrivedException = null;
		HotAreaDetectorContext context = null;
		String[] args = new String[3];
		args[0] = "-client=svn.exe";
		args[1] = "-url=http://localhost/svn";
		args[2] = "-revision=12345";
		try {
			context = ParameterHandler.parseParameters(args);
		} catch (ParameterException e) {
			arrivedException = e;
		}
		assertTrue(arrivedException == null);
		assertEquals(12345, context.getRevision().intValue());
	}
	

	/**
	 * Test revision, invalid format.
	 */
	@Test
	public void testParseParametersRevisionInvalid() {
		ParameterException arrivedException = null;
		String[] args = new String[3];
		args[0] = "-client=svn.exe";
		args[1] = "-url=http://localhost/svn";
		args[2] = "-revision=abcde";
		try {
			ParameterHandler.parseParameters(args);
		} catch (ParameterException e) {
			arrivedException = e;
		}
		assertTrue(arrivedException != null);
		assertEquals("Format of parameter revison is invalid. Should be numeric, e.g. 12345, but was abcde.", arrivedException.getMessage());
	}
	

	/**
	 * Test revision, not set.
	 */
	@Test
	public void testParseParametersRevisionNotSet() {
		ParameterException arrivedException = null;
		HotAreaDetectorContext context = null;
		String[] args = new String[2];
		args[0] = "-client=svn.exe";
		args[1] = "-url=http://localhost/svn";
		try {
			context = ParameterHandler.parseParameters(args);
		} catch (ParameterException e) {
			arrivedException = e;
		}
		assertTrue(arrivedException == null);
		assertEquals(null, context.getRevision());
	}
	

	/**
	 * Save logs only test, true.
	 */
	@Test
	public void testParseParametersSaveLogsOnlyTrueSvn() {
		ParameterException arrivedException = null;
		HotAreaDetectorContext context = null;
		String[] args = new String[3];
		args[0] = "-client=svn.exe";
		args[1] = "-url=http://localhost/svn";
		args[2] = "-saveLogsOnly=true";
		try {
			context = ParameterHandler.parseParameters(args);
		} catch (ParameterException e) {
			arrivedException = e;
		}
		assertTrue(arrivedException == null);
		assertTrue(context.isSaveLogsOnly());
	}
	
	/**
	 * Save logs only test, false.
	 */
	@Test
	public void testParseParametersSaveLogsOnlyFalse() {
		ParameterException arrivedException = null;
		HotAreaDetectorContext context = null;
		String[] args = new String[3];
		args[0] = "-client=svn.exe";
		args[1] = "-url=http://localhost/svn";
		args[2] = "-saveLogsOnly=FALSE";
		try {
			context = ParameterHandler.parseParameters(args);
		} catch (ParameterException e) {
			arrivedException = e;
		}
		assertTrue(arrivedException == null);
		assertFalse(context.isSaveLogsOnly());
	}
	
	/**
	 * Save logs only test, invalid.
	 */
	@Test
	public void testParseParametersSaveLogsOnlyInvalid() {
		ParameterException arrivedException = null;
		String[] args = new String[3];
		args[0] = "-client=svn.exe";
		args[1] = "-url=http://localhost/svn";
		args[2] = "-saveLogsOnly=invalid";
		try {
			ParameterHandler.parseParameters(args);
		} catch (ParameterException e) {
			arrivedException = e;
		}
		assertTrue(arrivedException != null);
		assertEquals("Parameter 'saveLogsOnly' must be either true or false (case insensitive), but was: invalid.", arrivedException.getMessage());
	}
	
	/**
	 * Checks if printUsage works correct. It just checks if exception occurs or not.
	 */
	@Test
	public void testPrintUsage() {
		Throwable actualThrowable = null;
		try {
			ParameterHandler.printUsage();
		} catch (Throwable t) {
			actualThrowable = t;
		}
		assertEquals(null, actualThrowable);
	}
		
}
