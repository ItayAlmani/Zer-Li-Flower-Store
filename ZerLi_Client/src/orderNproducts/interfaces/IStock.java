package orderNproducts.interfaces;

import java.io.IOException;
import java.math.BigInteger;

import orderNproducts.entities.Order;
import orderNproducts.entities.Product;
import orderNproducts.entities.Store;

public interface IStock {

	/**
	 * Function to get stock of a specified store
	 * @param storeID - unique identifier to decide from which store we will get the stock
	 * @throws IOException Context.clientConsole.handleMessageFromClientUI throws IOException.
	 */
	void getStockByStore(BigInteger storeID) throws IOException;
	
	/**
	 * Function to check if specific store have the requested products in stock to be able to deliver a specific order
	 * @param order - order object to identify products to check in store
	 * @param store - store object to identify if in this store have enough products in stock to supply order
	 * @return {@link Product} -  if the store have enough of it in stock<br>
	 * null - if store doesn't have enough products in stock for this order
	 */
	Product checkStockByOrder(Order order, Store store);
	
	/**
	 * Function to update order object in DB
	 * @param order - order object to be updated in DB
	 * @throws IOException Context.clientConsole.handleMessageFromClientUI throws IOException.
	 */
	void update(Order order) throws IOException;
	
	

}