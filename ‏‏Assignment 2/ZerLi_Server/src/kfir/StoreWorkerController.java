package kfir;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;

import controllers.ParentController;
import entities.PaymentAccount;
import entities.StoreWorker;

public class StoreWorkerController extends ParentController{

	public ArrayList<Object> getStoreWorkerByUser(BigInteger userID) throws SQLException {
		myMsgArr.clear();
		myMsgArr.add(new StoreWorker(userID));
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