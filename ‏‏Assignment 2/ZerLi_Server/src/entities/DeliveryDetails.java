package entities;

import java.io.Serializable;
import java.util.Date;

public class DeliveryDetails implements Serializable {

	private int deliveryID;
	private int orderID;
	protected Date date;
	private boolean isImmediate = true;
	private Store store;

	public DeliveryDetails(int deliveryID) {
		super();
		this.deliveryID = deliveryID;
	}

	public DeliveryDetails(int orderID, Date date, boolean isImmediate, Store store) {
		super();
		this.orderID = orderID;
		this.date = date;
		this.isImmediate = isImmediate;
		this.store = store;
	}
	
	public DeliveryDetails(int deliveryID, int orderID, Date date, boolean isImmediate, Store store) {
		super();
		this.deliveryID = deliveryID;
		this.orderID = orderID;
		this.date = date;
		this.isImmediate = isImmediate;
		this.store = store;
	}



	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setIsImmediate(boolean isImmediate) {
		this.isImmediate = isImmediate;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public int getDeliveryID() {
		return deliveryID;
	}

	public void setDeliveryID(int deliveryID) {
		this.deliveryID = deliveryID;
	}

	public boolean isImmediate() {
		return isImmediate;
	}

	public void setImmediate(boolean isImmediate) {
		this.isImmediate = isImmediate;
	}

	public int getOrderID() {
		return orderID;
	}

	public Store getStore() {
		return store;
	}
	
	

}