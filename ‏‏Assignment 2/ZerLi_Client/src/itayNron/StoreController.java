package itayNron;

import java.util.ArrayList;
import java.util.HashMap;

import entities.Order;
import entities.Product;
import entities.ProductInOrder;
import entities.Stock;
import entities.Store;
import entities.Store.StoreType;
import itayNron.interfaces.IStore;

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
	public Product checkStockByOrder(Order order, Store store) {
		ArrayList<Stock> stock = (ArrayList<Stock>) store.getStock().clone();
		for (ProductInOrder ordProd : order.getProducts()) {
			Product prod = ordProd.getProduct();
			Stock stk = store.getProductFromStock(prod);
			if(	stk==null //not in stock
					|| stk.getQuantity()-ordProd.getQuantity()<0 //Not enough products in stock
					)	
				return prod;
		}
		return null;
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
	
	public ArrayList<Store> getOrdersOnlyStoresFromArrayList(ArrayList<Store> allStores){
		ArrayList<Store> ordersOnly = new ArrayList<>();
		for (Store store : allStores)
			if(store.getType().equals(StoreType.OrdersOnly))
				ordersOnly.add(store);
		return ordersOnly;
	}
}