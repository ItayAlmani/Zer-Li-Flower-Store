package lior.interfaces;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;

public interface ISatisfactionReportController {
	/**
	 * <p> This function receives a request from the client to receive all related data.
	 * In this case all surveys data.
	 * Then calculates the average scores that customers have given the store and returns them.
</p>
	 * @param arr - the arguments that send from the client which we used to make the query from the DB
	 *arr is arraylist because we can send only one argument so all the requested arguments
	 *are collected into this arraylist.
	 * @return The returned value is an arraylist of object that the client parsing into the requested
	 * type of data.
	 * @throws Exception -because {@link ProduceSatisfactionReport} calls to
	 * {@link analyzeSurveys}
	 */
	ArrayList<Object> ProduceSatisfactionReport(LocalDate date, BigInteger storeID) throws Exception;
	/**
	 * <p> This function takes the information from the server and processes it into a report
	 *  with all the data associated with it, and returns the report to the client.
	 *  </p>
	 * @param objs - ArrayList of surveys of the requested store.
	 * @return ArrayList of SatisfactionReport (object that the client controller parsing)
	 * @throws Exception - if there is no data that returns from the server (DB)
	 */
	ArrayList<Object> analyzeSurveys(ArrayList<Object> objs) throws Exception;
	
	ArrayList<Object> getSatisfactionReport(ArrayList<Object> arr) throws Exception;
	ArrayList<Object> update(Object obj) throws Exception;
	ArrayList<Object> add(ArrayList<Object> arr) throws Exception;
	ArrayList<Object> handleGet(ArrayList<Object> obj);

}