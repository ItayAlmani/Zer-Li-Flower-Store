package entities;

import java.util.ArrayList;

import enums.StoreType;

public class Store {

	private int storeID;
	private ArrayList<Product> stock;
	private StoreType type;
	private StoreWorker manager;
	public int getStoreID() {
		return storeID;
	}
	
	
	public Store(int storeID) {
		super();
		this.storeID = storeID;
	}


	public void setStoreID(int storeID) {
		this.storeID = storeID;
	}
	public ArrayList<Product> getStock() {
		return stock;
	}
	public void setStock(ArrayList<Product> stock) {
		this.stock = stock;
	}
	public StoreType getType() {
		return type;
	}
	public void setType(StoreType type) {
		this.type = type;
	}
	public StoreWorker getManager() {
		return manager;
	}
	public void setManager(StoreWorker manager) {
		this.manager = manager;
	}
	
	
}