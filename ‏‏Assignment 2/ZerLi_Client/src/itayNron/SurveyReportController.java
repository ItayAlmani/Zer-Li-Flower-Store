package itayNron;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import common.Context;
import controllers.ParentController;
import entities.CSMessage;
<<<<<<< HEAD
=======
import entities.Order;
import entities.Product;
import entities.Store;
import entities.Survey;
import entities.Survey.SurveyType;
import entities.SurveyReport;
>>>>>>> branch 'master' of https://github.com/ItayAlmani/Zer-Li-Flower-Store.git
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

<<<<<<< HEAD
	@Override
	public void getSurveyReportsByStore(int storeid) {
		
	}
	
	public void handleGet(ArrayList<SurveyReport> surveys) {
=======
	
	

	@Override
	public void handleGet(ArrayList<Object> obj) {
			ArrayList<SurveyReport> sr = new ArrayList<>();
			for (int i = 0; i < obj.size(); i += 5) {
					sr.add(parse(
							BigInteger.valueOf(Long.valueOf((int) obj.get(i))), 
							(Survey) obj.get(i + 1), 
							(String) obj.get(i + 2),
							(LocalDateTime) obj.get(i + 3),
							(LocalDateTime) obj.get(i + 4)
				)); 
			
			}
			sendSurveyReports(sr);
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
>>>>>>> branch 'master' of https://github.com/ItayAlmani/Zer-Li-Flower-Store.git
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
<<<<<<< HEAD
=======




	@Override
	public void getSurveyReportsByStore(BigInteger storeID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(
				"SELECT surveyreport.*" + 
				" FROM orders AS ord" + 
				" JOIN survey ON surveyreport.surveyID=survey.surveyID" + 
				" WHERE survey.storeID='"+storeID+"'" + " AND survey.type='Analyzes'");
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT, myMsgArr, SurveyReport.class));

		
	}




	@Override
	public SurveyReport parse(BigInteger id,Survey surveyAnalyzes, String verbalReport, LocalDateTime startDate,
			LocalDateTime endDate) {
		
		return new SurveyReport(id,surveyAnalyzes,verbalReport,startDate,endDate);
	}

>>>>>>> branch 'master' of https://github.com/ItayAlmani/Zer-Li-Flower-Store.git
	
	@Override
	public void analyzeSurveys(LocalDate start, LocalDate end) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		ArrayList<Object> arr = new ArrayList<>();
		arr.add(start);
		arr.add(end);
		myMsgArr.add(arr);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT, myMsgArr, SurveyReport.class));
	}
}