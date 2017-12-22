package entities;
public class Customer extends User {

	private string customerID;
	private PaymentAccount paymentAccount;
	private boolean IsConnecting;

	public string getCustomerID() {
		return this.customerID;
	}

}