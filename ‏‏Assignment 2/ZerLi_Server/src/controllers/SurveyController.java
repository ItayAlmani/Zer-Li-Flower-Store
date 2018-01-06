package controllers;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;

import common.ServerController;
import entities.Survey;

public class SurveyController 
{
	public static BigInteger addSurvey(Survey survey) throws Exception {
		String query = "INSERT INTO survey(storeID, answer1, answer2, answer3, answer4, answer5, answer6, date, type) " + 
				" VALUES ('" + survey.getStoreID().toString()+"',";
		for (Float ans : survey.getSurveyAnswerers())
			query+="'"+ans+"', ";
		query+="'"+java.sql.Date.valueOf(survey.getDate())+"','"+
			survey.getType().toString()+"')";
		ServerController.db.updateQuery(query);
		query = "SELECT Max(surveyID) from survey;";
		ArrayList<Object> arr =  ServerController.db.getQuery(query);
		if(arr!=null && arr.size()==1 && arr.get(0) instanceof Integer)
			return BigInteger.valueOf((Integer)arr.get(0));
		throw new Exception();
		
	}
}
