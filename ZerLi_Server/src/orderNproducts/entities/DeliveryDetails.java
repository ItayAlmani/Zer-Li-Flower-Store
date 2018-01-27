package orderNproducts.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;

public class DeliveryDetails implements Serializable{	
	
	private static final long serialVersionUID = 6L;
	private BigInteger deliveryID;
	protected LocalDateTime date;
	private Boolean isImmediate;
	private Store store;

	public DeliveryDetails() {
		
	}
	
	public DeliveryDetails(BigInteger deliveryID) {
		super();
		this.deliveryID = deliveryID;
	}
	
	public DeliveryDetails(DeliveryDetails delivery) {
		this.deliveryID = delivery.deliveryID;
		this.date=delivery.date;
		this.isImmediate=delivery.isImmediate;
		this.store=delivery.store;
	}
	
	public DeliveryDetails(Store store) {
		this.store = store;
	}

	public DeliveryDetails(LocalDateTime date, Boolean isImmediate, Store store) {
		this.date = date;
		this.isImmediate = isImmediate;
		this.store = store;
	}
	
	public DeliveryDetails(BigInteger deliveryID, LocalDateTime date, Boolean isImmediate, Store store) {
		super();
		this.deliveryID = deliveryID;
		this.date = date;
		this.isImmediate = isImmediate;
		this.store = store;
	}

	public LocalDateTime getDate() {
		return this.date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
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

	public Boolean isImmediate() {
		return isImmediate;
	}

	public void setImmediate(Boolean isImmediate) {
		this.isImmediate = isImmediate;
	}

	public Store getStore() {
		return store;
	}
}