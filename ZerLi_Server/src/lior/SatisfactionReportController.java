package lior;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;

import common.EchoServer;
import controllers.ParentController;
import entities.SatisfactionReport;
import entities.Survey;
import entities.Survey.SurveyType;
import lior.interfaces.ISatisfactionReportController;

public class SatisfactionReportController extends ParentController implements ISatisfactionReportController {
	private SatisfactionReport sReport;
	
	public ArrayList<Object> getSatisfactionReport(ArrayList<Object> arr) throws Exception {
		if(arr!=null && (arr.get(0) instanceof LocalDate == false)|| arr.get(1) instanceof BigInteger == false)
			return null;
		//=====CHANGE DATE TO THE END OF THE QUARTER======
		LocalDate date = (LocalDate)arr.get(0);
		BigInteger storeID = (BigInteger)arr.get(1);
		String query = String.format(
				"SELECT *"
				+ " FROM satisfactionreport "
				+ " WHERE storeID='%d'"
				+ " AND endOfQuarterDate='%s'",
					storeID.intValue(),
					Timestamp.valueOf(date.atStartOfDay()).toString());
		ArrayList<Object> inObjs = handleGet(EchoServer.fac.dataBase.db.getQuery(query));
		if(inObjs == null)	throw new Exception();
		if(inObjs.size()==1 && inObjs.get(0) instanceof SatisfactionReport)
			return inObjs;
		else if(inObjs.isEmpty())
			return produceSatisfactionReport(date, storeID);
		throw new Exception();
	}

	/* (non-Javadoc)
	 * @see lior.ISatisfactionReport#ProduceSatisfactionReport(java.util.ArrayList)
	 */
	@Override
	public ArrayList<Object> produceSatisfactionReport(LocalDate date, BigInteger storeID) throws Exception {
		sReport=new SatisfactionReport(date,storeID);
		this.sReport.setStoreID(storeID);
		this.sReport.setStartdate(date.minusMonths(3).plusDays(1));
		this.sReport.setEnddate(date);
		ArrayList<Object> obj=new ArrayList<>();
		obj.add(this.sReport.getStartdate().atStartOfDay());
		obj.add(this.sReport.getEnddate().atStartOfDay());
		return analyzeSurveys(EchoServer.fac.survey.getSurveyByDates(obj));
	}

	public ArrayList<Object> analyzeSurveys(ArrayList<Object> objs) throws Exception{
		ArrayList<Survey> surveys= new ArrayList<>();
		if(objs == null)
			throw new Exception();
		if(objs.isEmpty()) {
			ArrayList<Object> ar = new ArrayList<>();
			ar.add(this.sReport);
			add(ar);
			return ar;
		}
		for (Object o : objs) {
			if(o instanceof Survey)
				surveys.add((Survey)o);
		}
		ArrayList<Survey> ar1=new ArrayList<>();
		float[] answers = new float[6];
		float totans=0;
		for(int i=0;i<surveys.size();i++)
		{
			if(surveys.get(i).getStoreID().equals(this.sReport.getStoreID())
					&& surveys.get(i).getType().equals(SurveyType.Answer)
					)
			{
				ar1.add(surveys.get(i));
			}	
		}
		for(int i=0;i<ar1.size();i++)
		{
			for(int j=0;j<6;j++)
			{
				answers[j]+=ar1.get(i).getSurveyAnswerers()[j];
			}	
		}
		for(int j=0;j<6;j++)
		{
			answers[j]=answers[j]/ar1.size();
			totans+=answers[j];
		}	
			ArrayList<Object> ar = new ArrayList<>();
			this.sReport.setSurveys(ar1);
			this.sReport.setFinalanswers(answers);
			this.sReport.setAverageTotanswer(totans/6);
			ar.add(this.sReport);
			add(ar);
			return ar;
	}
	
	@Override
	public ArrayList<Object> handleGet(ArrayList<Object> obj){
		if (obj == null || obj.size()<9)
			return new ArrayList<>();
		ArrayList<Object> srs = new ArrayList<>();
		BigInteger storeID = BigInteger.valueOf((Integer) obj.get(0));
		LocalDate endDate = ((Timestamp) obj.get(1)).toLocalDateTime().toLocalDate();
		SatisfactionReport sr = new SatisfactionReport(endDate,storeID);
		sr.setEnddate(endDate);
		sr.setStartdate(endDate.minusMonths(3).plusDays(1));
		for (int i = 0; i < obj.size(); i += 9) {
			parse(	sr,
					(Float) obj.get(i + 2),
					(Float) obj.get(i + 3),
					(Float) obj.get(i + 4),
					(Float) obj.get(i + 5),
					(Float) obj.get(i + 6),
					(Float) obj.get(i + 7),
					(Float) obj.get(i + 8)
				);
		}
		srs.add(sr);
		return srs;
	}
	
	private void parse(SatisfactionReport sr,Float AVGAns,Float AVGQ1,Float AVGQ2,Float AVGQ3
			,Float AVGQ4,Float AVGQ5,Float AVGQ6) {
		sr.setAverageTotanswer(sr.getAverageTotanswer()+AVGAns);
		float[] temp=new float[6];
		temp[0]=AVGQ1;
		temp[1]=AVGQ2;
		temp[2]=AVGQ3;
		temp[3]=AVGQ4;
		temp[4]=AVGQ5;
		temp[5]=AVGQ6;
		sr.setFinalanswers(temp);
	}

	@Override
	public ArrayList<Object> add(ArrayList<Object> arr) throws Exception {
		if(arr==null || arr.isEmpty() || arr.get(0) instanceof SatisfactionReport == false )
			throw new Exception();
		SatisfactionReport sr = (SatisfactionReport)arr.get(0);
		ArrayList<String> queries = new ArrayList<>();
		queries.add(String.format(
				"INSERT INTO satisfactionreport"
				+ " (storeID, endOfQuarterDate,AVGAns,AVGQ1,AVGQ2,AVGQ3,AVGQ4,AVGQ5,AVGQ6)"
				+ " VALUES ('%d', '%s','%f','%f','%f','%f','%f','%f','%f');",
				sr.getStoreID(),
				Timestamp.valueOf(sr.getEndOfQuarterDate().atStartOfDay()).toString(),
				sr.getAverageTotanswer(),sr.getFinalanswers()[0],sr.getFinalanswers()[1]
		,sr.getFinalanswers()[2],sr.getFinalanswers()[3]
		,sr.getFinalanswers()[4],sr.getFinalanswers()[5]));
		EchoServer.fac.dataBase.db.insertWithBatch(queries);
		return null;
	}

	@Override
	public ArrayList<Object> update(Object obj) throws Exception {
		return null;
	}
}