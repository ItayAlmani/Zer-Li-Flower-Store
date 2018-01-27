package quarterlyReports.interfaces;

import java.time.LocalDate;
import java.util.ArrayList;

import orderNproducts.entities.Store;

public interface IHistogramOfCustomerCompaintsReportController {
	
	/**
	 * <p> This function takes the information from the server and processes it into a report
	 *  with all the data associated with it, and returns the report to the client.</p>
	 * @param objs - ArrayList of complaints of the requested store.
	 * @return ArrayList of HistogramOfCustomerComplaintsReport (object that the client controller parsing)
	 * @throws Exception - if there is no data that returns from the server (DB)
	 */
	ArrayList<Object> analyzeComplaints(ArrayList<Object> objs) throws Exception;
	/**
	 * <p>A function that performs a query to check whether the requested report exists in the database.
	 *	If it does return the above report another produces it (if it is in the correct date range) </p>
	 * @param arr - the arguments that send from the client which we used to make the query from the DB
	 * 				arr is arraylist because we can send only one argument so all the requested arguments
	 * 				are collected into this arraylist.
	 * @return The returned value is an arraylist of object that the client parsing into the requested
	 * type of data.
	 * @throws Exception -if somthing went wrong with the ask from the DB
	 */
	ArrayList<Object> getHistogramOfCustomerComplaintsReport(ArrayList<Object> arr) throws Exception;
	/**
	 * <p> This function receives details if the report does not exist and produces it by receiving the
	 * relevant data from the DB in this case all complaints data.
	 * Then calculates the number of treated complaints,
	   number of untreated reports,
	   and number of complaints being refunded.</p>
	 * @param date - the end of the requested quarter date
	 * @param store - the store we want to check
	 * @return The returned value is an ArrayList of object that the client parsing into the requested
	 * type of data. 
	 * @throws Exception -because this function calls to
	 * {@link analyzeComplaints}
	 */
	ArrayList<Object> produceHistogramOfCustomerComplaintsReport(LocalDate date, Store store) throws Exception;
	/**
	 * <p> A function that get objects from the DB and calls parse after parse it pushes
	 *  the data back as a report </p>
	 * @param obj - an array list of objects the represent row in histogramreport table in DB
	 * @return an ArrayList of Objects with all the information that related to the asking report
	 */
	ArrayList<Object> handleGet(ArrayList<Object> obj);
	/**
	 * <p>A function that inserts a row into a histogramreport table in DB</p>
	 * @param arr-ArrayList of Object that represents the data that we want to insert as row in DB
	 * @return always NULL
	 * @throws Exception- {@link insertWithBatch} Contacting the DB and can throw EXCEPTION at any time if the query fails
	 */
	ArrayList<Object> add(ArrayList<Object> arr) throws Exception;
}