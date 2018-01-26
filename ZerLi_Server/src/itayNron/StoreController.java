package itayNron;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;


import common.EchoServer;
import controllers.*;
import entities.*;

public class StoreController extends ParentController {
	public boolean asked_to_get_store_by_manager = false;
	
	@Override
	public ArrayList<Object> handleGet(ArrayList<Object> obj) throws Exception{
		if(obj == null) return new ArrayList<>();
		ArrayList<Object> stores = new ArrayList<>();
		for (int i = 0; i < obj.size(); i += 3)
			stores.add(parse
					(
					BigInteger.valueOf((Integer) obj.get(i)), 
					BigInteger.valueOf((Integer)obj.get(i+1)),
					(String) obj.get(i + 2))
					);
		return stores;
	}
	
	/**
	 * <p>
	 * Function to get all stores from DB
	 * </p>
	 * @return generic object arrayList which will become store arrayList   
	 * @throws Exception Context.clientConsole.handleMessageFromClientUI throws Exception.
	 */
	public ArrayList<Object> getAllStores() throws Exception{
		String query = "SELECT * FROM store";
		ArrayList<Object> arr = handleGet(EchoServer.fac.dataBase.db.getQuery(query));
		if(arr==null)
			throw new SQLException("Error in Store Worker of Store(s)\n");
		return arr;
	}
	
	@Override
	public ArrayList<Object> update(Object obj) throws Exception {
		if(obj instanceof Store) {
			Store store = (Store)obj;
			String query = String.format(
				"UPDATE store" + 
				" SET managerID='%d'"
				+ ",name='%s'"
				+ " WHERE storeID='%d'",
				store.getManager().getStoreWorkerID(),store.getName(),store.getStoreID());
			EchoServer.fac.dataBase.db.updateQuery(query);
			myMsgArr.clear();
			myMsgArr.add(true);
			return myMsgArr;
		}
		else
			throw new Exception();
	}
	
	/**
	 * <p>
	 * Function to get specific store by ID
	 * </p>
	 * @param storeID - unique identifier to pull store data by it
	 * @return generic object arrayList which will become store object
	 * @throws Exception Context.clientConsole.handleMessageFromClientUI throws Exception
	 */
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
	
/**
 * <p>
 * Function to get all stores with their stock
 * </p>
 * @return generic object arrayList which will become store object with her stock
 * @throws Exception Context.clientConsole.handleMessageFromClientUI throws Exception.
 */
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
	
	/**
	 * <p>
	 * Function to get stock object according to specific store
	 * </p>
	 * @param store - unique identifier of store to pull stock data from DB of it
	 * @throws Exception Context.clientConsole.handleMessageFromClientUI throws Exception.
	 */
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
	/**
	 * <p>
	 * Function to create new store object with data from DB
	 * </p>
	 * @param storeID - parameter for storeID field from DB
	 * @param managerID - parameter for managerID field from DB
	 * @param name - parameter for name field from DB
	 * @return  new created store object filled with data from DB
	 * @throws Exception Context.clientConsole.handleMessageFromClientUI throws Exception
	 */
	public Store parse(BigInteger storeID, BigInteger managerID, String name) throws Exception {
		Store s = null;
		if(asked_to_get_store_by_manager == false) {
			ArrayList<Object> strWrksObj = EchoServer.fac.storeWorker.getStoreWorkerByID(managerID);
			if(strWrksObj!=null && strWrksObj.size()==1)
				s =  new Store(storeID, name, (StoreWorker)strWrksObj.get(0));
			else throw new SQLException();
		}
		else {
			s = new Store(storeID, name);
			asked_to_get_store_by_manager=false;
		}
		
		ArrayList<Object> stocksObj = EchoServer.fac.stock.getStockByStore(storeID);
		s.setStock(new ArrayList<>());
		for (Object o : stocksObj) {
			if(o instanceof Stock)
				s.getStock().add((Stock)o);
		}
		return s;
	}	
	@Override
	public ArrayList<Object> add(ArrayList<Object> arr) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}