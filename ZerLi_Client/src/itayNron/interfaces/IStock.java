package itayNron.interfaces;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

import entities.Order;
import entities.Product;
import entities.ProductInOrder;
import entities.Stock;
import entities.Store;
import interfaces.IParent;

public interface IStock {

	/**
	 * <p>
	 * Function to get stock of a specified store
	 * </p>
	 * @param storeID - unique identifier to decide from which store we will get the stock
	 * @throws IOException Context.clientConsole.handleMessageFromClientUI throws IOException.
	 */
	void getStockByStore(BigInteger storeID) throws IOException;
	/**
	 * <p>
	 * Function to check if specific store have the requested products in stock to be able to deliver a specific order
	 * </p>
	 * @param order - order object to identify products to check in store
	 * @param store - store object to identify if in this store have enough products in stock to supply order
	 * @return {@link Product} -  if the store have enough of it in stock<br>
	 * null - if store doesn't have enough products in stock for this order
	 * 
	 */

	Product checkStockByOrder(Order order, Store store);
/**
 * <p>
 * Function to update order object in DB
 * </p>
 * @param order - order object to be updated in DB
 * @throws IOException Context.clientConsole.handleMessageFromClientUI throws IOException.
 */
	void update(Order order) throws IOException;
	
	

}