package orderNproducts.interfaces;

import java.io.IOException;
import java.math.BigInteger;

import orderNproducts.entities.Store;

public interface IStore {

/**
 * <p>
 * Function to get all stores from DB 
 * </p>
 * @throws IOException Context.clientConsole.handleMessageFromClientUI throws IOException.
 */
	void getAllStores() throws IOException;
	/**
	 * <p>
	 * Function to insert data,which coming from server, to store object
	 * </p>
	 * @param storeID - parameter to insert to storeID field
	 * @param type - parameter to insert to type field
	 * @param managerID - parameter to insert to managerID field
	 * @param name - parameter to insert to name field
	 * @return new {@link Store} object
	 */
	
	Store parse(BigInteger storeID, String type, BigInteger managerID, String name) ;
	

	/**
	 * <p>
	 * Function to update store object in DB
	 * </p>
	 * @param store - store object to be updated in DB
	 * @throws IOException Context.clientConsole.handleMessageFromClientUI throws IOException.
	 */
	void update(Store store) throws IOException;

}