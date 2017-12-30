package itayNron.interfaces;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

import entities.Product;
import entities.Stock;
import entities.Order;
import entities.Store;
import interfaces.IParent;

public interface IStore extends IParent {

	/**
	 * Static method
	 */
	void getAllStores() throws IOException;
	
	void sendStores(ArrayList<Store> stores);
	
	Store parse(BigInteger storeID, String type, BigInteger managerID, String name) ;
	

	/**
	 * 
	 * @param order
	 */
	Product checkStockByOrder(Order order, Store store);

	/**
	 * 
	 * @param order
	 */
	void updateStock(Order order) throws IOException;

	/**
	 * 
	 * @param order
	 */
	void updateStore(Store store) throws IOException;

	/**
	 * 
	 * @param storeid
	 */
	void getStockByStore(int storeID) throws IOException;
	
	void sendStock(Stock stock) throws IOException;

	void getAllPhysicalStores() throws IOException;
}