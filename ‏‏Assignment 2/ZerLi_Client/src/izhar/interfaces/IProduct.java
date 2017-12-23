package izhar.interfaces;

import java.io.IOException;
import java.util.ArrayList;

import entities.Product;
import entities.Product.Color;
import entities.Product.ProductType;

public interface IProduct {

	void getProduct() throws IOException;

	void updateProduct(Product p) throws IOException;

	/**
	 * 
	 * @param type
	 * @param priceStart	-	the minimum price of the wanted product
	 * @param priceEnd		-	the maximum price of the wanted product
	 */
	void createNewProduct(ProductType type, float priceStart, float priceEnd);

	/**
	 * 
	 * @param type
	 * @param priceStart	-	the minimum price of the wanted product
	 * @param priceEnd		-	the maximum price of the wanted product
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