package izhar;

import java.util.ArrayList;

import entities.Item;
import interfaces.IBrowsingProcess;

public class BrowsingProcessHandler implements IBrowsingProcess{

	@Override
	public ArrayList<Item> getItemsInCart(int cartID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Item getItemDetails(int itemID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateFinalPrice() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean addItemToCart(Item item) {
		// TODO Auto-generated method stub
		return false;
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