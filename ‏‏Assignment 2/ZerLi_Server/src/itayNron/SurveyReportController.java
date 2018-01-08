package itayNron;

import java.math.BigInteger;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import common.EchoServer;
import controllers.ParentController;
import entities.Survey;
import entities.Survey.SurveyType;
import entities.SurveyReport;

public class SurveyReportController extends ParentController
{
	@Override
	public ArrayList<Object> add(ArrayList<Object> arr) throws Exception
	{
		if(arr!=null && (arr.get(0) instanceof SurveyReport == false) || arr.get(1) instanceof Boolean == false)
			throw new Exception();
		SurveyReport sr = (SurveyReport)arr.get(0);
		boolean isReturnNextID = (boolean)arr.get(1);
		if(surveyExists(sr)==false)
		try {
			sr.getSurveyAnalyzes().setSurveyID(addSurveyWithSurveyReport(sr.getSurveyAnalyzes()));
			} catch (Exception e) {
				System.err.println("Error with get id in survey report process");
			e.printStackTrace();
			}
		insertSurveyReport(sr);
		myMsgArr.clear();
		if(isReturnNextID)
			myMsgArr.add(getNextID());
		else
			myMsgArr.add(true);
		return myMsgArr;
	}
	
	public void insertSurveyReport(SurveyReport sr) throws Exception {
		String query = "INSERT INTO surveyreport (surveyID, verbalReport,startDate,endDate)" 
		+ " VALUES ('" + sr.getSurveyAnalyzes().getSurveyID()  
		+ "','"+ sr.getVerbalReport()+ "','"+ sr.getStartDate()+ "','"+ sr.getEndDate()+"')";
		EchoServer.fac.dataBase.db.updateQuery(query);		
	}
	
	private BigInteger getNextID() throws Exception {
		String query="SELECT Max(surveyreportID) from surveyreport";
		myMsgArr =  EchoServer.fac.dataBase.db.getQuery(query);
		if(myMsgArr!=null && myMsgArr.size()==1 && myMsgArr.get(0) instanceof Integer)
			return BigInteger.valueOf((Integer)myMsgArr.get(0));
		throw new Exception("Error SurveyReport add - no id");
	}
	
	private BigInteger addSurveyWithSurveyReport(Survey survey) throws Exception {
		try {
			myMsgArr.clear();
			myMsgArr.add(survey);
			myMsgArr.add(true);
			myMsgArr = EchoServer.fac.survey.add(myMsgArr);
			if(myMsgArr.get(0) instanceof BigInteger)
				return (BigInteger)myMsgArr.get(0);
			throw new Exception("addSurveyWithSurveyReport() error\n");
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}
	
	private boolean surveyExists(SurveyReport sr) throws Exception {
		String query= "SELECT * FROM surveyreport WHERE startDate='"+sr.getStartDate()+"'"+ "AND"
				+ " endDate='"+sr.getEndDate()+"'";
		ArrayList<Object> arr =  EchoServer.fac.dataBase.db.getQuery(query);
		if(arr!=null && arr.size()!=0)
			return true;
		return false;
		
	}

	@Override
	public ArrayList<Object> handleGet(ArrayList<Object> obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Object> update(Object obj) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ArrayList<Object> analyzeSurveys(ArrayList<Object> obj) throws SQLException{
		ArrayList<Object> arr = EchoServer.fac.survey.getSurveyByDates(obj);
		if(arr==null || arr.isEmpty())
			return arr;
		float [] totalSumOfAnswers = new float[6];
		for (Object o : arr) 
		{
			Survey survey = (Survey)o;
			float[] surveyAnswers =  survey.getSurveyAnswerers();
			for (int i=0;i<surveyAnswers.length;i++)
				totalSumOfAnswers[i]+=surveyAnswers[i];
		}
		for (int i=0;i<totalSumOfAnswers.length;i++)
			totalSumOfAnswers[i]=totalSumOfAnswers[i]/(float)arr.size();
		BigInteger storeID = ((Survey)arr.get(0)).getStoreID();
		arr = new ArrayList<>();
		arr.add(new SurveyReport(new Survey(totalSumOfAnswers, LocalDate.now(), storeID,SurveyType.Analyzes)));
		return arr;
	}
}