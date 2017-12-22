package entities;
public class Complaint {

	private string complaintID;
	private string complaintReason;
	private string customerID;
	/**
	 * Today's date
	 */
	private DateTime date;
	/**
	 * initial value = false
	 */
	private boolean isTreated = false;
	private boolean isRefunded = false;
	private string Status;
	private string storeID;

	public string getComplaintReason() {
		return this.complaintReason;
	}

	public void setComplaintReason(string complaintReason) {
		this.complaintReason = complaintReason;
	}

	public string getCustomerID() {
		return this.customerID;
	}

	public void setCustomerID(string customerID) {
		this.customerID = customerID;
	}

	public DateTime getDate() {
		return this.date;
	}

	public void setDate(DateTime date) {
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