package interfaces;

import java.io.IOException;
import java.util.ArrayList;

import entities.Product;
import entities.Product.Color;
import entities.Product.ProductType;

public interface IProduct {

	void getProduct() throws IOException;

	void updateProduct(Product p) throws IOException;

	/**
	 * Parsing obj to ArrayList of <code>Product</code> and sending it to the client
	 * @param obj - ArrayList of each cell in the table
	 */
	void handleGet(ArrayList<Object> obj);

	/**
	 * 
	 * @param type
	 * @param priceStart
	 * @param priceEnd
	 */
	void createNewProduct(ProductType type, float priceStart, float priceEnd);

	/**
	 * 
	 * @param type
	 * @param priceStart
	 * @param priceEnd
	 * @param color
	 */
	void createNewProduct(ProductType type, float priceStart, float priceEnd, Color color);

	/**
	 * 
	 * @param type
	 * @param priceStart
	 * @param priceEnd
	 * @param color
	 */
	String[] assembleItemFromDB(ProductType type, float priceStart, float priceEnd, Color color);

}