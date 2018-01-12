package itayNron;

import java.math.BigInteger;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import common.EchoServer;
import controllers.ParentController;
import entities.Complaint;
import entities.Survey;
import entities.Survey.SurveyType;


public class ComplaintController extends ParentController {

	@Override
	public ArrayList<Object> handleGet(ArrayList<Object> obj) throws Exception {
		if(obj == null) return null;
		ArrayList<Object> comp = new ArrayList<>();
		for (int i = 0; i < obj.size(); i += 7)
			comp.add(parse
					(
					BigInteger.valueOf(Long.valueOf((int) obj.get(i))), 
					BigInteger.valueOf((int)obj.get(i+1)), 
					BigInteger.valueOf((int)obj.get(i+2)),
					(String) obj.get(i + 3),
					(Timestamp) obj.get(i + 4),
					(Boolean)obj.get(i+5),
					(Boolean)obj.get(i+6)
					));
		return comp;
		
	}

	@Override
	public ArrayList<Object> add(ArrayList<Object> arr) throws Exception {
		if(arr!=null && (arr.get(0) instanceof Complaint == false) || arr.get(1) instanceof Boolean == false)
			throw new Exception();
		Complaint comp = (Complaint)arr.get(0);
		boolean isReturnNextID = (boolean)arr.get(1);
			try {
				insertComplaint(comp);
				myMsgArr.clear();
				if(isReturnNextID)
					myMsgArr.add(getNextID());
				else
					myMsgArr.add(true);
				return myMsgArr;
				} catch (Exception e) {
					System.err.println("Error with get id in complaint process");
				e.printStackTrace();
				}
		
		throw new Exception("couldn't add complaint");
	}
	public void insertComplaint(Complaint comp) throws Exception {
		String query = "INSERT INTO complaint (customerID, storeID,complaintReason,date,isTreated,isRefunded)" 
		+ " VALUES ('" + comp.getCustomerID()  
		+ "','"+ comp.getStoreID()+ "','"+ comp.getDate()+ "','"+ comp.isIsTreated()+ "','"+ comp.isIsRefunded()+"')";
		EchoServer.fac.dataBase.db.updateQuery(query);		
	}
	
	private BigInteger getNextID() throws Exception {
		String query="SELECT Max(ComplaintID) from complaint";
		myMsgArr =  EchoServer.fac.dataBase.db.getQuery(query);
		if(myMsgArr!=null && myMsgArr.size()==1 && myMsgArr.get(0) instanceof Integer)
			return BigInteger.valueOf((Integer)myMsgArr.get(0));
		throw new Exception("Error Complaint add - no id");
	}
	@Override
	public ArrayList<Object> update(Object obj) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public ArrayList<Object> getNotTreatedComplaints(Object obj) throws Exception {
		String query = "SELECT complaint.*" + 
				"FROM complaint" + 
				"WHERE complaint.isTreated='0'";
		ArrayList<Object> arr = handleGet(EchoServer.fac.dataBase.db.getQuery(query));
		if(arr==null)
			throw new SQLException("Error in Store Worker of Store(s)\n");
		return arr;	
	}
	
	public Complaint parse(BigInteger complaintID,BigInteger customerID, BigInteger storeID,String complaintReason,Timestamp date,boolean isTreated,boolean isRefunded) {
		LocalDateTime ldtDate = date.toLocalDateTime();
		
		return new Complaint(complaintID,customerID,storeID,complaintReason,ldtDate,isTreated,isRefunded);
	}

}
