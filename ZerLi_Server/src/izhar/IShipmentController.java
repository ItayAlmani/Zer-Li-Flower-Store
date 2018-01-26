package izhar;

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
	 * @param shipmentID
	 * @return
	 * @throws Exception
	 */
	ArrayList<Object> getShipmentByID(BigInteger shipmentID) throws Exception;

	ArrayList<Object> update(Object obj) throws Exception;

	ShipmentDetails parse(BigInteger shipmentID, BigInteger deliveryID, String street, String city, String postCode,
			String customerName, String phoneNumber) throws Exception;

}