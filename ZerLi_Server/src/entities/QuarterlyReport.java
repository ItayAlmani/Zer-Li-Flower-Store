package entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

public abstract class QuarterlyReport implements Serializable {
	
	private static final long serialVersionUID = 14L;
	private int qReportID;
	private ReportType type;
	private Date productionDate;
	
	/** The date of the last month - will take the range of this date to 3 months back */
	private Date ReportDate;
	private BigInteger storeID;

	
	
	public BigInteger getStoreID() {
		return storeID;
	}

	public void setType(ReportType Type) {
		this.type = Type;
	}

	public void setReportDate(Date ReportDate) {
		this.ReportDate = ReportDate;
	}

	public void setStoreID(BigInteger storeID) {
		this.storeID = storeID;
	}
	
	public enum ReportType {
		Order, Satisfaction, Incomes, CustomerComplaints;
	}

}