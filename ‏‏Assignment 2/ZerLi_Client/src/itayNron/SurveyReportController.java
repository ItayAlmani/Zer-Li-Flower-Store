package itayNron;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import common.Context;
import controllers.ParentController;
import entities.CSMessage;
import entities.CSMessage.MessageType;
import entities.SurveyReport;
import gui.controllers.ParentGUIController;
import itayNron.interfaces.ISurveyReport;

public class SurveyReportController extends ParentController implements ISurveyReport {

	@Override
	public void add(SurveyReport sr, boolean getCurrentID) throws IOException {
		myMsgArr.clear();
		//adds this method's name - in this case = "add"
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		ArrayList<Object> arr = new ArrayList<>();
		arr.add(sr);
		arr.add(getCurrentID);
		myMsgArr.add(arr);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.INSERT, myMsgArr,SurveyReport.class));
	}

	public void getSurveyReportsByStore(int storeid) {
		
	}
	
	public void handleGet(ArrayList<SurveyReport> surveys) {
		String methodName = "setSurveyReports";
		Method m = null;
		try {
			//a controller asked data, not GUI
			if(Context.askingCtrl!=null && Context.askingCtrl.size()!=0) {
				m = Context.askingCtrl.get(0).getClass().getMethod(methodName,ArrayList.class);
				Object obj = Context.askingCtrl.get(0);
				Context.askingCtrl.remove(0);
				m.invoke(obj, surveys);
			}
			else {
				m = ParentGUIController.currentGUI.getClass().getMethod(methodName,ArrayList.class);
				m.invoke(ParentGUIController.currentGUI, surveys);
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			System.err.println("Couldn't invoke method '"+methodName+"'");
			e1.printStackTrace();
		} catch (NoSuchMethodException | SecurityException e2) {
			System.err.println("No method called '"+methodName+"'");
			e2.printStackTrace();
		}
		
	}
	
	@Override
	public void analyzeSurveys(LocalDateTime start, LocalDateTime end) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		ArrayList<Object> arr = new ArrayList<>();
		arr.add(start);
		arr.add(end);
		myMsgArr.add(arr);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT, myMsgArr, SurveyReport.class));
	}
}