package izhar;

import java.util.ArrayList;
import java.util.HashMap;

import entities.Order;
import entities.Product;
import entities.Store;
import izhar.interfaces.IStore;

public class StoreController implements IStore {

	@Override
	public void handleGet(ArrayList<Object> obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getAllStores() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendStores(ArrayList<Store> stores) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void checkStockByOrder(Order order) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateStock(Order order) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateStore(Order order) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getStockByStore(int storeid) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void sendStock(HashMap<Product,Integer> stock) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getAllPhysicalStores() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getStoreByOrder(Order order) {
		// TODO Auto-generated method stub
		
	}

}