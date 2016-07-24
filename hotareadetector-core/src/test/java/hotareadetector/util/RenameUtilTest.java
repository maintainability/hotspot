package hotareadetector.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RenameUtilTest {
	/**
	 * Checks the correctness of function determineRenamedValue.
	 * It tests the cases listed in the commend of the code + one corner case.
	 */
	@Test
	public void testDetermineRenamedValue() {
		assertEquals("/com/mycompany/myapp/main/Main.java", RenameUtil.determineRenamedValue("/com/mycompany/myapp/Main.java", "/com/mycompany/myapp/main/Main.java", "/com/mycompany/myapp/Main.java"));
		assertEquals("/com/mycompany/myapp/Util.java", RenameUtil.determineRenamedValue("/com/mycompany/myapp/Main.java", "/com/mycompany/myapp/main/Main.java", "/com/mycompany/myapp/Util.java"));
		assertEquals("/org/mycompany/myapp/Main.java", RenameUtil.determineRenamedValue("/com/mycompany", "/org/mycompany", "/com/mycompany/myapp/Main.java"));
		assertEquals("/com/mycompanymyapp/Main.java", RenameUtil.determineRenamedValue("/com/mycompany", "/org/mycompany", "/com/mycompanymyapp/Main.java"));
	}

}
