package entities;

import java.util.Date;

import enums.ReportType;

public abstract class QuarterlyReport {

	private ReportType Type;
	private Date ProductionDate;
	private String ID;
	
	/** The date of the last month - will take the range of this date to 3 months back */
	private Date ReportDate;
	private String storeID;

	public void setType(ReportType Type) {
		this.Type = Type;
	}

	public void setReportDate(Date ReportDate) {
		this.ReportDate = ReportDate;
	}

	public void setStoreID(String storeID) {
		this.storeID = storeID;
	}

}