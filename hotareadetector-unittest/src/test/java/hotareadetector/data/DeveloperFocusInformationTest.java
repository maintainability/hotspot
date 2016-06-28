package hotareadetector.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Set;

/**
 * Developer focus information related unit test.
 */
public class DeveloperFocusInformationTest {
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd. hh:mm:ss");
	
	/**
	 * Build up the example in the comment of the main file.
	 */
	protected DeveloperFocusInformation buildDeveloperFocusInformation() throws ParseException {
		DeveloperFocusInformation developerFocusInformation = new DeveloperFocusInformation();
		developerFocusInformation.addModification("steve", dateFormat.parse("2016.02.12. 12:43:15"), "/com/mycompany/myapp/Main.java");
		developerFocusInformation.addModification("steve", dateFormat.parse("2016.02.12. 12:43:15"), "/com/mycompany/myapp/game/Game.java");
		developerFocusInformation.addModification("steve", dateFormat.parse("2016.02.12. 12:43:15"), "/com/mycompany/myapp/util/Conversions.java");
		developerFocusInformation.addModification("steve", dateFormat.parse("2016.03.08. 10:29:34"), "/com/mycompany/myapp/Main.java");
		developerFocusInformation.addModification("steve", dateFormat.parse("2016.03.08. 10:29:34"), "/com/mycompany/myapp/util/Calculations.java");
		developerFocusInformation.addModification("bob",   dateFormat.parse("2016.03.08. 16:18:54"), "/com/mycompany/myapp/Main.java");
		developerFocusInformation.addModification("bob",   dateFormat.parse("2016.03.08. 16:18:54"), "/com/mycompany/myapp/util/Calculations.java");
		developerFocusInformation.addModification("steve", dateFormat.parse("2016.03.09. 17:22:08"), "/com/mycompany/myapp/util/Calculations.java");
		developerFocusInformation.addModification("steve", dateFormat.parse("2016.03.09. 17:22:08"), "/com/mycompany/myapp/util/Asserts.java");
		return developerFocusInformation;
	}
	
