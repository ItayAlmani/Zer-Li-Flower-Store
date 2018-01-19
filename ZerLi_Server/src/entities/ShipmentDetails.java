package entities;

import java.io.Serializable;
import java.math.BigInteger;

public class ShipmentDetails extends DeliveryDetails implements Serializable  {

	private static final long serialVersionUID = 16L;
	private BigInteger shipmentID;
	private String street;
	private String city;
	private String postCode;
	private String customerName;
	private String phoneNumber;
	
	public ShipmentDetails(BigInteger shipmentID, DeliveryDetails del,
			String street, String city, String postCode, String customerName,
			String phoneNumber) {
		super(del);
		this.shipmentID=shipmentID;
		this.street = street;
		this.city = city;
		this.postCode = postCode;
		this.customerName = customerName;
		this.phoneNumber = phoneNumber;
	}

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

	public BigInteger getShipmentID() {
		return shipmentID;
	}

	public void setShipmentID(BigInteger shipmentID) {
		this.shipmentID = shipmentID;
	}
}