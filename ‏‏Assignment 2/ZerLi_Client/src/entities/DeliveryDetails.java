package entities;

import java.util.Date;

public class DeliveryDetails {

	private int deliveryID;
	private String orderID;
	protected Date date;
	private boolean isImmediate = true;
	private Store store;

	public DeliveryDetails(int deliveryID) {
		super();
		this.deliveryID = deliveryID;
	}

	public void setOrderID(String orderID) {
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

	public String getOrderID() {
		return orderID;
	}

	public Store getStore() {
		return store;
	}
	
	

}