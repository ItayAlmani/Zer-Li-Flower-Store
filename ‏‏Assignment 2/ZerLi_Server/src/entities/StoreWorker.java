package entities;

import java.math.BigInteger;

public class StoreWorker extends User {
	
	public StoreWorker(BigInteger storeWorkerID) {
		super();
		this.storeWorkerID = storeWorkerID;
	}

	private BigInteger storeWorkerID;
	private Store store;
	
	private static BigInteger idInc = null;
	
	public StoreWorker(String privateID, String firstName, String lastName, String userName, String password, UserType permissions) {
		super(privateID,firstName, lastName, userName, password, permissions);
		// TODO Auto-generated constructor stub
	}
	
	public StoreWorker(BigInteger userID,BigInteger storeWorkerID) {
		super(userID);
		this.storeWorkerID=storeWorkerID;
	}
	
	public StoreWorker(BigInteger userID,BigInteger storeWorkerID, Store store) {
		super(userID);
		this.storeWorkerID=storeWorkerID;
		this.store=store;
	}

	public Store getStore() {
		return store;
	}
	
	public BigInteger getStoreID() {
		return store.getStoreID();
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public BigInteger getStoreWorkerID() {
		return storeWorkerID;
	}

	public void setStoreWorkerID(BigInteger storeWorkerID) {
		this.storeWorkerID = storeWorkerID;
	}

	public static BigInteger getIdInc() {
		return idInc;
	}

	public static void setIdInc(BigInteger idInc) {
		StoreWorker.idInc = idInc;
	}

}