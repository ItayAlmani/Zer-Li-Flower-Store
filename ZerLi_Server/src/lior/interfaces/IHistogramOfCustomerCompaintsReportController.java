package lior.interfaces;

import java.util.ArrayList;

public interface IHistogramOfCustomerCompaintsReportController {
	
	/**
	 * <p> This function receives a request from the client to receive all related data.
	 * In this case all complaints data.
	 * Then calculates the number of treated complaints,
	   number of untreated reports,
	   and number of complaints being refunded.</p>
	 * @param arr - the arguments that send from the client which we used to make the query from the DB
	 * 				arr is arraylist because we can send only one argument so all the requested arguments
	 * 				are collected into this arraylist.
	 * @return The returned value is an arraylist of object that the client parsing into the requested
	 * type of data.
	 * @throws Exception -because {@link produceHistogramOfCustomerComplaintsReport} calls to
	 * {@link analyzeComplaints}
	 */
	ArrayList<Object> produceHistogramOfCustomerComplaintsReport(ArrayList<Object> arr) throws Exception;
	/**
	 * <p> This function takes the information from the server and processes it into a report
	 *  with all the data associated with it, and returns the report to the client.</p>
	 * @param objs - ArrayList of complaints of the requested store.
	 * @return ArrayList of HistogramOfCustomerComplaintsReport (object that the client controller parsing)
	 * @throws Exception - if there is no data that returns from the server (DB)
	 */
	ArrayList<Object> analyzeComplaints(ArrayList<Object> objs) throws Exception;

}