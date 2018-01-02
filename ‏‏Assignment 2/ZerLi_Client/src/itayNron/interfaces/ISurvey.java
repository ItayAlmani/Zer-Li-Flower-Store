package itayNron.interfaces;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import entities.Store;
import entities.Survey;
import entities.Survey.SurveyType;
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
	void getSurveyByStore(Store store) throws IOException;

	void sendSurveys(ArrayList<Survey> surveys);

	/**
	 * 
	 * @param startDate
	 * @param endDate
	 */
	void getSurveyByDates(Date startDate, Date endDate) throws IOException;
	
	public Survey parse(BigInteger surveyID,int[] surveyAnswerers, LocalDate date, Store store, SurveyType type);

}