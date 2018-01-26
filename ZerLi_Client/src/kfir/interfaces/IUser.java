package kfir.interfaces;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

import entities.User;

public interface IUser{
	
	/**
	 * update exist {@link User} in DataBase
	 * @param user -  {@link User} to update
	 * @throws IOException
	 */
	void update(User user) throws IOException;
	
	/**
	 * ask from DataBase to select all {@link User}s exist
	 * @throws IOException
	 */
	void getAllUsers() throws IOException;
	
	/**
	 * ask from DataBase to select {@link User} according to the {@link User} given who try to LogIn
	 * @param user - {@link User} given, trying to LogIn
	 * @throws IOException if {@link User} does not exist
	 */
	void getUserForLogIn(User user) throws IOException;
	
	/**
	 * ask from Database to select {@link User} according to the ID given
	 * @param uID - {@link User} ID given
	 * @throws IOException
	 */
	void getUser(BigInteger uID) throws IOException;
	
	/**
	 * handle the information from the server, back to (GUI / asking controller)
	 * </p>
	 * @param users - {@link User}s after parse
	 */
	public void handleGet(ArrayList<User> users);

}