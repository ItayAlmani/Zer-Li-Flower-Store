package customersSatisfaction.interfaces;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;

import customersSatisfaction.entities.Complaint;
import orderNproducts.entities.Store;

public interface IComplaint {

	/**
	 * <p>
	 * Function to check if 24 hours passed since customer service worker entered the complaint.
	 * if the time passed, the function changes the value of isAnswered24Hours to false.  
	 * </p>
	 * @param c - complaint object for setting timer
	 */
	void setComplaintTimer(Complaint c);
	/**
	 * <p>
	 * Function sends query about all not treated complaints from DB
	 * </p>
	 * @return generic object arrayList, eventually will be arrayList of complaints
	 * @throws Exception 
	 */
	ArrayList<Object> getNotTreatedComplaintsAnd24NotPassed() throws Exception;
	/**
	 * <p> A function that get objects from the DB and calls {@link parse}.
	 *  after parse it pushes the data back as a report </p>
	 * @param obj - an array list of objects the represent row in complaint table in DB.
	 * @return  an ArrayList of Objects with all the information that related to the asking report
	 * @throws Exception This function uses {@link parse} that can throw an exception
	 */
	ArrayList<Object> handleGet(ArrayList<Object> obj) throws Exception;

	ArrayList<Object> add(ArrayList<Object> arr) throws Exception;

	/**
	 * <p>
	 * Function to insert with query complaint object to DB
	 * </p>
	 * @param comp - complaint object to be inserted to DB
	 * @throws Exception
	 */
	void insertComplaint(Complaint comp) throws Exception;

	ArrayList<Object> update(Object obj) throws Exception;

	/**
	 * <p>
	 * Function sends query about all not treated complaints that the this <b>customer service worker</b> inserted from DB
	 * </p>
	 * @param cswID - customer service worker ID
	 * @return generic object arrayList, eventually will be arrayList of complaints
	 * @throws Exception 
	 */
	ArrayList<Object> getNotTreatedComplaints(BigInteger cswID) throws Exception;

	/**
	 * <p>
	 * Function sends query to get all complaints by specific store
	 * </p>
	 * @param obj - generic object that identify the specific store
	 * @return - generic object arrayList, eventually turn to arrayList of complaints
	 * @throws Exception Context.clientConsole.handleMessageFromClientUI throws IOException.
	 */
	ArrayList<Object> getComplaintsByStore(Store store, LocalDate startDate, LocalDate endDate) throws Exception;

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
	 * @param isAnswered24Hours - parameter for isAnswered24Hours field in object
	 * @param cswID - parameter for cswID field in object
	 * @return new complaint object filled with data from DB
	 * @throws Exception 
	 */
	Complaint parse(BigInteger complaintID, BigInteger customerID, BigInteger storeID, String complaintReason,
			Timestamp date, boolean isTreated, boolean isRefunded, boolean isAnswered24Hours, BigInteger cswID)
			throws Exception;

}