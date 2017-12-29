package entities;

import java.math.BigInteger;
import java.util.Date;

public class DeliveryDetails {	
	private BigInteger deliveryID;
	private BigInteger orderID;
	protected Date date;
	private boolean isImmediate = true;
	private Store store;

	public DeliveryDetails(BigInteger deliveryID) {
		super();
		this.deliveryID = deliveryID;
	}
	
	public DeliveryDetails(DeliveryDetails delivery) {
		this.deliveryID = delivery.deliveryID;
		this.orderID = delivery.orderID;
		this.date=delivery.date;
		this.isImmediate=delivery.isImmediate;
		this.store=delivery.store;
	}
	
	public DeliveryDetails(BigInteger orderID, Store store) {
		super();
		this.orderID = orderID;
		this.store = store;
	}

	public DeliveryDetails(BigInteger orderID, Date date, boolean isImmediate, Store store) {
		super();
		this.orderID = orderID;
		this.date = date;
		this.isImmediate = isImmediate;
		this.store = store;
	}
	
	public DeliveryDetails(BigInteger deliveryID, BigInteger orderID, Date date, boolean isImmediate, Store store) {
		super();
		this.deliveryID = deliveryID;
		this.orderID = orderID;
		this.date = date;
		this.isImmediate = isImmediate;
		this.store = store;
	}



	public void setOrderID(BigInteger orderID) {
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

	public BigInteger getDeliveryID() {
		return deliveryID;
	}

	public void setDeliveryID(BigInteger deliveryID) {
		this.deliveryID = deliveryID;
	}

	public boolean isImmediate() {
		return isImmediate;
	}

	public void setImmediate(boolean isImmediate) {
		this.isImmediate = isImmediate;
	}

	public BigInteger getOrderID() {
		return orderID;
	}

	public Store getStore() {
		return store;
	}
	
	

}