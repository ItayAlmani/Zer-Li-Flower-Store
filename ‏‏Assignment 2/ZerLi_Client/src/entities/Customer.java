package entities;

import java.io.Serializable;
import java.math.BigInteger;

public class Customer extends User implements Serializable {
	private BigInteger customerID;
	private PaymentAccount paymentAccount;
	
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
		setPrivateID(user.getPrivateID());
		setFirstName(user.getFirstName());
		setLastName(user.getLastName());
		setUserName(user.getUserName());
		setPassword(user.getPassword());
		setPermissions(user.getPermissions());
		setConnected(user.isConnected());
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

	@Override
	public String toString() {
		return getFullName();
	}
}