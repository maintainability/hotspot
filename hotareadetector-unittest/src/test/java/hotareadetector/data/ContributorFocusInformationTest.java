package hotareadetector.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Set;

/**
 * Contributor focus information related unit test.
 */
public class ContributorFocusInformationTest {
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd. hh:mm:ss");
	
	/**
	 * Build up the example in the comment of the main file.
	 */
	protected ContributorFocusInformation buildContributorFocusInformation() throws ParseException {
		ContributorFocusInformation contributorFocusInformation = new ContributorFocusInformation();
		contributorFocusInformation.addModification("steve", dateFormat.parse("2016.02.12. 12:43:15"), "/com/mycompany/myapp/Main.java");
		contributorFocusInformation.addModification("steve", dateFormat.parse("2016.02.12. 12:43:15"), "/com/mycompany/myapp/game/Game.java");
		contributorFocusInformation.addModification("steve", dateFormat.parse("2016.02.12. 12:43:15"), "/com/mycompany/myapp/util/Conversions.java");
		contributorFocusInformation.addModification("steve", dateFormat.parse("2016.03.08. 10:29:34"), "/com/mycompany/myapp/Main.java");
		contributorFocusInformation.addModification("steve", dateFormat.parse("2016.03.08. 10:29:34"), "/com/mycompany/myapp/util/Calculations.java");
		contributorFocusInformation.addModification("bob",   dateFormat.parse("2016.03.08. 16:18:54"), "/com/mycompany/myapp/Main.java");
		contributorFocusInformation.addModification("bob",   dateFormat.parse("2016.03.08. 16:18:54"), "/com/mycompany/myapp/util/Calculations.java");
		contributorFocusInformation.addModification("steve", dateFormat.parse("2016.03.09. 17:22:08"), "/com/mycompany/myapp/util/Calculations.java");
		contributorFocusInformation.addModification("steve", dateFormat.parse("2016.03.09. 17:22:08"), "/com/mycompany/myapp/util/Asserts.java");
		return contributorFocusInformation;
	}
	
