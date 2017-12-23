package entities;

import enums.UserType;

public class Customer extends User {
	private int customerID;
	private PaymentAccount paymentAccount;
	private boolean isConnecting;
	
	public Customer(String firstName, String lastName, String userName, String password, UserType permissions,
			int customerID) {
		super(firstName, lastName, userName, password, permissions);
		this.customerID = customerID;
	}

	public Customer(String firstName, String lastName, String userName, String password, UserType permissions) {
		super(firstName, lastName, userName, password, permissions);
		// TODO Auto-generated constructor stub
	}

	public int getCustomerID() {
		return customerID;
	}

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}

	public PaymentAccount getPaymentAccount() {
		return paymentAccount;
	}

	public void setPaymentAccount(PaymentAccount paymentAccount) {
		this.paymentAccount = paymentAccount;
	}

	public boolean isConnecting() {
		return isConnecting;
	}

	public void setConnecting(boolean isConnecting) {
		this.isConnecting = isConnecting;
	}
	
	
}