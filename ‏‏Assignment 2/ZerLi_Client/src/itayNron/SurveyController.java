package itayNron;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import common.Context;
import controllers.ParentController;
import entities.CSMessage;
import entities.Store;
import entities.Survey;
import entities.CSMessage.MessageType;
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
	public void getSurveyByStore(Store store) {
	}
	
	@Override
	public void sendSurvys(ArrayList<Survey> survys) {
	}

	@Override
	public void getSurveyByDates(Date startDate, Date endDate) {
	}

	@Override
	public void handleGet(ArrayList<Object> obj) {
		
		
	}
}