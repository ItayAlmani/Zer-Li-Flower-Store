package kfir;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;

import common.EchoServer;
import controllers.ParentController;
import entities.PaymentAccount;
import entities.Store;
import entities.StoreWorker;
import entities.Store.StoreType;

public class StoreWorkerController extends ParentController{

	public ArrayList<Object> getStoreWorkerByUser(BigInteger userID) throws Exception {
		myMsgArr.clear();
		StoreWorker sw = new StoreWorker(userID);
		sw.setStore(new Store(BigInteger.ONE, "Karmiel", StoreType.Physical, sw));
		myMsgArr.add(sw);
		return myMsgArr;
		//NEED TO IMPLEMENT!!!!!!!!!!!!!!!
	}

	@Override
	public ArrayList<Object> handleGet(ArrayList<Object> obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Object> add(ArrayList<Object> arr) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Object> update(Object obj) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}