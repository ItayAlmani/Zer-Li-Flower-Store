package interfaces;

import java.util.ArrayList;

import entities.DeliveryDetails;

public interface IPickup {

	void insertPickupToDB(DeliveryDetails deliveryDetails);

	/**
	 * Parsing obj to ArrayList of <code>Product</code> and sending it to the client
	 * @param obj - ArrayList of each cell in the table
	 * For example, go to controllers.ProductController
	 */
	public static void handleGet(ArrayList<Object> obj) {
		// TODO - implement handleGet
	}
}