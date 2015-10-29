package hotareadetector.logic;

import hotareadetector.data.CommitFileMatrix;
import hotareadetector.data.Credentials;
import hotareadetector.data.HotAreaDetectorContext;
import hotareadetector.data.HotNumber;
import hotareadetector.data.SourceControlInformation;
import hotareadetector.interfaces.SourceControlCommandExecutor;
import hotareadetector.interfaces.SourceControlCommandParser;
import hotareadetector.svn.SvnCommandExecutor;
import hotareadetector.svn.SvnCommandParser;

import java.io.IOException;
import java.util.List;

/**
 * Class for hot area command execution.
 */
public class HotAreaDetectorCommandExecutor {
	/**
	 * Executes hot area detection command, based on the context.
	 */
	public static List<HotNumber> executeHotAreaDetection(HotAreaDetectorContext context) throws IOException {
		SourceControlCommandExecutor executor = null;
		SourceControlCommandParser parser = null;
		
		SourceControlInformation sourceControl = new SourceControlInformation();
		sourceControl.setUrl(context.getSourceControlUrl());
		Credentials credentials = new Credentials(context.getSourceControlUserName(), context.getSourceControlPassword());
		executor = new SvnCommandExecutor();
		executor.setAccess(context, sourceControl, credentials);
		parser = new SvnCommandParser();
		
		SourceControlLogic sourceControlLogic = new SourceControlLogic();
		CommitFileMatrix commitFileMatrix = sourceControlLogic.readCommitData(
				executor, 
				parser, 
				context);
		if (commitFileMatrix == null) {
			return null;
		}
		int revision = commitFileMatrix.getLatestRevision();
		if (context.getRevision() != null) {
			revision = context.getRevision();
		}
		HotAreaCalculator hotAreaCalculator = new HotAreaCalculator(commitFileMatrix, revision);
		List<HotNumber> hotNumbers = hotAreaCalculator.calculateHotNumbers(context.isIgnoreChurn(), context.isIgnoreOwnership());
		return hotNumbers;
	}

}
