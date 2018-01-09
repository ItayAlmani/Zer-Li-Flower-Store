package itayNron.interfaces;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import entities.Survey;
import entities.SurveyReport;
import interfaces.IParent;

public interface ISurveyReport {

	/**
	 * 
	 * @param surveyReport
	 * @param getCurrentID TODO
	 */
	void add(SurveyReport surveyReport, boolean getCurrentID) throws IOException;

	/**
	 * 
	 * @param storeid
	 */
	void getSurveyReportsByStore(int storeid);
	
	
	void analyzeSurveys(LocalDate start, LocalDate end) throws IOException;
}