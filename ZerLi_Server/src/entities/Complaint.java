package entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;

public class Complaint implements Serializable {

	private BigInteger complaintID;
	private String complaintReason;
	private String customerID;
	private LocalDateTime date;
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

	public LocalDateTime getDate() {
		return this.date;
	}

	public void setDate(LocalDateTime date) {
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getStoreID() {
		return storeID;
	}

	public void setStoreID(String storeID) {
		this.storeID = storeID;
	}

	@Override
	public String toString() {
		return "Customer ID: "+customerID +". Complaint Reason:" + complaintReason;
	}
}