package itayNron.interfaces;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
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
	void getSurveyByStore(Store store) throws IOException;

	void sendSurveys(ArrayList<Survey> surveys);

	/**
	 * 
	 * @param startDate
	 * @param endDate
	 */
	void getSurveyByDates(LocalDate startDate, LocalDate endDate) throws IOException;
	
	public Survey parse(BigInteger surveyID,BigInteger storeID, float answer1,float answer2,float answer3,float answer4,float answer5,float answer6, Timestamp date, String type);

}