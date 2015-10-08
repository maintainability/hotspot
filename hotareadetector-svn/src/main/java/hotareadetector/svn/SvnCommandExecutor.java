package hotareadetector.svn;

import hotareadetector.data.Credentials;
import hotareadetector.data.HotAreaDetectorContext;
import hotareadetector.data.SourceControlInformation;
import hotareadetector.data.SourceControlResultData;
import hotareadetector.interfaces.SourceControlCommandExecutor;
import hotareadetector.util.BufferHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * SVN implementation of the source control commands.
 */
public class SvnCommandExecutor implements SourceControlCommandExecutor {
	private String svnExecutable = null;
	private String url = null;
	private Credentials credentials = null;
	private String dirName = "";
	private boolean atRNecessary = true;
	
	@Override
	public void setAccess(HotAreaDetectorContext context, SourceControlInformation sourceControl, Credentials credentials) {
		this.svnExecutable = context.getSourceControlClientExecutor();
		this.url = sourceControl.getUrl();
		this.credentials = credentials;
		this.dirName = context.getDirName();
		this.atRNecessary = context.isAtRNecessary();
	}

	@Override
	public SourceControlResultData executeSourceControlLogCommand() throws IOException {
		return executeSvnCommand("log -v --non-interactive --no-auth-cache", dirName + "svnlog.txt", "");
	}

	@Override
	public SourceControlResultData executeSourceControlDiffCommand(String revision1, String revision2) throws IOException {
		return executeSvnCommand("diff -r " + revision1 + ":" + revision2 + " --ignore-properties", dirName + "svndiff_" + revision1 + "_" + revision2 + ".txt", atRNecessary ? "@" + revision1 : "");
	}
	
	private SourceControlResultData executeSvnCommand(String command, String fileNameToSave, String urlPostfix) throws IOException {
		String svnUsername = credentials.getUsername() == null ? "" : "--username " + credentials.getUsername();
		String svnPassword = credentials.getPassword() == null ? "" : "--password " + credentials.getPassword();
		String commandToExecute = svnExecutable + " " + command + " " + svnUsername + " " + svnPassword + " " + url + urlPostfix;
		
		int maxTries = 10;
		for (int i = 1; i <= maxTries; i++) {
			if (i >= 2) {
				System.out.println("Error occurred during execution. Retrying after a short while. Execution #" + i + "/" + maxTries);
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Trying again.");
			}
			SourceControlResultData svnResult = executeCommandOnce(commandToExecute, fileNameToSave);
			if (svnResult != null) {
				return svnResult;
			}
		}
		System.out.println("Permanent error. Giving up.");
		return null;
	}
	
	private SourceControlResultData executeCommandOnce(String commandToExecute, String fileNameToSave) throws IOException {
		if (new File(fileNameToSave).exists()) {
			return new SourceControlResultData(commandToExecute, 0, fileNameToSave, new ArrayList<String>());
		}
		Process process = Runtime.getRuntime().exec(commandToExecute);
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
		BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		String tempFileName = dirName + "temp.txt";
		File tempFile = new File(tempFileName);
		tempFile.delete();
		BufferHandler.readBufferIntoFile(stdInput, tempFileName);
		List<String> errorStrList = BufferHandler.readBufferIntoArray(stdError);
		int exitCode = -1;
		try {
			exitCode = process.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		SourceControlResultData result = new SourceControlResultData(commandToExecute, exitCode, fileNameToSave, errorStrList);
		if (exitCode != 0) {
			System.out.println("Command ended with error.");
			System.out.println("Exit code: " + exitCode);
			System.out.println("Executed command: " + commandToExecute);
			System.out.println("Error message: ");
			for (String stdErrorLine : errorStrList) {
				System.out.println(stdErrorLine);
			}
			return null;
		}
		File destFile = new File(fileNameToSave);
		if (tempFile.renameTo(destFile)) {
			return result;
		}
		return null;
	}

}
