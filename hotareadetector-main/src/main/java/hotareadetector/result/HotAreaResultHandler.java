package hotareadetector.result;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Handling the results of hot area calculation.
 */
public class HotAreaResultHandler {
	
	/**
	 * Displays the result of hot area detection.
	 */
	public static void writeHotNumbersIntoFile(Map<String, Map<String, Double>> hotNumbers, String filePath)  throws FileNotFoundException, UnsupportedEncodingException {
		String result = formatResult(hotNumbers);
		System.out.print(result);
		PrintWriter writer = new PrintWriter(filePath, "UTF-8");
		writer.print(result);
		writer.close();
	}
	
	protected static String formatResult(Map<String, Map<String, Double>> hotNumbers) {
		NumberFormat formatter = new DecimalFormat("#0.00000"); 
		System.out.println("Hot numbers:\n");
		Set<String> hotNumberTypesSet = hotNumbers.keySet();
		List<String> hotNumberTypesList = new ArrayList<String>();
		Set<String> fileNameSet = null;
		List<String> fileNameList = new ArrayList<String>();
		hotNumberTypesList.addAll(hotNumberTypesSet);
		Collections.sort(hotNumberTypesList);
		StringBuffer result = new StringBuffer();
		result.append("Filename");
		for (String hotNumberType : hotNumberTypesList) {
			result.append(";" + hotNumberType);
			if (fileNameSet == null) {
				fileNameSet = hotNumbers.get(hotNumberType).keySet();
			}
		}
		result.append("\n");
		fileNameList.addAll(fileNameSet);
		Collections.sort(fileNameList);
		
		for (String fileName : fileNameList) {
			result.append(fileName);
			for (String hotNumberType : hotNumberTypesList) {
				Double hotNumber = hotNumbers.get(hotNumberType).get(fileName);
				result.append(";" + formatter.format(hotNumber));
			}
			result.append("\n");
		}
		return result.toString();
	}

}
