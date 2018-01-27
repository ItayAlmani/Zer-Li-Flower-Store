package customersSatisfaction.interfaces;

import java.math.BigInteger;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import customersSatisfaction.entities.Survey;

public interface ISurvey {

	ArrayList<Object> add(ArrayList<Object> arr) throws Exception;
	ArrayList<Object> update(Object obj) throws Exception;
	/**
	 * <p> A function that get objects from the DB and calls {@link parse}.
	 *  after parse it pushes the data back as a report </p>
	 * @param obj - an array list of objects the represent row in survey table in DB.
	 * @return  an ArrayList of Objects with all the information that related to the asking report
SS	 * @throws Exception This function uses {@link parse} that can throw an exception
	 */
	ArrayList<Object> handleGet(ArrayList<Object> obj);
	/**
	 * <p>
	 * Function to get surveys by specific store
	 * </p>
	 * @param storeID - the store we want to get the surveys from
	 * @param stratDate - start date range 
	 * @param endDate - end date range
	 * @return generic object arrayList which become survey arrayList
	 * @throws SQLException Context.clientConsole.handleMessageFromClientUI throws SQLException
	 */
	ArrayList<Object> getSurveyByDatesAndStore(BigInteger storeID, LocalDateTime startDate, LocalDateTime endDate)
			throws SQLException;

	/**
	 * <p>
	 * Function to get survey from all stores by range of dates
	 * </p>
	 * @param arr - start and end date of date range which we want survey from
	 * @return generic object arrayList which will become survey arrayList
	 * @throws SQLException Context.clientConsole.handleMessageFromClientUI throws SQLException
	 */
	ArrayList<Object> getSurveyByDates(ArrayList<Object> arr) throws SQLException;

	/**
	 * Function to create new survey object with data from DB
	 * @param surveyID - parameter for surveyID field in object
	 * @param storeID - parameter for storeID field in object
	 * @param answer1 - parameter for answer1 field in object
	 * @param answer2 - parameter for ansewr2 field in object
	 * @param answer3 - parameter for answer3 field in object
	 * @param answer4 - parameter for answer4 field in object
	 * @param answer5 - parameter for answer5 field in object
	 * @param answer6 - parameter for answer6 field in object
	 * @param date - parameter for date field in object
	 * @param type - parameter for type field in object
	 * @return new created survey object with data from DB
	 */
	Survey parse(BigInteger surveyID, BigInteger storeID, float answer1, float answer2, float answer3, float answer4,
			float answer5, float answer6, Timestamp date, String type);

}