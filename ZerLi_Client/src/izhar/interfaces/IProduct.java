package izhar.interfaces;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

import entities.Product;
import entities.Product.Color;
import entities.Product.ProductType;
import entities.Stock;
import entities.Subscription;
import interfaces.IParent;

public interface IProduct {
	
	void add(Product p, boolean getID) throws IOException;

	void getProductByID(BigInteger prdID) throws IOException;
	
	void getAllProducts() throws IOException;

	void update(Product p) throws IOException;

	/**
	 * 
	 * @param type
	 * @param priceStart
	 * @param priceEnd
	 * @param color
	 * @param stocks TODO
	 * @param sub TODO
	 * @return TODO
	 */
	ArrayList<Stock> assembleProduct(ProductType type, Float priceStart, Float priceEnd, Color color, ArrayList<Stock> stocks, Subscription sub);
	
	public void getProductsInCatalog() throws IOException;
	
	void getAllProductsNotInCatalog() throws IOException;
}