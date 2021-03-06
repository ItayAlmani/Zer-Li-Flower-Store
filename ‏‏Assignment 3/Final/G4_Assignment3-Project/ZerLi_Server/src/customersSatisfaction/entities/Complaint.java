package customersSatisfaction.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import usersInfo.entities.Customer;

public class Complaint implements Serializable {
	public BigInteger getCswID() {
		return cswID;
	}

	public void setCswID(BigInteger cswID) {
		this.cswID = cswID;
	}

	private static final long serialVersionUID = 1L;

	private BigInteger complaintID;
	private String complaintReason;
	private Customer customer;
	private LocalDateTime date;
	private boolean isTreated = false;
	private boolean isRefunded = false;
	private BigInteger storeID;
	private boolean isAnswered24Hours = true;
	private BigInteger cswID;

	public Complaint(BigInteger complaintID, Customer customer,BigInteger storeID,String complaintReason, LocalDateTime date,
			boolean isTreated, boolean isRefunded,boolean isAnswered24Hours, BigInteger cswID) {
		super();
		this.complaintID = complaintID;
		this.customer = customer;
		this.storeID = storeID;
		this.complaintReason = complaintReason;
		this.date = date;
		this.isTreated = isTreated;
		this.isRefunded = isRefunded;
		this.isAnswered24Hours=isAnswered24Hours;
		this.cswID = cswID;
	}

	public Complaint(String complaintReason, Customer customer, BigInteger storeID, LocalDateTime date,BigInteger cswID) {
		this.complaintReason = complaintReason;
		this.customer = customer;
		this.storeID = storeID;
		this.date = date;
		this.cswID=cswID;
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
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
		return "Customer: "+customer.toString() +". Date: " + date.format(formatter);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean isAnswered24Hours() {
		return isAnswered24Hours;
	}

	public void setAnswered24Hours(boolean isAnswered24Hours) {
		this.isAnswered24Hours = isAnswered24Hours;
	}
}