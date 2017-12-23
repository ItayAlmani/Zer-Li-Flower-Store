package interfaces;

import java.util.ArrayList;
import java.util.Date;

import entities.IncomesReport;

public interface IIncomesReportController {

	/**
	 * 
	 * @param Reqdate
	 * @param storeID
	 */
	IncomesReport ProduceIncomesReport(Date Reqdate, String storeID);

	
	/**
	 * Parsing obj to ArrayList of <code>Product</code> and sending it to the client
	 * @param obj - ArrayList of each cell in the table
	 * For example, go to controllers.ProductController
	 */
	public static void handleGet(ArrayList<Object> obj) {
		// TODO - implement handleGet
	}
}