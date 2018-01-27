package quarterlyReports;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;

import common.CSMessage;
import common.Context;
import common.ParentController;
import common.CSMessage.MessageType;
import common.gui.controllers.ParentGUIController;
import quarterlyReports.entities.OrderReport;
import quarterlyReports.interfaces.IOrderReportController;

public class OrderReportController extends ParentController implements IOrderReportController {
	
	public void initproduceOrderReport(LocalDate date, BigInteger storeID) throws IOException{
		getOrderReport(date, storeID);
	}
	
	public void handleGet(ArrayList<OrderReport> oReports) {
		String methodName = "setOrderReports";
		Method m = null;
		try {
				m = ParentGUIController.currentGUI.getClass().getMethod(methodName,ArrayList.class);
				m.invoke(ParentGUIController.currentGUI, oReports);
				oReports=null;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			System.err.println("Couldn't invoke method '"+methodName+"'");
			e1.printStackTrace();
		} catch (NoSuchMethodException | SecurityException e2) {
			System.err.println("No method called '"+methodName+"'");
			e2.printStackTrace();
		}

	}

	@Override
	public void getOrderReport(LocalDate date, BigInteger storeID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		ArrayList<Object> arr = new ArrayList<>();
		arr.add(date);
		arr.add(storeID);
		myMsgArr.add(arr);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT, myMsgArr, OrderReport.class));
	}
}