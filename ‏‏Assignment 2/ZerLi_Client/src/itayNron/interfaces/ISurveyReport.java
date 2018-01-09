package itayNron.interfaces;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import entities.Store;
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

	
	void analyzeSurveys(LocalDateTime start, LocalDateTime end) throws IOException;
}