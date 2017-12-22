package entities;

import enums.UserType;

public class User {
	private String privateID;
	private String firstName;
	private String lastName;
	private String userName;
	private String password;
	private UserType permissions;
	
	private static Integer idCounter = 1;
	public User() {
		this.privateID=idCounter.toString();
		idCounter++;
	}
	
	public User(String firstName, String lastName, String userName, String password, UserType permissions) {
		this();
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.password = password;
		this.permissions = permissions;
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

}