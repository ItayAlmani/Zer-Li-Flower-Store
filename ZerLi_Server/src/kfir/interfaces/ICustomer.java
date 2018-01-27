package kfir.interfaces;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

import entities.CreditCard;
import entities.Customer;
import entities.User;
import orderNproducts.entities.Order;
import orderNproducts.entities.Store;

public interface ICustomer{

	/**
	 *asks from DataBase all customers with all User details - names, privateID...
	 * @throws IOException 
	 */
	public ArrayList<Object> getAllCustomers() throws Exception;	
	
	/**
	 * asks from DatatBase to select the specific {@link Customer} with the given {@link User}'s ID 
	 * @param userID
	 * @throws IOException
	 */
	public ArrayList<Object> getCustomerByUser(BigInteger userID) throws Exception;
	
	/**
	 * handle the information from the server send to the pars function, and than back to client
	 * </p>
	 * @param obj - {@link ArrayList} of fields from DB
	 */
	public ArrayList<Object> handleGet(ArrayList<Object> obj);		
	
	/**
	 * asks from DataBase all {@link Customer}s in the specific {@link Store} 
	 * @param storeID
	 * @throws IOException
	 */
	public ArrayList<Object> getAllCustomersOfStore(BigInteger storeID) throws Exception;

	/**
	 * add new {@link Customer} to the DataBase
	 * @param arr - {@link ArrayList} that contain the {@link Customer} to add
	 * @throws Exception
	 */
	public ArrayList<Object> add(ArrayList<Object> arr) throws Exception;	

	/**
	 * ask from DataBase the specific {@link Customer} with the given ID
	 * @param customerID
	 * @throws Exception
	 */
	public ArrayList<Object> getCustomerByID(BigInteger customerID) throws Exception;
	
	/**
	 * ask from DataBase the specific {@link Customer} with the given privateID
	 * @param privateID
	 * @throws Exception
	 */
	ArrayList<Object> getCustomerByPrivateID(String privateID) throws Exception;
	
	/**
	  *<p>
	 * create new {@link Customer} object with data from DB
	 * <p>
	 * @param customerID  - parameter for custID field from DB
	 * @param user  - parameter for {@link User} field from DB
	 * @return {@link Customer}
	 */
	public Customer parse(BigInteger customerID, User user);
	
}