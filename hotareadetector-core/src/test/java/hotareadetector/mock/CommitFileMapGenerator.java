package hotareadetector.mock;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import hotareadetector.data.CommitData;
import hotareadetector.data.CommitDataExtended;
import hotareadetector.util.DateUtil;

public class CommitFileMapGenerator {
	public static final Date date1 = DateUtil.convertToDate("2016-02-12 12:43:15");
	public static final Date date2 = DateUtil.convertToDate("2016-03-08 10:29:34");
	public static final Date date3 = DateUtil.convertToDate("2016-03-08 16:18:54");
	public static final Date date4 = DateUtil.convertToDate("2016-03-09 17:22:08");
	
	public static final String file1 = "/com/mycompany/myapp/Main.java";
	public static final String file2 = "/com/mycompany/myapp/game/Game.java";
	public static final String file3 = "/com/mycompany/myapp/util/Conversions.java";
	public static final String file4 = "/com/mycompany/myapp/util/Calculations.java";
	public static final String file5 = "/com/mycompany/myapp/util/Asserts.java";
	
	public static final String contributor1 = "steve";
	public static final String contributor2 = "bob";

	/**
	 * Creates the following structure (the last number is the churn value):
	 * 
	 * /com/mycompany/myapp/Main.java -> 
	 *   {1, steve, 2016.02.12. 12:43:15}, 1
	 *   {2, steve, 2016.03.08. 10:29:34}, 2
	 *   {3, bob, 2016.03.08. 16:18:54}, 3
	 *   
	 * /com/mycompany/myapp/game/Game.java ->
	 *   {1, steve, 2016.02.12. 12:43:15}, 4
	 *   
	 * /com/mycompany/myapp/util/Conversions.java ->
	 *   {1, steve, 2016.02.12. 12:43:15}, 5
	 * 
	 * /com/mycompany/myapp/util/Calculations.java ->
	 *   {2, steve, 2016.03.08. 10:29:34}, 6
	 *   {3, bob, 2016.03.08. 16:18:54}, 7
	 *   {4, steve, 2016.03.09. 17:22:08}, 8
	 * 
	 * /com/mycompany/myapp/util/Asserts.java ->
	 *   {4, steve, 2016.03.09. 17:22:08}, 9
	 */
	public static Map<String, List<CommitDataExtended>> createFileCommitMap(boolean setChurnValue) {
		Map<String, List<CommitDataExtended>> fileCommitMap = new TreeMap<String, List<CommitDataExtended>>();
		
		CommitData commit1 = new CommitData(1, contributor1, date1, "Commit 1", null);
		CommitData commit2 = new CommitData(2, contributor1, date2, "Commit 2", null);
		CommitData commit3 = new CommitData(3, contributor2, date3, "Commit 3", null);
		CommitData commit4 = new CommitData(4, contributor1, date4, "Commit 4", null);
		
		List<CommitDataExtended> commitDataExtendedListFile1 = new ArrayList<CommitDataExtended>();
		commitDataExtendedListFile1.add(new CommitDataExtended(commit1, setChurnValue ? 1 : null));
		commitDataExtendedListFile1.add(new CommitDataExtended(commit2, setChurnValue ? 2 : null));
		commitDataExtendedListFile1.add(new CommitDataExtended(commit3, setChurnValue ? 3 : null));

		List<CommitDataExtended> commitDataExtendedListFile2 = new ArrayList<CommitDataExtended>();
		commitDataExtendedListFile2.add(new CommitDataExtended(commit1, setChurnValue ? 4 : null));
		
		List<CommitDataExtended> commitDataExtendedListFile3 = new ArrayList<CommitDataExtended>();
		commitDataExtendedListFile3.add(new CommitDataExtended(commit1, setChurnValue ? 5 : null));
		
		List<CommitDataExtended> commitDataExtendedListFile4 = new ArrayList<CommitDataExtended>();
		commitDataExtendedListFile4.add(new CommitDataExtended(commit2, setChurnValue ? 6 : null));
		commitDataExtendedListFile4.add(new CommitDataExtended(commit3, setChurnValue ? 7 : null));
		commitDataExtendedListFile4.add(new CommitDataExtended(commit4, setChurnValue ? 8 : null));
		
		List<CommitDataExtended> commitDataExtendedListFile5 = new ArrayList<CommitDataExtended>();
		commitDataExtendedListFile5.add(new CommitDataExtended(commit4, setChurnValue ? 9 : null));
		
		fileCommitMap.put(file1, commitDataExtendedListFile1);
		fileCommitMap.put(file2, commitDataExtendedListFile2);
		fileCommitMap.put(file3, commitDataExtendedListFile3);
		fileCommitMap.put(file4, commitDataExtendedListFile4);
		fileCommitMap.put(file5, commitDataExtendedListFile5);
		
		return fileCommitMap;
	}
	
	public static Map<String, List<CommitDataExtended>> createFileCommitMap() {
		return createFileCommitMap(true);
	}
	
}
