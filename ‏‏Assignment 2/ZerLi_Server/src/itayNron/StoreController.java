package itayNron;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;

import common.EchoServer;
import controllers.*;
import entities.*;
import entities.CSMessage.MessageType;
import entities.Store.StoreType;
import izhar.ProductController;

public class StoreController extends ParentController {
	
	@Override
	public ArrayList<Object> handleGet(ArrayList<Object> obj){
		if(obj == null) return null;
		ArrayList<Object> stores = new ArrayList<>();
		for (int i = 0; i < obj.size(); i += 4)
			try {
				stores.add(parse
						(
						BigInteger.valueOf(Long.valueOf((int) obj.get(i))), 
						(String) obj.get(i + 2), 
						BigInteger.valueOf((int)obj.get(i+1)),
						(String) obj.get(i + 3))
						);
			} catch (SQLException e) {
				return null;
			}
		return stores;
	}
	
	public ArrayList<Object> getAllStores() throws SQLException   {
		String query = "SELECT * FROM store";
		ArrayList<Object> arr = handleGet(EchoServer.fac.dataBase.db.getQuery(query));
		if(arr==null)
			throw new SQLException("Error in Store Worker of Store(s)\n");
		return arr;
	}
	
	public ArrayList<Object> update(Object obj) throws Exception {
		if(obj instanceof Store) {
			Store store = (Store)obj;
			String query = String.format(
				"UPDATE store " + 
				" SET storeID=%d, managerID=%d,type=%s,name=%s" + 
				" WHERE storeID=%d",
				store.getStoreID(),store.getManager().getUserID(),store.getType().toString(),store.getName());
			EchoServer.fac.dataBase.db.updateQuery(query);
			myMsgArr.clear();
			myMsgArr.add(true);
			return myMsgArr;
		}
		else
			throw new Exception();
	}

	public ArrayList<Object> getAllPhysicalStores() throws SQLException {
		String query = "SELECT store.*" + 
				"FROM store" + 
				"WHERE store.type='Physical'";
		ArrayList<Object> arr = handleGet(EchoServer.fac.dataBase.db.getQuery(query));
		if(arr==null)
			throw new SQLException("Error in Store Worker of Store(s)\n");
		return arr;	
	}

	public Store parse(BigInteger storeID, String type, BigInteger managerID, String name) throws SQLException {
		ArrayList<Object> strWrksObj = EchoServer.fac.storeWorker.getStoreWorkerByUser(managerID); 
		if(strWrksObj!=null && strWrksObj.size()==1)
			return new Store(storeID, name, StoreType.valueOf(type), (StoreWorker)strWrksObj.get(0));
		else throw new SQLException();
	}

	
	@Override
	public ArrayList<Object> add(ArrayList<Object> arr) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}