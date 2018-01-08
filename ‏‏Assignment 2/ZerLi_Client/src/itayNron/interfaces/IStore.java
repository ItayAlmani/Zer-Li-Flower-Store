package itayNron.interfaces;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

import entities.Product;
import entities.Stock;
import entities.Order;
import entities.Store;
import interfaces.IParent;

public interface IStore {

	/**
	 * Static method
	 */
	void getAllStores() throws IOException;
	
	Store parse(BigInteger storeID, String type, BigInteger managerID, String name) ;
	

	/**
	 * 
	 * @param order
	 */
	void update(Store store) throws IOException;

	void getAllPhysicalStores() throws IOException;
}