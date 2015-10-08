package hotareadetector;

import java.io.IOException;
import java.util.List;

import hotareadetector.data.HotAreaDetectorContext;
import hotareadetector.data.HotNumber;
import hotareadetector.exception.ParameterException;
import hotareadetector.logic.HotAreaDetectorCommandExecutor;
import hotareadetector.logic.ParameterHandler;
import hotareadetector.result.HotAreaResultHandler;

/**
 * Main entry of the hot area detector.
 */
public class HotAreaDetector {
	/**
	 * Main entry of the program.
	 */
	public static void main(String[] args) {
		System.out.println("Hot area detector started");
		try {
			HotAreaDetectorContext context = ParameterHandler.parseParameters(args);
			List<HotNumber> hotNumbers = HotAreaDetectorCommandExecutor.executeHotAreaDetection(context);
			if (hotNumbers != null) {
				HotAreaResultHandler.displayHotNumbers(hotNumbers);
				if (context.getOutputFileName() != null) {
					String dirFileName = context.getDirName() + context.getOutputFileName();
					HotAreaResultHandler.writeHotNumbersIntoFile(hotNumbers, dirFileName);
					System.out.println("Result has been written to file " + dirFileName);
				}
			}
		} catch (ParameterException e) {
			System.out.println("Parameter error occurred: " + e.getMessage());
			ParameterHandler.printUsage();
		} catch (IOException e) {
			System.out.println("IO Error occurred" + e);
		} 
		System.out.println("Hot area detector ended");
	}

}
