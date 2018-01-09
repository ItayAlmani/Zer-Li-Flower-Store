package lior;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import common.Context;
import controllers.ParentController;
import entities.Complaint;
import entities.CustomerComplaintsReport;
import entities.SatisfactionReport;
import entities.Survey;
import entities.Survey.SurveyType;
import lior.interfaces.IHistogramOfCustomerComplaintsReportController;

public class HistogramOfCustomerComplaintsReportController extends ParentController implements IHistogramOfCustomerComplaintsReportController {

	/*
	private CustomerComplaintsReport[] cReports;
	private LocalDate rDate, startDate;
	int ind;
	
	@Override
	public void handleGet(ArrayList<Object> obj) {
		// TODO Auto-generated method stub
		
	}
	
	public void initProduceHistogramOfCustomerComplaintsReport(LocalDate date, BigInteger storeID) throws ParseException{
		this.cReports = new CustomerComplaintsReport[2];
		ProduceHistogramOfCustomerComplaintsReport(date, storeID);
	}
	
	public void sendCustomerComplaintsReports(ArrayList<CustomerComplaintsReport> cReports) {
		String methodName = "setCustomerComplaintsReports";
		Method m = null;
		try {
			//a controller asked data, not GUI
			/*if(Context.askingCtrl!=null && Context.askingCtrl.size()!=0) {
				m = Context.askingCtrl.get(0).getClass().getMethod(methodName,ArrayList.class);
				m.invoke(Context.askingCtrl, orders);
				Context.askingCtrl.remove(0);
			}
			else {*/
				m = Context.currentGUI.getClass().getMethod(methodName,ArrayList.class);
				m.invoke(Context.currentGUI, cReports);
				cReports=null;
			//}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			System.err.println("Couldn't invoke method '"+methodName+"'");
			e1.printStackTrace();
		} catch (NoSuchMethodException | SecurityException e2) {
			System.err.println("No method called '"+methodName+"'");
			e2.printStackTrace();
		}

	}
	
	@Override
	public void ProduceHistogramOfCustomerComplaintsReport(LocalDate Reqdate, BigInteger storeID) {
		int ind = 1;
		if(this.cReports[0]==null)
			ind = 0;
		if(this.cReports[0]!=null&&this.cReports[1]!=null)
			ind = 0;
		cReports[ind]=new CustomerComplaintsReport();
		this.cReports[ind].setStoreID(storeID);
		/*for (int i = 0; i < Product.ProductType.values().length; i++) {
			this.sReports[ind].addToCounterPerType(0);
			this.oReports[ind].addToSumPerType(0f);
		}*/
		rDate=Reqdate;
		Calendar c = Calendar.getInstance(); 
		c.setTime(Date.from(rDate.atStartOfDay(ZoneId.systemDefault()).toInstant())); 
		c.add(Calendar.MONTH, -3);
		startDate = c.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		//rDate.setHours(23);
		//rDate.setMinutes(59);
		//rDate.setSeconds(59);
		this.cReports[ind].setStartdate(this.startDate);
		this.cReports[ind].setEnddate(this.rDate);
		try {
			Context.askingCtrl.add(this);
			//TODO Context.fac.survey.getSurveyByDates(this.startDate,this.rDate);
		} catch (IOException e) {
			System.err.println("CustomerComplaintsReportController\n");
			e.printStackTrace();
		}
		
	}
	
	public void setComplaints(ArrayList<Complaint> Complaints) {
		/*int flag=0;
		int ind = 1;
		if(this.cReports[0].getSurveys().size()==0/*||this.sReports[0].getSurveys().size()==null)
			ind = 0;
		if(this.cReports[0].getSurveys().size()!=0&&this.sReports[1].getSurveys().size()!=0)
			ind = 0;
		//oReports[ind]=new OrderReport();
		//this.sReports[ind].setSurveys(Surveys);
		ArrayList<Survey> ar=new ArrayList<>();
		float[] answers = new float[6];
		float totans=0;
		for(int i=0;i<Surveys.size();i++)
		{
			if(Surveys.get(i).getStoreID()==this.sReports[ind].getStoreID()
					&& Surveys.get(i).getType().equals(SurveyType.Answer)
					)
			{
				ar.add(Surveys.get(i));
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
		ArrayList<SatisfactionReport> ar1=new ArrayList<>();
		this.sReports[ind].setSurveys(ar);
		this.sReports[ind].setFinalanswers(answers);
		this.sReports[ind].setAverageTotanswer(totans/6);
		ar1.add(this.sReports[ind]);
		sendSatisfactionReports(ar1);*/
	}

}
