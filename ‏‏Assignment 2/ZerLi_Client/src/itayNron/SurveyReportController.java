package itayNron;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;

import common.Context;
import controllers.ParentController;
import entities.CSMessage;
import entities.Survey;
import entities.Survey.SurveyType;
import entities.SurveyReport;
import entities.CSMessage.MessageType;
import itayNron.interfaces.ISurveyReport;

public class SurveyReportController extends ParentController implements ISurveyReport {

	@Override
	public void addSurveyReport(SurveyReport sr) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(sr);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.INSERT, myMsgArr,SurveyReport.class));
	}

	@Override
	public void getSurveyReportsByStore(int storeid) {
		
	}
	
	
	
	

	@Override
	public void handleGet(ArrayList<Object> obj) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void getSurveysForAnalays(LocalDate start, LocalDate end) throws IOException {
		Context.askingCtrl.add(this);
		Context.fac.survey.getSurveyByDates(start, end);
	}
	
	public void setSurveys(ArrayList<Survey> surveys) //analysis survey
	{ 
		float [] totalSumOfAnswers = new float[6];
		for (Survey survey : surveys) 
		{
			float[] surveyAnswers =  survey.getSurveyAnswerers();
			for (int i=0;i<surveyAnswers.length;i++)
				totalSumOfAnswers[i]+=surveyAnswers[i];
		}
		for (int i=0;i<totalSumOfAnswers.length;i++)
			totalSumOfAnswers[i]=totalSumOfAnswers[i]/(float)surveys.size();
		ArrayList<SurveyReport> arr = new ArrayList<>();
		arr.add(new SurveyReport(new Survey(totalSumOfAnswers, LocalDate.now(), surveys.get(0).getStoreID(),SurveyType.Analyzes)));
		sendSurveyReports(arr);
	}

	
	
	@Override
	public void sendSurveyReports(ArrayList<SurveyReport> surveys) {
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
				m = Context.currentGUI.getClass().getMethod(methodName,ArrayList.class);
				m.invoke(Context.currentGUI, surveys);
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			System.err.println("Couldn't invoke method '"+methodName+"'");
			e1.printStackTrace();
		} catch (NoSuchMethodException | SecurityException e2) {
			System.err.println("No method called '"+methodName+"'");
			e2.printStackTrace();
		}
	}

	
}