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
import entities.SatisfactionReport;
import entities.Survey;
import lior.interfaces.ISatisfactionReportController;

public class SatisfactionReportController extends ParentController implements ISatisfactionReportController {

	private SatisfactionReport[] sReports;
	private LocalDate rDate, startDate;
	int ind;
	
	public void handleGet(ArrayList<Object> obj) {
	}
	
	public void initProduceSatisfactionReport(LocalDate date, BigInteger storeID) throws ParseException{
		this.sReports = new SatisfactionReport[2];
		ProduceSatisfactionReport(date, storeID);
	}
	
	public void sendSatisfactionReports(ArrayList<SatisfactionReport> sReports) {
		String methodName = "setSatisfactionReports";
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
				m.invoke(Context.currentGUI, sReports);
				sReports=null;
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
	public void ProduceSatisfactionReport(LocalDate Reqdate, BigInteger storeID) throws ParseException {
		int ind = 1;
		if(this.sReports[0]==null)
			ind = 0;
		if(this.sReports[0]!=null&&this.sReports[1]!=null)
			ind = 0;
		sReports[ind]=new SatisfactionReport();
		this.sReports[ind].setStoreID(storeID);
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
		this.sReports[ind].setStartdate(this.startDate);
		this.sReports[ind].setEnddate(this.rDate);
		try {
			Context.askingCtrl.add(this);
			Context.fac.survey.getSurveyByDates(this.startDate,this.rDate);
		} catch (IOException e) {
			System.err.println("SatisFactionReportController\n");
			e.printStackTrace();
		}
	}

	public void setOrders(ArrayList<Survey> Surveys) {
		int flag=0;
		int ind = 1;
		if(this.sReports[0].getSurveys().size()==0)
			ind = 0;
		if(this.sReports[0].getSurveys().size()!=0&&this.sReports[1].getSurveys().size()!=0)
			ind = 0;
		//oReports[ind]=new OrderReport();
		//this.sReports[ind].setSurveys(Surveys);
		ArrayList<Survey> ar=new ArrayList<>();
		for(int i=0;i<Surveys.size();i++)
		{
			if(Surveys.get(i).getStoreID()==this.sReports[ind].getStoreID()
					/*&& orders.get(i).getOrderStatus().equals(OrderStatus.Paid)*/
					)
			{
				ar.add(Surveys.get(i));
			}	
		}
		ArrayList<SatisfactionReport> ar1=new ArrayList<>();
		this.sReports[ind].setSurveys(ar);
		ar1.add(this.sReports[ind]);
		sendSatisfactionReports(ar1);
	}
}