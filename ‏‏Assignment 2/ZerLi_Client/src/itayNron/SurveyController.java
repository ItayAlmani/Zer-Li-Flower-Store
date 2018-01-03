package itayNron;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
				" VALUES ('" + survey.getStoreID().toString()+"',";
		for (Float ans : survey.getSurveyAnswerers())
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
				Object obj = Context.askingCtrl.get(0);
				Context.askingCtrl.remove(0);
				m.invoke(obj, Surveys);
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
	public void getSurveyByDates(LocalDate startDate, LocalDate endDate) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(
				"SELECT survey.*" + " FROM survey WHERE (date >= '"+startDate.toString()+"' AND date <= '"+endDate.toString()+"' AND type='Answer')");
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT, myMsgArr, Survey.class));
	}

	@Override
	public void handleGet(ArrayList<Object> obj) {
		ArrayList<Survey> survey = new ArrayList<>();
		for (int i = 0; i < obj.size(); i += 10)
				survey.add(parse
						(
						BigInteger.valueOf(Long.valueOf((int) obj.get(i))), 
						BigInteger.valueOf(Long.valueOf((int) obj.get(i+1))),
						(float) obj.get(i+2),
						(float) obj.get(i+3),
						(float) obj.get(i+4),
						(float) obj.get(i+5),
						(float) obj.get(i+6),
						(float) obj.get(i+7),
						(Timestamp) obj.get(i + 8), 
						(String) obj.get(i + 9)));
			 
		sendSurveys(survey);
	}
	
	@Override
	public Survey parse(BigInteger surveyID,BigInteger storeID, float answer1,float answer2,float answer3,float answer4,float answer5,float answer6, Timestamp date, String type) {
		LocalDate ldtDate = date.toLocalDateTime().toLocalDate();
		float[] answers = new float[6];
		answers[0]= answer1;
		answers[1]= answer2;
		answers[2]= answer3;
		answers[3]= answer4;
		answers[4]= answer5;
		answers[5]= answer6;
		
		return new Survey(surveyID,answers,ldtDate,storeID,SurveyType.valueOf(type));
		
	}
}
