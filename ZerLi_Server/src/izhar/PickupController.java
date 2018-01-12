package izhar;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import common.EchoServer;
import common.ServerController;
import controllers.ParentController;
import entities.DeliveryDetails;
import entities.Order;
import entities.ProductInOrder;
import entities.Store;

public class PickupController extends ParentController {
	private String isImm,dateStr;
	
	@Override
	public ArrayList<Object> add(ArrayList<Object> arr) throws Exception {
		if(arr!=null && arr.size()==2 &&
				(arr.get(0) instanceof DeliveryDetails == false ||
				arr.get(1) instanceof Boolean == false))
			throw new Exception();
		DeliveryDetails del = (DeliveryDetails)arr.get(0);
		boolean isReturnNextID = (boolean)arr.get(1);
		
		prepareStrings(del);
		
		String query = "INSERT INTO deliverydetails (storeID, date, isImmediate) "+
				"VALUES ('" + del.getStore().getStoreID().toString() + "', "
				+ dateStr + ", "
				+ isImm + ");";
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
	
	private void prepareStrings(DeliveryDetails del) {
		this.isImm = "NULL"; this.dateStr = "NULL";
		if(del.isImmediate()!=null) {
			if(del.isImmediate())	isImm = "'1'";
			else					isImm = "'0'";
		}
		if(del.getDate()!=null)
			dateStr = "'"+(Timestamp.valueOf(del.getDate())).toString()+"'";
	}

	@Override
	public ArrayList<Object> update(Object obj) throws Exception {
		if (obj instanceof DeliveryDetails) {
			DeliveryDetails del = (DeliveryDetails) obj;
			if(del.getDeliveryID()==null) {
				myMsgArr.clear();
				myMsgArr.add(del);
				myMsgArr.add(false);
				return add(myMsgArr);
			}
			prepareStrings(del);
			
			String query = String.format("UPDATE deliverydetails" + 
					" SET storeID='%s'," + 
					"    date=%s," + 
					"    isImmediate=%s" + 
					"    WHERE deliveryID='%s'" + 
					"", del.getStore().getStoreID(),dateStr,isImm,del.getDeliveryID());
			EchoServer.fac.dataBase.db.updateQuery(query);
			myMsgArr.clear();
			myMsgArr.add(true);
			return myMsgArr;
		} else
			throw new Exception();
	}
	
	public ArrayList<Object> getDeliveryByID(BigInteger deliveryID) throws Exception{
		String query = "SELECT *" + 
				" FROM deliverydetails" + 
				" WHERE deliveryID='"+deliveryID.toString()+"'";
		return handleGet(EchoServer.fac.dataBase.db.getQuery(query));
	}
	
	@Override
	public ArrayList<Object> handleGet(ArrayList<Object> obj) throws Exception {
		if(obj == null) return null;
		ArrayList<Object> dels = new ArrayList<>();
		for (int i = 0; i < obj.size(); i += 4) {
			dels.add(parse(
					BigInteger.valueOf((Integer) obj.get(i)), 
					BigInteger.valueOf((Integer) obj.get(i+1)), 
					obj.get(i+2) == null ? null : ((Timestamp)obj.get(i+2)).toLocalDateTime(),
					obj.get(i + 3) == null ? null : ((Integer) obj.get(i + 3))==1
					));
		}
		return dels;
	}
	
	public DeliveryDetails parse(BigInteger deliveryID, BigInteger storeID, LocalDateTime date, Boolean isImmediate) throws Exception {
		ArrayList<Object> storesObj = EchoServer.fac.store.getStoreByID(storeID);
		if(storesObj!=null && storesObj.size()==1 && storesObj.get(0) instanceof Store) {
			return new DeliveryDetails(deliveryID, date, isImmediate, (Store)storesObj.get(0));
		}
		throw new Exception();
	}
}