package entities;

import java.util.ArrayList;

public class Store {
	
	public enum StoreType {
		Physical,
		OrdersOnly
	}
	
	private int storeID;
	private String name;
	private ArrayList<Stock> stock;
	private StoreType type;
	private StoreWorker manager;
	public int getStoreID() {
		return storeID;
	}
	
	
	public Store(int storeID) {
		super();
		this.storeID = storeID;
	}


	public Store(int storeID, String name, StoreType type, StoreWorker manager) {
		super();
		this.storeID = storeID;
		this.name = name;
		this.type = type;
		this.manager = manager;
	}


	public void setStoreID(int storeID) {
		this.storeID = storeID;
	}
	public ArrayList<Stock> getStock() {
		return stock;
	}
	public void setStock(ArrayList<Stock> stock) {
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


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
	public Stock getProductFromStock(Product prod) {
		for (Stock prodInStock : stock) {
			if(prodInStock.getProduct().getPrdID() == prod.getPrdID())
				return prodInStock;
		}
		return null;
	}
}