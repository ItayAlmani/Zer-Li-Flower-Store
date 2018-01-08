package izhar.interfaces;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

import entities.Product;
import entities.ProductInOrder;

public interface IProductInOrder {
	
	void update(ProductInOrder p) throws IOException;
	void add(ProductInOrder p) throws IOException;
	void getPIOsByOrder(BigInteger orderID) throws IOException;
	void updatePriceOfPIO(ProductInOrder p) throws IOException;
	void handleGet(ArrayList<ProductInOrder> pios);
}