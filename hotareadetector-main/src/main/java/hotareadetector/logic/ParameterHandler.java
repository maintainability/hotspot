package hotareadetector.logic;

import hotareadetector.data.AnalysisType;
import hotareadetector.data.HotAreaDetectorContext;
import hotareadetector.exception.ParameterException;

/**
 * Class for handling program parameters.
 */
public class ParameterHandler {

	/**
	 * The purpose of this function is to parse parameters.
	 */
	public static HotAreaDetectorContext parseParameters(String[] args) throws ParameterException {
		HotAreaDetectorContext context = new HotAreaDetectorContext();
		if (args == null || args.length == 0) {
			throw new ParameterException("Parameter list must not be empty.");
		}
		for (String arg : args) {
			int equalSignIndex = arg.indexOf('=');
			if (equalSignIndex < 0) {
				throw new ParameterException("Parameter format must be arg=param. The following parameter doesn't comply this: " + arg);
			}
			String paramName = arg.substring(0, equalSignIndex);
			String paramValue = arg.substring(equalSignIndex + 1);
			switch (paramName) {
			case "-client":
				context.setSourceControlClientExecutor(paramValue);
				break;

			case "-url":
				context.setSourceControlUrl(paramValue);
				break;

			case "-userName":
				context.setSourceControlUserName(paramValue);
				break;

			case "-password":
				context.setSourceControlPassword(paramValue);
				break;

			case "-dirName":
				context.setDirName(paramValue);
				break;

			case "-outputFileName":
				context.setOutputFileName(paramValue);
				break;

			case "-metricsPrefix":
				context.setMetricsFileName(paramValue);
				break;
				
			case "-extensions":
				context.setExtensions(paramValue);
				break;

			case "-includePrefixes":
				context.setIncludePrefixes(paramValue);
				break;

			case "-excludePrefixes":
				context.setExcludePrefixes(paramValue);
				break;

			case "-revision":
				try {
					context.setRevision(Integer.parseInt(paramValue));
				} catch (NumberFormatException e) {
					throw new ParameterException("Format of parameter revison is invalid. Should be numeric, e.g. 12345, but was " + paramValue + ".");
				}
				break;

			case "-atRNecessary":
				if ("true".compareToIgnoreCase(paramValue) == 0) {
					context.setAtRNecessary(true);
				} else if ("false".compareToIgnoreCase(paramValue) == 0) {
					context.setAtRNecessary(false);
				} else {
					throw new ParameterException("Parameter 'atRNecessary' must be either true or false (case insensitive), but was: " + paramValue + ".");
				}
				break;

			case "-analysisType":
				try {
					context.setAnalysisType(AnalysisType.valueOf(paramValue));
				} catch (IllegalArgumentException iae) {
					throw new ParameterException("Wrong 'analysisType'; must be one of " + HotAreaDetectorContext.getFormattedAnalyisTypes() + ", but was: " + paramValue + ".");
				}
				break;

			default:
				throw new ParameterException("Invalid parameter: " + paramName);
			}
		}

		return context;
	}

	/**
	 * Displays usage of the program.
	 */
	public static void printUsage() {
		System.out.println("The parameter format must be param=value. Parameters:");
		System.out.println("    -client:          if the type is svn, then the executable svn client, else it must be omitted.");
		System.out.println("    -userName:        if the type is svn, then the user name, else it must be omitted.");
		System.out.println("    -password:        if the type is svn, then the password, else it must be omitted.");
		System.out.println("    -dirName:         name of the directory where the logs reside");
		System.out.println("    -outputFileName:  name of the result file. If set, then the result is written to that file. If not set, then the result is written on the console.");
		System.out.println("    -metricsPrefix:   indicates that the metrics should be saved as well, into files. Prefix of the result files, without extension. (Not written if not set.)");
		System.out.println("    -extensions:      extensions to consider, e.g. java,xml (use empty for folders)");
		System.out.println("    -includePrefixes: prefixes of the paths to consider only (e.g. /trunk/myproject)");
		System.out.println("    -excludePrefixes: prefixes to exclude from analysis (e.g. /trunk/myproject/test)");
		System.out.println("    -revision:        revision at which the analysis should be performed (e.g. 12345)");
		System.out.println("    -saveLogsOnly:    true or false; indicates if full analysis should be performed (true) or just save the logs (false).");
		System.out.println("    -atRNecessary:    true or false; indicates if the URL should be appended with @[revision] (true).");
		System.out.println("    -analysisType:    " + HotAreaDetectorContext.getFormattedAnalyisTypes() + "; indicates the type of the analysis (FULL).");
	}

}
