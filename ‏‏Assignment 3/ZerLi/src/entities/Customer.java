package entities;
public class Customer extends User {

	private String customerID;
	private PaymentAccount paymentAccount;
	private boolean IsConnecting;

	public String getCustomerID() {
		return this.customerID;
	}

}