package orderNproducts.interfaces;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;

import orderNproducts.entities.DeliveryDetails;

public interface IPickupController {
	/**
	 * <p>A function that inserts a row into a deliverydetails table in DB</p>
	 * @param arr - ArrayList of Object that represents the data that we want to insert as row in DB
	 * @return myMsgArr-An ArrayList of Objects that represnets the answer from DB
	 * @throws Exception the Exception can be throwed in 2 situations, if there is a problem with the query that sent to the DB.
	 * or if the returned values is not from the type we expected. 
	 */
	ArrayList<Object> add(ArrayList<Object> arr) throws Exception;
	/**
	 * Function that updates a line within the DB if necessary.
	 * @param obj - need to be instance of DeliveryDetails,than it parse into a proper 
	 * Structure that needed to be write as row in DB
	 * @return myMsgArr - An ArrayList of Objects that represnets the answer from DB
	 * @throws Exception the Exception can be throwed in 2 situations, if there is a problem with the query that sent to the DB.
	 * or if the returned values is not from the type we expected.
	 */
	ArrayList<Object> update(Object obj) throws Exception;
	/**
	 * A function which sends a request to the DB to receive a line from the DB's delivery by the deliveryID
	 * @param deliveryID - The ID of the delivery we want to get
	 * @return ArrayList with the data we pulled from the DB
	 * @throws Exception the Exception can be if there is a problem with the query that sent to the DB. 
	 */
	ArrayList<Object> getDeliveryByID(BigInteger deliveryID) throws Exception;
	/**
	 * <p> A function that get objects from the DB and calls {@link parse}.
	 *  after parse it pushes the data back as a report </p>
	 * @param obj -  an array list of objects the represent row in deliverydetails table in DB
	 * @return an ArrayList of Objects with all the information that related to the asking report
	 * @throws Exception This function uses {@link parse} that can throw an exception
	 */
	ArrayList<Object> handleGet(ArrayList<Object> obj) throws Exception;
	/**
	 * A function that gets all data as parameters, finds the required StoreID, 
	 * and creates a deliveryDetails instance with all associated data.
	 * @param deliveryID - The Required deliveryID to the new instance.
	 * @param storeID - The Required storeID to the new instance.
	 * @param date - The Required date to the new instance.
	 * @param isImmediate - represents if the delivery is immediate
	 * @return new DeliveryDetails instance with all the required data.
	 * @throws Exception Can be injected in two cases, first case if there is a problem with the query of receiving a store by ID.
	 * A second case if one of the data received does not match what we expected.
	 */
	DeliveryDetails parse(BigInteger deliveryID, BigInteger storeID, LocalDateTime date, Boolean isImmediate)
			throws Exception;

}