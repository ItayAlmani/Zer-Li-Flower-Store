package entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

public abstract class QuarterlyReport implements Serializable {
	public enum ReportType {
		Order, Satisfaction, Incomes, CustomerComplaints;
	}
	
	private ReportType Type;
	private Date ProductionDate;
	private int qReportID;
	
	/** The date of the last month - will take the range of this date to 3 months back */
	private Date ReportDate;
	private BigInteger storeID;

	public BigInteger getStoreID() {
		return storeID;
	}

	public void setType(ReportType Type) {
		this.Type = Type;
	}

	public void setReportDate(Date ReportDate) {
		this.ReportDate = ReportDate;
	}

	public void setStoreID(BigInteger storeID) {
		this.storeID = storeID;
	}

}