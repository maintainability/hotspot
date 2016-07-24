package hotareadetector;

import java.io.IOException;
import java.util.Map;

import hotareadetector.data.HotAreaDetectorContext;
import hotareadetector.exception.ParameterException;
import hotareadetector.logic.HotAreaDetectorCommandExecutor;
import hotareadetector.logic.ParameterHandler;
import hotareadetector.result.HotAreaResultHandler;

public class HotAreaDetector {
	public static void main(String[] args) {
		System.out.println("Hot area detector started");
		try {
			HotAreaDetectorContext context = ParameterHandler.parseParameters(args);
			Map<String, Map<String, Double>> hotNumbers = (new HotAreaDetectorCommandExecutor()).executeHotAreaDetection(context);
			String outputPath = context.getDirName() + "hotspotResults.csv";
			if (context.getOutputFileName() != null) {
				outputPath = context.getDirName() + context.getOutputFileName();
			}
			HotAreaResultHandler.writeHotNumbersIntoFile(hotNumbers, outputPath);
			System.out.println("Hot area detector ended successfully.");
		} catch (ParameterException e) {
			System.out.println("Parameter error occurred: " + e.getMessage());
			ParameterHandler.printUsage();
		} catch (IOException e) {
			System.out.println("IO Error occurred" + e);
		} 
		System.out.println("Hot area detector ended");
	}

}
