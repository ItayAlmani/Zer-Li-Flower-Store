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
import entities.Customer;
import entities.ProductInOrder;
import entities.Survey;
import entities.Survey.SurveyType;
import izhar.ProductController;


public class ComplaintController extends ParentController {

	@Override
	public ArrayList<Object> handleGet(ArrayList<Object> obj) throws Exception {
		if(obj == null) return new ArrayList<>();
		ArrayList<Object> comp = new ArrayList<>();
		for (int i = 0; i < obj.size(); i += 7)
			comp.add(parse
					(
					BigInteger.valueOf((Integer) obj.get(i)), 
					BigInteger.valueOf((Integer) obj.get(i+1)), 
					BigInteger.valueOf((Integer) obj.get(i+2)),
					(String) obj.get(i + 3),
					(Timestamp) obj.get(i + 4),
					((Integer)obj.get(i+5))!=0,
					((Integer)obj.get(i+6))!=0
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
			throw e;
		}
	}
	public void insertComplaint(Complaint comp) throws Exception {
		String treStr = comp.isTreated()==true?"1":"0",
				refStr = comp.isRefunded()==true?"1":"0";
		String query = "INSERT INTO complaint (customerID, storeID,complaintReason,date,isTreated,isRefunded)" 
		+ " VALUES ('" 
				+ comp.getCustomer().getCustomerID().toString() + "','"
				+ comp.getStoreID().toString()+ "','"
				+ comp.getComplaintReason()+ "','"
				+ Timestamp.valueOf(comp.getDate())+ "','"
				+ treStr+ "','"
				+ refStr+"')";
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
		if(obj instanceof Complaint) {
			Complaint comp = (Complaint)obj;
			String query = String.format("UPDATE complaint"
					+ " SET customerID='%d',"
					+ " storeID='%d',"
					+ " complaintReason='%s',"
					+ " date='%s',"
					+ " isTreated='%s',"
					+ " isRefunded='%s'"
					+ " WHERE complaintID='%d'" , 
			comp.getCustomer().getCustomerID(),
			comp.getStoreID(),
			comp.getComplaintReason(),
			Timestamp.valueOf(comp.getDate()).toString(),
			comp.isTreated()==true?"1":"0",
			comp.isRefunded()==true?"1":"0",
			comp.getComplaintID());
			EchoServer.fac.dataBase.db.updateQuery(query);
			myMsgArr.clear();
			myMsgArr.add(true);
			return myMsgArr;
		}
		else
			throw new Exception();
	}
	
	
	public ArrayList<Object> getNotTreatedComplaints() throws Exception {
		String query = "SELECT complaint.* FROM complaint WHERE complaint.isTreated='0'";
		return handleGet(EchoServer.fac.dataBase.db.getQuery(query));
	}
	
	public ArrayList<Object> getComplaintsByStore (Object obj) throws Exception
	{
		if(obj instanceof Complaint) {
			Complaint comp = (Complaint)obj;
		String query = String.format(
				"SELECT complaint.* FROM survey WHERE storeID = %d",
				comp.getStoreID());
		return handleGet(EchoServer.fac.dataBase.db.getQuery(query));
		}
		else throw new SQLException();
	}
	public Complaint parse(BigInteger complaintID,BigInteger customerID, BigInteger storeID,String complaintReason,Timestamp date,boolean isTreated,boolean isRefunded) throws Exception {
		LocalDateTime ldtDate = date.toLocalDateTime();
		ArrayList<Object> custObj = EchoServer.fac.customer.getCustomerByID(customerID);
		if(custObj==null || custObj.size()!=1 || custObj.get(0) instanceof Customer == false)
			throw new Exception("Ani? Ata");
		
		return new Complaint(complaintID,(Customer)custObj.get(0),storeID,complaintReason,ldtDate,isTreated,isRefunded);
	}

}
