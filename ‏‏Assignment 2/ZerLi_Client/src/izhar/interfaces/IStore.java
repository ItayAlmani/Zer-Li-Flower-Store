package izhar.interfaces;

import java.util.ArrayList;
import java.util.HashMap;

import entities.Product;
import entities.Order;
import entities.Store;
import interfaces.IParent;

public interface IStore extends IParent {

	/**
	 * Static method
	 */
	ArrayList<Store> getAllStores();

	/**
	 * 
	 * @param order
	 */
	boolean checkStockByOrder(Order order);

	/**
	 * 
	 * @param order
	 */
	void updateStock(Order order);

	/**
	 * 
	 * @param order
	 */
	void updateStoreInDB(Order order);

	/**
	 * 
	 * @param storeid
	 */
	HashMap<Product,Integer> getStockByStore(int storeid);

	ArrayList<Store> getAllPhysicalStores();

	Store getOrderOnlyStore();
}