package izhar.interfaces;

import java.util.ArrayList;

import entities.Product;

public interface IBrowsingProcess {

	/**
	 * 
	 * @param cartID
	 */
	ArrayList<Product> getProductsInCart(int cartID);

	/**
	 * 
	 * @param productID
	 */
	Product getItemDetails(int productID);

	void updateFinalPrice();

	/**
	 * 
	 * @param product
	 */
	boolean addProductToCart(Product product);

}
