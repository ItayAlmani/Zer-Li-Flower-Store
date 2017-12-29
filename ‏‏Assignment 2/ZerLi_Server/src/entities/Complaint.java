package entities;

import java.math.BigInteger;
import java.util.Date;

public class Complaint {

	private BigInteger complaintID;
	private String complaintReason;
	private String customerID;
	private Date date;
	private boolean isTreated = false;
	private boolean isRefunded = false;
	private String status;
	private String storeID;

	public String getComplaintReason() {
		return this.complaintReason;
	}

	public void setComplaintReason(String complaintReason) {
		this.complaintReason = complaintReason;
	}

	public String getCustomerID() {
		return this.customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean isIsTreated() {
		return this.isTreated;
	}

	public void setIsTreated(boolean isTreated) {
		this.isTreated = isTreated;
	}

	public boolean isIsRefunded() {
		return this.isRefunded;
	}

	public void setIsRefunded(boolean isRefunded) {
		this.isRefunded = isRefunded;
	}

}