	@Test
	public void testBuildUp() throws ParseException {
		ContributorFocusInformation contributorFocusInformation = buildContributorFocusInformation();
		
		assertEquals(2, contributorFocusInformation.contributorsModifications.keySet().size());
		assertTrue(contributorFocusInformation.contributorsModifications.containsKey("steve"));
		assertTrue(contributorFocusInformation.contributorsModifications.containsKey("bob"));
		assertFalse(contributorFocusInformation.contributorsModifications.containsKey("dave"));
		assertEquals(3, contributorFocusInformation.contributorsModifications.get("steve").size());

		assertTrue(contributorFocusInformation.contributorsModifications.get("steve").keySet().contains(dateFormat.parse("2016.02.12. 12:43:15")));
		assertEquals(3, contributorFocusInformation.contributorsModifications.get("steve").get(dateFormat.parse("2016.02.12. 12:43:15")).size());
		assertEquals("/com/mycompany/myapp/Main.java", contributorFocusInformation.contributorsModifications.get("steve").get(dateFormat.parse("2016.02.12. 12:43:15")).get(0));
		assertEquals("/com/mycompany/myapp/game/Game.java", contributorFocusInformation.contributorsModifications.get("steve").get(dateFormat.parse("2016.02.12. 12:43:15")).get(1));
		assertEquals("/com/mycompany/myapp/util/Conversions.java", contributorFocusInformation.contributorsModifications.get("steve").get(dateFormat.parse("2016.02.12. 12:43:15")).get(2));
		
		assertTrue(contributorFocusInformation.contributorsModifications.get("steve").keySet().contains(dateFormat.parse("2016.03.08. 10:29:34")));
		assertEquals(2, contributorFocusInformation.contributorsModifications.get("steve").get(dateFormat.parse("2016.03.08. 10:29:34")).size());
		assertEquals("/com/mycompany/myapp/Main.java", contributorFocusInformation.contributorsModifications.get("steve").get(dateFormat.parse("2016.03.08. 10:29:34")).get(0));
		assertEquals("/com/mycompany/myapp/util/Calculations.java", contributorFocusInformation.contributorsModifications.get("steve").get(dateFormat.parse("2016.03.08. 10:29:34")).get(1));
		
		assertTrue(contributorFocusInformation.contributorsModifications.get("steve").keySet().contains(dateFormat.parse("2016.03.09. 17:22:08")));
		assertEquals(2, contributorFocusInformation.contributorsModifications.get("steve").get(dateFormat.parse("2016.03.09. 17:22:08")).size());
		assertEquals("/com/mycompany/myapp/util/Calculations.java", contributorFocusInformation.contributorsModifications.get("steve").get(dateFormat.parse("2016.03.09. 17:22:08")).get(0));
		assertEquals("/com/mycompany/myapp/util/Asserts.java", contributorFocusInformation.contributorsModifications.get("steve").get(dateFormat.parse("2016.03.09. 17:22:08")).get(1));
		
		assertFalse(contributorFocusInformation.contributorsModifications.get("steve").keySet().contains(dateFormat.parse("2016.03.08. 16:18:54")));
		
		assertEquals(1, contributorFocusInformation.contributorsModifications.get("bob").size());
		assertFalse(contributorFocusInformation.contributorsModifications.get("bob").keySet().contains(dateFormat.parse("2016.02.12. 12:43:15")));
		assertFalse(contributorFocusInformation.contributorsModifications.get("bob").keySet().contains(dateFormat.parse("2016.03.08. 10:29:34")));
		assertFalse(contributorFocusInformation.contributorsModifications.get("bob").keySet().contains(dateFormat.parse("2016.03.09. 17:22:08")));
		assertTrue(contributorFocusInformation.contributorsModifications.get("bob").keySet().contains(dateFormat.parse("2016.03.08. 16:18:54")));
		assertEquals(2, contributorFocusInformation.contributorsModifications.get("bob").get(dateFormat.parse("2016.03.08. 16:18:54")).size());
		assertEquals("/com/mycompany/myapp/Main.java", contributorFocusInformation.contributorsModifications.get("bob").get(dateFormat.parse("2016.03.08. 16:18:54")).get(0));
		assertEquals("/com/mycompany/myapp/util/Calculations.java", contributorFocusInformation.contributorsModifications.get("bob").get(dateFormat.parse("2016.03.08. 16:18:54")).get(1));
		
		assertEquals(dateFormat.parse("2016.02.12. 12:43:15"), contributorFocusInformation.fileLastModifiedByContributor.get(new ContributorFile("steve", "/com/mycompany/myapp/game/Game.java")));
		assertEquals(dateFormat.parse("2016.02.12. 12:43:15"), contributorFocusInformation.fileLastModifiedByContributor.get(new ContributorFile("steve", "/com/mycompany/myapp/util/Conversions.java")));
		assertEquals(dateFormat.parse("2016.03.08. 10:29:34"), contributorFocusInformation.fileLastModifiedByContributor.get(new ContributorFile("steve", "/com/mycompany/myapp/Main.java")));
		assertEquals(dateFormat.parse("2016.03.08. 16:18:54"), contributorFocusInformation.fileLastModifiedByContributor.get(new ContributorFile("bob", "/com/mycompany/myapp/Main.java")));
		assertEquals(dateFormat.parse("2016.03.08. 16:18:54"), contributorFocusInformation.fileLastModifiedByContributor.get(new ContributorFile("bob", "/com/mycompany/myapp/util/Calculations.java")));
		assertEquals(dateFormat.parse("2016.03.09. 17:22:08"), contributorFocusInformation.fileLastModifiedByContributor.get(new ContributorFile("steve", "/com/mycompany/myapp/util/Calculations.java")));
		assertEquals(dateFormat.parse("2016.03.09. 17:22:08"), contributorFocusInformation.fileLastModifiedByContributor.get(new ContributorFile("steve", "/com/mycompany/myapp/util/Asserts.java")));
	}
	
	/**
	 * Which files has been modified by steve no later than last modification of him of file /com/mycompany/myapp/Main.java?
	 * Last modification of that file by steve: 2016.03.08. 10:29:34.
	 * Files modified by steve no later:
	 * - /com/mycompany/myapp/Main.java (2 times)
	 * - /com/mycompany/myapp/game/Game.java
	 * - /com/mycompany/myapp/util/Conversions.java
	 * - /com/mycompany/myapp/util/Calculations.java
	 */
	@Test
	public void testGetModifiedFilesPerContributor() throws ParseException {
		ContributorFocusInformation contributorFocusInformation = buildContributorFocusInformation();
		
		Set<String> modifiedFiles = contributorFocusInformation.getModifiedFilesPerContributor("steve", "/com/mycompany/myapp/Main.java");
		
		assertEquals(4, modifiedFiles.size());
		assertTrue(modifiedFiles.contains("/com/mycompany/myapp/Main.java"));
		assertTrue(modifiedFiles.contains("/com/mycompany/myapp/game/Game.java"));
		assertTrue(modifiedFiles.contains("/com/mycompany/myapp/util/Conversions.java"));
		assertTrue(modifiedFiles.contains("/com/mycompany/myapp/util/Calculations.java"));
		assertFalse(modifiedFiles.contains("/com/mycompany/myapp/util/Asserts.java"));
		assertFalse(modifiedFiles.contains("/com/mycompany/myapp/util/NoSuchFile.java"));
	}
	
