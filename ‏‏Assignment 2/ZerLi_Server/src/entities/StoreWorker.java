package entities;

public class StoreWorker extends User {
	private int storeWorkerID;
	private Store store;
	
	public StoreWorker(String firstName, String lastName, String userName, String password, UserType permissions) {
		super(firstName, lastName, userName, password, permissions);
		// TODO Auto-generated constructor stub
	}
	
	public StoreWorker(int userID,int storeWorkerID) {
		super(userID);
		this.storeWorkerID=storeWorkerID;
	}
	
	public StoreWorker(int userID,int storeWorkerID, Store store) {
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

	public int getStoreWorkerID() {
		return storeWorkerID;
	}

	public void setStoreWorkerID(int storeWorkerID) {
		this.storeWorkerID = storeWorkerID;
	}

}