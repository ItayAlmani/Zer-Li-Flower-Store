package controllers;

import java.math.BigInteger;
import java.util.ArrayList;

import entities.DeliveryDetails;
import entities.ShipmentDetails;

public class ShipmentController {
	
	public static BigInteger addShipment(DeliveryDetails delDetails, ShipmentDetails shipmentDetails) throws Exception {
		BigInteger id = null;
		try {
			id = PickupController.addPickup(new DeliveryDetails(
					shipmentDetails.getOrderID(),
					shipmentDetails.getDate(),
					shipmentDetails.isImmediate(),
					shipmentDetails.getStore()));
			delDetails.setDeliveryID(id);
			String query = "INSERT INTO shipmentdetails (deliveryID, street, city, postCode, customerName, phoneNumber)" +
					"VALUES ('"+id.toString() + "', '"
					+ shipmentDetails.getStreet() + "', '"
					+ shipmentDetails.getCity() + "', '"
					+ shipmentDetails.getPostCode() + "', '"
					+ shipmentDetails.getCustomerName() + "', '"
					+ shipmentDetails.getPhoneNumber()
					+ "');";
			query = "SELECT Max(shipmentID) from shipmentdetails;";
			ArrayList<Object> arr =  ServerController.db.getQuery(query);
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