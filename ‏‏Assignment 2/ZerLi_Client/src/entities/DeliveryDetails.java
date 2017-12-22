package entities;

import java.util.Date;

public class DeliveryDetails {

	private String deliveryID;
	private String orderID;
	protected Date date;
	private boolean isImmediate = true;
	private Store store;
	
	private static Integer idCounter = 1;
	public DeliveryDetails() {
		this.deliveryID = idCounter.toString();
		idCounter++;
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

}