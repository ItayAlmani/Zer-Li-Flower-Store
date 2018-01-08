package izhar;

import java.math.BigInteger;
import java.util.ArrayList;

import common.EchoServer;
import common.ServerController;
import controllers.ParentController;
import entities.DeliveryDetails;
import entities.Order;
import entities.ShipmentDetails;

public class ShipmentController extends ParentController {
	
	public ArrayList<Object> add(ArrayList<Object> arr) throws Exception {
		if(arr!=null && (arr.get(0) instanceof ShipmentDetails == false) ||
				arr.get(1) instanceof Boolean == false)
			throw new Exception();
		ShipmentDetails shipmentDetails = (ShipmentDetails)arr.get(0);
		boolean isReturnNextID = (boolean)arr.get(1);
		BigInteger id = null;
		try {
			id=addPickupToShipment(shipmentDetails);
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
			if(isReturnNextID) {
				query = "SELECT Max(shipmentID) from shipmentdetails;";
				myMsgArr =  EchoServer.fac.dataBase.db.getQuery(query);
				if(myMsgArr!=null && myMsgArr.size()==1 && myMsgArr.get(0) instanceof Integer) {
					myMsgArr.set(0, BigInteger.valueOf((Integer)myMsgArr.get(0)));
				}
				else throw new Exception();
			}
			else
				myMsgArr.add(true);
			return myMsgArr;
		} catch (Exception e) {
			System.err.println("Error with get id in order process");
			e.printStackTrace();
			throw e;
		}
	}
	
	private BigInteger addPickupToShipment(ShipmentDetails shipmentDetails) throws Exception {
		DeliveryDetails del = new DeliveryDetails(
				shipmentDetails.getOrderID(),
				shipmentDetails.getDate(),
				shipmentDetails.isImmediate(),
				shipmentDetails.getStore());
		myMsgArr.clear();
		myMsgArr.add(del);
		myMsgArr.add(true);
		myMsgArr = EchoServer.fac.pickup.add(myMsgArr);
		if(myMsgArr.get(0) instanceof BigInteger)
			return (BigInteger)myMsgArr.get(0);
		throw new Exception();
	}

	@Override
	public ArrayList<Object> handleGet(ArrayList<Object> obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Object> update(Object obj) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}