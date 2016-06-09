package hotareadetector.util;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import org.junit.Test;

public class DeveloperFocusUtilTest {
	
	@Test
	public void testCalculateDistance() {
		assertEquals(3, DeveloperFocusUtil.calculateDistance("com/mycompany/mymodule/MyFile.java", "com/mycompany/myservice/connect/MyConnect.java"));
		assertEquals(0, DeveloperFocusUtil.calculateDistance("com/mycompany/mymodule/MyFile.java", "com/mycompany/mymodule/MyOtherFile.java"));
		assertEquals(0, DeveloperFocusUtil.calculateDistance("com/mycompany/mymodule/MyFile.java", "com/mycompany/mymodule/MyFile.java"));
		assertEquals(2, DeveloperFocusUtil.calculateDistance("com/mycompany/mymodule/MyFile.java", "com/mycompany/myservice/MyService.java"));
		assertEquals(1, DeveloperFocusUtil.calculateDistance("com/mycompany/mymodule/MyFile.java", "com/mycompany/Main.java"));
		assertEquals(3, DeveloperFocusUtil.calculateDistance("com/mycompany/mymodule/MyFile.java", "com/mycompany/myservice/connect/MyConnect.java"));
	}

	@Test
	public void testCalculateFocusInformation() {
		ArrayList<String> listOfModifiedFiles = new ArrayList<String>();
		listOfModifiedFiles.add("org/apache/tools/ant/ProjectHelper.java");
		listOfModifiedFiles.add("org/apache/tools/ant/taskdefs/Target.java");
		listOfModifiedFiles.add("org/apache/tools/ant/taskdefs/UpToDate.java");
		listOfModifiedFiles.add("org/apache/tools/ant/types/mappers/FilterMapper.java");
		assertEquals(0.0, DeveloperFocusUtil.calculateFocusValue(null), 0.01);
		assertEquals(0.0, DeveloperFocusUtil.calculateFocusValue(new ArrayList<String>()), 0.01);
		assertEquals(0.0, DeveloperFocusUtil.calculateFocusValue(listOfModifiedFiles.subList(0, 1)), 0.01);
		assertEquals(20.0/3, DeveloperFocusUtil.calculateFocusValue(listOfModifiedFiles), 0.01);
		assertEquals(0.0, DeveloperFocusUtil.calculateFocusValue(listOfModifiedFiles.subList(1, 3)), 0.01);
	}
}
