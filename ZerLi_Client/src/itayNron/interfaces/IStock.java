package itayNron.interfaces;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

import entities.Order;
import entities.Product;
import entities.ProductInOrder;
import entities.Stock;
import entities.Store;
import interfaces.IParent;

public interface IStock {

	void getStockByStore(BigInteger storeID) throws IOException;

	Product checkStockByOrder(Order order, Store store);

	void update(Order order) throws IOException;
	
	public void updateAfterCancellation(Order order) throws IOException;

}