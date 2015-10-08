package hotareadetector.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Data necessary for command execution.
 */
public class HotAreaDetectorContext {
	private String sourceControlClientExecutor = null;
	private String sourceControlUrl = null;
	private String sourceControlUserName = null;
	private String sourceControlPassword = null;
	private String dirName = "";
	private String outputFileName = null;
	private boolean deepAnalysis = false;
	private List<String> extensions = null;
	private List<String> includePrefixes = null;
	private List<String> excludePrefixes = null;
	private Integer revision = null;
	private boolean saveLogsOnly = false;
	private boolean atRNecessary = true;

	public String getSourceControlClientExecutor() {
		return sourceControlClientExecutor;
	}
	
	public void setSourceControlClientExecutor(String sourceControlClientExecutor) {
		this.sourceControlClientExecutor = sourceControlClientExecutor;
	}
	
	public String getSourceControlUrl() {
		return sourceControlUrl;
	}
	
	public void setSourceControlUrl(String sourceControlUrl) {
		this.sourceControlUrl = sourceControlUrl;
	}
	
	public String getSourceControlUserName() {
		return sourceControlUserName;
	}
	
	public void setSourceControlUserName(String sourceControlUserName) {
		this.sourceControlUserName = sourceControlUserName;
	}
	
	public String getSourceControlPassword() {
		return sourceControlPassword;
	}
	
	public void setSourceControlPassword(String sourceControlPassword) {
		this.sourceControlPassword = sourceControlPassword;
	}
	
	public String getDirName() {
		return dirName;
	}
	
	public void setDirName(String dirName) {
		this.dirName = dirName;
		if (!this.dirName.isEmpty() && !this.dirName.endsWith("!")) {
			this.dirName = this.dirName + "/";
		}
		File directory = new File(dirName);
		if (!directory.exists()) {
			directory.mkdir();
		}
	}

	public String getOutputFileName() {
		return outputFileName;
	}

	public void setOutputFileName(String outputFileName) {
		this.outputFileName = outputFileName;
	}

	public boolean isDeepAnalysis() {
		return deepAnalysis;
	}

	public void setDeepAnalysis(boolean deepAnalysis) {
		this.deepAnalysis = deepAnalysis;
	}

	public List<String> getExtensions() {
		return extensions;
	}

	public void setExtensions(String extensionsStr) {
		String[] extensionsArray = extensionsStr.split(",");
		extensions = new ArrayList<String>();
		for (String extensionElement : extensionsArray) {
			extensions.add(extensionElement);
		}
	}

	public List<String> getIncludePrefixes() {
		return includePrefixes;
	}

	public void setIncludePrefixes(String includePrefixesStr) {
		String[] includePrefixesArray = includePrefixesStr.split(",");
		includePrefixes = new ArrayList<String>();
		for (String includePrefixesElement : includePrefixesArray) {
			includePrefixes.add(includePrefixesElement);
		}
		
	}

	public List<String> getExcludePrefixes() {
		return excludePrefixes;
	}

	public void setExcludePrefixes(String excludePrefixesStr) {
		String[] excludePrefixesArray = excludePrefixesStr.split(",");
		excludePrefixes = new ArrayList<String>();
		for (String excludePrefixesElement : excludePrefixesArray) {
			excludePrefixes.add(excludePrefixesElement);
		}
	}

	public Integer getRevision() {
		return revision;
	}

	public void setRevision(Integer revision) {
		this.revision = revision;
	}

	public boolean isSaveLogsOnly() {
		return saveLogsOnly;
	}

	public void setSaveLogsOnly(boolean saveLogsOnly) {
		this.saveLogsOnly = saveLogsOnly;
	}

	public boolean isAtRNecessary() {
		return atRNecessary;
	}

	public void setAtRNecessary(boolean atRNecessary) {
		this.atRNecessary = atRNecessary;
	}

}
