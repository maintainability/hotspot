package hotareadetector.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import hotareadetector.data.CommitDataExtended;
import hotareadetector.data.ContributorFile;
import hotareadetector.data.ContributorFocusStructure;
import hotareadetector.mock.CommitFileMapGenerator;


public class ContributorFocusUtilTest {
	@Test
	public void testCalculateFocusWeightedOwnership() {
		Map<String, List<CommitDataExtended>> fileCommitMap = CommitFileMapGenerator.createFileCommitMap();
		
		Map<String, Double> result = (new ContributorFocusUtil()).calculateFocusWeightedOwnership(fileCommitMap);
		
		assertEquals(5, result.size());
		assertEquals(new Double(20.0/3), result.get(CommitFileMapGenerator.file1));
		assertEquals(new Double(4.0), result.get(CommitFileMapGenerator.file2));
		assertEquals(new Double(4.0), result.get(CommitFileMapGenerator.file3));
		assertEquals(new Double(7.0), result.get(CommitFileMapGenerator.file4));
		assertEquals(new Double(5.0), result.get(CommitFileMapGenerator.file5));
	}
	
	/**
	 * Expected result is the following:
	 * 
	 * Map<String, TreeMap<Date, ArrayList<String>>> contributorsModifications
	 * Contains all the contributors with all their modifications (date and file name), like this:
	 * 
	 * steve ->
	 *   2016.02.12. 12:43:15 ->
	 *     /com/mycompany/myapp/Main.java
	 *     /com/mycompany/myapp/game/Game.java
	 *     /com/mycompany/myapp/util/Conversions.java
	 *   
	 *   2016.03.08. 10:29:34 ->
	 *     /com/mycompany/myapp/Main.java
	 *     /com/mycompany/myapp/util/Calculations.java
	 *   
	 *   
	 *   2016.03.09. 17:22:08 ->
	 *     /com/mycompany/myapp/util/Calculations.java
	 *     /com/mycompany/myapp/util/Asserts.java
	 * 
	 * bob ->
	 *   2016.03.08. 16:18:54 ->
	 *     /com/mycompany/myapp/Main.java
	 *     /com/mycompany/myapp/util/Calculations.java
	 *     
	 *  The internal map (date -> list of modified files) is sorted map.
	 *  
	 * 
	 *  
	 * Map<ContributorFile, Date> fileLastModifiedByContributor
	 * 
	 * Contains the last modification date by contributor and file, like this:
	 * 
	 * (steve, /com/mycompany/myapp/game/Game.java)         -> 2016.02.12. 12:43:15
	 * (steve, /com/mycompany/myapp/util/Conversions.java)  -> 2016.02.12. 12:43:15
	 * (steve, /com/mycompany/myapp/Main.java)              -> 2016.03.08. 10:29:34
	 * (bob,   /com/mycompany/myapp/Main.java)              -> 2016.03.08. 16:18:54
	 * (bob,   /com/mycompany/myapp/util/Calculations.java) -> 2016.03.08. 16:18:54
	 * (steve, /com/mycompany/myapp/util/Calculations.java) -> 2016.03.09. 17:22:08
	 * (steve, /com/mycompany/myapp/util/Asserts.java)      -> 2016.03.09. 17:22:08
	 */
	@Test
	public void testBuildContributorFocusStructure() {
		Map<String, List<CommitDataExtended>> fileCommitMap = CommitFileMapGenerator.createFileCommitMap();
		
		ContributorFocusStructure result = (new ContributorFocusUtil()).buildContributorFocusStructure(fileCommitMap);
		
		assertEquals(3, result.getContributorsModifications().get(CommitFileMapGenerator.contributor1).size());
		
		assertEquals(3, result.getContributorsModifications().get(CommitFileMapGenerator.contributor1).get(CommitFileMapGenerator.date1).size());
		assertEquals(CommitFileMapGenerator.file1, result.getContributorsModifications().get(CommitFileMapGenerator.contributor1).get(CommitFileMapGenerator.date1).get(0));
		assertEquals(CommitFileMapGenerator.file2, result.getContributorsModifications().get(CommitFileMapGenerator.contributor1).get(CommitFileMapGenerator.date1).get(1));
		assertEquals(CommitFileMapGenerator.file3, result.getContributorsModifications().get(CommitFileMapGenerator.contributor1).get(CommitFileMapGenerator.date1).get(2));
		
		assertEquals(2, result.getContributorsModifications().get(CommitFileMapGenerator.contributor1).get(CommitFileMapGenerator.date2).size());
		assertEquals(CommitFileMapGenerator.file1, result.getContributorsModifications().get(CommitFileMapGenerator.contributor1).get(CommitFileMapGenerator.date2).get(0));
		assertEquals(CommitFileMapGenerator.file4, result.getContributorsModifications().get(CommitFileMapGenerator.contributor1).get(CommitFileMapGenerator.date2).get(1));
		
		assertEquals(2, result.getContributorsModifications().get(CommitFileMapGenerator.contributor1).get(CommitFileMapGenerator.date4).size());
		assertEquals(CommitFileMapGenerator.file5, result.getContributorsModifications().get(CommitFileMapGenerator.contributor1).get(CommitFileMapGenerator.date4).get(0));
		assertEquals(CommitFileMapGenerator.file4, result.getContributorsModifications().get(CommitFileMapGenerator.contributor1).get(CommitFileMapGenerator.date4).get(1));
		
		assertEquals(1, result.getContributorsModifications().get(CommitFileMapGenerator.contributor2).size());
		
		assertEquals(2, result.getContributorsModifications().get(CommitFileMapGenerator.contributor2).get(CommitFileMapGenerator.date3).size());
		assertEquals(CommitFileMapGenerator.file1, result.getContributorsModifications().get(CommitFileMapGenerator.contributor2).get(CommitFileMapGenerator.date3).get(0));
		assertEquals(CommitFileMapGenerator.file4, result.getContributorsModifications().get(CommitFileMapGenerator.contributor2).get(CommitFileMapGenerator.date3).get(1));
		
		
		assertEquals(CommitFileMapGenerator.date1, result.getFileLastModifiedByContributor().get(new ContributorFile(CommitFileMapGenerator.contributor1, CommitFileMapGenerator.file2)));
		assertEquals(CommitFileMapGenerator.date1, result.getFileLastModifiedByContributor().get(new ContributorFile(CommitFileMapGenerator.contributor1, CommitFileMapGenerator.file3)));
		assertEquals(CommitFileMapGenerator.date2, result.getFileLastModifiedByContributor().get(new ContributorFile(CommitFileMapGenerator.contributor1, CommitFileMapGenerator.file1)));
		assertEquals(CommitFileMapGenerator.date3, result.getFileLastModifiedByContributor().get(new ContributorFile(CommitFileMapGenerator.contributor2, CommitFileMapGenerator.file1)));
		assertEquals(CommitFileMapGenerator.date3, result.getFileLastModifiedByContributor().get(new ContributorFile(CommitFileMapGenerator.contributor2, CommitFileMapGenerator.file4)));
		assertEquals(CommitFileMapGenerator.date4, result.getFileLastModifiedByContributor().get(new ContributorFile(CommitFileMapGenerator.contributor1, CommitFileMapGenerator.file4)));
		assertEquals(CommitFileMapGenerator.date4, result.getFileLastModifiedByContributor().get(new ContributorFile(CommitFileMapGenerator.contributor1, CommitFileMapGenerator.file5)));
	}
	
