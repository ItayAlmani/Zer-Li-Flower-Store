package customersSatisfaction.interfaces;

import java.io.IOException;
import java.time.LocalDate;

import customersSatisfaction.entities.Survey;

public interface ISurvey{

	/**
	 * <p>
	 * Function to add new survey to DB
	 * </p>
	 * @param survey - new survey to add to DB
	 * @throws IOException 
	 */
	void add(Survey survey) throws IOException;

/**
 * <p>
 * Function to get all surveys from a specific range of dates
 * </p>
 * @param startDate - start date of date range
 * @param endDate - end date of date range
 * @throws IOException 
 */
	void getSurveyByDates(LocalDate startDate, LocalDate endDate) throws IOException;

}