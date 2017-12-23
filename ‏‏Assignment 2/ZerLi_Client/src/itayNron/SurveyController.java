package itayNron;

import java.util.ArrayList;
import java.util.Date;

import entities.Store;
import entities.Survey;

public class SurveyController {

	/**
	 * 
	 * @param survey
	 */
	public String createNewSurveyToDB(Survey survey) {
		// TODO - implement SurveyController.createNewSurveyToDB
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param store
	 */
	public Survey[] getSurveyByStore(Store store) {
		// TODO - implement SurveyController.getSurveyByStore
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param startDate
	 * @param endDate
	 */
	public Survey analyzeReportByDates(Date startDate, Date endDate) {
		// TODO - implement SurveyController.analyzeReportByDates
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