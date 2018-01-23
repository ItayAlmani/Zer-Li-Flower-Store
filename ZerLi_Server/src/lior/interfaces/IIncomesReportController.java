package lior.interfaces;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;

public interface IIncomesReportController {
	/**
	 * <p> This function receives a request from the client to receive all related data.
	 * In this case all orders data.
	 * Then calculates the number of orders of this store and sums the prices of all orders of this.
</p>
	 * @param arr - the arguments that send from the client which we used to make the query from the DB
	 *arr is arraylist because we can send only one argument so all the requested arguments
	 *are collected into this arraylist.
	 * @return The returned value is an arraylist of object that the client parsing into the requested
	 * type of data.
	 * @throws Exception -because {@link ProduceIncomesReport} calls to
	 * {@link analyzeOrders}
	 */
	ArrayList<Object> getIncomesReport(ArrayList<Object> arr) throws Exception;
	/**
	 * <p> This function takes the information from the server and processes it into a report
	 *  with all the data associated with it, and returns the report to the client.
	 *  In addition, this function calls {@link setPIOsInOrder} to attach to each order the products linked
	 *   to it.</p>
	 * @param objs - ArrayList of orders of the requested store.
	 * @return ArrayList of IncomesReport (object that the client controller parsing)
	 * @throws Exception - if there is no data that returns from the server (DB)
	 */
	ArrayList<Object> analyzeOrders(ArrayList<Object> objs) throws Exception;
	/**
	 * <p>This function links to each order the products within it.</p>
	 * @param objs - ArrayList of products of the requested order.
	 * @throws Exception - if there is no data that returns from the server (DB)
	 */
	void setPIOsInOrder(ArrayList<Object> objs) throws Exception;
	
	
	
	ArrayList<Object> update(Object obj) throws Exception;
	ArrayList<Object> ProduceIncomesReport(LocalDate date, BigInteger storeID) throws Exception;
	ArrayList<Object> handleGet(ArrayList<Object> obj);
	ArrayList<Object> add(ArrayList<Object> arr) throws Exception;

}