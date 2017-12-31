package entities;

import java.math.BigInteger;

public class User {
	
	public User() {
		super();
	}

	private static BigInteger idInc = null;
	
	public enum UserType {
		Customer,
		StoreManager,
		ChainStoreWorker,
		ChainStoreManager,
		ServiceExpert,
		CustomerServiceWorker
	}
	
	public enum UserState {
		Active,
		Blocked,
		LoggedIn,
		LoggedOut
	}
	
	private BigInteger userID;
	private String privateID;
	private String firstName;
	private String lastName;
	private String userName;
	private String password;
	private UserType permissions;
	private boolean isConnected;
	
	public User(BigInteger userID) {
		super();
		this.userID = userID;
	}

	public User(User user) {
		this.firstName = user.firstName;
		this.lastName = user.lastName;
		this.userName = user.userName;
		this.password = user.password;
		this.permissions = user.permissions;
	}
	
	public User(String privateID, String firstName, String lastName, String userName, String password, UserType permissions) {
		this.privateID = privateID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.password = password;
		this.permissions = permissions;
	}

	public User(BigInteger userID, String privateID, String firstName, String lastName, String userName, String password,
			UserType permissions, boolean isConnected) {
		super();
		this.userID = userID;
		this.privateID = privateID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.password = password;
		this.permissions = permissions;
		this.isConnected=isConnected;
	}
	
	public User(BigInteger userID, String privateID, String firstName, String lastName, String userName, String password,
			UserType permissions) {
		super();
		this.userID = userID;
		this.privateID = privateID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.password = password;
		this.permissions = permissions;
	}

	public User(String userName, String password) {
		super();
		this.userName = userName;
		this.password = password;
	}



	public UserType getPermissions() {
		return this.permissions;
	}

	public void setPermissions(UserType permissions) {
		this.permissions = permissions;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPrivateID() {
		return privateID;
	}

	public BigInteger getUserID() {
		return userID;
	}

	public void setUserID(BigInteger userID) {
		this.userID = userID;
	}

	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}

	public void setPrivateID(String privateID) {
		this.privateID = privateID;
	}

}