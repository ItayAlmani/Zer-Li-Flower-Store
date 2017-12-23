package entities;
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

	/**
	 * 
	 * @param address
	 * @param cusName
	 * @param pNum
	 */
	public ShipmentDetails(String[] address, String cusName, String pNum) {
		
	}

}