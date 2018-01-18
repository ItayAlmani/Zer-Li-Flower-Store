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
import izhar.ProductController;

public class StoreController extends ParentController {
	
	@Override
	public ArrayList<Object> handleGet(ArrayList<Object> obj) throws Exception{
		if(obj == null) return null;
		ArrayList<Object> stores = new ArrayList<>();
		for (int i = 0; i < obj.size(); i += 3)
			stores.add(parse
					(
					BigInteger.valueOf(Long.valueOf((int) obj.get(i))), 
					BigInteger.valueOf((int)obj.get(i+1)),
					(String) obj.get(i + 2))
					);
		return stores;
	}
	
	public ArrayList<Object> getAllStores() throws Exception   {
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
				" SET storeID=%d, managerID=%d,name=%s" + 
				" WHERE storeID=%d",
				store.getStoreID(),store.getManager().getUserID(),store.getName());
			EchoServer.fac.dataBase.db.updateQuery(query);
			myMsgArr.clear();
			myMsgArr.add(true);
			return myMsgArr;
		}
		else
			throw new Exception();
	}
	
	public ArrayList<Object> getStoreByID(BigInteger storeID) throws Exception{
		String query = "SELECT *" + 
				" FROM store" + 
				" WHERE storeID='"+storeID.toString()+"'";
		ArrayList<Object> arr = handleGet(EchoServer.fac.dataBase.db.getQuery(query));
		if(arr!=null && arr.size()==1 && arr.get(0) instanceof Store) {
			getStockByStore((Store)arr.get(0));
			return arr;
		}
		throw new Exception();
	}

	public ArrayList<Object> getAllStoresWithStock() throws Exception{
		ArrayList<Object> stores = getAllStores();
		if(stores!=null && stores.isEmpty()==false) {
			for (Object object : stores) {
				if(object instanceof Store) {
					getStockByStore((Store)object);
				}
			}
		}
		return stores;
	}
	
	private void getStockByStore(Store store) throws Exception{
		if(store == null || store.getStoreID()==null)
			throw new Exception();
		
		ArrayList<Object> storeStockObj = EchoServer.fac.stock.getStockByStore(store.getStoreID());
		if(storeStockObj != null && storeStockObj.isEmpty()==false) {
			store.setStock(new ArrayList<>());
			for (Object object : storeStockObj) {
				if(object instanceof Stock)
					store.getStock().add((Stock)object);
			}
		}
	}
	
	public Store parse(BigInteger storeID, BigInteger managerID, String name) throws Exception {
		ArrayList<Object> strWrksObj = EchoServer.fac.storeWorker.getStoreWorkerByUser(managerID),
				stocksObj = EchoServer.fac.stock.getStockByStore(storeID);
		if(strWrksObj!=null && strWrksObj.size()==1) {
			Store s =  new Store(storeID, name, (StoreWorker)strWrksObj.get(0));
			s.setStock(new ArrayList<>());
			for (Object o : stocksObj) {
				if(o instanceof Stock)
					s.getStock().add((Stock)o);
			}
			return s;
		}
		else throw new SQLException();
	}	
	@Override
	public ArrayList<Object> add(ArrayList<Object> arr) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}