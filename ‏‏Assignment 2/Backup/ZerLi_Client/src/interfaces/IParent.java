package interfaces;

import java.util.ArrayList;

public interface IParent {
	
	/**
	 * Parsing obj to ArrayList of <code>Product</code> and sending it to the client
	 * @param obj - ArrayList of each cell in the table
	 * For example, go to controllers.ProductController
	 */
	void handleGet(ArrayList<Object> obj);
}
