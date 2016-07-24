package hotareadetector.util;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class HotspotStringUtilTest {
	@Test
	public void testCreateSortedListFromSet() {
		Set<String> stringSet = new HashSet<String>();
		stringSet.add("peach");
		stringSet.add("apple");
		stringSet.add("banana");
		stringSet.add("grape");
		
		List<String> stringList = HotspotStringUtil.createSortedListFromSet(stringSet);
		
		assertEquals(4, stringList.size());
		assertEquals("apple", stringList.get(0));
		assertEquals("banana", stringList.get(1));
		assertEquals("grape", stringList.get(2));
		assertEquals("peach", stringList.get(3));
	}
}
