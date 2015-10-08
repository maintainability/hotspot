package hotareadetector.util;

import java.util.List;

/**
 * String related utility.
*/
public class StringUtil {
	
	/**
	 * Checks if the extension of the file name is contained by the set of extensions.
	 * If the extensions is null, then it contains per definition.
	 */
	public static boolean containsExtension(String fileName, List<String> extensions) {
		if (extensions == null) {
			return true;
		}
		int lastDot = fileName.lastIndexOf(".");
		if (lastDot >= 0 && fileName.length() > lastDot + 1) {
			String extension = fileName.substring(lastDot + 1);
			for (String actualExtension : extensions) {
				if (extension.equals(actualExtension)) {
					return true;
				}
			}
		} else {
			for (String actualExtension : extensions) {
				if ("".equals(actualExtension)) {
					return true;
				}
			}
		}
		return false;
	}
}
