package itayNron.interfaces;

import java.util.ArrayList;
import java.util.Date;

import entities.Store;
import entities.Survey;
import interfaces.IParent;

public interface ISurvey extends IParent {

	/**
	 * 
	 * @param survey
	 */
	void addSurvey(Survey survey);

	/**
	 * 
	 * @param store
	 */
	void getSurveyByStore(Store store);

	void sendSurvys(ArrayList<Survey> survys);

	/**
	 * 
	 * @param startDate
	 * @param endDate
	 */
	void getSurveyByDates(Date startDate, Date endDate);

}