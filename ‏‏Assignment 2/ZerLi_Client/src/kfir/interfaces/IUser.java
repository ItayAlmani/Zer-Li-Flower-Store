package kfir.interfaces;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

import entities.User;
import interfaces.IParent;

public interface IUser extends IParent{

	Boolean getConnection();

	/**
	 * 
	 * @param boollean
	 */
	void SetConnection(Boolean bool);

	/**
	 * 
	 * @param Username
	 * @param password
	 */
	void getUser(User user) throws IOException;

	/**
	 * 
	 * @param username
	 * @param password
	 */
	void LogIn(User user);

	/**
	 * 
	 * @param UserDet
	 */
	boolean CheckMemberPermission(User UserDet);
	
	User parse(BigInteger userID, String privateID, String firstName,
			String lastName, String userName, String password, 
			String permissions, boolean isConnected);

	void sendUsers(ArrayList<User> users);
	
	void updateUser(User user);
	
	void getAllUsers() throws IOException;
}