package izhar.interfaces;

import java.util.ArrayList;

import entities.Product;

public interface IBrowsingProcess {

	/**
	 * 
	 * @param cartID
	 */
	void getProductsInCart(int cartID);
	
	void sendProductsInCart(ArrayList<Product> products);

	/**
	 * 
	 * @param productID
	 */
	Product getProductDetails(int productID);

	void updateFinalPrice();

	/**
	 * 
	 * @param product
	 */
	boolean addProductToCart(Product product);

}
