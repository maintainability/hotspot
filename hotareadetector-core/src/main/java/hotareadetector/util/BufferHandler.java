package hotareadetector.util;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Buffer handling function.
 */
public class BufferHandler {
	/**
	 * Reads data form buffered reader and returns it as an array of strings.
	 */
	public static List<String> readBufferIntoArray(BufferedReader buffer) throws IOException {
		List<String> result = new ArrayList<String>();
		String nextLine = null;
		while ((nextLine = buffer.readLine()) != null) {
			result.add(nextLine);
		}
		return result;
	}
	
	/**
	 * Reads data form buffered reader and saves it to file.
	 */
	public static void readBufferIntoFile(BufferedReader buffer, String fileName) throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(fileName);
		PrintWriter printWriter = new PrintWriter(fileOutputStream);
		String nextLine = null;
		while ((nextLine = buffer.readLine()) != null) {
			printWriter.println(nextLine);
		}
		printWriter.flush();
		printWriter.close();
	}
}
