package itayNron.interfaces;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import entities.Survey;
import entities.SurveyReport;
import interfaces.IParent;

public interface ISurveyReport extends IParent {

	/**
	 * 
	 * @param surveyReport
	 */
	void addSurveyReport(SurveyReport surveyReport) throws IOException;

	/**
	 * 
	 * @param storeid
	 */
	void getSurveyReportsByStore(int storeid);
	
	

	void sendSurveyReports(ArrayList<SurveyReport> surveyReports);
	
	void getSurveysForAnalays(LocalDate start, LocalDate end) throws IOException;
}