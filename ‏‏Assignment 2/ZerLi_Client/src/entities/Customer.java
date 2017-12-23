package entities;

import enums.UserType;

public class Customer extends User {
	private int customerID;
	private PaymentAccount paymentAccount;
	private boolean isConnecting;

	public Customer(String firstName, String lastName, String userName, String password, UserType permissions) {
		super(firstName, lastName, userName, password, permissions);
		// TODO Auto-generated constructor stub
	}
}