package hotareadetector.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FocusDistanceCalculatorTest {
	@Test
	public void testCalculateDistance() {
		FocusDistanceCalculator focusDistanceCalculator = new FocusDistanceCalculator();
		assertEquals(3, focusDistanceCalculator.calculateDistance("com/mycompany/mymodule/MyFile.java", "com/mycompany/myservice/connect/MyConnect.java"));
		assertEquals(0, focusDistanceCalculator.calculateDistance("com/mycompany/mymodule/MyFile.java", "com/mycompany/mymodule/MyOtherFile.java"));
		assertEquals(0, focusDistanceCalculator.calculateDistance("com/mycompany/mymodule/MyFile.java", "com/mycompany/mymodule/MyFile.java"));
		assertEquals(2, focusDistanceCalculator.calculateDistance("com/mycompany/mymodule/MyFile.java", "com/mycompany/myservice/MyService.java"));
		assertEquals(1, focusDistanceCalculator.calculateDistance("com/mycompany/mymodule/MyFile.java", "com/mycompany/Main.java"));
		assertEquals(3, focusDistanceCalculator.calculateDistance("com/mycompany/mymodule/MyFile.java", "com/mycompany/myservice/connect/MyConnect.java"));
	}
}
