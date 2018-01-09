package entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;

public class ShipmentDetails extends DeliveryDetails implements Serializable  {

	private BigInteger shipmentID;
	private String street;
	private String city;
	private String postCode;
	private String customerName;
	private String phoneNumber;
	
	public ShipmentDetails(BigInteger shipmentID) {
		super();
		this.shipmentID=shipmentID;
	}
	
	public ShipmentDetails(BigInteger deliveryID, BigInteger shipmentID) {
		super(deliveryID);
		this.shipmentID=shipmentID;
	}

	public ShipmentDetails(DeliveryDetails del,
			String street, String city, String postCode, String customerName,
			String phoneNumber) {
		super(del);
		this.street = street;
		this.city = city;
		this.postCode = postCode;
		this.customerName = customerName;
		this.phoneNumber = phoneNumber;
	}

	public final static float shipmentPrice = 20.0f; 

	public void setStreet(String street) {
		this.street = street;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public ShipmentDetails(BigInteger deliveryID, LocalDateTime date, boolean isImmediate, Store store, 
			String[] address, String customerName, String phoneNumber) {
		super(deliveryID, date, isImmediate, store);
		this.street = address[0];
		this.city = address[1];
		this.postCode = address[2];
		this.customerName = customerName;
		this.phoneNumber = phoneNumber;
	}

	public String getStreet() {
		return street;
	}

	public String getCity() {
		return city;
	}

	public String getPostCode() {
		return postCode;
	}

	public String getCustomerName() {
		return customerName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public static float getShipmentprice() {
		return shipmentPrice;
	}

	public BigInteger getShipmentID() {
		return shipmentID;
	}

	public void setShipmentID(BigInteger shipmentID) {
		this.shipmentID = shipmentID;
	}
}