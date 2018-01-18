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
import entities.Store.StoreType;

public class StoreWorkerController extends ParentController{

	public ArrayList<Object> getStoreWorkerByUser(BigInteger userID) throws Exception {
		myMsgArr.clear();
		StoreWorker sw = new StoreWorker(userID);
		sw.setStore(new Store(BigInteger.ONE, "Karmiel", StoreType.Physical, sw));
		ArrayList<Stock> stocks = new ArrayList<>();
		ArrayList<Object> prdsObj = EchoServer.fac.product.getAllProducts();
		for (Object pObj : prdsObj) {
			Stock s = new Stock((Product)pObj, 50,sw.getStore().getStoreID());
			stocks.add(s);
		}
		stocks.get(0).setSalePercetage(0.1f);
		sw.getStore().setStock(stocks);
		myMsgArr.add(sw);
		return myMsgArr;
		//NEED TO IMPLEMENT!!!!!!!!!!!!!!!
	}

	@Override
	public ArrayList<Object> handleGet(ArrayList<Object> obj) throws Exception {
		if(obj == null) return null;
		ArrayList<Object> sw = new ArrayList<>();
		for (int i = 0; i < obj.size(); i += 2) {
			sw.add(parse(
					BigInteger.valueOf((Integer)obj.get(i)),
					(Store)(EchoServer.fac.store.getStoreByID(BigInteger.valueOf((Integer)obj.get(i))).get(0))));
		}
		return sw;
	}
	
	public StoreWorker parse(BigInteger storeWorkerID, Store store)
	{
		return new StoreWorker(storeWorkerID,store);
	}

	@Override
	public ArrayList<Object> add(ArrayList<Object> arr) throws Exception { 
		return null;	}

	@Override
	public ArrayList<Object> update(Object obj) throws Exception {
		return null;
	}
}