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
	void getAllStores();
	
	void sendStores(ArrayList<Store> stores);

	/**
	 * 
	 * @param order
	 */
	void checkStockByOrder(Order order);

	/**
	 * 
	 * @param order
	 */
	void updateStock(Order order);

	/**
	 * 
	 * @param order
	 */
	void updateStore(Order order);

	/**
	 * 
	 * @param storeid
	 */
	void getStockByStore(int storeid);
	
	void sendStock(HashMap<Product,Integer> stock);

	void getAllPhysicalStores();

	void getStoreByOrder(Order order);
}