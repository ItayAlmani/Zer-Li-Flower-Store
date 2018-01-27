package usersInfo.interfaces;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;

import usersInfo.entities.User;

public interface IUser{
	
	/**
	 * handle the information from the server send to the pars function, and than back to client
	 * @param obj - - {@link ArrayList} of field from DB
	 * @throws Exception
	 */
	public ArrayList<Object> handleGet(ArrayList<Object> obj) throws Exception;

	/**
	  *<p>
	 *create new {@link User} object with data from DB
	 * <p>
	 * @param userID - parameter for userID field from DB
	 * @param privateID - parameter for privateID field from DB
	 * @param firstName - parameter for firstName field from DB
	 * @param lastName - parameter for lastName field from DB
	 * @param userName - parameter for userName field from DB
	 * @param password - parameter for password field from DB
	 * @param permissions - parameter for permissions field from DB
	 * @param isConnected - parameter for isConnected field from DB
	 * @param isActive - parameter for isActive field from DB
	 * @return {@link User}
	 */
	public User parse(BigInteger userID, String privateID, String firstName,
			String lastName, String userName, String password, 
			String permissions, boolean isConnected, boolean isActive);

	/**
	 * ask from DataBase to select all {@link User} exist
	 * @throws SQLException
	 */
	public ArrayList<Object> getAllUsers() throws SQLException;
	
	/**
	 * ask from DataBase to select {@link User} according to the {@link User} given who try to LogIn
	 * @param user - {@link User} given, trying to LogIn
	 * @throws SQLException
	 */
	public ArrayList<Object> getUserForLogIn(User user) throws SQLException;
	
	/**
	 * ask from DataBase to select the {@link User} with the given {@link User} ID
	 * @param uID
	 * @throws SQLException
	 */
	public ArrayList<Object> getUser(BigInteger uID) throws SQLException;

	
	/**Dummy function*/
	public ArrayList<Object> add(ArrayList<Object> arr) throws Exception;
	
	/**
	 * update exist {@link User} details in DataBase
	 * @param obj - {@link User} to update
	 * @throws Exception
	 */
	public ArrayList<Object> update(Object obj) throws Exception;
}