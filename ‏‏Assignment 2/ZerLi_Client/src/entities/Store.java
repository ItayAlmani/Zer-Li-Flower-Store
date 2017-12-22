package entities;

import java.util.ArrayList;

import enums.StoreType;

public class Store {

	private String storeID;
	private ArrayList<Item> stock;
	private StoreType type;
	private StoreManager manager;
	
	private static Integer idCounter = 1;
	public Store() {
		this.storeID = idCounter.toString();
		idCounter++;
	}
}