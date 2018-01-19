package kfir;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;

import common.EchoServer;
import controllers.ParentController;
import entities.PaymentAccount;
import entities.Product;
import entities.Stock;
import entities.Store;
import entities.StoreWorker;
import entities.User;

public class StoreWorkerController extends ParentController{

	public ArrayList<Object> getStoreWorkerByUser(BigInteger userID) throws Exception {
		String query = "SELECT * FROM storeworker WHERE userID='"+userID+"'";
		return handleGet(EchoServer.fac.dataBase.db.getQuery(query));
	}
	
	public ArrayList<Object> getStoreWorkerByID(BigInteger swID) throws Exception {
		String query = "SELECT * FROM storeworker WHERE storeWorkerID='"+swID+"'";
		return handleGet(EchoServer.fac.dataBase.db.getQuery(query));
	}

	@Override
	public ArrayList<Object> handleGet(ArrayList<Object> obj) throws Exception {
		if(obj == null) return null;
		ArrayList<Object> sw = new ArrayList<>();
		for (int i = 0; i < obj.size(); i += 3) {
			sw.add(parse(
					BigInteger.valueOf((Integer)obj.get(i)),
					BigInteger.valueOf((Integer)obj.get(i+1)),
					BigInteger.valueOf((Integer)obj.get(i+2))));
		}
		return sw;
	}
	
	public StoreWorker parse(BigInteger storeWorkerID, BigInteger userID, BigInteger storeID) throws Exception
	{
		EchoServer.fac.store.asked_to_get_store_by_manager=true;
		ArrayList<Object> strObjs = EchoServer.fac.store.getStoreByID(storeID);
		ArrayList<Object> users = EchoServer.fac.user.getUser(userID);
		if(strObjs!=null && strObjs.size()==1 && strObjs.get(0) instanceof Store &&
				users!=null && users.size()==1 && users.get(0) instanceof User) {
			Store s = (Store)strObjs.get(0);
			StoreWorker sw = new StoreWorker((User)users.get(0),storeWorkerID, s);
			s.setManager(sw);
			return sw;
		}
		throw new Exception();
	}

	/**Dummy function*/
	@Override
	public ArrayList<Object> add(ArrayList<Object> arr) throws Exception { 
		return null;	
	}

	@Override
	public ArrayList<Object> update(Object obj) throws Exception {
		return null;
	}
}