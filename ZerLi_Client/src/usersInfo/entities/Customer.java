package usersInfo.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;

public class Customer extends User implements Serializable {
	
	private static final long serialVersionUID = 4L;
	private BigInteger customerID;
	private ArrayList<PaymentAccount> paymentAccounts = new ArrayList<>();
	
	public Customer(User user, BigInteger customerID) {
		super(user);
		this.customerID = customerID;
	}
	public Customer (BigInteger customerID)
	{
		this.customerID=customerID;
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
		setActive(user.isActive());
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
	
	public void addPaymentAccount(PaymentAccount pa) {
		if(this.paymentAccounts==null)
			this.paymentAccounts=new ArrayList<>();
		this.paymentAccounts.add(pa);
	}

	@Override
	public String toString() {
		return getFullName();
	}

	public ArrayList<PaymentAccount> getPaymentAccounts() {
		return paymentAccounts;
	}

	public void setPaymentAccounts(ArrayList<PaymentAccount> paymentAccounts) {
		this.paymentAccounts = paymentAccounts;
	}
}