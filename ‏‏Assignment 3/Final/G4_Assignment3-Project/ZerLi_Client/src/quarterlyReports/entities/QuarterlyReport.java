package quarterlyReports.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;

public abstract class QuarterlyReport implements Serializable {
	
	private static final long serialVersionUID = 14L;
	
	/** The date of the last month - will take the range of this date to 3 months back */
	private LocalDate endOfQuarterDate;
	private BigInteger storeID;
	
	public QuarterlyReport() {
		
	}
	
	public QuarterlyReport(LocalDate endOfQuarterDate, BigInteger storeID) {
		this.endOfQuarterDate = endOfQuarterDate;
		this.storeID = storeID;
	}
	
	public enum ReportType {
		Order, Satisfaction, Incomes, CustomerComplaints;
	}


	public LocalDate getEndOfQuarterDate() {
		return endOfQuarterDate;
	}


	public void setEndOfQuarterDate(LocalDate endOfQuarterDate) {
		this.endOfQuarterDate = endOfQuarterDate;
	}


	public BigInteger getStoreID() {
		return storeID;
	}


	public void setStoreID(BigInteger storeID) {
		this.storeID = storeID;
	}

}