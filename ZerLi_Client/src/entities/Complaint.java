package entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;

public class Complaint implements Serializable {

	private BigInteger complaintID;
	private String complaintReason;
	private Customer customer;
	private LocalDateTime date;
	private boolean isTreated = false;
	private boolean isRefunded = false;
	private BigInteger storeID;

	public Complaint(BigInteger complaintID, Customer customer,BigInteger storeID,String complaintReason, LocalDateTime date,
			boolean isTreated, boolean isRefunded) {
		super();
		this.complaintID = complaintID;
		this.customer = customer;
		this.storeID = storeID;
		this.complaintReason = complaintReason;
		this.date = date;
		this.isTreated = isTreated;
		this.isRefunded = isRefunded;
	}

	public Complaint(String complaintReason, Customer customer, BigInteger storeID, LocalDateTime date) {
		this.complaintReason = complaintReason;
		this.customer = customer;
		this.storeID = storeID;
		this.date = date;
	}

	public String getComplaintReason() {
		return this.complaintReason;
	}

	public void setComplaintReason(String complaintReason) {
		this.complaintReason = complaintReason;
	}

	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
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
		return "Customer: "+customer.toString() +". Complaint Reason:" + complaintReason;
	}
}