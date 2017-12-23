package izhar;

import java.util.ArrayList;
import java.util.HashMap;

import entities.Item;
import entities.Order;
import entities.Store;
import izhar.interfaces.IStore;

public class StoreController implements IStore {

	public ArrayList<Store> getAllStores() {
	}

	public boolean checkStockByOrder(Order order) {
	}

	public void updateStock(Order order) {
	}

	public void updateStoreInDB(Order order) {
	}

	public HashMap<Item,Integer> getStockByStore(String storeid) {
	}

	public ArrayList<Store> getAllPhysicalStores() {
	}

	public Store getOrderOnlyStore() {
	}

}