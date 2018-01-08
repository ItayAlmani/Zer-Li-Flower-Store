package controllers;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import common.EchoServer;
import common.ServerController;
import entities.Order;
import entities.ShipmentDetails;
import entities.SurveyReport;
import entities.Order.DeliveryType;

public class SurveyReportController 
{
	public static BigInteger addSurveyReport(SurveyReport sr) throws Exception 
	{
		if(surveyExists(sr)==false)
		try {
			BigInteger id = SurveyController.addSurvey(sr.getSurveyAnalyzes());
			sr.getSurveyAnalyzes().setSurveyID(id);
			} catch (Exception e) {
				System.err.println("Error with get id in survey report process");
			e.printStackTrace();
			}
			return insertSurveyReport(sr);
	}
	
	public static BigInteger insertSurveyReport(SurveyReport sr) throws Exception {
		String query = "INSERT INTO surveyreport (surveyID, verbalReport,startDate,endDate)" 
		+ " VALUES ('" + sr.getSurveyAnalyzes().getSurveyID()  
		+ "','"+ sr.getVerbalReport()+ "','"+ sr.getStartDate()+ "','"+ sr.getEndDate()+"')";
		EchoServer.fac.dataBase.db.updateQuery(query);
		query="SELECT Max(surveyreportID) from surveyreport";
		ArrayList<Object> arr =  EchoServer.fac.dataBase.db.getQuery(query);
		if(arr!=null && arr.size()==1 && arr.get(0) instanceof Integer)
			return BigInteger.valueOf((Integer)arr.get(0)+1);
		throw new Exception();
		
	}
	
	private static boolean surveyExists(SurveyReport sr) throws Exception
	{
		String query= "SELECT * FROM surveyreport WHERE startDate='"+sr.getStartDate()+"'"+ "AND"
				+ " endDate='"+sr.getEndDate()+"'";
		ArrayList<Object> arr =  EchoServer.fac.dataBase.db.getQuery(query);
		if(arr!=null && arr.size()!=0)
			return true;
		return false;
		
	}
	
}
	

