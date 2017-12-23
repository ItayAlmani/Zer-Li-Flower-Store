package interfaces;

import java.util.ArrayList;
import java.util.Date;

public interface IHistogramOfCustomerComplaintsReportController {

	/**
	 * 
	 * @param Reqdate
	 * @param storeID
	 */
	HistogramOfCustomerComplaintsReport ProduceHistogramOfCustomerComplaintsReport(Date Reqdate, String storeID);

	
	/**
	 * Parsing obj to ArrayList of <code>Product</code> and sending it to the client
	 * @param obj - ArrayList of each cell in the table
	 * For example, go to controllers.ProductController
	 */
	public static void handleGet(ArrayList<Object> obj) {
		// TODO - implement handleGet
	}
}