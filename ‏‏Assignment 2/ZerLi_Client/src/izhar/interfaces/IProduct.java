package izhar.interfaces;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
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
	
	Product parse(BigInteger prdID, String name, String type, float price, String color, boolean inCatalog, String imageURL) throws FileNotFoundException;
}