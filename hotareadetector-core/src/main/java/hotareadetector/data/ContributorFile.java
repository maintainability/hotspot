package hotareadetector.data;

public class ContributorFile {
	private String contributor;
	private String fileName;
	
	public ContributorFile(String contributor, String fileName) {
		this.contributor = contributor;
		this.fileName = fileName;
	}
	
	public String getContributor() {
		return contributor;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	@Override
	public String toString() {
		return "[contributor=" + contributor + ", fileName=" + fileName + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contributor == null) ? 0 : contributor.hashCode());
		result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContributorFile other = (ContributorFile) obj;
		if (contributor == null) {
			if (other.contributor != null)
				return false;
		} else if (!contributor.equals(other.contributor))
			return false;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		return true;
	}
}
