package izhar.interfaces;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

import entities.Product;
import entities.ProductInOrder;

public interface IProductInOrder {
	
	void updatePIO(ProductInOrder p) throws IOException;
	void handleGet(ArrayList<Object> obj);
	ProductInOrder parse(Product prod, BigInteger orderID, int quantity, float finalPrice);
	void sendPIOs(ArrayList<ProductInOrder> prds);
	void addPIO(ProductInOrder p) throws IOException;
	void getPIOsByOrder(BigInteger orderID) throws IOException;
	void updatePriceOfPIO(ProductInOrder p) throws IOException;
}