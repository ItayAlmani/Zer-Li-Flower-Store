package entities;

import java.math.BigInteger;

public class Customer extends User {
	private BigInteger customerID;
	private PaymentAccount paymentAccount;
	
	private static BigInteger idInc = null;
	
	public Customer(User user, BigInteger customerID, PaymentAccount paymentAccount) {
		super(user);
		this.customerID = customerID;
	}
	
	public Customer(String privateID, String firstName, String lastName, String userName, String password, UserType permissions,
			BigInteger customerID) {
		super(privateID, firstName, lastName, userName, password, permissions);
		this.customerID = customerID;
	}
	
	
	public void setUser(User user) {
		setUserID(user.getUserID());
		setFirstName(user.getFirstName());
		setLastName(user.getLastName());
		setUserName(user.getUserName());
		setPassword(user.getPassword());
		setPermissions(user.getPermissions());
	}

	public Customer(BigInteger customerID, String privateID, String firstName, String lastName, String userName, String password,
			UserType permissions) {
		super(privateID, firstName, lastName, userName, password, permissions);
		this.customerID=customerID;
	}

	public BigInteger getCustomerID() {
		return customerID;
	}

	public void setCustomerID(BigInteger customerID) {
		this.customerID = customerID;
	}

	public PaymentAccount getPaymentAccount() {
		return paymentAccount;
	}

	public void setPaymentAccount(PaymentAccount paymentAccount) {
		this.paymentAccount = paymentAccount;
	}

	public static BigInteger getIdInc() {
		return idInc;
	}

	public static void setIdInc(BigInteger idInc) {
		Customer.idInc = idInc;
	}
	
}