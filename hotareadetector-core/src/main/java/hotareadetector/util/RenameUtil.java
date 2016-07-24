package hotareadetector.util;

/**
 * Utility for rename handling.
 *
 */
public class RenameUtil {
	/**
	 * Determines the new value of the rename. Examples:
	 * 
	 * 1.
	 * From:      /com/mycompany/myapp/Main.java
	 * To:        /com/mycompany/myapp/main/Main.java
	 * File name: /com/mycompany/myapp/Main.java
	 * Result:    /com/mycompany/myapp/main/Main.java
	 * 
	 * 2. 
	 * From:      /com/mycompany
	 * To:        /org/mycompany
	 * File name: /com/mycompany/myapp/Main.java
	 * Result:    /org/mycompany/myapp/Main.java
	 * 
	 * 3.
	 * From:      /com/mycompany/myapp/Main.java
	 * To:        /com/mycompany/myapp/main/Main.java
	 * File name: /com/mycompany/myapp/Util.java
	 * Result:    /com/mycompany/myapp/Util.java
	 */
	public static String determineRenamedValue(String from, String to, String fileName) {
		if (fileName.equals(from)) {
			return to;
		} if (fileName.startsWith(from + "/")) {
			return to + fileName.substring(from.length());
		}
		return fileName;
	}
}
