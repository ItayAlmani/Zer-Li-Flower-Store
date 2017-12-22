package interfaces;

import java.util.ArrayList;

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
	ArrayList<Stock> getStockByStore(String storeid);

	ArrayList<Stock> getAllPhysicalStores();

	Store getAOrderOnlyStore();

}