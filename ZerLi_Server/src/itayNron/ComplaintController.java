package itayNron;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import common.EchoServer;
import controllers.ParentController;
import entities.Complaint;
import entities.Customer;
import entities.Store;


public class ComplaintController extends ParentController {	
	
	public ComplaintController() {
		ArrayList<Object> comObj;
		try {
			comObj = getNotTreatedComplaintsAnd24NotPassed();
			for (Object o : comObj)
				setComplaintTimer((Complaint)o);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setComplaintTimer(final Complaint c) {
		final long period_in_minutes = Long.valueOf(60*24)-Duration.between(c.getDate(), LocalDateTime.now()).toMinutes();

		//24 or more hours passed
		if(period_in_minutes<0) {
			c.setAnswered24Hours(false);
    		try {
				update(c);
			} catch (Exception e) {
				e.printStackTrace();
			}
    		return;
		}
		
		//24 hours not passed
		Timer timer = new Timer();
		TimerTask task = new TimerTask () {
		    @Override
		    public void run () {
		    	ArrayList<Object> cObj;
				try {
					cObj = getComplaintById(c.getComplaintID());
					if(cObj==null || cObj.size()!=1) {
			    		System.err.println("Error in cObj");
			    		return;
			    	}
			    	Complaint c2 = (Complaint)cObj.get(0);
			    	//not treated
			    	if(c2.isTreated()==false) {
			    		c2.setAnswered24Hours(false);
			    		update(c2);
			    	}
				} catch (Exception e) {
					e.printStackTrace();
				}
		    }
		};
		timer.schedule(task, 1000*60*period_in_minutes);
	}
	
	private ArrayList<Object> getComplaintById(BigInteger complaintID) throws Exception {
		String query = String.format(
				"SELECT * FROM complaint WHERE complaintID = %d",
				complaintID);
		return handleGet(EchoServer.fac.dataBase.db.getQuery(query));
	}
	
	/*private ArrayList<Object> getComplaintByCswID(BigInteger cswID) throws Exception
	{
		String query = String.format(
				"SELECT * FROM complaint WHERE cswID = %d",
				cswID);
		return handleGet(EchoServer.fac.dataBase.db.getQuery(query));
	}*/
	
	/**
	 * <p>
	 * Function sends query about all not treated complaints from DB
	 * </p>
	 * @return generic object arrayList, eventually will be arrayList of complaints
	 * @throws Exception Context.clientConsole.handleMessageFromClientUI throws IOException.
	 */
	public ArrayList<Object> getNotTreatedComplaintsAnd24NotPassed() throws Exception {
		String query = "SELECT * FROM complaint WHERE isTreated=false AND isAnswered24Hours=true";
		return handleGet(EchoServer.fac.dataBase.db.getQuery(query));
	}

	@Override
	public ArrayList<Object> handleGet(ArrayList<Object> obj) throws Exception {
		if(obj == null) return new ArrayList<>();
		ArrayList<Object> comp = new ArrayList<>();
		for (int i = 0; i < obj.size(); i += 9)
			comp.add(parse
					(
					BigInteger.valueOf((Integer) obj.get(i)), 
					BigInteger.valueOf((Integer) obj.get(i+1)), 
					BigInteger.valueOf((Integer) obj.get(i+2)),
					(String) obj.get(i + 3),
					(Timestamp) obj.get(i + 4),
					(Boolean)obj.get(i+5),
					(Boolean)obj.get(i+6),
					(Boolean)obj.get(i+7),
					BigInteger.valueOf((Integer)obj.get(i+8))
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
			comp.setComplaintID(getNextID());
			setComplaintTimer(comp);
			myMsgArr.clear();
			if(isReturnNextID)
				myMsgArr.add(comp.getComplaintID());
			else
				myMsgArr.add(true);
			return myMsgArr;
		} catch (Exception e) {
			System.err.println("Error with get id in complaint process");
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * <p>
	 * Function to insert with query complaint object to DB
	 * </p>
	 * @param comp - complaint object to be inserted to DB
	 * @throws Exception Context.clientConsole.handleMessageFromClientUI throws IOException.
	 */
	public void insertComplaint(Complaint comp) throws Exception {
		String query = "INSERT INTO complaint (customerID, storeID,complaintReason,date,isTreated,isRefunded,isAnswered24Hours,cswID)" 
		+ " VALUES ('" 
				+ comp.getCustomer().getCustomerID().toString() + "','"
				+ comp.getStoreID().toString()+ "','"
				+ comp.getComplaintReason()+ "','"
				+ Timestamp.valueOf(comp.getDate())+ "',"
				+ comp.isTreated()+ ","
				+ comp.isRefunded()+ ","
				+ comp.isAnswered24Hours()+",'"
				+ comp.getCswID()+"')";
		EchoServer.fac.dataBase.db.updateQuery(query);		
	}
	/**
	 * <p>
	 * Function to get the next ID for complaint object from DB
	 * </p>
	 * @return ID which used by the next complaint object that will be inserted to DB
	 * @throws Exception Context.clientConsole.handleMessageFromClientUI throws IOException.
	 */
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
					+ " isTreated=%b,"
					+ " isRefunded=%b,"
					+ " isAnswered24Hours=%b,"
					+ " cswID=%d"
					+ " WHERE complaintID='%d'" , 
			comp.getCustomer().getCustomerID(),
			comp.getStoreID(),
			comp.getComplaintReason(),
			Timestamp.valueOf(comp.getDate()).toString(),
			comp.isTreated(),
			comp.isRefunded(),
			comp.isAnswered24Hours(),
			comp.getCswID(),
			comp.getComplaintID());
			EchoServer.fac.dataBase.db.updateQuery(query);
			myMsgArr.clear();
			myMsgArr.add(true);
			return myMsgArr;
		}
		else
			throw new Exception();
	}
	
	/**
	 * <p>
	 * Function sends query about all not treated complaints from DB
	 * </p>
	 * @return generic object arrayList, eventually will be arrayList of complaints
	 * @throws Exception Context.clientConsole.handleMessageFromClientUI throws IOException.
	 */
	public ArrayList<Object> getNotTreatedComplaints(BigInteger cswID) throws Exception {
		String query =String.format(("SELECT * FROM complaint WHERE isTreated=false AND cswID='%d'"),
			cswID);	
				
		return handleGet(EchoServer.fac.dataBase.db.getQuery(query));
	}
	/**
	 * <p>
	 * Function sends query to get all complaints by specific store
	 * </p>
	 * @param obj - generic object that identify the specific store
	 * @return - generic object arrayList, eventually turn to arrayList of complaints
	 * @throws Exception Context.clientConsole.handleMessageFromClientUI throws IOException.
	 */
	public ArrayList<Object> getComplaintsByStore (Store store, LocalDate startDate, LocalDate endDate) throws Exception
	{
		String query=null;
		if(!store.getStoreID().equals(BigInteger.valueOf(-1))) {
			query = String.format(
					"SELECT * FROM complaint WHERE (storeID = %d AND date >='%s' \n"+
					"AND date <='%s');",
					store.getStoreID(),(Timestamp.valueOf(startDate.atStartOfDay())).toString(),
					(Timestamp.valueOf(endDate.atTime(LocalTime.of(23, 59, 59)))).toString());
		}
		else {
			query = String.format(
					"SELECT * FROM complaint WHERE (date >='%s' \n"+
					"AND date <='%s');",
					(Timestamp.valueOf(startDate.atStartOfDay())).toString(),
					(Timestamp.valueOf(endDate.atTime(LocalTime.of(23, 59, 59)))).toString());
		}
		return handleGet(EchoServer.fac.dataBase.db.getQuery(query));
	}
	
	/**
	 * <p>
	 * The function will get data of complaint object from server and fill the fields of it.
	 * </p>
	 * @param complaintID - parameter for complaintID field in object
	 * @param customerID - parameter for customerID field in object
	 * @param storeID - parameter for storeID field in object
	 * @param complaintReason - parameter for complaintReason field in object
	 * @param date - parameter for date field in object
	 * @param isTreated - parameter for isTreated field in object
	 * @param isRefunded - parameter for isRefunded field in object
	 * @return new complaint object filled with data from DB
	 * @throws Exception Context.clientConsole.handleMessageFromClientUI throws IOException.
	 */
	public Complaint parse(BigInteger complaintID,BigInteger customerID, BigInteger storeID,String complaintReason,Timestamp date,boolean isTreated,boolean isRefunded, boolean isAnswered24Hours,BigInteger cswID) throws Exception {
		LocalDateTime ldtDate = date.toLocalDateTime();
		ArrayList<Object> custObj = EchoServer.fac.customer.getCustomerByID(customerID);
		if(custObj==null || custObj.size()!=1 || custObj.get(0) instanceof Customer == false)
			throw new Exception();
		
		return new Complaint(complaintID,(Customer)custObj.get(0),storeID,complaintReason,ldtDate,isTreated,isRefunded,isAnswered24Hours,cswID);
	}

}
