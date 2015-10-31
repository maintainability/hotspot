package hotareadetector.result;

import hotareadetector.data.HotNumber;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

/**
 * Handling the results of hot area calculation.
 */
public class HotAreaResultHandler {
	
	/**
	 * Displays the result of hot area detection.
	 */
	public static void displayHotNumbers(List<HotNumber> hotNumbers) {
		NumberFormat formatter = new DecimalFormat("#0.00000"); 
		System.out.println("Hot numbers:");
		for (HotNumber hotNumber : hotNumbers) {
			System.out.println(hotNumber.getFileName() + ": " + formatter.format(hotNumber.getHotNumber()));
		}
	}

	/**
	 * Writes the result of hot area detection into file.
	 */
	public static void writeHotNumbersIntoFile(List<HotNumber> hotNumbers, String fileName) throws FileNotFoundException, UnsupportedEncodingException {
		NumberFormat formatter = new DecimalFormat("#0.00000"); 
		PrintWriter writer = new PrintWriter(fileName, "UTF-8");
		writer.println("Name;Hotnr");
		for (HotNumber hotNumber : hotNumbers) {
			writer.println(hotNumber.getFileName() + ";" + formatter.format(hotNumber.getHotNumber()));
		}
		writer.close();
	}

}
