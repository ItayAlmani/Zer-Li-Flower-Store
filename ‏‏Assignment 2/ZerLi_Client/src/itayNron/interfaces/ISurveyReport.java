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
	void getSurveyReportsByStore(BigInteger storeID) throws IOException;
	
	 SurveyReport parse (BigInteger id,Survey surveyAnalyzes,String verbalReport,LocalDateTime startDate,LocalDateTime endDate);
	 void handleGet(ArrayList<Object> obj);
	

	void sendSurveyReports(ArrayList<SurveyReport> surveyReports);
	
	void getSurveysForAnalays(LocalDate start, LocalDate end) throws IOException;
}