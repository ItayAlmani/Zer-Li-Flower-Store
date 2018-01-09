package lior;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import common.EchoServer;
import entities.SatisfactionReport;
import entities.Survey;
import entities.Survey.SurveyType;

public class SatisfactionReportController {
	private SatisfactionReport sReport;
	private LocalDate rDate, startDate;

	public ArrayList<Object> ProduceSatisfactionReport(ArrayList<Object> arr) throws Exception {
		if(arr!=null && (arr.get(0) instanceof LocalDate == false) ||
				arr.get(1) instanceof LocalDate == false||arr.get(2) instanceof BigInteger == false)
			throw new Exception();
		LocalDate enddate = (LocalDate)arr.get(1);
		LocalDate  startdate= (LocalDate)arr.get(0);
		BigInteger storeID=(BigInteger)arr.get(2);
		//int ind = 1;
		/*if(this.oReports[0]==null)
			ind = 0;
		if(this.oReports[0]!=null&&this.oReports[1]!=null)
			ind = 0;*/
		sReport/*s[ind]*/=new SatisfactionReport();
		this.sReport/*s[ind]*/.setStoreID(storeID);
		rDate=enddate;
		startDate = startdate;
		this.sReport/*s[ind]*/.setStartdate(this.startDate);
		this.sReport/*s[ind]*/.setEnddate(this.rDate);
		ArrayList<Object> obj=new ArrayList<>();
		obj.add(startdate.atStartOfDay());
		obj.add(enddate.atStartOfDay());
		return analyzeSurveys(EchoServer.fac.survey.getSurveyByDates(obj));
	}

	private ArrayList<Object> analyzeSurveys(ArrayList<Object> objs) throws Exception{
		ArrayList<Survey> surveys= new ArrayList<>();
		if(objs == null || objs.isEmpty())
			throw new Exception();
		for (Object o : objs) {
			if(o instanceof Survey)
				surveys.add((Survey)o);
		}
		int flag=0;
		ArrayList<Survey> ar=new ArrayList<>();
		float[] answers = new float[6];
		float totans=0;
		for(int i=0;i<surveys.size();i++)
		{
			if(surveys.get(i).getStoreID().equals(this.sReport.getStoreID())
					&& surveys.get(i).getType().equals(SurveyType.Answer)
					)
			{
				ar.add(surveys.get(i));
			}	
		}
		for(int i=0;i<ar.size();i++)
		{
			for(int j=0;j<6;j++)
			{
				answers[j]+=ar.get(i).getSurveyAnswerers()[j];
			}	
		}
		for(int j=0;j<6;j++)
		{
			answers[j]=answers[j]/ar.size();
			totans+=answers[j];
		}	
		ArrayList<Object> ar1=new ArrayList<>();
		this.sReport.setSurveys(ar);
		this.sReport.setFinalanswers(answers);
		this.sReport.setAverageTotanswer(totans/6);
		if(flag==1) {
			ar1.add(this.sReport/*s[ind]*/);
			return ar1;
		}
		else {
			ar1.add(this.sReport/*s[ind]*/);
			return ar1;
		}
	}
}