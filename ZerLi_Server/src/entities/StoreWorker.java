package entities;

import java.io.Serializable;
import java.math.BigInteger;

public class StoreWorker extends User implements Serializable  {
	
	private static final long serialVersionUID = 19L;
	private BigInteger storeWorkerID;
	private Store store;
	
	public StoreWorker(User user, BigInteger storeWorkerID,Store store) {
		super(user);
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