	/**
	 * Checks the correctness of function determineRenamedValue.
	 * It tests the cases listed in the commend of the code.
	 */
	@Test
	public void testDetermineRenamedValue() {
		ContributorFocusInformation contributorFocusInformation = new ContributorFocusInformation();
		
		assertEquals("/com/mycompany/myapp/main/Main.java", contributorFocusInformation.determineRenamedValue("/com/mycompany/myapp/Main.java", "/com/mycompany/myapp/main/Main.java", "/com/mycompany/myapp/Main.java"));
		assertEquals("/com/mycompany/myapp/Util.java", contributorFocusInformation.determineRenamedValue("/com/mycompany/myapp/Main.java", "/com/mycompany/myapp/main/Main.java", "/com/mycompany/myapp/Util.java"));
		assertEquals("/org/mycompany/myapp/Main.java", contributorFocusInformation.determineRenamedValue("/com/mycompany", "/org/mycompany", "/com/mycompany/myapp/Main.java"));
	}
	
	/**
	 * Checks if renaming a file works correctly.
	 */
	@Test
	public void testApplyRenameFile() throws ParseException {
		ContributorFocusInformation contributorFocusInformation = buildContributorFocusInformation();
		
		contributorFocusInformation.applyRename("/com/mycompany/myapp/Main.java", "/com/mycompany/myapp/main/Main.java");
		
		assertEquals(2, contributorFocusInformation.contributorsModifications.keySet().size());
		assertTrue(contributorFocusInformation.contributorsModifications.containsKey("steve"));
		assertTrue(contributorFocusInformation.contributorsModifications.containsKey("bob"));
		assertFalse(contributorFocusInformation.contributorsModifications.containsKey("dave"));
		assertEquals(3, contributorFocusInformation.contributorsModifications.get("steve").size());

		assertTrue(contributorFocusInformation.contributorsModifications.get("steve").keySet().contains(dateFormat.parse("2016.02.12. 12:43:15")));
		assertEquals(3, contributorFocusInformation.contributorsModifications.get("steve").get(dateFormat.parse("2016.02.12. 12:43:15")).size());
		assertEquals("/com/mycompany/myapp/main/Main.java", contributorFocusInformation.contributorsModifications.get("steve").get(dateFormat.parse("2016.02.12. 12:43:15")).get(0));
		assertEquals("/com/mycompany/myapp/game/Game.java", contributorFocusInformation.contributorsModifications.get("steve").get(dateFormat.parse("2016.02.12. 12:43:15")).get(1));
		assertEquals("/com/mycompany/myapp/util/Conversions.java", contributorFocusInformation.contributorsModifications.get("steve").get(dateFormat.parse("2016.02.12. 12:43:15")).get(2));
		
		assertTrue(contributorFocusInformation.contributorsModifications.get("steve").keySet().contains(dateFormat.parse("2016.03.08. 10:29:34")));
		assertEquals(2, contributorFocusInformation.contributorsModifications.get("steve").get(dateFormat.parse("2016.03.08. 10:29:34")).size());
		assertEquals("/com/mycompany/myapp/main/Main.java", contributorFocusInformation.contributorsModifications.get("steve").get(dateFormat.parse("2016.03.08. 10:29:34")).get(0));
		assertEquals("/com/mycompany/myapp/util/Calculations.java", contributorFocusInformation.contributorsModifications.get("steve").get(dateFormat.parse("2016.03.08. 10:29:34")).get(1));
		
		assertTrue(contributorFocusInformation.contributorsModifications.get("steve").keySet().contains(dateFormat.parse("2016.03.09. 17:22:08")));
		assertEquals(2, contributorFocusInformation.contributorsModifications.get("steve").get(dateFormat.parse("2016.03.09. 17:22:08")).size());
		assertEquals("/com/mycompany/myapp/util/Calculations.java", contributorFocusInformation.contributorsModifications.get("steve").get(dateFormat.parse("2016.03.09. 17:22:08")).get(0));
		assertEquals("/com/mycompany/myapp/util/Asserts.java", contributorFocusInformation.contributorsModifications.get("steve").get(dateFormat.parse("2016.03.09. 17:22:08")).get(1));
		
		assertFalse(contributorFocusInformation.contributorsModifications.get("steve").keySet().contains(dateFormat.parse("2016.03.08. 16:18:54")));
		
		assertEquals(1, contributorFocusInformation.contributorsModifications.get("bob").size());
		assertFalse(contributorFocusInformation.contributorsModifications.get("bob").keySet().contains(dateFormat.parse("2016.02.12. 12:43:15")));
		assertFalse(contributorFocusInformation.contributorsModifications.get("bob").keySet().contains(dateFormat.parse("2016.03.08. 10:29:34")));
		assertFalse(contributorFocusInformation.contributorsModifications.get("bob").keySet().contains(dateFormat.parse("2016.03.09. 17:22:08")));
		assertTrue(contributorFocusInformation.contributorsModifications.get("bob").keySet().contains(dateFormat.parse("2016.03.08. 16:18:54")));
		assertEquals(2, contributorFocusInformation.contributorsModifications.get("bob").get(dateFormat.parse("2016.03.08. 16:18:54")).size());
		assertEquals("/com/mycompany/myapp/main/Main.java", contributorFocusInformation.contributorsModifications.get("bob").get(dateFormat.parse("2016.03.08. 16:18:54")).get(0));
		assertEquals("/com/mycompany/myapp/util/Calculations.java", contributorFocusInformation.contributorsModifications.get("bob").get(dateFormat.parse("2016.03.08. 16:18:54")).get(1));
		
		assertEquals(dateFormat.parse("2016.02.12. 12:43:15"), contributorFocusInformation.fileLastModifiedByContributor.get(new ContributorFile("steve", "/com/mycompany/myapp/game/Game.java")));
		assertEquals(dateFormat.parse("2016.02.12. 12:43:15"), contributorFocusInformation.fileLastModifiedByContributor.get(new ContributorFile("steve", "/com/mycompany/myapp/util/Conversions.java")));
		assertEquals(dateFormat.parse("2016.03.08. 10:29:34"), contributorFocusInformation.fileLastModifiedByContributor.get(new ContributorFile("steve", "/com/mycompany/myapp/main/Main.java")));
		assertEquals(dateFormat.parse("2016.03.08. 16:18:54"), contributorFocusInformation.fileLastModifiedByContributor.get(new ContributorFile("bob", "/com/mycompany/myapp/main/Main.java")));
		assertEquals(dateFormat.parse("2016.03.08. 16:18:54"), contributorFocusInformation.fileLastModifiedByContributor.get(new ContributorFile("bob", "/com/mycompany/myapp/util/Calculations.java")));
		assertEquals(dateFormat.parse("2016.03.09. 17:22:08"), contributorFocusInformation.fileLastModifiedByContributor.get(new ContributorFile("steve", "/com/mycompany/myapp/util/Calculations.java")));
		assertEquals(dateFormat.parse("2016.03.09. 17:22:08"), contributorFocusInformation.fileLastModifiedByContributor.get(new ContributorFile("steve", "/com/mycompany/myapp/util/Asserts.java")));
	}
	