	/**
	 * Expected result:
	 * 
	 * 1. /com/mycompany/myapp/Main.java
	 * Modified by steve and bob
	 * 
	 * A. steve
	 * Last modified in commit: 2
	 * Files:
	 * - /com/mycompany/myapp/Main.java
	 * - /com/mycompany/myapp/game/Game.java
	 * - /com/mycompany/myapp/util/Conversions.java
	 * - /com/mycompany/myapp/util/Calculations.java
	 * Focus value: (4 / (4 over 2)) * (1 + 1 + 1 + 2 + 2 + 0) = (4 / 6) * 7 = 14/3
	 * 
	 * B. bob:
	 * Last modified in commit: 3
	 * - /com/mycompany/myapp/Main.java
	 * - /com/mycompany/myapp/util/Calculations.java
	 * Focus value: (2 / (2 over 2)) * (1) = (2 / 1) * 1 = 2
	 * 
	 * Resulting focus value: 14/3 + 2 = 20/3
	 * 
	 * 
	 * 2. /com/mycompany/myapp/game/Game.java
	 * Modified by steve
	 * 
	 * A. steve
	 * Last modified in commit: 1
	 * Files:
	 * - /com/mycompany/myapp/Main.java
	 * - /com/mycompany/myapp/game/Game.java
	 * - /com/mycompany/myapp/util/Conversions.java
	 * Focus value: (3 / (3 over 2)) * (1 + 1 + 2) = (3 / 3) * 4 = 4
	 * 
	 * Resulting focus value: 4
	 * 
	 * 
	 * 3. /com/mycompany/myapp/util/Conversions.java
	 * Modified by steve
	 * 
	 * A. steve
	 * Last modified in commit: 1
	 * Focus value: 4
	 * 
	 * Resulting focus value: 4
	 * 
	 * 
	 * 
	 * 4. /com/mycompany/myapp/util/Calculations.java
	 * Modified by steve and bob
	 * 
	 * A. steve
	 * Last modified in commit: 4
	 * Files:
	 * - /com/mycompany/myapp/Main.java
	 * - /com/mycompany/myapp/game/Game.java
	 * - /com/mycompany/myapp/util/Conversions.java
	 * - /com/mycompany/myapp/util/Calculations.java
	 * - /com/mycompany/myapp/util/Asserts.java
	 * Focus value: (5 / (5 over 2)) * (1 + 1 + 1 + 1 + 2 + 2 + 2 + 0 + 0 + 0) = (5 / 10) * 10 = 5
	 * 
	 * B. bob
	 * Last modified in commit: 3
	 * Focus value: 2
	 * 
	 * Resulting focus value: 7
	 * 
	 * 
	 * 5. /com/mycompany/myapp/util/Asserts.java
	 * Last modified in commit: 4
	 * Modified by steve
	 * Focus value: 5
	 * 
	 * Resulting focus value: 5
	 */
	@Test
	public void testCalculateFocusWeightedOwnershipOfFiles() {
		Map<String, List<CommitDataExtended>> fileCommitMap = CommitFileMapGenerator.createFileCommitMap();
		ContributorFocusStructure contributorFocusStructure = (new ContributorFocusUtil()).buildContributorFocusStructure(fileCommitMap);
		
		Map<String, Double> result = (new ContributorFocusUtil()).calculateFocusWeightedOwnershipOfFiles(contributorFocusStructure);
		
		assertEquals(5, result.size());
		assertEquals(new Double(20.0/3), result.get(CommitFileMapGenerator.file1));
		assertEquals(new Double(4.0), result.get(CommitFileMapGenerator.file2));
		assertEquals(new Double(4.0), result.get(CommitFileMapGenerator.file3));
		assertEquals(new Double(7.0), result.get(CommitFileMapGenerator.file4));
		assertEquals(new Double(5.0), result.get(CommitFileMapGenerator.file5));
	}
	
	
	@Test
	public void testCalculateFocusWeightedOwnershipOfFile() {
		Map<String, List<CommitDataExtended>> fileCommitMap = CommitFileMapGenerator.createFileCommitMap();
		ContributorFocusUtil contributorFocusUtil = new ContributorFocusUtil();
		ContributorFocusStructure contributorFocusStructure = contributorFocusUtil.buildContributorFocusStructure(fileCommitMap);
		
		assertEquals(new Double(20.0/3), (new ContributorFocusUtil()).calculateFocusWeightedOwnershipOfFile(contributorFocusStructure, CommitFileMapGenerator.file1));
	}
	
