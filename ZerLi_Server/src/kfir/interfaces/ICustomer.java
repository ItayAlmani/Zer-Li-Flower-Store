package kfir.interfaces;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

import entities.CreditCard;
import entities.Customer;
import entities.Order;
import entities.Store;
import entities.User;

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
	 * bill {@link Customer}'s {@link CreditCard} according to the amount given
	 * @param customer, amount
	 * @return success status
	 */
	boolean billCreditCardOfCustomer(Customer customer, float amount);
	
	/**
	 * bill {@link CreditCard} according to the amount given
	 * @param cc - {@link Customer}'s {@link CreditCard}
	 * @param amount - {@link Order} price
	 * @return success status
	 */
	public boolean billCard(CreditCard cc, float amount);
	
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
	  *<p>
	 * create new {@link Customer} object with data from DB
	 * <p>
	 * @param customerID  - parameter for custID field from DB
	 * @param user  - parameter for {@link User} field from DB
	 * @return {@link Customer}
	 */
	public Customer parse(BigInteger customerID, User user);
	

	
	





}