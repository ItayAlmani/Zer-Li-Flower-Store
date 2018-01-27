package quarterlyReports;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;

import common.CSMessage;
import common.Context;
import common.ParentController;
import common.CSMessage.MessageType;
import common.gui.controllers.ParentGUIController;
import orderNproducts.entities.Store;
import quarterlyReports.entities.HistogramOfCustomerComplaintsReport;
import quarterlyReports.interfaces.IHistogramOfCustomerComplaintsReportController;

public class HistogramOfCustomerComplaintsReportController extends ParentController implements IHistogramOfCustomerComplaintsReportController{

	public void initproduceHistogramOfCustomerComplaintsReport(LocalDate date, Store store) throws IOException{
		getHistogramOfCustomerComplaintsReport(date, store);
	}
	
	public void handleGet(ArrayList<HistogramOfCustomerComplaintsReport> ccReports) {
		String methodName = "setHistogramOfCustomerComplaintsReports";
		Method m = null;
		try {
				m = ParentGUIController.currentGUI.getClass().getMethod(methodName,ArrayList.class);
				m.invoke(ParentGUIController.currentGUI, ccReports);
				ccReports=null;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			System.err.println("Couldn't invoke method '"+methodName+"'");
			e1.printStackTrace();
		} catch (NoSuchMethodException | SecurityException e2) {
			System.err.println("No method called '"+methodName+"'");
			e2.printStackTrace();
		}

	}
	
	@Override
	public void getHistogramOfCustomerComplaintsReport(LocalDate Reqdate, Store store) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		ArrayList<Object> arr = new ArrayList<>();
		arr.add(Reqdate);
		arr.add(store);
		myMsgArr.add(arr);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT, myMsgArr, HistogramOfCustomerComplaintsReport.class));
	}

}
