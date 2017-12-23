package entities;

import java.util.ArrayList;

import enums.StoreType;

public class Store {

	private int storeID;
	private ArrayList<Product> stock;
	private StoreType type;
	private StoreManager manager;
}