package orderNproducts.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;

import usersInfo.entities.StoreWorker;

public class Store implements Serializable  {
	
	private static final long serialVersionUID = 18L;
	private BigInteger storeID;
	private String name;
	private ArrayList<Stock> stock = null;
	private StoreWorker manager;
	
	public BigInteger getStoreID() {
		return storeID;
	}
	
	
	public Store(BigInteger storeID) {
		super();
		this.storeID = storeID;
	}
	
	public Store(BigInteger storeID, String name) {
		this.storeID = storeID;
		this.name = name;
	}
	
	
	public Store(BigInteger storeID, String name, StoreWorker manager) {
		super();
		this.storeID = storeID;
		this.name = name;
		this.manager = manager;
	}
	
	public void setStoreID(BigInteger storeID) {
		this.storeID = storeID;
	}
	public ArrayList<Stock> getStock() {
		return stock;
	}
	public void setStock(ArrayList<Stock> stock) {
		this.stock = stock;
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
			if(prodInStock.getProduct().getPrdID().equals(prod.getPrdID()))
				return prodInStock;
		}
		return null;
	}


	@Override
	public String toString() {
		return getName();
	}
}