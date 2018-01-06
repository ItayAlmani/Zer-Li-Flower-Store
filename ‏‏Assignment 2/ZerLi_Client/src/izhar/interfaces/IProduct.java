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

	void getProductByID(BigInteger prdID) throws IOException;
	
	void getAllProducts() throws IOException;

	void updateProduct(Product p) throws IOException;
	
	void sendProducts(ArrayList<Product> prds);

	/**
	 * 
	 * @param type
	 * @param priceStart
	 * @param priceEnd
	 * @param color
	 * @param products TODO
	 * @return TODO
	 */
	ArrayList<Product> assembleProduct(ProductType type, Float priceStart, Float priceEnd, Color color, ArrayList<Product> products) throws IOException;
	
	public void getProductsInCatalog() throws IOException;
	
	Product parse(BigInteger prdID, String name, String type, float price, String color, boolean inCatalog, String imageURL) throws FileNotFoundException;
	
	void getAllProductsNotInCatalog() throws IOException;
}