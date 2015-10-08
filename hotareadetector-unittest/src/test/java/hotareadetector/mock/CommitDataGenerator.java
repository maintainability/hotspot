package hotareadetector.mock;

import hotareadetector.data.CommitData;
import hotareadetector.data.CommitedFileData;
import hotareadetector.data.OperationType;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates commit data.
 */
public class CommitDataGenerator {
	
	/**
	 * Creates the following series of commits:
	 * 
	 * The 1st commit contains data on both trunk and branches and tags, in all cases productive, unit test and black box test (add).
	 * The 2nd commit contains productive code only on trunk (modify).
	 * The 3rd commit contains test code only on trunk (modify).
	 * The 4th commit contains productive code only on a branch (modify).
	 * The 5th commit contains test code only on a branch (modify).
	 * The 6th commit contains productive code only on a tag (modify).
	 * The 7th commit contains test code only on a tag (modify).
	 * The 8th commit contains bbt code only on trunk (modify).
	 * The 9th commit contains bbt code only on a branch (modify).
	 * The 10th commit contains bbt code only on a tag (modify).
	 */
	public static List<CommitData> generateCommitDataList() {
		CommitedFileData commitedFile11 = new CommitedFileData();
		commitedFile11.setFileName("/trunk/myproject/src/mypackage/MyFile.xml");
		commitedFile11.setOperationType(OperationType.A);
		CommitedFileData commitedFile12 = new CommitedFileData();
		commitedFile12.setFileName("/trunk/myproject/test/mypackage/MyFileTest.java");
		commitedFile12.setOperationType(OperationType.A);
		CommitedFileData commitedFile13 = new CommitedFileData();
		commitedFile13.setFileName("/branches/mybranch/myproject/src/mypackage/MyFile.xml");
		commitedFile13.setOperationType(OperationType.A);
		CommitedFileData commitedFile14 = new CommitedFileData();
		commitedFile14.setFileName("/branches/mybranch/myproject/test/mypackage/MyFileTest.java");
		commitedFile14.setOperationType(OperationType.A);
		CommitedFileData commitedFile15 = new CommitedFileData();
		commitedFile15.setFileName("/tags/mytag/myproject/src/mypackage/MyFile.xml");
		commitedFile15.setOperationType(OperationType.A);
		CommitedFileData commitedFile16 = new CommitedFileData();
		commitedFile16.setFileName("/tags/mytag/myproject/test/mypackage/MyFileTest.java");
		commitedFile16.setOperationType(OperationType.A);
		CommitedFileData commitedFile17 = new CommitedFileData();
		commitedFile17.setFileName("/trunk/myproject/bbt/BbtFramework.java");
		commitedFile17.setOperationType(OperationType.A);
		CommitedFileData commitedFile18 = new CommitedFileData();
		commitedFile18.setFileName("/branches/mybranch/myproject/bbt/BbtFramework.java");
		commitedFile18.setOperationType(OperationType.A);
		CommitedFileData commitedFile19 = new CommitedFileData();
		commitedFile19.setFileName("/tags/mytag/myproject/bbt/BbtFramework.java");
		commitedFile19.setOperationType(OperationType.A);
		CommitData commitData1 = new CommitData();
		commitData1.setRevisionNumber(1);
		commitData1.addCommitedFile(commitedFile11);
		commitData1.addCommitedFile(commitedFile12);
		commitData1.addCommitedFile(commitedFile13);
		commitData1.addCommitedFile(commitedFile14);
		commitData1.addCommitedFile(commitedFile15);
		commitData1.addCommitedFile(commitedFile16);
		commitData1.addCommitedFile(commitedFile17);
		commitData1.addCommitedFile(commitedFile18);
		commitData1.addCommitedFile(commitedFile19);
		
		CommitedFileData commitedFile21 = new CommitedFileData();
		commitedFile21.setFileName("/trunk/myproject/src/mypackage/MyFile.xml");
		commitedFile21.setOperationType(OperationType.M);
		CommitData commitData2 = new CommitData();
		commitData2.setRevisionNumber(2);
		commitData2.addCommitedFile(commitedFile21);
		
		CommitedFileData commitedFile31 = new CommitedFileData();
		commitedFile31.setFileName("/trunk/myproject/test/mypackage/MyFileTest.java");
		commitedFile31.setOperationType(OperationType.M);
		CommitData commitData3 = new CommitData();
		commitData3.setRevisionNumber(3);
		commitData3.addCommitedFile(commitedFile31);
		
		CommitedFileData commitedFile41 = new CommitedFileData();
		commitedFile41.setFileName("/branches/mybranch/myproject/src/mypackage/MyFile.xml");
		commitedFile41.setOperationType(OperationType.M);
		CommitData commitData4 = new CommitData();
		commitData4.setRevisionNumber(4);
		commitData4.addCommitedFile(commitedFile41);
		
		CommitedFileData commitedFile51 = new CommitedFileData();
		commitedFile51.setFileName("/branches/mybranch/myproject/test/mypackage/MyFileTest.java");
		commitedFile51.setOperationType(OperationType.M);
		CommitData commitData5 = new CommitData();
		commitData5.setRevisionNumber(5);
		commitData5.addCommitedFile(commitedFile51);
				
		CommitedFileData commitedFile61 = new CommitedFileData();
		commitedFile61.setFileName("/tags/mytag/myproject/src/mypackage/MyFile.xml");
		commitedFile61.setOperationType(OperationType.M);
		CommitData commitData6 = new CommitData();
		commitData6.setRevisionNumber(6);
		commitData6.addCommitedFile(commitedFile61);
		
		CommitedFileData commitedFile71 = new CommitedFileData();
		commitedFile71.setFileName("/tags/mytag/myproject/test/mypackage/MyFile.xml");
		commitedFile71.setOperationType(OperationType.M);
		CommitData commitData7 = new CommitData();
		commitData7.setRevisionNumber(7);
		commitData7.addCommitedFile(commitedFile71);
				
		CommitedFileData commitedFile81 = new CommitedFileData();
		commitedFile81.setFileName("/trunk/myproject/bbt/BbtFramework.java");
		commitedFile81.setOperationType(OperationType.M);
		CommitData commitData8 = new CommitData();
		commitData8.setRevisionNumber(8);
		commitData8.addCommitedFile(commitedFile81);
				
		CommitedFileData commitedFile91 = new CommitedFileData();
		commitedFile91.setFileName("/branches/mybranch/myproject/bbt/BbtFramework.java");
		commitedFile91.setOperationType(OperationType.M);
		CommitData commitData9 = new CommitData();
		commitData9.setRevisionNumber(9);
		commitData9.addCommitedFile(commitedFile91);
				
		CommitedFileData commitedFile101 = new CommitedFileData();
		commitedFile101.setFileName("/tags/mytag/myproject/bbt/BbtFramework.java");
		commitedFile101.setOperationType(OperationType.M);
		CommitData commitData10 = new CommitData();
		commitData10.setRevisionNumber(10);
		commitData10.addCommitedFile(commitedFile101);
				
		List<CommitData> result = new ArrayList<CommitData>();
		result.add(commitData1);
		result.add(commitData2);
		result.add(commitData3);
		result.add(commitData4);
		result.add(commitData5);
		result.add(commitData6);
		result.add(commitData7);
		result.add(commitData8);
		result.add(commitData9);
		result.add(commitData10);
		
		return result;
	}
}
