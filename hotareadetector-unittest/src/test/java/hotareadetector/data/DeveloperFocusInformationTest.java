package hotareadetector.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

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
		assertEquals(1, developerFocusInformation.developersModifications.get("steve").get(dateFormat.parse("2016.03.09. 17:22:08")).size());
		assertEquals("/com/mycompany/myapp/util/Calculations.java", developerFocusInformation.developersModifications.get("steve").get(dateFormat.parse("2016.03.09. 17:22:08")).get(0));
		
		assertFalse(developerFocusInformation.developersModifications.get("steve").keySet().contains(dateFormat.parse("2016.03.08. 16:18:54")));
		
		assertEquals(1, developerFocusInformation.developersModifications.get("bob").size());
		assertFalse(developerFocusInformation.developersModifications.get("bob").keySet().contains(dateFormat.parse("2016.02.12. 12:43:15")));
		assertFalse(developerFocusInformation.developersModifications.get("bob").keySet().contains(dateFormat.parse("2016.03.08. 10:29:34")));
		assertFalse(developerFocusInformation.developersModifications.get("bob").keySet().contains(dateFormat.parse("2016.03.09. 17:22:08")));
		assertTrue(developerFocusInformation.developersModifications.get("bob").keySet().contains(dateFormat.parse("2016.03.08. 16:18:54")));
		assertEquals(2, developerFocusInformation.developersModifications.get("bob").get(dateFormat.parse("2016.03.08. 16:18:54")).size());
		assertEquals("/com/mycompany/myapp/Main.java", developerFocusInformation.developersModifications.get("bob").get(dateFormat.parse("2016.03.08. 16:18:54")).get(0));
		assertEquals("/com/mycompany/myapp/util/Calculations.java", developerFocusInformation.developersModifications.get("bob").get(dateFormat.parse("2016.03.08. 16:18:54")).get(1));
	}
	
	@Test
	public void testGetModifiedFilesPerDeveloperAfter() throws ParseException {
		DeveloperFocusInformation developerFocusInformation = buildDeveloperFocusInformation();
		List<String> modifiedFiles = developerFocusInformation.getModifiedFilesPerDeveloperAfter("steve", dateFormat.parse("2016.03.01. 00:00:00"));
		
		assertEquals(3, modifiedFiles.size());
		assertEquals("/com/mycompany/myapp/Main.java", modifiedFiles.get(0));
		assertEquals("/com/mycompany/myapp/util/Calculations.java", modifiedFiles.get(1));
		assertEquals("/com/mycompany/myapp/util/Calculations.java", modifiedFiles.get(2));
	}

	@Test
	public void testGetModifiedFilesPerDeveloperAfterEmpty() throws ParseException {
		DeveloperFocusInformation developerFocusInformation = buildDeveloperFocusInformation();
		List<String> modifiedFiles = developerFocusInformation.getModifiedFilesPerDeveloperAfter("steve", dateFormat.parse("2016.04.01. 00:00:00"));
		
		assertTrue(modifiedFiles.isEmpty());
	}

	@Test
	public void testGetModifiedFilesPerDeveloperAfterAll() throws ParseException {
		DeveloperFocusInformation developerFocusInformation = buildDeveloperFocusInformation();
		List<String> modifiedFiles = developerFocusInformation.getModifiedFilesPerDeveloperAfter("steve", dateFormat.parse("1970.01.01. 00:00:00"));
		
		assertEquals(6, modifiedFiles.size());
		assertEquals("/com/mycompany/myapp/Main.java", modifiedFiles.get(0));
		assertEquals("/com/mycompany/myapp/game/Game.java", modifiedFiles.get(1));
		assertEquals("/com/mycompany/myapp/util/Conversions.java", modifiedFiles.get(2));
		assertEquals("/com/mycompany/myapp/Main.java", modifiedFiles.get(3));
		assertEquals("/com/mycompany/myapp/util/Calculations.java", modifiedFiles.get(4));
		assertEquals("/com/mycompany/myapp/util/Calculations.java", modifiedFiles.get(5));
	}

}
