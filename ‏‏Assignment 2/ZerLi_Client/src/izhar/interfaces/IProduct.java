package izhar.interfaces;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import entities.Product;
import entities.Product.Color;
import entities.Product.ProductType;
import interfaces.IParent;

public interface IProduct extends IParent {
	
	void addProduct(Product p) throws IOException;

	void getProductByID(int prdID) throws IOException;
	
	void getAllProducts() throws IOException;

	void updateProduct(Product p) throws IOException;
	
	void sendProducts(ArrayList<Product> prds);

	/**
	 * 
	 * @param type
	 * @param priceStart
	 * @param priceEnd
	 * @param color
	 */
	void assembleItemFromDB(ProductType type, float priceStart, float priceEnd, Color color) throws IOException;
	
	public void getProductsInCatalog() throws IOException;
	
	/**
	 * asks from server an Order with orderid=<code>orderID</code>
	 * @param orderID - the id of the Order
	 * @throws IOException
	 */
	void getProductsByOrder(int orderID) throws IOException;

	Product parse(int prdID, String name, String type, float price, String color, boolean inCatalog, String imageURL) throws FileNotFoundException;
}