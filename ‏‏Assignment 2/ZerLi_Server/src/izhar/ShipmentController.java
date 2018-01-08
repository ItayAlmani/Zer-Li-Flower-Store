package izhar;

import java.math.BigInteger;
import java.util.ArrayList;

import common.EchoServer;
import common.ServerController;
import entities.DeliveryDetails;
import entities.ShipmentDetails;

public class ShipmentController {
	
	public static BigInteger addShipment(ShipmentDetails shipmentDetails) throws Exception {
		BigInteger id = null;
		try {
			id = PickupController.addPickup(new DeliveryDetails(
					shipmentDetails.getOrderID(),
					shipmentDetails.getDate(),
					shipmentDetails.isImmediate(),
					shipmentDetails.getStore()));
			shipmentDetails.setDeliveryID(id);
			String query = "INSERT INTO shipmentdetails (deliveryID, street, city, postCode, customerName, phoneNumber)" +
					"VALUES ('"+id.toString() + "', '"
					+ shipmentDetails.getStreet() + "', '"
					+ shipmentDetails.getCity() + "', '"
					+ shipmentDetails.getPostCode() + "', '"
					+ shipmentDetails.getCustomerName() + "', '"
					+ shipmentDetails.getPhoneNumber()
					+ "');";
			EchoServer.fac.dataBase.db.updateQuery(query);
			query = "SELECT Max(shipmentID) from shipmentdetails;";
			ArrayList<Object> arr =  EchoServer.fac.dataBase.db.getQuery(query);
			if(arr!=null && arr.size()==1 && arr.get(0) instanceof Integer)
				return BigInteger.valueOf((Integer)arr.get(0));
			throw new Exception();
		} catch (Exception e) {
			System.err.println("Error with get id in order process");
			e.printStackTrace();
			throw e;
		}
	}
}