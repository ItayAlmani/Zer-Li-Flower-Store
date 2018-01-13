package entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;

public class Complaint implements Serializable {

	private BigInteger complaintID;
	private String complaintReason;
	private BigInteger customerID;
	private LocalDateTime date;
	private boolean isTreated = false;
	private boolean isRefunded = false;
	private BigInteger storeID;

	public Complaint(BigInteger complaintID, BigInteger customerID,BigInteger storeID,String complaintReason, LocalDateTime date,
			boolean isTreated, boolean isRefunded) {
		super();
		this.complaintID = complaintID;
		this.customerID = customerID;
		this.storeID = storeID;
		this.complaintReason = complaintReason;
		this.date = date;
		this.isTreated = isTreated;
		this.isRefunded = isRefunded;
	}

	public Complaint(String complaintReason, BigInteger customerID, BigInteger storeID, LocalDateTime date) {
		this.complaintReason = complaintReason;
		this.customerID = customerID;
		this.storeID = storeID;
		this.date = date;
	}

	public String getComplaintReason() {
		return this.complaintReason;
	}

	public void setComplaintReason(String complaintReason) {
		this.complaintReason = complaintReason;
	}

	public BigInteger getCustomerID() {
		return this.customerID;
	}

	public void setCustomerID(BigInteger customerID) {
		this.customerID = customerID;
	}

	public LocalDateTime getDate() {
		return this.date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public void setIsTreated(boolean isTreated) {
		this.isTreated = isTreated;
	}

	public void setIsRefunded(boolean isRefunded) {
		this.isRefunded = isRefunded;
	}


	public BigInteger getComplaintID() {
		return complaintID;
	}

	public void setComplaintID(BigInteger complaintID) {
		this.complaintID = complaintID;
	}

	public boolean isTreated() {
		return isTreated;
	}

	public void setTreated(boolean isTreated) {
		this.isTreated = isTreated;
	}

	public boolean isRefunded() {
		return isRefunded;
	}

	public void setRefunded(boolean isRefunded) {
		this.isRefunded = isRefunded;
	}

	public BigInteger getStoreID() {
		return storeID;
	}

	public void setStoreID(BigInteger storeID) {
		this.storeID = storeID;
	}

	@Override
	public String toString() {
		return "Customer ID: "+customerID +". Complaint Reason:" + complaintReason;
	}
}