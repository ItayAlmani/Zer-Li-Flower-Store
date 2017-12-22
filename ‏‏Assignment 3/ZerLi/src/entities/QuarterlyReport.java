package entities;
public abstract class QuarterlyReport {

	private ReportType Type;
	private DateTime ProductionDate;
	private string ID;
	/**
	 * The date of the last month - will take the range of this date to 3 months back
	 */
	private DateTime ReportDate;
	private string storeID;

	public void setType(ReportType Type) {
		this.Type = Type;
	}

	public void setReportDate(DateTime ReportDate) {
		this.ReportDate = ReportDate;
	}

	public void setStoreID(string storeID) {
		this.storeID = storeID;
	}

}