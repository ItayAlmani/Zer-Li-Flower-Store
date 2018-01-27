package customersSatisfaction.interfaces;

import java.io.IOException;
import java.time.LocalDateTime;

import customersSatisfaction.entities.SurveyReport;

public interface ISurveyReport {

/**
 * <p>
 * Function to add new surveyReport to DB
 * </p>
 * @param surveyReport - surveyReport object to be added
 * @param getCurrentID - boolean object to decide correct ID in server
 * @throws IOException 
 */
	void add(SurveyReport surveyReport, boolean getCurrentID) throws IOException;
/**
 * <p>
 * Function to analyze surveys in specific range of dates
 * @param start - start date of range dates
 * @param end - end date of ranges dates
 * @throws IOException 
 */
	
	void analyzeSurveys(LocalDateTime start, LocalDateTime end) throws IOException;
}