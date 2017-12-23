package entities;

import enums.UserType;

public class Admin extends User {

	public Admin(String firstName, String lastName, String userName, String password, UserType permissions) {
		super(firstName, lastName, userName, password, permissions);
		// TODO Auto-generated constructor stub
	}

	private Object AdminID;
	
	
}