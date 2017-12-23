package interfaces;

import java.util.ArrayList;

import entities.Item;

public interface IBrowsingProcess {

	/**
	 * 
	 * @param cartID
	 */
	ArrayList<Item> getItemsInCart(int cartID);

	/**
	 * 
	 * @param itemID
	 */
	Item getItemDetails(int itemID);

	void updateFinalPrice();

	/**
	 * 
	 * @param item
	 */
	boolean addItemToCart(Item item);
	
	/**
	 * Parsing obj to ArrayList of <code>Product</code> and sending it to the client
	 * @param obj - ArrayList of each cell in the table
	 * For example, go to controllers.ProductController
	 */
	public static void handleGet(ArrayList<Object> obj) {
		// TODO - implement handleGet
	}

}
