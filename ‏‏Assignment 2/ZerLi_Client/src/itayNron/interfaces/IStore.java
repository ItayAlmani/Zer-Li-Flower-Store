package itayNron.interfaces;

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
	 * checks if all products in order can be bought at the specific store
	 * @param order
	 * @param store
	 * @return the product which out of store if exists, else null
	 */
	Product checkStockByOrder(Order order, Store store);

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