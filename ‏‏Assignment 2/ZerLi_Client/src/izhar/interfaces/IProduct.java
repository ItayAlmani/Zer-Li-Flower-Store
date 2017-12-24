package izhar.interfaces;

import java.io.IOException;
import java.util.ArrayList;

import entities.Product;
import entities.Product.Color;
import entities.Product.ProductType;
import interfaces.IParent;

public interface IProduct extends IParent {
	
	void addProduct(Product p) throws IOException;

	public void getProductByID(int prdID) throws IOException;

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

	Product parse(int prdID, String name, String type, float price, String color, boolean inCatalog);
}