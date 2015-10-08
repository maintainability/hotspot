package hotareadetector.data;

/**
 * General source control information.
 * 
 * (The current implementation contains the URL only, which is used in case of SVN.)
 */
public class SourceControlInformation {
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
