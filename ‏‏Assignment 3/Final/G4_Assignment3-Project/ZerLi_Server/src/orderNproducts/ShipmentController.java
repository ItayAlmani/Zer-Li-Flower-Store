package orderNproducts;

import java.math.BigInteger;
import java.util.ArrayList;

import common.EchoServer;
import common.ParentController;
import orderNproducts.entities.DeliveryDetails;
import orderNproducts.entities.ShipmentDetails;
import orderNproducts.interfaces.IShipmentController;

public class ShipmentController extends ParentController implements IShipmentController {
	
	@Override
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
	public ArrayList<Object> handleGet(ArrayList<Object> obj) throws Exception {
		if(obj == null) return null;
		ArrayList<Object> ships = new ArrayList<>();
		for (int i = 0; i < obj.size(); i += 7) {
			ships.add(parse(
					BigInteger.valueOf((Integer) obj.get(i)), 
					BigInteger.valueOf((Integer) obj.get(i+1)), 
					(String)obj.get(i+2),
					(String)obj.get(i+3),
					(String)obj.get(i+4),
					(String)obj.get(i+5),
					(String)obj.get(i+6)
					));
		}
		return ships;
	}

	@Override
	public ArrayList<Object> getShipmentByID(BigInteger shipmentID) throws Exception{
		String query = "SELECT *" + 
				" FROM shipmentdetails" + 
				" WHERE shipmentID='"+shipmentID.toString()+"'";
		return handleGet(EchoServer.fac.dataBase.db.getQuery(query));
	}
	
	@Override
	public ArrayList<Object> update(Object obj) throws Exception { return null;	}
	
	@Override
	public ShipmentDetails parse(BigInteger shipmentID, BigInteger deliveryID, String street,
			String city, String postCode, String customerName, String phoneNumber) throws Exception {
		ArrayList<Object> delObjs = EchoServer.fac.pickup.getDeliveryByID(deliveryID);
		if(delObjs!=null && delObjs.size()==1 && delObjs.get(0) instanceof DeliveryDetails) {
			return new ShipmentDetails(shipmentID, 
					(DeliveryDetails)delObjs.get(0),
					street, city, postCode, customerName, phoneNumber);
		}
		throw new Exception();
	}
}