	/**
	 * Checks if renaming a directory works correctly.
	 */
	@Test
	public void testApplyRenameDirectory() throws ParseException {
		ContributorFocusInformation contributorFocusInformation = buildContributorFocusInformation();
		
		contributorFocusInformation.applyRename("/com/mycompany", "/org/mycompany");
		
		assertEquals(2, contributorFocusInformation.contributorsModifications.keySet().size());
		assertTrue(contributorFocusInformation.contributorsModifications.containsKey("steve"));
		assertTrue(contributorFocusInformation.contributorsModifications.containsKey("bob"));
		assertFalse(contributorFocusInformation.contributorsModifications.containsKey("dave"));
		assertEquals(3, contributorFocusInformation.contributorsModifications.get("steve").size());

		assertTrue(contributorFocusInformation.contributorsModifications.get("steve").keySet().contains(dateFormat.parse("2016.02.12. 12:43:15")));
		assertEquals(3, contributorFocusInformation.contributorsModifications.get("steve").get(dateFormat.parse("2016.02.12. 12:43:15")).size());
		assertEquals("/org/mycompany/myapp/Main.java", contributorFocusInformation.contributorsModifications.get("steve").get(dateFormat.parse("2016.02.12. 12:43:15")).get(0));
		assertEquals("/org/mycompany/myapp/game/Game.java", contributorFocusInformation.contributorsModifications.get("steve").get(dateFormat.parse("2016.02.12. 12:43:15")).get(1));
		assertEquals("/org/mycompany/myapp/util/Conversions.java", contributorFocusInformation.contributorsModifications.get("steve").get(dateFormat.parse("2016.02.12. 12:43:15")).get(2));
		
		assertTrue(contributorFocusInformation.contributorsModifications.get("steve").keySet().contains(dateFormat.parse("2016.03.08. 10:29:34")));
		assertEquals(2, contributorFocusInformation.contributorsModifications.get("steve").get(dateFormat.parse("2016.03.08. 10:29:34")).size());
		assertEquals("/org/mycompany/myapp/Main.java", contributorFocusInformation.contributorsModifications.get("steve").get(dateFormat.parse("2016.03.08. 10:29:34")).get(0));
		assertEquals("/org/mycompany/myapp/util/Calculations.java", contributorFocusInformation.contributorsModifications.get("steve").get(dateFormat.parse("2016.03.08. 10:29:34")).get(1));
		
		assertTrue(contributorFocusInformation.contributorsModifications.get("steve").keySet().contains(dateFormat.parse("2016.03.09. 17:22:08")));
		assertEquals(2, contributorFocusInformation.contributorsModifications.get("steve").get(dateFormat.parse("2016.03.09. 17:22:08")).size());
		assertEquals("/org/mycompany/myapp/util/Calculations.java", contributorFocusInformation.contributorsModifications.get("steve").get(dateFormat.parse("2016.03.09. 17:22:08")).get(0));
		assertEquals("/org/mycompany/myapp/util/Asserts.java", contributorFocusInformation.contributorsModifications.get("steve").get(dateFormat.parse("2016.03.09. 17:22:08")).get(1));
		
		assertFalse(contributorFocusInformation.contributorsModifications.get("steve").keySet().contains(dateFormat.parse("2016.03.08. 16:18:54")));
		
		assertEquals(1, contributorFocusInformation.contributorsModifications.get("bob").size());
		assertFalse(contributorFocusInformation.contributorsModifications.get("bob").keySet().contains(dateFormat.parse("2016.02.12. 12:43:15")));
		assertFalse(contributorFocusInformation.contributorsModifications.get("bob").keySet().contains(dateFormat.parse("2016.03.08. 10:29:34")));
		assertFalse(contributorFocusInformation.contributorsModifications.get("bob").keySet().contains(dateFormat.parse("2016.03.09. 17:22:08")));
		assertTrue(contributorFocusInformation.contributorsModifications.get("bob").keySet().contains(dateFormat.parse("2016.03.08. 16:18:54")));
		assertEquals(2, contributorFocusInformation.contributorsModifications.get("bob").get(dateFormat.parse("2016.03.08. 16:18:54")).size());
		assertEquals("/org/mycompany/myapp/Main.java", contributorFocusInformation.contributorsModifications.get("bob").get(dateFormat.parse("2016.03.08. 16:18:54")).get(0));
		assertEquals("/org/mycompany/myapp/util/Calculations.java", contributorFocusInformation.contributorsModifications.get("bob").get(dateFormat.parse("2016.03.08. 16:18:54")).get(1));
		
		assertEquals(dateFormat.parse("2016.02.12. 12:43:15"), contributorFocusInformation.fileLastModifiedByContributor.get(new ContributorFile("steve", "/org/mycompany/myapp/game/Game.java")));
		assertEquals(dateFormat.parse("2016.02.12. 12:43:15"), contributorFocusInformation.fileLastModifiedByContributor.get(new ContributorFile("steve", "/org/mycompany/myapp/util/Conversions.java")));
		assertEquals(dateFormat.parse("2016.03.08. 10:29:34"), contributorFocusInformation.fileLastModifiedByContributor.get(new ContributorFile("steve", "/org/mycompany/myapp/Main.java")));
		assertEquals(dateFormat.parse("2016.03.08. 16:18:54"), contributorFocusInformation.fileLastModifiedByContributor.get(new ContributorFile("bob", "/org/mycompany/myapp/Main.java")));
		assertEquals(dateFormat.parse("2016.03.08. 16:18:54"), contributorFocusInformation.fileLastModifiedByContributor.get(new ContributorFile("bob", "/org/mycompany/myapp/util/Calculations.java")));
		assertEquals(dateFormat.parse("2016.03.09. 17:22:08"), contributorFocusInformation.fileLastModifiedByContributor.get(new ContributorFile("steve", "/org/mycompany/myapp/util/Calculations.java")));
		assertEquals(dateFormat.parse("2016.03.09. 17:22:08"), contributorFocusInformation.fileLastModifiedByContributor.get(new ContributorFile("steve", "/org/mycompany/myapp/util/Asserts.java")));
	}
}
