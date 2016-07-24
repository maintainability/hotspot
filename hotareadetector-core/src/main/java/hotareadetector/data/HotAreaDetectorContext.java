package hotareadetector.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import static hotareadetector.data.AnalysisType.*;

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
	private List<String> extensions = null;
	private List<String> includePrefixes = null;
	private List<String> excludePrefixes = null;
	private Integer revision = null;
	private boolean atRNecessary = true;
	private AnalysisType analysisType = FULL;
	private String prefix = null;

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

	public boolean isAtRNecessary() {
		return atRNecessary;
	}

	public void setAtRNecessary(boolean atRNecessary) {
		this.atRNecessary = atRNecessary;
	}

	public AnalysisType getAnalysisType() {
		return analysisType;
	}

	public void setAnalysisType(AnalysisType analysisType) {
		this.analysisType = analysisType;
	}
	
	private static String formattedAnalyisTypes = null;
	
	public static String getFormattedAnalyisTypes() {
		if (formattedAnalyisTypes == null) {
			String analysisTypeValues = "";
			for (AnalysisType analysisType : AnalysisType.values()) {
				analysisTypeValues += analysisType + ", ";
			}
			formattedAnalyisTypes = analysisTypeValues.substring(0, analysisTypeValues.length() - 2);
		}
		return formattedAnalyisTypes;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

}
