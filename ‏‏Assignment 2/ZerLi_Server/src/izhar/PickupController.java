package izhar;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import common.EchoServer;
import common.ServerController;
import controllers.ParentController;
import entities.DeliveryDetails;
import entities.ProductInOrder;
import entities.Store;

public class PickupController extends ParentController {
	@Override
	public ArrayList<Object> add(ArrayList<Object> arr) throws Exception {
		if(arr!=null && arr.size()==2 &&
				(arr.get(0) instanceof DeliveryDetails == false ||
				arr.get(1) instanceof Boolean == false))
			throw new Exception();
		DeliveryDetails deliveryDetails = (DeliveryDetails)arr.get(0);
		boolean isReturnNextID = (boolean)arr.get(1);
		
		String res = "0";
		if(deliveryDetails.isImmediate())
			res = "1";
		String query = "INSERT INTO deliverydetails (orderID, storeID, date, isImmediate) "+
				"VALUES ('"+deliveryDetails.getOrderID() + "', '"
				+ deliveryDetails.getStore().getStoreID() + "', '"
				+ (Timestamp.valueOf(deliveryDetails.getDate())).toString() + "', '"
				+ res + "');";
		EchoServer.fac.dataBase.db.updateQuery(query);
		if(isReturnNextID) {
			query = "SELECT Max(deliveryID) from deliverydetails;";
			myMsgArr =  EchoServer.fac.dataBase.db.getQuery(query);
			if(myMsgArr!=null && myMsgArr.size()==1 && myMsgArr.get(0) instanceof Integer) {
				myMsgArr.set(0, BigInteger.valueOf((Integer)myMsgArr.get(0)));
			}
			else throw new Exception();
		}
		else
			myMsgArr.add(true);
		return myMsgArr;
	}

	@Override
	public ArrayList<Object> update(Object obj) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public ArrayList<Object> handleGet(ArrayList<Object> obj) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public DeliveryDetails parse(BigInteger deliveryID, BigInteger orderID, Store store, LocalDateTime date, boolean isImmediate) {
		return new DeliveryDetails(deliveryID, orderID, date, isImmediate, store);
	}
}