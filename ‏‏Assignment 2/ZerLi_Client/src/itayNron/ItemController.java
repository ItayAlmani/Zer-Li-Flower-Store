package itayNron;

import java.util.ArrayList;

import entities.Item;
import enums.Color;
import enums.ItemType;

public class ItemController {

	/**
	 * 
	 * @param itemID
	 */
	public Item getItem(int itemID) {
		// TODO - implement ItemController.getItem
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param type
	 * @param priceStart
	 * @param priceEnd
	 */
	public void createNewItem(ItemType type, float priceStart, float priceEnd) {
		// TODO - implement ItemController.createNewItem
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param type
	 * @param priceStart
	 * @param priceEnd
	 * @param color
	 */
	public void createNewItem(ItemType type, float priceStart, float priceEnd, Color color) {
		// TODO - implement ItemController.createNewItem
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param type
	 * @param priceStart
	 * @param priceEnd
	 * @param color
	 */
	public String[] assembleItemFromDB(ItemType type, float priceStart, float priceEnd, Color color) {
		// TODO - implement ItemController.assembleItemFromDB
		throw new UnsupportedOperationException();
	}

	/**
	 * Parsing obj to ArrayList of <code>Product</code> and sending it to the client
	 * @param obj - ArrayList of each cell in the table
	 * For example, go to controllers.ProductController
	 */
	public static void handleGet(ArrayList<Object> obj) {
		// TODO - implement handleGet
	}
}