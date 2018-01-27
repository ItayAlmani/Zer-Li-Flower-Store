package orderNproducts.interfaces;

import java.io.IOException;
import java.math.BigInteger;

import orderNproducts.entities.Store;

public interface IStore {

	/**
	 * Function to get all stores from DB 
	 * @throws IOException Context.clientConsole.handleMessageFromClientUI throws IOException.
	 */
	void getAllStores() throws IOException;
	
	/**
	 * Function to insert data,which coming from server, to store object
	 * @param storeID - parameter to insert to storeID field
	 * @param type - parameter to insert to type field
	 * @param managerID - parameter to insert to managerID field
	 * @param name - parameter to insert to name field
	 * @return new {@link Store} object
	 */
	Store parse(BigInteger storeID, String type, BigInteger managerID, String name) ;

	/**
	 * Function to update store object in DB
	 * @param store - store object to be updated in DB
	 * @throws IOException Context.clientConsole.handleMessageFromClientUI throws IOException.
	 */
	void update(Store store) throws IOException;

}