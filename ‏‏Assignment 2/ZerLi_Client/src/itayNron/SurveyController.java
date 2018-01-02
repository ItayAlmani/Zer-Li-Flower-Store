package itayNron;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import common.Context;
import controllers.ParentController;
import entities.CSMessage;
import entities.Store;
import entities.StoreWorker;
import entities.Survey;
import entities.CSMessage.MessageType;
import entities.Store.StoreType;
import entities.Survey.SurveyType;
import itayNron.interfaces.ISurvey;

public class SurveyController extends ParentController implements ISurvey {

	@Override
	public void addSurvey(Survey survey) {
		myMsgArr.clear();
		String query = "INSERT INTO survey(storeID, answer1, answer2, answer3, answer4, answer5, answer6, date, type) " + 
				" VALUES ('" + survey.getStore().getStoreID()+"',";
		for (Integer ans : survey.getSurveyAnswerers())
			query+="'"+ans+"', ";
		query+="'"+java.sql.Date.valueOf(survey.getDate())+"','"+
			survey.getType().toString()+"')";
		myMsgArr.add(query);
		try {
			Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE, myMsgArr));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void getSurveyByStore(Store store) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(String.format("SELECT survey.*" + "FROM survey" + "WHERE storeID = %d",store.getStoreID()));
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT, myMsgArr, Survey.class));
	}
	
	@Override
	public void sendSurveys(ArrayList<Survey> Surveys) {
		String methodName = "setSurveys";
		Method m = null;
		try {
			//a controller asked data, not GUI
			if(Context.askingCtrl!=null && Context.askingCtrl.size()!=0) {
				m = Context.askingCtrl.get(0).getClass().getMethod(methodName,ArrayList.class);
				m.invoke(Context.askingCtrl.get(0), Surveys);
				Context.askingCtrl.remove(0);
			}
			else {
				m = Context.currentGUI.getClass().getMethod(methodName,ArrayList.class);
				m.invoke(Context.currentGUI, Surveys);
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
	public void getSurveyByDates(Date startDate, Date endDate) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(
				"SELECT survey.*" + "FROM survey" + "WHERE (date >= '"+startDate.toString()+"'" + "AND" +  "date <= '"+endDate.toString()+"'"
				+ "AND" + "type='Answer')");
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT, myMsgArr, Survey.class));
	}

	@Override
	public void handleGet(ArrayList<Object> obj) {
		ArrayList<Survey> survey = new ArrayList<>();
		for (int i = 0; i < obj.size(); i += 4)
				survey.add(parse
						(
						BigInteger.valueOf(Long.valueOf((int) obj.get(i))), 
						
						(String) obj.get(i + 2), 
						BigInteger.valueOf((int)obj.get(i+1)),
						(String) obj.get(i + 3)));
			 
		sendSurveys(survey);
	}
	
	

	@Override
	public Survey parse(BigInteger surveyID, int[] surveyAnswerers, LocalDate date, Store store, SurveyType type) {
		return new Survey(surveyID,surveyAnswerers,date,store,type);
		
	}
}
