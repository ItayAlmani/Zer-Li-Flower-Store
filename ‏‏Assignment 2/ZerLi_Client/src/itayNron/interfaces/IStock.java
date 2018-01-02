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

public interface IStock extends IParent {

	void getStockByStore(BigInteger storeID) throws IOException;

	void sendStocks(ArrayList<Stock> stocks);

	Product checkStockByOrder(Order order, Store store);

	void updateStock(Order order) throws IOException;

	void setPIOs(ArrayList<ProductInOrder> prds) throws IOException;

}