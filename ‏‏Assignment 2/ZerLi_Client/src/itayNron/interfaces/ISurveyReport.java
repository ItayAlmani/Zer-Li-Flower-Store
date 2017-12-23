package itayNron.interfaces;

import java.util.ArrayList;

import entities.SurveyReport;
import interfaces.IParent;

public interface ISurveyReport extends IParent {

	/**
	 * 
	 * @param surveyReport
	 */
	void addSurveyReport(SurveyReport surveyReport);

	/**
	 * 
	 * @param storeid
	 */
	void getSurveyReportsByStore(int storeid);

	void sendSurveyReports(ArrayList<SurveyReport> surveyReports);

}