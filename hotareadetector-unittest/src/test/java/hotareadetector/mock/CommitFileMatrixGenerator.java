package hotareadetector.mock;

import java.io.IOException;
import java.util.List;

import hotareadetector.data.CommitFileCell;
import hotareadetector.data.CommitFileMatrix;
import hotareadetector.data.HotAreaDetectorContext;
import hotareadetector.interfaces.SourceControlCommandExecutor;
import hotareadetector.interfaces.SourceControlCommandParser;
import hotareadetector.logic.SourceControlLogic;
import hotareadetector.svn.SvnCommandParser;

/**
 * Helper class for generating commit-file matrix.
 */
public class CommitFileMatrixGenerator {
	/**
	 * Generates a commit-file matrix. It contains 3 commits:
	 * 
	 * 1st commit: Main.java, Game.java and Test.java are added by mike.
	 * 2nd commit: Game.java is modified, Data.java is added and Test.java is deleted by sully.
	 * 3rd commit: Main.java is updated by mike.
	 */
	public static CommitFileMatrix generateCommitFileMatrixThreeRevisions() {
		List<CommitFileCell> firstCommit = CommitFileCellGenerator.generateFirstCommitThreeFileCells();
		List<CommitFileCell> secondCommit = CommitFileCellGenerator.generateSecondCommitThreeFileCells();
		List<CommitFileCell> thirdCommit = CommitFileCellGenerator.generateThirdCommitOneFileCell();
		CommitFileMatrix commitFileMatrix = new CommitFileMatrix(true);
		commitFileMatrix.addCommitedFileDataCollection(firstCommit);
		commitFileMatrix.addCommitedFileDataCollection(secondCommit);
		commitFileMatrix.addCommitedFileDataCollection(thirdCommit);
		return commitFileMatrix;
	}
	
	/**
	 * Create a commit file matrix about test data taken form real SVN executions.
	 */
	public static CommitFileMatrix generateCommitFileMatrixRealExecution(boolean deepAnalysis) throws IOException {
		SourceControlLogic sourceControlLogic = new SourceControlLogic();
		SourceControlCommandExecutor executor = new SourceControlCommandExecutorMock();		
		SourceControlCommandParser parser = new SvnCommandParser();
		HotAreaDetectorContext context = new HotAreaDetectorContext();
		context.setDeepAnalysis(deepAnalysis);
		sourceControlLogic.readCommitData(executor, parser, context);
		CommitFileMatrix commitFileMatrix = sourceControlLogic.readCommitData(executor, parser, context);
		return commitFileMatrix;
	}
	
	/**
	 * Commit file matrix for directory rename.
	 * 
	 * /dir
	 * /dir/file1
	 * /dir/file2
	 * /dir2
	 * /dir2/file1
	 */
	public static CommitFileMatrix generateCommitFileMatrixForDirectoryRename() {
		List<CommitFileCell> commitForRename = CommitFileCellGenerator.generateCommitForDirectoryRename();
		CommitFileMatrix commitFileMatrix = new CommitFileMatrix(true);
		commitFileMatrix.addCommitedFileDataCollection(commitForRename);
		return commitFileMatrix;
	}
	

}
