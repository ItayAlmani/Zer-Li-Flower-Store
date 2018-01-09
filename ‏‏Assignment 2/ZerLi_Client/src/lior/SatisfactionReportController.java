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
import entities.CSMessage;
import entities.SatisfactionReport;
import entities.Survey;
import entities.CSMessage.MessageType;
import entities.Survey.SurveyType;
import gui.controllers.ParentGUIController;
import lior.interfaces.ISatisfactionReportController;

public class SatisfactionReportController extends ParentController implements ISatisfactionReportController {
	
	
	public void initProduceSatisfactionReport(LocalDate date, BigInteger storeID) throws IOException{
		//this.sReports = new SatisfactionReport[2];
		ProduceSatisfactionReport(date, storeID);
	}
	
	public void handleGet(ArrayList<SatisfactionReport> sReports) {
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
				m = ParentGUIController.currentGUI.getClass().getMethod(methodName,ArrayList.class);
				m.invoke(ParentGUIController.currentGUI, sReports);
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
	public void ProduceSatisfactionReport(LocalDate Reqdate, BigInteger storeID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		ArrayList<Object> arr = new ArrayList<>();
		Calendar c = Calendar.getInstance(); 
		c.setTime(Date.from(Reqdate.atStartOfDay(ZoneId.systemDefault()).toInstant())); 
		c.add(Calendar.MONTH, -3);
		arr.add(c.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		arr.add(Reqdate);
		arr.add(storeID);
		myMsgArr.add(arr);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT, myMsgArr, SatisfactionReport.class));
	}
}