package entities;

public class Customer extends User {
	private int customerID;
	private PaymentAccount paymentAccount;
	
	public Customer(User user, int customerID, PaymentAccount paymentAccount) {
		super(user);
		this.customerID = customerID;
	}
	
	public Customer(String privateID, String firstName, String lastName, String userName, String password, UserType permissions,
			int customerID) {
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

	public Customer(int customerID, String privateID, String firstName, String lastName, String userName, String password,
			UserType permissions) {
		super(privateID, firstName, lastName, userName, password, permissions);
		this.customerID=customerID;
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
	
}