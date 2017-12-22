package interfaces;

import java.util.ArrayList;
import java.util.HashMap;

import entities.Item;
import entities.Order;
import entities.Stock;
import entities.Store;

public interface IStore {

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
	HashMap<Item,Integer> getStockByStore(String storeid);

	ArrayList<Store> getAllPhysicalStores();

	Store getAOrderOnlyStore();

}