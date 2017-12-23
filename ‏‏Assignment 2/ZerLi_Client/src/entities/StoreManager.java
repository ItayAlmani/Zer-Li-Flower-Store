package entities;

import enums.UserType;

public class StoreManager extends User {
	private Store store;
	
	public StoreManager(String firstName, String lastName, String userName, String password, UserType permissions) {
		super(firstName, lastName, userName, password, permissions);
		// TODO Auto-generated constructor stub
	}

}