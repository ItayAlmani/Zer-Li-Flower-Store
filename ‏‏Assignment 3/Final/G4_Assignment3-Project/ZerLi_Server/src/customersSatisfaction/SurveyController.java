package customersSatisfaction;

import java.math.BigInteger;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import common.EchoServer;
import common.ParentController;
import customersSatisfaction.entities.Survey;
import customersSatisfaction.entities.Survey.SurveyType;
import customersSatisfaction.interfaces.ISurvey;

public class SurveyController extends ParentController implements ISurvey
{
	
	@Override
	public ArrayList<Object> add(ArrayList<Object> arr) throws Exception {
		if(arr!=null && (arr.get(0) instanceof Survey == false) || arr.get(1) instanceof Boolean == false)
			throw new Exception();
		Survey survey = (Survey)arr.get(0);
		boolean isReturnNextID = (boolean)arr.get(1);
		String query = "INSERT INTO survey(storeID, answer1, answer2, answer3, answer4, answer5, answer6, date, type) " + 
				" VALUES ('" + survey.getStoreID().toString()+"',";
		for (Float ans : survey.getSurveyAnswerers())
			query+="'"+ans+"', ";
		query+="'"+java.sql.Date.valueOf(survey.getDate())+"','"+
			survey.getType().toString()+"')";
		EchoServer.fac.dataBase.db.updateQuery(query);
		if(isReturnNextID) {
			query = "SELECT Max(surveyID) from survey";
			myMsgArr =  EchoServer.fac.dataBase.db.getQuery(query);
			if(myMsgArr!=null && myMsgArr.size()==1 && myMsgArr.get(0) instanceof Integer) {
				myMsgArr.set(0, BigInteger.valueOf((Integer)myMsgArr.get(0)));
			}
			else throw new Exception();
		}
		else
			myMsgArr.add(true);
		return myMsgArr;
	}

	@Override
	public ArrayList<Object> handleGet(ArrayList<Object> obj) {
		if(obj==null) return null;
		ArrayList<Object> surve√ys = new ArrayList<>();
		for (int i = 0; i < obj.size(); i += 10)
			surve√ys.add(parse
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
		return surve√ys;
	}

	
	@Override
	public ArrayList<Object> update(Object obj) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public ArrayList<Object> getSurveyByDatesAndStore(BigInteger storeID, LocalDateTime startDate, LocalDateTime endDate) throws SQLException {
		String query =String.format(
				"SELECT survey.*"
				+ " FROM survey"
				+ " WHERE storeID = '%d' AND "
				+ " date >= '%s'"
				+ " AND date <= '%s'"
				+ " AND type='Answer'", 
				storeID,
				startDate.toString(),
				endDate.toString()); 
		return handleGet(EchoServer.fac.dataBase.db.getQuery(query));
	}
	
	
	@Override
	public ArrayList<Object> getSurveyByDates(ArrayList<Object> arr) throws SQLException{
		if(arr!=null && arr.size()==2 
				&& arr.get(0) instanceof LocalDateTime && arr.get(1) instanceof LocalDateTime) {
			LocalDateTime startDate = (LocalDateTime)arr.get(0), endDate = (LocalDateTime)arr.get(1);
			String query = String.format(
					"SELECT survey.*"
					+ " FROM survey"
					+ " WHERE date >= '%s'"
					+ " AND date <= '%s'"
					+ " AND type='Answer'", 
					startDate.toString(),
					endDate.toString());
			
			return handleGet(EchoServer.fac.dataBase.db.getQuery(query));
		}
		throw new SQLException();
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