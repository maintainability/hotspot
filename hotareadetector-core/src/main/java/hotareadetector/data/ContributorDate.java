package hotareadetector.data;

import java.util.Date;

/**
 * Structure for saving contributor-dater pairs.
 */
public class ContributorDate {
	private String contributor;
	private Date date;
	
	public ContributorDate(String contributor, Date date) {
		this.contributor = contributor;
		this.date = date;
	}
	
	public String getContributor() {
		return contributor;
	}
	
	public Date getDate() {
		return date;
	}
}
