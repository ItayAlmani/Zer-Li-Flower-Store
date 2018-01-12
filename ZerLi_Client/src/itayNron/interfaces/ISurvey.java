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

public interface ISurvey{

	/**
	 * 
	 * @param survey
	 */
	void add(Survey survey) throws IOException;

	/**
	 * 
	 * @param store
	 */
	void getSurveyByStore(Store store) throws IOException;

	/**
	 * 
	 * @param startDate
	 * @param endDate
	 */
	void getSurveyByDates(LocalDate startDate, LocalDate endDate) throws IOException;

}