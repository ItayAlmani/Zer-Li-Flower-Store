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
import entities.IncomesReport;
import entities.Order;
import entities.OrderReport;
import entities.ProductInOrder;
import entities.CSMessage.MessageType;
import gui.controllers.ParentGUIController;
import lior.interfaces.IIncomesReportController;

public class IncomesReportController extends ParentController implements IIncomesReportController {
	
	public void initProduceIncomesReport(LocalDate date, BigInteger storeID) throws IOException
	{
		//this.iReport = new IncomesReport[2];
		ProduceIncomesReport(date, storeID);
	}
	
	public void handleGet(ArrayList<IncomesReport> iReports) {
		String methodName = "setIncomeReports";
		Method m = null;
		try {
			//a controller asked data, not GUI
			/*if(Context.askingCtrl!=null && Context.askingCtrl.size()!=0) {
				m = Context.askingCtrl.get(0).getClass().getMethod(methodName,ArrayList.class);
				Object obj = Context.askingCtrl.get(0);
				Context.askingCtrl.remove(0);
				m.invoke(obj, iReports);
			}
			else {*/
				m = ParentGUIController.currentGUI.getClass().getMethod(methodName,ArrayList.class);
				m.invoke(ParentGUIController.currentGUI, iReports);
			//}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			System.err.println("Couldn't invoke method '"+methodName+"'");
			e1.printStackTrace();
		} catch (NoSuchMethodException | SecurityException e2) {
			System.err.println("No method called '"+methodName+"'");
			e2.printStackTrace();
		}

	}

	
	public void ProduceIncomesReport(LocalDate date, BigInteger storeID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		ArrayList<Object> arr = new ArrayList<>();
		arr.add(date);
		arr.add(storeID);
		myMsgArr.add(arr);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT, myMsgArr, IncomesReport.class));
	}

}