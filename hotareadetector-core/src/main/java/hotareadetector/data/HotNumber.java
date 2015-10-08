package hotareadetector.data;

/**
 * Result of the hot number calculation.
 */
public class HotNumber implements Comparable<HotNumber> {
	private final String fileName;
	private final Double hotNumber;
	
	public HotNumber(String fileName, Double hotNumber) {
		this.fileName = fileName;
		this.hotNumber = hotNumber;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public Double getHotNumber() {
		return hotNumber;
	}
	
	@Override
	public int compareTo(HotNumber o) {
		HotNumber otherHotNumber = (HotNumber)o;
		if (hotNumber != null) {
			return hotNumber.compareTo(otherHotNumber.hotNumber);
		}
		return 0;
	}

}
