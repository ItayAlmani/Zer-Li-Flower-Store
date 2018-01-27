package customersSatisfaction.interfaces;

import java.sql.SQLException;
import java.util.ArrayList;

import customersSatisfaction.entities.SurveyReport;

public interface ISurveyReport {

	ArrayList<Object> add(ArrayList<Object> arr) throws Exception;
	ArrayList<Object> update(Object obj) throws Exception;
	/**
	 * <p>
	 * Function to insert surveyReport with data from DB and client side
	 * </p>
	 * @param sr - surveyReport object data which be inserted to DB
	 * @throws Exception Context.clientConsole.handleMessageFromClientUI throws Exception
	 */
	void insertSurveyReport(SurveyReport sr) throws Exception;
	/**
	 * <p> A function that get objects from the DB and calls {@link parse}.
	 *  after parse it pushes the data back as a report </p>
	 * @param obj - an array list of objects the represent row in surveyreport table in DB.
	 * @return  an ArrayList of Objects with all the information that related to the asking report
	 * @throws Exception This function uses {@link parse} that can throw an exception
	 */
	ArrayList<Object> handleGet(ArrayList<Object> obj);
	/**
	 * <p>
	 * Function to create survey object of type analyzes. we want to analyze surveys by range of dates<br>
	 * to be seen as table with all survey answers of the requested range of dates.
	 * </p>
	 * @param obj - survey objects to be analyzed by date
	 * @return - generic object arrayList which will become survey object in type analyze.
	 * @throws SQLException Context.clientConsole.handleMessageFromClientUI throws SQLException.
	 */
	ArrayList<Object> analyzeSurveys(ArrayList<Object> obj) throws SQLException;

}