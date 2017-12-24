package entities;

import java.util.Date;

public class ShipmentDetails extends DeliveryDetails {

	private String street;
	private String city;
	private String postCode;
	private String customerName;
	private String phoneNumber;
	
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

	public ShipmentDetails(int deliveryID, int orderID, Date date, boolean isImmediate, Store store, 
			String[] address, String customerName, String phoneNumber) {
		super(deliveryID, orderID, date, isImmediate, store);
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
}