	@Test
	public void testBuildUp() throws ParseException {
		DeveloperFocusInformation developerFocusInformation = buildDeveloperFocusInformation();
		
		assertEquals(2, developerFocusInformation.developersModifications.keySet().size());
		assertTrue(developerFocusInformation.developersModifications.containsKey("steve"));
		assertTrue(developerFocusInformation.developersModifications.containsKey("bob"));
		assertFalse(developerFocusInformation.developersModifications.containsKey("dave"));
		assertEquals(3, developerFocusInformation.developersModifications.get("steve").size());

		assertTrue(developerFocusInformation.developersModifications.get("steve").keySet().contains(dateFormat.parse("2016.02.12. 12:43:15")));
		assertEquals(3, developerFocusInformation.developersModifications.get("steve").get(dateFormat.parse("2016.02.12. 12:43:15")).size());
		assertEquals("/com/mycompany/myapp/Main.java", developerFocusInformation.developersModifications.get("steve").get(dateFormat.parse("2016.02.12. 12:43:15")).get(0));
		assertEquals("/com/mycompany/myapp/game/Game.java", developerFocusInformation.developersModifications.get("steve").get(dateFormat.parse("2016.02.12. 12:43:15")).get(1));
		assertEquals("/com/mycompany/myapp/util/Conversions.java", developerFocusInformation.developersModifications.get("steve").get(dateFormat.parse("2016.02.12. 12:43:15")).get(2));
		
		assertTrue(developerFocusInformation.developersModifications.get("steve").keySet().contains(dateFormat.parse("2016.03.08. 10:29:34")));
		assertEquals(2, developerFocusInformation.developersModifications.get("steve").get(dateFormat.parse("2016.03.08. 10:29:34")).size());
		assertEquals("/com/mycompany/myapp/Main.java", developerFocusInformation.developersModifications.get("steve").get(dateFormat.parse("2016.03.08. 10:29:34")).get(0));
		assertEquals("/com/mycompany/myapp/util/Calculations.java", developerFocusInformation.developersModifications.get("steve").get(dateFormat.parse("2016.03.08. 10:29:34")).get(1));
		
		assertTrue(developerFocusInformation.developersModifications.get("steve").keySet().contains(dateFormat.parse("2016.03.09. 17:22:08")));
		assertEquals(2, developerFocusInformation.developersModifications.get("steve").get(dateFormat.parse("2016.03.09. 17:22:08")).size());
		assertEquals("/com/mycompany/myapp/util/Calculations.java", developerFocusInformation.developersModifications.get("steve").get(dateFormat.parse("2016.03.09. 17:22:08")).get(0));
		assertEquals("/com/mycompany/myapp/util/Asserts.java", developerFocusInformation.developersModifications.get("steve").get(dateFormat.parse("2016.03.09. 17:22:08")).get(1));
		
		assertFalse(developerFocusInformation.developersModifications.get("steve").keySet().contains(dateFormat.parse("2016.03.08. 16:18:54")));
		
		assertEquals(1, developerFocusInformation.developersModifications.get("bob").size());
		assertFalse(developerFocusInformation.developersModifications.get("bob").keySet().contains(dateFormat.parse("2016.02.12. 12:43:15")));
		assertFalse(developerFocusInformation.developersModifications.get("bob").keySet().contains(dateFormat.parse("2016.03.08. 10:29:34")));
		assertFalse(developerFocusInformation.developersModifications.get("bob").keySet().contains(dateFormat.parse("2016.03.09. 17:22:08")));
		assertTrue(developerFocusInformation.developersModifications.get("bob").keySet().contains(dateFormat.parse("2016.03.08. 16:18:54")));
		assertEquals(2, developerFocusInformation.developersModifications.get("bob").get(dateFormat.parse("2016.03.08. 16:18:54")).size());
		assertEquals("/com/mycompany/myapp/Main.java", developerFocusInformation.developersModifications.get("bob").get(dateFormat.parse("2016.03.08. 16:18:54")).get(0));
		assertEquals("/com/mycompany/myapp/util/Calculations.java", developerFocusInformation.developersModifications.get("bob").get(dateFormat.parse("2016.03.08. 16:18:54")).get(1));
		
		assertEquals(dateFormat.parse("2016.02.12. 12:43:15"), developerFocusInformation.fileLastModifiedByDeveloper.get(new ContributorFile("steve", "/com/mycompany/myapp/game/Game.java")));
		assertEquals(dateFormat.parse("2016.02.12. 12:43:15"), developerFocusInformation.fileLastModifiedByDeveloper.get(new ContributorFile("steve", "/com/mycompany/myapp/util/Conversions.java")));
		assertEquals(dateFormat.parse("2016.03.08. 10:29:34"), developerFocusInformation.fileLastModifiedByDeveloper.get(new ContributorFile("steve", "/com/mycompany/myapp/Main.java")));
		assertEquals(dateFormat.parse("2016.03.08. 16:18:54"), developerFocusInformation.fileLastModifiedByDeveloper.get(new ContributorFile("bob", "/com/mycompany/myapp/Main.java")));
		assertEquals(dateFormat.parse("2016.03.08. 16:18:54"), developerFocusInformation.fileLastModifiedByDeveloper.get(new ContributorFile("bob", "/com/mycompany/myapp/util/Calculations.java")));
		assertEquals(dateFormat.parse("2016.03.09. 17:22:08"), developerFocusInformation.fileLastModifiedByDeveloper.get(new ContributorFile("steve", "/com/mycompany/myapp/util/Calculations.java")));
		assertEquals(dateFormat.parse("2016.03.09. 17:22:08"), developerFocusInformation.fileLastModifiedByDeveloper.get(new ContributorFile("steve", "/com/mycompany/myapp/util/Asserts.java")));
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
	public void testGetModifiedFilesPerDeveloper() throws ParseException {
		DeveloperFocusInformation developerFocusInformation = buildDeveloperFocusInformation();
		
		Set<String> modifiedFiles = developerFocusInformation.getModifiedFilesPerDeveloper("steve", "/com/mycompany/myapp/Main.java");
		
		assertEquals(4, modifiedFiles.size());
		assertTrue(modifiedFiles.contains("/com/mycompany/myapp/Main.java"));
		assertTrue(modifiedFiles.contains("/com/mycompany/myapp/game/Game.java"));
		assertTrue(modifiedFiles.contains("/com/mycompany/myapp/util/Conversions.java"));
		assertTrue(modifiedFiles.contains("/com/mycompany/myapp/util/Calculations.java"));
		assertFalse(modifiedFiles.contains("/com/mycompany/myapp/util/Asserts.java"));
		assertFalse(modifiedFiles.contains("/com/mycompany/myapp/util/NoSuchFile.java"));
	}
}
