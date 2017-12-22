package interfaces;

import java.util.ArrayList;

import entities.Item;

public interface IBrowsingProcess {

	/**
	 * 
	 * @param cartID
	 */
	ArrayList<Item> getItemsInCart(String cartID);

	/**
	 * 
	 * @param itemID
	 */
	Item getItemDetails(String itemID);

	void updateFinalPrice();

	/**
	 * 
	 * @param item
	 */
	boolean addItemToCart(Item item);

}
