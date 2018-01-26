package interfaces;

import java.math.BigInteger;
import java.util.ArrayList;

import entities.ShipmentDetails;

public interface IShipmentController {

	ArrayList<Object> add(ArrayList<Object> arr) throws Exception;
	/**
	 * <p> A function that get objects from the DB and calls {@link parse}.
	 *  after parse it pushes the data back as a report </p>
	 * @param obj - an array list of objects the represent row in product table in DB.
	 * @return  an ArrayList of Objects with all the information that related to the asking report
	 * @throws Exception This function uses {@link parse} that can throw an exception
	 */
	ArrayList<Object> handleGet(ArrayList<Object> obj) throws Exception;
	/**
	 * Function the return shipment by shipmentID
	 * @param shipmentID-the shipment id
	 * @return an ArrayList of Objects with all the information that related to the asking report
	 * @throws Exception the Exception can be if there is a problem with the query that sent to the DB.
	 */
	ArrayList<Object> getShipmentByID(BigInteger shipmentID) throws Exception;

	ArrayList<Object> update(Object obj) throws Exception;
	/**
	 *  function that gets all data as parameters, 
	 * and creates a ShipmentDetails instance with all associated data.
	 * @param shipmentID - shipment id
	 * @param deliveryID - the delivery id
	 * @param street - street to ship
	 * @param city - city to ship
	 * @param postCode - post code of street in city
	 * @param customerName - the name of the customer
	 * @param phoneNumber - the phone number of the customer
	 * @return new ShipmentDetails instance with all the required data.
	 * @throws Exception Exception Can be thrown when updating if update fails
	 */
	ShipmentDetails parse(BigInteger shipmentID, BigInteger deliveryID, String street, String city, String postCode,
			String customerName, String phoneNumber) throws Exception;

}