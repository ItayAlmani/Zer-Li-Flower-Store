package controllers;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;

import entities.DeliveryDetails;

public class PickupController {
	
	public static BigInteger addPickup(DeliveryDetails deliveryDetails) throws Exception {
		String res = "0";
		if(deliveryDetails.isImmediate())
			res = "1";
		String query = "INSERT INTO deliverydetails (orderID, storeID, date, isImmediate) "+
				"VALUES ('"+deliveryDetails.getOrderID() + "', '"
				+ deliveryDetails.getStore().getStoreID() + "', '"
				+ (Timestamp.valueOf(deliveryDetails.getDate())).toString() + "', '"
				+ res + "');";
		ServerController.db.updateQuery(query);
		query = "SELECT Max(deliveryID) from deliverydetails;";
		ArrayList<Object> arr =  ServerController.db.getQuery(query);
		if(arr!=null && arr.size()==1 && arr.get(0) instanceof Integer)
			return BigInteger.valueOf((Integer)arr.get(0));
		throw new Exception();
	}
}