	@Test
	public void testDetermineModifiedFilesPerContributor() {
		Map<String, List<CommitDataExtended>> fileCommitMap = CommitFileMapGenerator.createFileCommitMap();
		ContributorFocusStructure contributorFocusStructure = (new ContributorFocusUtil()).buildContributorFocusStructure(fileCommitMap);
		
		Set<String> fileModifications1 = (new ContributorFocusUtil()).determineModifiedFilesPerContributor(contributorFocusStructure, CommitFileMapGenerator.contributor1, CommitFileMapGenerator.file1);
		
		assertEquals(4, fileModifications1.size());
		assertTrue(fileModifications1.contains(CommitFileMapGenerator.file1));
		assertTrue(fileModifications1.contains(CommitFileMapGenerator.file2));
		assertTrue(fileModifications1.contains(CommitFileMapGenerator.file3));
		assertTrue(fileModifications1.contains(CommitFileMapGenerator.file4));
		assertFalse(fileModifications1.contains(CommitFileMapGenerator.file5));
	}

	@Test
	public void testCalculateFocusValue() {
		ArrayList<String> listOfModifiedFiles = new ArrayList<String>();
		listOfModifiedFiles.add("org/apache/tools/ant/ProjectHelper.java");
		listOfModifiedFiles.add("org/apache/tools/ant/taskdefs/Target.java");
		listOfModifiedFiles.add("org/apache/tools/ant/taskdefs/UpToDate.java");
		listOfModifiedFiles.add("org/apache/tools/ant/types/mappers/FilterMapper.java");
		
		ContributorFocusUtil contributorFocusUtil = new ContributorFocusUtil();
		assertEquals(0.0, contributorFocusUtil.calculateFocusValue(null), 0.01);
		assertEquals(0.0, contributorFocusUtil.calculateFocusValue(new ArrayList<String>()), 0.01);
		assertEquals(0.0, contributorFocusUtil.calculateFocusValue(listOfModifiedFiles.subList(0, 1)), 0.01);
		assertEquals(20.0/3, contributorFocusUtil.calculateFocusValue(listOfModifiedFiles), 0.01);
		assertEquals(0.0, contributorFocusUtil.calculateFocusValue(listOfModifiedFiles.subList(1, 3)), 0.01);
	}
}
