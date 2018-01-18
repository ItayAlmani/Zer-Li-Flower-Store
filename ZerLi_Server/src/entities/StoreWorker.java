package entities;

import java.io.Serializable;
import java.math.BigInteger;

public class StoreWorker extends User implements Serializable  {
	private BigInteger storeWorkerID;
	private Store store;
	
	public StoreWorker(User user, BigInteger storeWorkerID,Store store) {
		super(user);
		this.storeWorkerID=storeWorkerID;
		this.store=store;
	}
	
	public void setUser(User user) {
		setUserID(user.getUserID());
		setFirstName(user.getFirstName());
		setLastName(user.getLastName());
		setUserName(user.getUserName());
		setPassword(user.getPassword());
		setPermissions(user.getPermissions());
	}
	
	public StoreWorker(BigInteger storeWorkerID) {
		super();
		this.storeWorkerID = storeWorkerID;
	}
	
	public StoreWorker(BigInteger storeWorkerID, Store store) {
		super();
		this.storeWorkerID = storeWorkerID;
		this.store = store;
	}
	
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

	public void setStore(Store store) {
		this.store = store;
	}

	public BigInteger getStoreWorkerID() {
		return storeWorkerID;
	}

	public void setStoreWorkerID(BigInteger storeWorkerID) {
		this.storeWorkerID = storeWorkerID;
	}
}