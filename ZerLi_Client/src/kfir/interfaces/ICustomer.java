package kfir.interfaces;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

import entities.CreditCard;
import entities.Customer;
import entities.User;
import orderNproducts.entities.Store;

public interface ICustomer{

	/**
	 *asks from DataBase all customers with all User details - names, privateID...
	 * @throws IOException 
	 */
	void getAllCustomers() throws IOException;
	
	/**
	 * asks from DatatBase to select the specific {@link Customer} where his {@link User} ID is the param
	 * @param userID
	 * @throws IOException
	 */
	void getCustomerByUser(BigInteger userID) throws IOException;

	
	
	/**
	 * Function handle the information from the server, back to (GUI / asking controller)
	 * </p>
	 * @param customers after parse
	 */
	public void handleGet(ArrayList<Customer> customers);
		
	/**
	 * asks from DataBase all {@link Customer}s in the specific {@link Store} 
	 * @param storeID
	 * @throws IOException
	 */
	public void getAllCustomersOfStore(BigInteger storeID) throws IOException;

	/**
	 * add new {@link Customer} to the DataBase
	 * @param cust - new {@link Customer} to add
	 * @param getID - boolean to know if function need to return new {@link Customer}'s ID
	 * @throws Exception
	 */
	public void add(Customer cust, boolean getID) throws Exception;
	
	/**
	 * delete {@link Customer} from DataBase
	 * @param userID
	 * @throws Exception
	 */
	public void delete(BigInteger userID) throws Exception;
	
	/**
	 * ask from DataBase the specific {@link Customer} with the given ID
	 * @param privateID
	 * @throws Exception
	 */
	public void getCustomerByPrivateID(String privateID) throws Exception;






}